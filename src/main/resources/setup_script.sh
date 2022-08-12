#!/bin/sh

sudo apt update
sudo apt install ansible
sudo apt-get install openjdk-11-jdk
sudo apt install-y openjfx
sudo mkdir /opt/kehmisApplicationToolbox/
sudo mkdir /opt/kehmisApplicationToolbox/Downloads
sudo cat /opt/kehmisApplicationToolbox/application.properties
sudo chmod 777 -R /opt/kehmisApplicationToolbox/
sudo /usr/lib/jvm/java-1.11.0-openjdk-amd64/bin/java --module-path /usr/share/openjfx/lib --add-modules ALL-MODULE-PATH -jar applicationtoolkit-1.0-SNAPSHOT.jar

