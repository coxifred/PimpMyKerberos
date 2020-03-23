#!/bin/bash

fct_echo()
{
 DATE=$(date +%Y%m%d_%H:%M:%S)
 echo "$DATE $@"
}

fct_command()
{
 fct_echo " -> command : $@"
 $@
 if [ "$?" -ne 0 ]
  then
   fct_echo ERR "Error, stopping here with exit 1"
   exit 1
 fi
}


fct_echo INF "Starting configuration of PimpMyKerberos"
echo ""

fct_echo INF "Checking position"
fct_command test -f gradlew

fct_echo INF "Checking java"
fct_command env java -version

fct_echo INF "Build java"
fct_command chmod a+x ./gradlew 
fct_command ./gradlew fatJar

fct_echo INF "Checking build"
fct_command test -f build/libs/PimpMyKerberos.jar

fct_echo INF "Checking aCore.xml file"
fct_command test -f configFile/aCore.xml

fct_echo INF "Run PimpMyKerberos"
java -jar build/libs/PimpMyKerberos.jar








