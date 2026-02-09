# 启动所有服务的脚本
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  启动 AIGC LocalWeb 服务" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

Set-Location $PSScriptRoot

# 设置 JDK 17 环境
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# 检查 Maven 是否可用
try {
    $mvnVersion = mvn -version 2>&1 | Select-Object -First 1
    Write-Host "Maven 版本: $mvnVersion" -ForegroundColor Green
} catch {
    Write-Host "错误: 未找到 Maven，请确保 Maven 已安装并添加到 PATH" -ForegroundColor Red
    exit 1
}

# 检查 Java 版本
try {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "Java 版本: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "错误: 未找到 Java，请确保 JDK 17 已安装并添加到 PATH" -ForegroundColor Red
    exit 1
}

Write-Host "`n正在启动 Server 服务（端口 8081）..." -ForegroundColor Yellow
$serverScript = @"
Set-Location '$PSScriptRoot'
`$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17'
`$env:PATH = "`$env:JAVA_HOME\bin;`$env:PATH"
Write-Host 'Server 服务启动中...' -ForegroundColor Green
mvn spring-boot:run -pl server
"@
Start-Process powershell -ArgumentList "-NoExit", "-Command", $serverScript -WindowStyle Normal

Write-Host "等待 Server 服务启动..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

Write-Host "`n正在启动 Worker 服务（端口 8082）..." -ForegroundColor Yellow
$workerScript = @"
Set-Location '$PSScriptRoot'
`$env:JAVA_HOME = 'C:\Program Files\Java\jdk-17'
`$env:PATH = "`$env:JAVA_HOME\bin;`$env:PATH"
Write-Host 'Worker 服务启动中...' -ForegroundColor Green
mvn spring-boot:run -pl worker
"@
Start-Process powershell -ArgumentList "-NoExit", "-Command", $workerScript -WindowStyle Normal

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  服务启动完成！" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Server 服务: http://localhost:8081" -ForegroundColor Green
Write-Host "Worker 服务: http://localhost:8082" -ForegroundColor Green
Write-Host "`n按任意键退出此窗口（服务将继续运行）..." -ForegroundColor Yellow
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
