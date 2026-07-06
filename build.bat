@echo off
cd C:\Users\HP\Documents\NetBeansProjects\PARENTPOINT

echo === 1. Mengcompile Java Classes ===
call compile_all.bat

echo === 2. Membuat Standalone JAR (Siap Pakai) ===
rmdir /s /q temp_build 2>nul
mkdir temp_build

:: Copy resources
xcopy src\* temp_build\ /E /I /Y
xcopy build\classes\* temp_build\ /E /I /Y

cd temp_build
jar xf ..\lib\mysql-connector-java-8.0.28.jar
jar xf ..\lib\sqlite-jdbc-3.41.2.2.jar
jar xf ..\lib\jcalendar-1.4.jar
jar xf ..\lib\commons-beanutils-1.9.4.jar
jar xf ..\lib\commons-collections-3.2.2.jar
jar xf ..\lib\commons-collections4-4.4.jar
jar xf ..\lib\commons-digester-2.1.jar
jar xf ..\lib\commons-logging-1.2.jar
jar xf ..\lib\ecj-3.21.0.jar
jar xf ..\lib\jasperreports-6.20.6.jar
rmdir /s /q META-INF 2>nul
cd ..

echo Main-Class: parentpoint.LOGIN.LOGIN> manifest_fat.txt
jar cfm PARENTPOINT_SiapPakai.jar manifest_fat.txt -C temp_build .

echo === SELESAI ===
echo File PARENTPOINT_SiapPakai.jar sudah siap!
