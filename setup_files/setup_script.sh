#!/bin/bash
# Script to backup MySQL databases

modules_dir=/usr/share/tomcat6/.OpenMRS/modules

#script directory
current_dir=$(pwd)
script_dir=$(dirname $0)

if [ $script_dir = '.' ]
then
script_dir="$current_dir"
fi
#echo script_directory: ${script_dir}

# MySQL settings
#mysql_user="root"
mysql_base_database="openmrs"

# Read MySQL password from stdin
printf "Enter mysql user: "
read mysql_user

# Read MySQL password from stdin
stty -echo
printf "Enter mysql password: "
read mysql_password
stty echo
echo

# Check MySQL password
echo exit | mysql --user=${mysql_user} --password=${mysql_password} -B 2>/dev/null
if [ "$?" -gt 0 ]; then
  echo "MySQL ${mysql_user} password incorrect"
  exit 1
else
  echo "MySQL ${mysql_user} password correct."
fi
echo

#export variables
export mysql_user
export mysql_password

echo "Stopping tomcat - skipping this for now..."
echo
#sudo service tomcat6 stop

# create a backup of the currently running KenyaEMR and dump the files in the rollback directory
echo "Backup currently running system"
sudo bash ${script_dir}/rollback_mechanism_setup.sh

echo "Creating db backup"
sudo bash ${script_dir}/openmrs-backup-tools/openmrs_backup.sh $mysql_user $mysql_password


read -p "Press enter to exit terminal" resp



