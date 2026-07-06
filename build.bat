@echo off
cd C:\Users\HP\Documents\NetBeansProjects\PARENTPOINT

echo === 1. Mengcompile Java Classes ===
call compile_all.bat

echo === 2. Membuat Standalone JAR (Aman dari Antivirus) ===
rmdir /s /q APP_Release 2>nul
mkdir APP_Release
mkdir APP_Release\lib

:: Copy library files
xcopy lib\* APP_Release\lib\ /E /I /Y

:: Create Manifest
echo Main-Class: parentpoint.LOGIN.LOGIN> manifest_safe.txt
echo Class-Path: lib/commons-beanutils-1.9.4.jar lib/commons-collections-3.2.2.jar lib/commons-collections4-4.4.jar lib/commons-digester-2.1.jar lib/commons-logging-1.2.jar lib/ecj-3.21.0.jar lib/jasperreports-6.20.6.jar lib/jcalendar-1.4.jar lib/mysql-connector-java-8.0.28.jar lib/sqlite-jdbc-3.41.2.2.jar>> manifest_safe.txt
echo.>> manifest_safe.txt

:: Copy compiled class files
xcopy src\parentpoint\images\*.* build\classes\parentpoint\images\ /Y /Q

cd build\classes
jar cfm ..\..\APP_Release\PARENTPOINT.jar ..\..\manifest_safe.txt *
cd ..\..

echo === SELESAI ===
echo File aplikasi sudah dibuat di dalam folder APP_Release!
