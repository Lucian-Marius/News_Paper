@echo off
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot"
set "PATH=C:\Program Files\Eclipse Adoptium\jdk-21.0.9.10-hotspot\bin;%PATH%"
cd /d "c:\Users\Lucia\Desktop\Final Project\NewsPaper\news_paper"
echo Using Java version:
java -version
echo.
echo Building with Maven:
mvnw.cmd clean compile