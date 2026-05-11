$path = "C:\Users\fsollit\Downloads\Pics_materials\AUAS Material Archive LR pictures"
$nums = @()
foreach ($f in (Get-ChildItem $path -Filter "*.jpg")) {
    if ($f.Name -match 'AUAS_0*(\d+)_') { $nums += [int]$Matches[1] }
}
$unique = $nums | Sort-Object -Unique
$unique | Out-File C:\Users\fsollit\Desktop\local_auas_numbers.txt -Encoding utf8
"Unique AUAS numbers in local folder: $($unique.Count)"
$unique | Select-Object -First 20
