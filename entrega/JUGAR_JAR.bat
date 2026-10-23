@echo off
cd /d "%~dp0"
if defined JAVA_HOME set "PATH=%JAVA_HOME%\bin;%PATH%"
java -jar GameLluvia.jar
if errorlevel 1 pause
