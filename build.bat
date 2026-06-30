@echo off
cd C:\Users\HP\Documents\NetBeansProjects\PARENTPOINT
rmdir /s /q temp_build 2>nul
mkdir temp_build
xcopy src\* temp_build\ /E /I /Y
cd temp_build
jar xf ..\lib\mysql-connector-java-8.0.28.jar
jar xf ..\lib\sqlite-jdbc-3.41.2.2.jar
jar xf ..\lib\jcalendar-1.4.jar
rmdir /s /q META-INF 2>nul
cd ..
jar cfm PARENTPOINT.jar manifest.txt -C temp_build .
del /F /Q Releases\PARENTPOINT.zip 2>nul
powershell -Command "Compress-Archive -Path PARENTPOINT.jar, lib -DestinationPath Releases\PARENTPOINT.zip -Force"
