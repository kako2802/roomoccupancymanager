# Room Occupancy Manager
Room Occupancy Manager

Project has the following structure:

Root project 'room-occupancy' <br/>
--- Project ':room-occupancy-api' <br/>
--- Project ':room-occupancy-web' <br/>

1. Install Java in version >= 16
2. Checkout the code from git repo: https://github.com/kako2802/roomoccupancymanager and 
   go to directory where the root project resides.

## How to run tests
###  Unit Tests in room occupancy api project
./gradlew room-occupancy-api:test

###  Unit Tests in room occupancy web project
./gradlew room-occupancy-web:test

###  Unit Tests in entire project
./gradlew test

## How to run application
./gradlew room-occupancy-web:bootRun
