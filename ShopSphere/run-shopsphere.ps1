$ErrorActionPreference = 'Stop'

$WorkspaceRoot = Split-Path -Parent $PSScriptRoot
$TomcatHome = Join-Path $WorkspaceRoot 'apache-tomcat-11.0.27'
$JavaExecutable = (Get-Command java.exe -ErrorAction Stop).Source
$JavaHome = Split-Path -Parent (Split-Path -Parent $JavaExecutable)

$env:JAVA_HOME = $JavaHome
$env:JRE_HOME = $JavaHome
$env:CATALINA_HOME = $TomcatHome
$env:CATALINA_BASE = $TomcatHome

if (-not (Test-Path (Join-Path $TomcatHome 'bin\startup.bat'))) {
    throw "Tomcat 11 was not found at $TomcatHome"
}

Push-Location $PSScriptRoot
try {
    mvn -s (Join-Path $PSScriptRoot 'maven-settings.xml') -DskipTests package
    if ($LASTEXITCODE -ne 0) { throw "Maven packaging failed with exit code $LASTEXITCODE" }
}
finally { Pop-Location }

Copy-Item -LiteralPath (Join-Path $PSScriptRoot 'target\ShopSphere.war') -Destination (Join-Path $TomcatHome 'webapps\ShopSphere.war') -Force
if (Test-NetConnection -ComputerName 127.0.0.1 -Port 8080 -InformationLevel Quiet -WarningAction SilentlyContinue) {
    Write-Host 'ShopSphere.war deployed. Tomcat is already listening on port 8080 and will deploy it automatically.'
}
else {
    Start-Process -FilePath (Join-Path $TomcatHome 'bin\startup.bat') -WindowStyle Hidden -WorkingDirectory (Join-Path $TomcatHome 'bin')
    Write-Host 'Tomcat 11 is starting. Open http://localhost:8080/ShopSphere/ in a few seconds.'
}
