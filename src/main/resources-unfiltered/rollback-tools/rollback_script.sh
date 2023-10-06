#!/bin/bash
# Script to backup MySQL databases
authorization=$1
mysql_password=$2
modules_dir=/var/lib/OpenMRS/modules
frontend_dir=/var/lib/OpenMRS/frontend
tomcat_dir=/var/lib/tomcat9/webapps/

#script directory
current_dir=$(pwd)
script_dir=$(dirname $0)

if [ $script_dir = '.' ] then
  script_dir="$current_dir"
fi
echo script_directory: ${script_dir}

# MySQL settings
#mysql_user="root"
mysql_base_database="openmrs"

# Read MySQL password from stdin
#printf "Enter mysql user: "
#read mysql_user

# Read MySQL password from stdin
#stty -echo
#printf "Enter mysql password: "
#read mysql_password
#stty echo
#echo

# Check MySQL password
echo exit | mysql --user=root --password=${mysql_password} -B 2>/dev/null
if [ "$?" -gt 0 ]; then
  echo "MySQL ${mysql_user} password incorrect"
  exit 1
else
  echo "MySQL ${mysql_user} password correct."
fi
echo

echo "Stopping tomcat9 Services"
#export variables
export mysql_user
export mysql_password


echo ${authorization} | sudo -S service tomcat9 stop
echo ${authorization} | sudo -S rm -R ${tomcat_dir}/openmrs*
echo ${authorization} | sudo -S cp /opt/opt/kehmisApplicationToolbox/rollback/webapp/openmrs.war ${tomcat_dir}/

echo "Restore the rollback database"

#
zcat "$(find /opt/kehmisApplicationToolbox/rollback/db -iname '*.gz' -print0)" | mysql --user=root --password=${mysql_password} ${mysql_base_database}
#gunzip < /path/to/file.sql.gz | mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database}

if [ "$?" -gt 0 ]; then
  echo "MYSQL encountered a problem while restoring the rollback database"
  exit 1
else
  echo "Successfully restored the rollback database .........................."
fi

# Start Modules Directory
echo "Deleting .omod files from the new release."
echo

echo ${authorization} | sudo -S rm -R ${modules_dir}/*.omod


echo "Finished deleting new release .omod files."
echo

echo "Restoring old .omod files."
echo

echo ${authorization} | sudo -S cp /opt/kehmisApplicationToolbox/rollback/modules/*.omod ${modules_dir}/

echo "Finished restoring old .omod files."
echo

echo "Granting read permission to the modules directory: ${modules_dir}."
echo ${authorization} | sudo -S chmod --recursive +r ${modules_dir}/*.omod

echo ${authorization} | sudo -S chown tomcat9:tomcat9  --recursive ${tomcat_dir}/
echo ${authorization} | sudo -S chown tomcat9:tomcat9  --recursive ${modules_dir}/*.omod
# End Modules Directory

# Start Frontend Directory
echo "Deleting frontend files from the new release."
echo

echo ${authorization} | sudo -S rm -R ${frontend_dir}/*


echo "Finished deleting new release frontend files."
echo

echo "Restoring old frontend files."
echo

echo ${authorization} | sudo -S cp -rR /opt/kehmisApplicationToolbox/rollback/frontend/* ${frontend_dir}/

echo "Finished restoring old frontend files."
echo

echo "Granting read permission to the frontend directory: ${frontend_dir}."
echo ${authorization} | sudo -S chmod --recursive +r ${frontend_dir}/*

echo ${authorization} | sudo -S chown tomcat9:tomcat9  --recursive ${tomcat_dir}/
echo ${authorization} | sudo -S chown tomcat9:tomcat9  --recursive ${frontend_dir}/*
# End Frontend Directory

echo "Deleting liquibase entries for ETL module"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM liquibasechangelog where id like 'kenyaemrChart%';"
echo
echo "Starting tomcat..."
echo

echo ${authorization} | sudo -S service tomcat9 start



