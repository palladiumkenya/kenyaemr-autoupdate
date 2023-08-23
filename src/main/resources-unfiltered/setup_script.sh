#!/bin/sh

# set -e
sudo apt update
# sudo apt install ansible
# sudo apt-get install openjdk-11-jdk
# sudo apt install-y openjfx
sudo mkdir -p /opt/kehmisApplicationToolbox/
sudo mkdir -p /opt/kehmisApplicationToolbox/Downloads
sudo mkdir -p /var/backups/KenyaEMR
# sudo cat /opt/kehmisApplicationToolbox/application.properties
sudo chmod 777 -R /opt/kehmisApplicationToolbox/
sudo chmod 777 -R /opt
sudo chmod 777 -R /var/lib/OpenMRS/openmrs-runtime.properties
sudo chmod 777 -R /var/backups/KenyaEMR

sudo /opt/java/toolkit/bin/java --module-path /opt/openjfx/lib --add-modules ALL-MODULE-PATH -jar kenyahmistoolkit.jar

