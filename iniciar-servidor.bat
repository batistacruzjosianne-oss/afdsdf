@echo off
title BedWars 1.8.9
if not exist server.jar (
  echo Coloque o arquivo server.jar nesta pasta.
  echo Use uma build Spigot 1.8.8 compativel com clientes 1.8.9.
  pause
  exit /b 1
)
java -Xms1G -Xmx2G -jar server.jar nogui
pause

