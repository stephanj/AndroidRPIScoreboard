# AndroidRPIScoreboard

KBBC Oostkamp scoreboard Android app running RPI 3 Model B using [emteria.OS](https://help.emteria.com/kb/devices-rpi-3).


## Connect via ADB to device using USB

~/java/tools/platform-tools $ ./adb connect 10.0.1.57:5555

## Run locally

Currently using Java 17 with Gradle 8.2 

    sdk use java 17.0.3-zulu   

Build and run the app using Gradle 

    ./gradlew


## Run Android on Simulated Device

    Tools > Device Manager > + Create Virtual Device > Pixel 3a > Android 11 
    
    ./gradlew installDebug

## ADB 

Download ADB from [here](https://dl.google.com/android/repository/platform-tools-latest-darwin.zip) and unzip to `~/java/tools/platform-tools`

    ./adb devices
    ./adb shell
    ./adb logcat

## Curl Scoreboard access

### Login 

    curl -X POST -d "username=test&password=test" http://localhost:8080/api/auth/login

    7a7a53ff-a904-4e92-af50-2dd7f17ebf60

### Create Game

    curl -X POST "http://localhost:8080/api/game?teamA=2&teamB=1&type=0&age=0&court=B&mirrored=true"

    {"game":{"id":14,"createdOn":"2023-12-06T16:31:47.232+00:00","userName":"na","teamA":{"id":12,"name":"2","key":"A","score":0,"fouls":0,"timeOut":0,"mirrored":true},"teamB":{"id":13,"name":"1","key":"B","score":0,"fouls":0,"timeOut":0,"mirrored":true},"gameType":"BASKET","ageCategory":"SENIOREN","quarter":1,"clock":600,"court":"B","mirrored":true}

### List Games

    curl http://localhost:8080/api/game/list

    [{"id":14,"createdOn":"2023-12-06T16:31:47.232+00:00","userName":"na","teamA":{"id":12,"name":"2","key":"A","score":0,"fouls":0,"timeOut":0,"mirrored":true},"teamB":{"id":13,"name":"1","key":"B","score":0,"fouls":0,"timeOut":0,"mirrored":true},"gameType":"BASKET","ageCategory":"SENIOREN","quarter":1,"clock":600,"court":"B","mirrored":true}]

### Get Game by Id

    curl http://localhost:8080/api/game/14

    {"id":14,"createdOn":"2023-12-06T16:31:47.232+00:00","userName":"na","teamA":{"id":12,"name":"2","key":"A","score":0,"fouls":0,"timeOut":0,"mirrored":true},"teamB":{"id":13,"name":"1","key":"B","score":0,"fouls":0,"timeOut":0,"mirrored":true},"gameType":"BASKET","ageCategory":"SENIOREN","quarter":1,"clock":600,"court":"B","mirrored":true}

### Get Game Clock

    curl http://localhost:8080/api/clock/14 

    0

### Start Clock

    curl -X PUT "http://localhost:8080/api/clock/start/14"


