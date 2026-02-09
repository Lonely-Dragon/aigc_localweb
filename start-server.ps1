# Server 启动脚本
Write-Host "正在启动 Server 服务（端口 8081）..." -ForegroundColor Green
Set-Location $PSScriptRoot

# 设置 JDK 17 环境
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

mvn spring-boot:run -pl server
