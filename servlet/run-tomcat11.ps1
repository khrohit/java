$ErrorActionPreference = 'Stop'

$ProjectDir = $PSScriptRoot
$WorkspaceDir = Split-Path -Parent $ProjectDir
$TomcatHome = Join-Path $WorkspaceDir 'apache-tomcat-11.0.27'
$WarFile = Join-Path $ProjectDir 'target\servlet.war'
$JavaExecutable = (Get-Command java.exe -ErrorAction Stop).Source
$JavaHome = Split-Path -Parent (Split-Path -Parent $JavaExecutable)

$env:JAVA_HOME = $JavaHome
$env:JRE_HOME = $JavaHome
$env:CATALINA_HOME = $TomcatHome
$env:CATALINA_BASE = $TomcatHome

if (-not (Test-Path (Join-Path $TomcatHome 'bin\startup.bat'))) {
    throw "Tomcat 11 was not found at $TomcatHome"
}

Push-Location $ProjectDir
try {
    mvn "-Dmaven.repo.local=$WorkspaceDir\.m2-repository" package
    if ($LASTEXITCODE -ne 0) {
        throw "Maven packaging failed with exit code $LASTEXITCODE"
    }
}
finally {
    Pop-Location
}

Copy-Item -LiteralPath $WarFile -Destination (Join-Path $TomcatHome 'webapps\servlet.war') -Force
if (Test-NetConnection -ComputerName 127.0.0.1 -Port 8080 -InformationLevel Quiet -WarningAction SilentlyContinue) {
    Write-Host 'Port 8080 is already listening. Tomcat will deploy the updated WAR automatically.'
}
else {
    Start-Process -FilePath (Join-Path $TomcatHome 'bin\startup.bat') -WindowStyle Hidden -WorkingDirectory (Join-Path $TomcatHome 'bin')
    Write-Host 'Tomcat 11 is starting. Open http://localhost:8080/servlet/ in a few seconds.'
}
