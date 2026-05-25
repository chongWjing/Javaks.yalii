@echo off
echo ==========================================
echo   失物招领管理系统 - Web 版启动脚本
echo ==========================================
echo.

echo [1/2] 启动后端 Spring Boot...
cd /d "%~dp0lost-found-api"
start "LostFound API" cmd /c "mvn spring-boot:run"

echo [2/2] 等待后端启动 (5 秒)...
timeout /t 5 /nobreak > nul

echo [3/3] 启动前端 Vue.js...
cd /d "%~dp0lost-found-frontend"
start "LostFound Frontend" cmd /c "npm run dev"

echo.
echo ==========================================
echo   系统已启动!
echo   前端: http://localhost:3000
echo   后端: http://localhost:8080
echo ==========================================
echo.
echo 按任意键退出...
pause > nul
