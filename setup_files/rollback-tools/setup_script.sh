#!/bin/bash
# Script to backup MySQL databases

modules_dir=/usr/share/tomcat6/.OpenMRS/modules
tomcat_dir=/var/lib/tomcat6/webapps/

#script directory
current_dir=$(pwd)
script_dir=$(dirname $0)

if [ $script_dir = '.' ]
then
script_dir="$current_dir"
fi
echo script_directory: ${script_dir}

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


sudo service tomcat6 stop
sudo rm -R ${tomcat_dir}/openmrs*
sudo cp /opt/KenyaEMRAutoupdate/rollback/webapp/openmrs.war ${tomcat_dir}/

echo "Restore the rollback database"

zcat "$(find /opt/KenyaEMRAutoupdate/rollback/db -iname '*.gz' -print0)" | mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database}
#gunzip < /path/to/file.sql.gz | mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database}

if [ "$?" -gt 0 ]; then
  echo "MYSQL encountered a problem while restoring the rollback database"
  exit 1
else
  echo "Successfully restored the rollback database .........................."
fi

echo "Deleting .omod files from the new release."
echo

sudo rm -R ${modules_dir}/*.omod


echo "Finished deleting new release .omod files."
echo

echo "Restoring old .omod files."
echo

sudo cp /opt/KenyaEMRAutoupdate/rollback/modules/*.omod ${modules_dir}/

echo "Finished restoring old .omod files."
echo

echo "Granting read permission to the modules directory: ${modules_dir}."
sudo chmod --recursive +r ${modules_dir}/*.omod

sudo chown tomcat6:tomcat6  --recursive ${tomcat_dir}/
sudo chown tomcat6:tomcat6  --recursive ${modules_dir}/*.omod
echo "Deleting liquibase entries for ETL module"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM liquibasechangelog where id like 'kenyaemrChart%';"
echo
echo "Starting tomcat..."
echo

sudo service tomcat6 start



