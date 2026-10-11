@echo off
cd /d "%~dp0"
if not defined JAVA_HOME if exist "%ProgramFiles%\Java\jdk-17\bin\java.exe" set "JAVA_HOME=%ProgramFiles%\Java\jdk-17"
call gradlew.bat :lwjgl3:run
if errorlevel 1 pause
