#!/bin/bash
# Script to backup MySQL databases


modules_dir=/var/lib/OpenMRS/modules
tomcat_dir=/var/lib/tomcat9/webapps

#script directory
current_dir=$(pwd)
script_dir=$(dirname $0)

if [ $script_dir = '.' ]
then
script_dir="$current_dir"
fi
echo script_directory: ${script_dir}

# MySQL settings
# MySQL settings
authorization=$1
mysql_user="root"
mysql_password=$2
mysql_base_database="openmrs"



# Read MySQL password from stdin if empty
if [ -z "${mysql_password}" ]; then
  echo -n "Enter MySQL ${mysql_user} password: "
  read -s mysql_password
  echo
fi

# Check MySQL password
echo exit | mysql --user=${mysql_user} --password=${mysql_password} -B 2>/dev/null
if [ "$?" -gt 0 ]; then
  echo "MySQL ${mysql_user} password incorrect"
  exit 1
else
  echo "MySQL ${mysql_user} password correct."
fi
echo
echo "Stopping tomcat..."
echo
echo ${authorization} | sudo -S service tomcat9 stop

echo "upgrading Concept Dictionary to the latest"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/dictionary/kenyaemr_buffered_concepts_dump-2023-02-21.sql" 
echo

if [ "$?" -gt 0 ]; then
  echo "MYSQL encountered a problem while processing KenyaEMR concepts."
  exit 1
else
  echo "Successfully updated concept dictionary .........................."
fi

echo "Deleting liquibase entries for ETL modules updates"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM liquibasechangelog where id like 'kenyaemrChart%';"

echo "Deleting Duplicate Manifests"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM kenyaemr_order_entry_lab_manifest_order WHERE id NOT IN (
select * from (SELECT MIN(id) FROM kenyaemr_order_entry_lab_manifest_order GROUP BY manifest_id, order_id) as x);"
echo

echo "Truncating ML Tables"
  mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/ml.sql"
  

echo "Deleting old .omod files."
echo
echo ${authorization} | sudo -S rm -R ${modules_dir}/*.omod
echo "Finished deleting old .omod files."
echo "Copying new .omod files."
echo
echo ${authorization} | sudo -S cp ${script_dir}/modules/*.omod ${modules_dir}/

echo "Finished copying new .omod files."
echo
echo "Granting read permission to the modules directory: ${modules_dir}."
echo ${authorization} | sudo -S chmod --recursive +r ${modules_dir}/*.omod
echo ${authorization} | sudo -S chown tomcat:tomcat  --recursive ${modules_dir}/*.omod
echo

echo
sudo ${authorization} | sudo -S rm -R ${frontend_dir}/
sudo ${authorization} | sudo -S mkdir ${frontend_dir}
echo "Finished creating frontend directory"
echo

echo
sudo ${authorization} | sudo -S rm -R ${configuration_dir}/
sudo ${authorization} | sudo -S mkdir ${configuration_dir}
echo "Finished creating configuration directory"
echo

echo "Copying frontend assets."
echo

sudo ${authorization} | sudo -S cp -R ${current_dir}/frontend/* ${frontend_dir}/

echo "Finished copying frontend assets."
echo

echo "Copying configuration assets."
echo
sudo ${authorization} | sudo -S cp  -R "${current_dir}"/configuration/* ${configuration_dir}/
echo "Finished copying configuration assets."
echo


echo "Granting read permission to the modules directory: ${modules_dir}."
sudo ${authorization} | sudo -S chmod --recursive +r ${modules_dir}/*.omod
sudo ${authorization} | sudo -S chown tomcat:tomcat  --recursive ${modules_dir}/*.omod

echo "Granting read permission to the frontend directory: ${frontend_dir}."
sudo ${authorization} | sudo -S chmod --recursive +rw ${frontend_dir}/*
sudo ${authorization} | sudo -S chown tomcat:tomcat  --recursive ${frontend_dir}/*

echo "Granting read permission to the configuration directory: ${configuration_dir}."
sudo ${authorization} | sudo -S chmod --recursive 777 ${configuration_dir}/*
sudo ${authorization} | sudo -S chown tomcat:tomcat  --recursive ${configuration_dir}/*


echo
echo "Starting tomcat..."
echo

echo ${authorization} | sudo -S service tomcat9 start



