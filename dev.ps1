# dev.ps1 — Start the Angular dev server with Node 14 (via fnm)
# Run from the project root: .\dev.ps1

$fnm = "$env:LOCALAPPDATA\Microsoft\WinGet\Packages\Schniz.fnm_Microsoft.Winget.Source_8wekyb3d8bbwe\fnm.exe"

if (-not (Test-Path $fnm)) {
    Write-Error "fnm not found. Install it with: winget install Schniz.fnm"
    exit 1
}

# Activate Node 14 via fnm for this shell session
$fnmEnv = & $fnm env --shell power-shell | Out-String
Invoke-Expression $fnmEnv
& $fnm use 14

Write-Host "Node version: $(node --version)" -ForegroundColor Cyan
Write-Host "npm version:  $(npm --version)" -ForegroundColor Cyan

Set-Location "$PSScriptRoot\visualmfrontend"

# Install dependencies if node_modules is missing or was built for a different Node version
$modulesOk = $false
if (Test-Path "node_modules") {
    try {
        $nodeVer = node -e "process.exit(0)" 2>&1
        # Quick sanity check: try loading a native module
        node -e "require('./node_modules/@angular/core/package.json')" 2>$null
        if ($LASTEXITCODE -eq 0) { $modulesOk = $true }
    } catch { }
}

if (-not $modulesOk) {
    Write-Host "Installing dependencies with Node 14..." -ForegroundColor Yellow
    Remove-Item -Recurse -Force node_modules -ErrorAction SilentlyContinue
    npm install --legacy-peer-deps
}

Write-Host ""
Write-Host "Starting dev server at http://localhost:4200 ..." -ForegroundColor Green
Write-Host "API calls are proxied to the live production server." -ForegroundColor DarkGray
Write-Host ""

npx ng serve --proxy-config proxy.conf.json --port 4200
