@echo off
color 0A
cd /d C:\Users\HP\Documents\NetBeansProjects\PARENTPOINT
echo Membersihkan build...
rmdir /s /q build2\classes 2>nul
mkdir build2\classes

echo Menyalin dependencies (JasperReports, JCalendar, MySQL, dll)...
cd build2\classes
for %%f in (..\..\lib\*.jar) do (
    jar xf "%%f"
)
:: Hapus folder META-INF dari jar eksternal agar tidak menimpa manifest utama
rmdir /s /q META-INF 2>nul
cd ..\..

echo Mencari source code...
dir /s /b src\*.java > sources.txt

echo Mengkompilasi semua file Java...
"C:\Program Files\Java\jdk-21\bin\javac.exe" -source 8 -target 8 -cp "lib\*" -d build2\classes -sourcepath src @sources.txt

echo Membuat file MANIFEST...
echo Manifest-Version: 1.0> manifest.txt
echo Main-Class: parentpoint.LOGIN.LOGIN>> manifest.txt
echo.>> manifest.txt

echo Membangun FAT JAR...
cd build2\classes
jar cfm ..\..\PARENTPOINT_SiapPakai.jar ..\..\manifest.txt .
cd ..\..

echo Menyalin ke Desktop...
copy PARENTPOINT_SiapPakai.jar C:\Users\HP\Desktop\PARENTPOINT_SiapPakai.jar /Y
echo SELESAI!
