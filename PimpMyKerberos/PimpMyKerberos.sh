#!/bin/bash


fct_start()
{
 echo "Logs will be dumped into ./logs under $(pwd)"
 mkdir logs 2>/dev/null
 nohup java -jar build/libs/PimpMyKerberos.jar ./configFile/aCore.xml > ./logs/pimpMyKerberos.log 2>&1 &
 i=0
 while [ "$i" -lt 10 ]
  do
   echo ".\c"
   sleep 1
   i=$(expr $i + 1)
  done
  tail -200 ./logs/pimpMyKerberos.log
}

fct_stop()
{
pkill -9 -f "PimpMyKerberos.jar" 2>/dev/null
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
