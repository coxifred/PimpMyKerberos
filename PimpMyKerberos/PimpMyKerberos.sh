#!/bin/bash


fct_start()
{
 if [ ! -f ./dockerMode ]
  then
   MAX=10
   echo "Logs will be dumped into ./logs under $(pwd)"
   mkdir logs 2>/dev/null
   nohup java -jar build/libs/PimpMyKerberos.jar ./configFile/aCore.xml > ./logs/pimpMyKerberos.log 2>&1 &
   sleep 1
   timeout 1 echo 1 >/dev/null 2>&1
   if [ "$?" = 0 ]
    then
     timeout 3s tail -1000f ./logs/pimpMyKerberos.log
    else
     i=0
     while [ "$i" -lt $MAX ]
      do
       echo "${i}/${MAX}"
       sleep 1
       i=$(expr $i + 1)
      done
      tail -200 ./logs/pimpMyKerberos.log
    fi
    echo " "
    echo " "
    grep "Please open this url in you favorite" ./logs/pimpMyKerberos.log 2>/dev/null
    echo " "
    echo " You can tail log by typing tail -100f ./logs/pimpMyKerberos.log"
    echo " "
    echo " And remember default user/passwd is admin/admin"
    echo " "
 fi
 docker-compose help >/dev/null 2>&1
 if [ "$?" = "0" ]
  then 
   docker-compose -f /kerberos/docker-compose.yml up -d
   echo " "
   echo " "
   echo " You can tail log by docker logs -f pimpmykerberos"
   echo " Open https://<your_ip>" 
 fi
 echo " "
 echo " And remember default user/passwd is admin/admin"
 echo " "



}

fct_stop()
{
pkill -9 -f "PimpMyKerberos.jar" 2>/dev/null
docker-compose help >/dev/null 2>&1
if [ "$?" = "0" ]
 then
  docker-compose -f /kerberos/docker-compose.yml down 
fi
echo "PimpMyKerberos stop"
}

case $1 in 
 "start")
  fct_start
 ;;
 "stop")
  fct_stop
 
 ;;
 "restart")
  fct_stop
  fct_start

 ;;
esac
