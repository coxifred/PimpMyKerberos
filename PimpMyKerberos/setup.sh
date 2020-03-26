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

fct_createTree()
{
 if [ "$1" != "FORCE" ]
  then
   fct_echo INF "Ok ready to launch, but i need to create this tree with 4 examples camera instances"
   echo "/kerberos                                 <----- Top directory"
   echo "         /camera1_name                    <----- Camera name you can rename after"
   echo "                      /capture            <----- Capture volume for kerberos.io mapped in docker"
   echo "                      /config             <----- Config volume for kerberos.io mapped in docker"
   echo "                      /logs               <----- Logs volume for kerberos.io mapped in docker"
   echo "                      /webconfig          <----- WebConfig volume for kerberos.io mapped in docker"
   echo "         /camera2_name"
   echo "                      /capture            <----- Capture volume for kerberos.io mapped in docker"
   echo "                      /config             <----- Config volume for kerberos.io mapped in docker"
   echo "                      /logs               <----- Logs volume for kerberos.io mapped in docker"
   echo "                      /webconfig          <----- WebConfig volume for kerberos.io mapped in docker"
   echo "         /camera3_name"
   echo "                      /capture            <----- Capture volume for kerberos.io mapped in docker"
   echo "                      /config             <----- Config volume for kerberos.io mapped in docker"
   echo "                      /logs               <----- Logs volume for kerberos.io mapped in docker"
   echo "                      /webconfig          <----- WebConfig volume for kerberos.io mapped in docker"
   echo "         /camera4_name"
   echo "                      /capture            <----- Capture volume for kerberos.io mapped in docker"
   echo "                      /config             <----- Config volume for kerberos.io mapped in docker"
   echo "                      /logs               <----- Logs volume for kerberos.io mapped in docker"
   echo "                      /webconfig          <----- WebConfig volume for kerberos.io mapped in docker"
   echo "         docker-compose.yml               <----- compose file for starting 4 instances of kerberos.io"
   echo ""
   echo " Continue [Y/n]:" ; read REP
   if [ -z "${REP}" -o "${REP}" = "y" -o "${REP}" = "Y" ]
    then
     fct_echo INF "Building directory"
     fct_command cp -Rp kerberos /kerberos
    else
     fct_echo INF "Abort" ; exit 1
   fi
  else
   fct_echo INF "Building directory"
   rm -rf /kerberos 2>/dev/null
   fct_command cp -Rp kerberos /kerberos
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


clear
if [ -d /kerberos ]
 then
  fct_echo INF "/kerberos already exist, reinit (all datas will be deleted) ? [y/N]:" ; read INIT
  if [ "${INIT}" = "y" -o "${INIT}" = "Y" ]
   then
    fct_createTree FORCE
  fi
 else
  fct_createTree
fi

fct_echo INF "PimpMyKerberos will start by executing ./PimpMyKerberos.sh restart"
fct_echo INF "For next time : "
fct_echo INF " For starting ./PimpMyKerberos.sh start"
fct_echo INF " For stopping ./PimpMyKerberos.sh stop"
fct_echo INF " For restart  ./PimpMyKerberos.sh restart"
echo " "
fct_echo INF "Press enter to continue" ; read SUITE
./PimpMyKerberos.sh restart
