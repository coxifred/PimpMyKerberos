<p align="center"><img src="https://github.com/coxifred/pimpMyKerberos/blob/master/resources/pimpMyKerberos.jpg?raw=true" height=300 ></p>

_Timing Correlation between VideoCam (based on Kerberos.io)_

# Table of Contents
  * [Under the hood](#chapter-1)
  * [Requirements](#chapter-2)
  * [Installation dockerMode](#chapter-3)
  * [Installation hostMode with compilation](#chapter-4)
  * [Configuration](#chapter-5)
  * [Update](#chapter-6)

## Under the hood <a name="chapter-1"></a>

**pimpMyKerberos** is a small footprint java (jetty) server which unify all your cameras in one single interface. As [Kerberos.io](https://kerberos.io), it displays your captures with chronology, but this time will all cameras time-synchronized (Interesting if you want to correlate some events on multiple locations). **pimpMyKerberos** scans capture's directories and sort files by time. Also works with every CCTV system which dump pictures or mp4 captures. **pimpMyKerberos** provides an https access broadcasting your local network cameras.

<p align="center"><img src=https://github.com/coxifred/pimpMyKerberos/blob/master/resources/infra.jpg?raw=true /></p>

<p align="center"><img src=https://github.com/coxifred/pimpMyKerberos/blob/master/resources/arch.jpg?raw=true /></p>

What **pimpMyKerberos** doesn't do:
  
   * It doesn't connect directly to your cameras, but only provides pictures/movies made by Kerberos.io
 
## Requirements <a name="chapter-2"></a>

- [x] Cameras :)
- [x] Linux distribution
- [x] Kerberos.io (docker mode with docker-compose) **yum install docker ; curl -L "https://github.com/docker/compose/releases/download/1.25.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose**
- [x] Git client (**yum install git**)

## Installation dockerMode <a name="chapter-3"></a>
  
 Before install, check that you will respect the following tree structure (volumes provided to kerberos.io container). If you don't have yet configure your docker, download this [docker-compose.yml](https://github.com/coxifred/pimpMyKerberos/blob/master/resources/docker-compose.yml).
  
  ```bash
      "camera1":
      ...
      volumes:
      - /kerberos/<your_cam1>/capture:/etc/opt/kerberosio/capture
      - /kerberos/<your_cam1>/config:/etc/opt/kerberosio/config
      - /kerberos/<your_cam1>/logs:/etc/opt/kerberosio/logs
      - /kerberos/<your_cam1>/webconfig:/var/www/web/config
      
      "camera2":
      ...
      volumes:
      - /kerberos/<your_cam2>/capture:/etc/opt/kerberosio/capture
      - /kerberos/<your_cam2>/config:/etc/opt/kerberosio/config
      - /kerberos/<your_cam2>/logs:/etc/opt/kerberosio/logs
      - /kerberos/<your_cam2>/webconfig:/var/www/web/config
  ```
  
## Installation hostMode with compilation <a name="chapter-4"></a>

 Extra requirement:
  
  - [x] Java JDK version >= 1.8 (**yum install java-1.8.0-openjdk-devel**)
 
  Download project
  ```bash
  git clone https://github.com/coxifred/pimpMyKerberos.git
  cd pimpMyKerberos/PimpMyKerberos
  ```
  Check java version
  ```bash
  [root@localhost PimpMyKerberos]# java -version
  openjdk version "1.8.0_242"
  OpenJDK Runtime Environment (build 1.8.0_242-b08)
  OpenJDK 64-Bit Server VM (build 25.242-b08, mixed mode)
  ```
  Build project
  ```bash
  chmod a+x ./gradlew ; ./gradlew fatJar
  # Jar is generated under build/libs with name PimpMyKerberos.jar
  ```

## Configuration <a name="chapter-5"></a>

  1. As docker container

  2. For java server over docker (running on the host)
  
## Update <a name="chapter-6"></a>  

```bash
git pull
./setup.sh
```
