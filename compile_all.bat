@echo off
cd /d C:\Users\HP\Documents\NetBeansProjects\PARENTPOINT
rmdir /s /q build\classes 2>nul
mkdir build\classes

dir /s /b src\*.java > sources.txt

"C:\Program Files\Java\jdk-21\bin\javac.exe" -source 8 -target 8 -cp "lib\*" -d build\classes -sourcepath src @sources.txt

echo.
echo === Checking inner class files ===
dir /s /b build\classes\*$*.class
echo.
echo === DONE ===
