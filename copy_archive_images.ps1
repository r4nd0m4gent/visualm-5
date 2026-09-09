# -----------------------------------------------------------------------
# copy_archive_images.ps1
#
# Copies AUAS archive images to the server's images folder and generates
# the SQL UPDATE statements needed to link them to their materials.
#
# Naming convention:   AUAS_NNNNN_1.jpg  -> overview_url  (suffix _1)
#                      AUAS_NNNNN_2.jpg  -> close_up_url  (first suffix >= 2)
# Match key: the numeric portion NNNNN == sequence_number_published
# -----------------------------------------------------------------------

$srcFolder  = "C:\Users\fsollit\Downloads\Pics_materials\AUAS Material Archive LR pictures"
$destFolder = "C:\Users\fsollit\Desktop\visualm-5-master\images\material\archive"
$sqlFile    = "C:\Users\fsollit\Desktop\visualm-5-master\update_images.sql"

# Create destination folder if it doesn't exist
New-Item -ItemType Directory -Force -Path $destFolder | Out-Null

# -----------------------------------------------------------------------
# Parse filenames: only accept the clean pattern AUAS_0*{digits}_{digits}.jpg
# -----------------------------------------------------------------------
$files  = Get-ChildItem $srcFolder -Filter "AUAS_*.jpg"
$groups = @{}   # key = sequence_number_published (int), value = hashtable imgNum->FileInfo

foreach ($file in $files) {
    # Match: AUAS_ + optional leading zeros + capture(digits) + _ + capture(digits) + .jpg
    if ($file.Name -match '^AUAS_0*(\d+)_(\d+)\.jpg$') {
        $seqPub = [int]$Matches[1]
        $imgNum = [int]$Matches[2]

        if (-not $groups.ContainsKey($seqPub)) {
            $groups[$seqPub] = @{}
        }
        $groups[$seqPub][$imgNum] = $file
    }
    # silently skip draft / lettered / malformed files
}

# -----------------------------------------------------------------------
# Copy files and build SQL
# -----------------------------------------------------------------------
$sqlLines = @()
$sqlLines += "-- -----------------------------------------------------------------------"
$sqlLines += "-- update_images.sql"
$sqlLines += "--"
$sqlLines += "-- Sets overview_url and close_up_url on published materials by matching"
$sqlLines += "-- sequence_number_published to the AUAS archive image number."
$sqlLines += "--"
$sqlLines += "-- Run this script against the live MySQL database AFTER the images have"
$sqlLines += "-- been copied to the server (via Docker volume or direct deployment)."
$sqlLines += "-- -----------------------------------------------------------------------"
$sqlLines += ""

$copied    = 0
$skipped   = 0
$sqlCount  = 0

foreach ($seqPub in ($groups.Keys | Sort-Object)) {
    $imgMap = $groups[$seqPub]

    # Copy all matched files for this material
    foreach ($imgNum in $imgMap.Keys) {
        $src  = $imgMap[$imgNum].FullName
        $dest = Join-Path $destFolder $imgMap[$imgNum].Name
        Copy-Item $src -Destination $dest -Force
        $copied++
    }

    $hasOverview  = $imgMap.ContainsKey(1)
    $closeUpNums  = $imgMap.Keys | Where-Object { $_ -ge 2 } | Sort-Object
    $hasCloseUp   = ($closeUpNums | Measure-Object).Count -gt 0

    if (-not $hasOverview -and -not $hasCloseUp) {
        $skipped++
        continue
    }

    $sqlLines += "-- Material sequence_number_published = $seqPub"

    if ($hasOverview) {
        $fname     = $imgMap[1].Name
        $dbPath    = "/images/material/archive/$fname"
        $sqlLines += "UPDATE ``material`` SET ``overview_url`` = '$dbPath' WHERE ``sequence_number_published`` = $seqPub;"
        $sqlCount++
    }

    if ($hasCloseUp) {
        $firstClose = $closeUpNums | Select-Object -First 1
        $fname      = $imgMap[$firstClose].Name
        $dbPath     = "/images/material/archive/$fname"
        $sqlLines  += "UPDATE ``material`` SET ``close_up_url`` = '$dbPath' WHERE ``sequence_number_published`` = $seqPub;"
        $sqlCount++
    }

    $sqlLines += ""
}

$sqlLines | Out-File -FilePath $sqlFile -Encoding UTF8

# -----------------------------------------------------------------------
# Summary
# -----------------------------------------------------------------------
Write-Host ""
Write-Host "Done."
Write-Host "  Images copied  : $copied  ->  $destFolder"
Write-Host "  SQL statements : $sqlCount  ->  $sqlFile"
Write-Host ""
Write-Host "Next steps:"
Write-Host "  1. If the server is running via Docker, the images are now available"
Write-Host "     inside the container at /app/images/material/archive/"
Write-Host "  2. Run update_images.sql against your MySQL database."
