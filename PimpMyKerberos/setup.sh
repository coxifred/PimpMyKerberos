#!/bin/bash

MODE=$1

## 

fct_echo()
{
 DATE=$(date +%Y%m%d_%H:%M:%S)
 echo "$DATE $@"
}


if [ -z "${MODE}" ]
 then
  clear
  fct_echo INF "Setup mode: "
  fct_echo INF " "
  fct_echo INF " 1) Full docker mode"
  fct_echo INF " 2) Just Kerberos/io in docker mode"
  fct_echo INF ""
  fct_echo ASK " Your choice [default 1]: " ; read CHOICE
  if [ -z "${CHOICE}" ] 
   then
    MODE=docker
   else
    case "${CHOICE}" in 
      1) MODE=docker;;
      2) MODE=half-docker;;
      *) MODE=docker;;
    esac
  fi
 else
  if [ "${MODE}" != "docker" -a "${MODE}" != "half-docker" ]
      then
       fct_echo DEAD "Authorized parameter docker or half-docker"
       exit 1
  fi
fi

fct_echo INF "Selected Mode : $MODE"



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
   echo " "
   echo "         /pimpMyKerberos/aCore.xml        <----- Config file in case of full stack docker"
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
   echo "         docker-compose.yml               <----- compose file for starting 4 instances of kerberos.io + pimpMyKerberos if you choose docker mode"
   echo ""
   fct_echo ASK " Continue [Y/n]:" ; read REP
   if [ -z "${REP}" -o "${REP}" = "y" -o "${REP}" = "Y" ]
    then
     fct_echo INF "Building directory"
     fct_command cp -RLp kerberos /kerberos
    else
     fct_echo INF "Abort" ; exit 1
   fi
  else
   fct_echo INF "Building directory, cleaning /kerberos ... could be long"
   rm -rf /kerberos 2>/dev/null
   fct_command cp -RLp kerberos /kerberos
fi

if [ "${MODE}" = "docker" ]
 then
  fct_echo INF "Switch docker-compose file for full docker"
  fct_command cp /kerberos/docker-compose.yml.dockerfull /kerberos/docker-compose.yml
fi
  
}

fct_halfdocker()
{

 fct_echo INF "Checking docker command"
 fct_command docker help >/dev/null 

 fct_echo INF "Checking docker-compose command"
 fct_command docker-compose help >/dev/null

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

 fct_echo INF "Starting with docker-compose"
 fct_command docker-compose -f /kerberos/docker-compose.yml up -d

}

fct_docker()
{
 fct_echo INF "Checking docker command"
 fct_command docker help >/dev/null 

 fct_echo INF "Checking docker-compose command"
 fct_command docker-compose help >/dev/null

 fct_echo INF "Creating /kerberos/pimpMyKerberos"
 if [ ! -d /kerberos/pimpMyKerberos ]
  then
   fct_command mkdir /kerberos/pimpMyKerberos 2>/dev/null
 fi

 fct_echo INF "Checking aCore.xml file"
 fct_command test -f configFile/aCore.xml

 fct_echo INF "Copy aCore.xml file into /kerberos/pimpMyKerberos"
 fct_command cp configFile/aCore.xml /kerberos/pimpMyKerberos

 fct_echo INF "Starting with docker-compose"
 fct_command docker-compose -f /kerberos/docker-compose.yml up -d
}

clear
fct_echo INF "Starting configuration of PimpMyKerberos"
echo ""
if [ -d /kerberos ]
 then
  fct_echo ASK "mountpoint or directory /kerberos already exist, reinit ? (all datas will be deleted), answer N if you just want to update PimpMyKerberos [y/N]:" ; read INIT
  if [ "${INIT}" = "y" -o "${INIT}" = "Y" ]
   then
    fct_createTree FORCE
  fi
 else
  fct_createTree
fi

case "${MODE}" in
  "docker")
          fct_docker
          fct_command touch dockerMode
          ;;
  "half-docker")
          rm dockerMode 2>/dev/null
          fct_halfdocker 
          ;;
esac





fct_echo INF "PimpMyKerberos will start by executing ./PimpMyKerberos.sh restart"
fct_echo INF "For next time : "
fct_echo INF " For starting ./PimpMyKerberos.sh start"
fct_echo INF " For stopping ./PimpMyKerberos.sh stop"
fct_echo INF " For restart  ./PimpMyKerberos.sh restart"
echo " "
fct_echo INF "Press enter to start PimpMyKerberos" ; read SUITE
./PimpMyKerberos.sh restart
