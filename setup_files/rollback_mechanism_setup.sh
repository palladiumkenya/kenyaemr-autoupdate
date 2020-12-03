#!/bin/bash
modules_dir=/usr/share/tomcat6/.OpenMRS/modules
warfile_dir=/var/lib/tomcat6/webapps

#script directory
current_dir=$(pwd)
script_dir=$(dirname $0)

if [ $script_dir = '.' ]
then
script_dir="$current_dir"
fi
echo script_directory: ${script_dir}



echo "setting up KenyaEMR auto-update rollback mechanism"

echo "Creating new directory"
sudo rm -R "/opt/KenyaEMRAutoupdate/rollback/webapp"
sudo rm -R "/opt/KenyaEMRAutoupdate/rollback/modules"
sudo rm -R "/opt/KenyaEMRAutoupdate/rollback/db"
sudo mkdir -p "/opt/KenyaEMRAutoupdate/rollback/webapp"
sudo mkdir -p "/opt/KenyaEMRAutoupdate/rollback/modules"
sudo mkdir -p "/opt/KenyaEMRAutoupdate/rollback/db"

sudo chmod -R 755 /opt/KenyaEMRAutoupdate/rollback
sudo chown -R $USER:$USER /opt/KenyaEMRAutoupdate/rollback/


sudo cp ${modules_dir}/*.omod /opt/KenyaEMRAutoupdate/rollback/modules
sudo cp ${warfile_dir}/openmrs.war /opt/KenyaEMRAutoupdate/rollback/webapp


echo "completed setup of auto-update rollback mechanism"
echo
echo




