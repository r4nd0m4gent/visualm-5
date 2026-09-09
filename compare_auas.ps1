$local = Get-Content C:\Users\fsollit\Desktop\local_auas_numbers.txt | Where-Object { $_ -match '^\d+$' } | ForEach-Object { [int]$_ }
$server = Get-Content C:\Users\fsollit\Desktop\server_auas_numbers.txt | Where-Object { $_ -match '^\d+$' } | ForEach-Object { [int]$_ }

$onlyLocal = $local | Where-Object { $server -notcontains $_ }
$onlyServer = $server | Where-Object { $local -notcontains $_ }

"AUAS numbers in local folder but NOT on server: $($onlyLocal.Count)"
$onlyLocal | ForEach-Object { $_ }
Write-Host "---"
"AUAS numbers on server but NOT in local folder: $($onlyServer.Count)"
