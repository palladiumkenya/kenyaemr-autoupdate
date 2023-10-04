#!/bin/bash
# Script to update KenyaHMIS toolkit
authorization=$1
current_dir=$(pwd)
script_dir=$(dirname $0)
echo ${script_dir}
echo ${authorization} | sudo -S cp -rf /opt/kehmisApplicationToolbox/Downloads/kenyahmistoolkit.jar  /usr/share/kenyahmistoolkit
echo "KenyaHMIS Toolkit Updates Succesfully the system will restart"

