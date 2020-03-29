<p align="center"><img src="https://github.com/coxifred/pimpMyKerberos/blob/master/resources/pimpMyKerberos.jpg?raw=true" height=300 ></p>

_Timing Correlation between VideoCam (based on Kerberos.io)_

# Table of Contents
  * [Screenshots](#chapter-1)
  * [Under the hood](#chapter-2)
  * [Requirements](#chapter-3)
  * [Installation dockerMode](#chapter-4)
  * [Installation hostMode with gradle compilation](#chapter-5)
  * [Installation binary mode without docker](#chapter-6)
  * [Installation in compilation mode without docker](#chapter-7)
  * [Configuration](#chapter-8)
  * [Update](#chapter-9)
  * [Next features](#chapter-10)
  
  
## Screenshots <a name="chapter-1"></a>  

<p align="center"><img src=https://github.com/coxifred/pimpMyKerberos/blob/master/resources/pimpMyKerberos.gif?raw=true /></p>

## Under the hood <a name="chapter-2"></a>

**pimpMyKerberos** is a small footprint java (jetty) server which unify all your cameras in one single interface. As [Kerberos.io](https://kerberos.io), it displays your captures with chronology, but this time will all cameras time-synchronized (Interesting if you want to correlate some events on multiple locations). **pimpMyKerberos** scans capture's directories and sort files by time. Also works with every CCTV system which dump pictures or mp4 captures. **pimpMyKerberos** provides an https access broadcasting your local network cameras.

<p align="center"><img src=https://github.com/coxifred/pimpMyKerberos/blob/master/resources/infra.jpg?raw=true /></p>

<p align="center"><img src=https://github.com/coxifred/pimpMyKerberos/blob/master/resources/arch.jpg?raw=true /></p>

What **pimpMyKerberos** doesn't do:
  
   * It doesn't connect directly to your cameras, but only provides pictures/movies made by Kerberos.io
 
## Requirements <a name="chapter-3"></a>

- [x] Cameras :)
- [x] Linux distribution
- [x] Kerberos.io (docker mode with docker-compose) **yum install docker ; curl -L "https://github.com/docker/compose/releases/download/1.25.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose ; chmod a+x /usr/local/bin/docker-compose**
- [x] Git client (**yum install git**)

## Installation in dockerMode <a name="chapter-4"></a>
  
 Full installation (automatic pull for kerberos.io). You will receive, kerberos.io docker containers and 1 pimpMyKerberos docker   container
  
  ```bash
  git clone https://github.com/coxifred/pimpMyKerberos.git
  cd pimpMyKerberos/PimpMyKerberos
  ./setup.sh docker
  ```
  
 For a pimpMyKerberos docker container:
 
 ```bash
 docker pull 
 ```

## Installation in hostMode with gradle compilation<a name="chapter-5"></a>

 You will receive, kerberos.io docker containers but pimpMyKerberos will run on the host (not in a docker container)

 Extra requirement:
  
  - [x] Java JDK version >= 1.8 (**yum install java-1.8.0-openjdk-devel**)
 
  Download project
  ```bash
  git clone https://github.com/coxifred/pimpMyKerberos.git
  
  cd pimpMyKerberos/PimpMyKerberos
  ./setup.sh half-docker
  ```
  
## Installation in binary mode without docker<a name="chapter-6"></a>

 Extra requirement:
  
  - [x] Java JDK version >= 1.8 (**yum install java-1.8.0-openjdk-devel or pkg install jdk-8**)
 
  Download project
  ```bash
  git clone https://github.com/coxifred/pimpMyKerberos.git
  
  ```
## Installation in compilation mode without docker<a name="chapter-7"></a>

 Extra requirement:
  
  - [x] Java JDK version >= 1.8 (**yum install java-1.8.0-openjdk-devel or pkg install jdk-8**)
 
  Download project
  ```bash
  git clone https://github.com/coxifred/pimpMyKerberos.git
  ./gradlew fatjar
  ```
  
## Configuration <a name="chapter-8"></a>

  Login prompt, user -> admin, password -> admin

  If you're in docker mode, configuration file should be in /kerberos/pimpMyKerberos/aCore.xml
  
  If you're in docker mode, configuration file should be in configFile/aCore.xml (under the place you git project)
  
  ```xml
  <pimpmykerberos.core.Core>
    <coreFile>./configFile/aCore.xml</coreFile>          <!-- Config file, for information only, read-only-->
    <webServerPort>443</webServerPort>                   <!-- Port for https-->
    <webSocketPort>4430</webSocketPort>                  <!-- Port for websocket, not used, read only-->
    <webServerIp>0.0.0.0</webServerIp>                   <!-- Can be blank, ip for binding web server-->
    <debug>true</debug>                                  <!-- Debug mode true|false-->
    <timeBetweenScan>60000</timeBetweenScan>             <!-- Time (in ms) between 2 directory analysis -->
    <debugJetty>false</debugJetty>                       <!-- Debug mode for jetty true|false -->
    <adminPassword>admin</adminPassword>                 <!-- Password for admin -->
    <maxLogEntries>1000</maxLogEntries>                  <!-- Max logs in memory -->
    <kerberosioPath>Z:\kerberos.io</kerberosioPath>      <!-- Path to kerberos structure, containing camera dir-->
    <maxDisplayLineInGUI>5</maxDisplayLineInGUI>         <!-- Crunch size in GUI -->
    <daysBeforePurge>30</daysBeforePurge>                <!-- After x days, older files are cleaned-->
    <maxDisplayColumnInGUI>6</maxDisplayColumnInGUI>     <!-- Max Columns in GUI -->
    <nightMode>0</nightMode>                             <!-- 1 for Night (black background) , 0 for day (white background) -->
    <muteMode>0</muteMode>                               <!-- Mute mode 1 for no message on GUI, 0 for messages-->
    <sessionId>0</sessionId>                             <!-- Not used -->
    <dataPath></dataPath>                                <!-- Not used -->
    <influxDbUrl></influxDbUrl>                          <!-- Url of a influxDb, files recorded by hour by cam, if empty no send -->
    <influxDbName>pimpMyKerberos</influxDbName>          <!-- influxDb database name -->
    <influxDbUser>root</influxDbUser>                    <!-- influxDb account -->
    <influxDbPasswd>root</influxDbPasswd>                <!-- influxDb passwd -->
</pimpmykerberos.core.Core>
```
  
## Update <a name="chapter-9"></a>  

```bash
git pull
```
then relaunch the appropriate installation mode.

<p align="center"><img src="https://github.com/coxifred/pimpMyKerberos/blob/master/resources/pimpMyKerberos.jpg?raw=true" height=300 ></p>

## Next features <a name="chapter-10"></a>  

Mobile responsive.
Reduce javascript memory leak from html5 video tag.
