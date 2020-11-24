#!/bin/bash
# Script to backup MySQL databases


#script directory
current_dir=$(pwd)
script_dir=$(dirname $0)

if [ $script_dir = '.' ]
then
script_dir="$current_dir"
fi
echo script_directory: ${script_dir}



echo "setting up KenyaEMR auto-update"

echo "Creating new directory"
sudo rm -R "/opt/KenyaEMRAutoupdate/"
sudo mkdir "/opt/KenyaEMRAutoupdate/"
sudo chmod -R 755 /opt/KenyaEMRAutoupdate/
sudo chown -R $USER:$USER /opt/KenyaEMRAutoupdate/


cp --recursive ${current_dir}/setup_files/* /opt/KenyaEMRAutoupdate/
sudo rm ~/Desktop/kenyaemr_upgrade_launcher.desktop
cp ${current_dir}/setup_files/kenyaemr_upgrade_launcher.desktop ~/Desktop

# make the desktop launcher executable
#sudo chown $USER:$USER ~/Desktop/KenyaEMRDataTools.desktop
sudo chmod +x ~/Desktop/kenyaemr_upgrade_launcher.desktop


echo "completed setup of auto-update"
echo
echo




