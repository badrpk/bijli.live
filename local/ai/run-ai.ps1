param([switch]$NoWindow)
$wd  = Split-Path -Parent $MyInvocation.MyCommand.Path
$py  = Join-Path $wd ".venv\Scripts\python.exe"
$args = @('-m','uvicorn','--app-dir', $wd, 'app:app', '--host','127.0.0.1','--port','8012','--log-level','info')

if ($NoWindow) {
  Start-Process -FilePath $py -ArgumentList $args -WorkingDirectory $wd -WindowStyle Hidden
} else {
  Push-Location $wd
  & $py @args
  Pop-Location
}
