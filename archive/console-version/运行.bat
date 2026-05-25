@echo off
echo 启动失物招领管理系统...
cd /d "%~dp0"
java -cp "out;lib/mysql-connector-java-8.0.33.jar" com.lostfound.LostFoundApp
pause
