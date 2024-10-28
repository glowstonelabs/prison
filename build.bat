@echo off
SETLOCAL

set plugin=prison
set version=1.0
set target=C:\Users\Amari\Desktop\test\plugins
call ./gradlew shadowJar
copy /y ".\build\libs\%plugin%-%version%-all.jar" "%target%\%plugin%-%version%.jar"
del /f  C:\Users\Amari\Desktop\test\plugins\prison

ENDLOCAL