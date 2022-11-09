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
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/dictionary/kenyaemr_2x_concepts_dump-2022-10-10.sql" 
echo

if [ "$?" -gt 0 ]; then
  echo "MYSQL encountered a problem while processing KenyaEMR concepts."
  exit 1
else
  echo "Successfully updated concept dictionary .........................."
fi

echo "Deleting liquibase entries for ETL modules updates"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM liquibasechangelog where id like 'kenyaemrChart%';"

echo "Deleting draft manifest"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM kenyaemr_order_entry_lab_manifest_order where status = 'Draft';"
echo

echo "Truncating ML and Location Tables"
 mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/ml.sql"
  
echo "Updating registry endpoints"
  mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/update_registry_endpoints.sql"  

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
echo "Starting tomcat..."
echo

echo ${authorization} | sudo -S service tomcat9 start



