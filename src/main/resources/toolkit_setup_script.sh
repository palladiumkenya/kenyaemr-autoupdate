#!/bin/bash
# Script to backup MySQL databases


modules_dir=/var/lib/OpenMRS/modules
tomcat_dir=/var/lib/tomcat9/webapps
frontend_dir=/var/lib/OpenMRS/frontend
configuration_dir=/var/lib/OpenMRS/configuration
confguration_check_sum_dir=/var/lib/OpenMRS/configuration_checksums

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

echo "Deleting liquibase entries for ETL modules updates"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM liquibasechangelog where id like 'kenyaemrChart%';"

echo "Deleting liquibase entries for ML modules updates"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM liquibasechangelog where id like '%kenyaemr-ML%';"
echo

echo "Deleting address layout format"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM global_property where property like 'layout.address.format%';"


echo "Deleting Queue concept names"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM global_property where property like 'queue.priorityConceptSetName%';"

mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM global_property where property like 'queue.serviceConceptSetName%';"

mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} -Bse "DELETE FROM global_property where property like 'queue.statusConceptSetName%';"

echo "Update address template layout format"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/addressTemplate/address_layout_format.sql" 

echo

echo "Truncate patient appontments and queue  tables"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/HIV/patient_appointment.sql" 
echo

echo "update drugs"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/drug/drug_2023_05_12.sql" 
echo

echo "Set location tag map for login,queues and appointment"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/location_tag_map/location_tag_map.sql" 

echo
echo "Create appointment services"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/appointment_service/appointment_services.sql" 

echo
echo "Create queues"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/queue/queues.sql" 


echo "Update HIV followup appointments"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/HIV/First_script_HIV_Followup.sql" 

echo "Update HIV drug refill appointments"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/HIV/Second_script_refill.sql" 

echo "Create relationship between Follow up and drug refill appointments"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/HIV/Third_script_relate_follow_refill.sql" 
echo

echo "Create other appointments i.e MCH, PREP, KP and CWC"
mysql --user=${mysql_user} --password=${mysql_password} ${mysql_base_database} < "${script_dir}/scripts/other_appointments/other_appointments.sql" 
echo

echo "Finished updating appointments and queues"
echo

echo "Deleting address hierarchy configuration checksum"
echo
sudo ${authorization} | sudo -S rm -R ${confguration_check_sum_dir}/

echo "Finished deleting address configurations"
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

echo "Granting read permission to the frontend directory: ${frontend_dir}."
sudo chmod --recursive +rw ${frontend_dir}/*
sudo chown tomcat:tomcat  --recursive ${frontend_dir}/*

echo "Granting read permission to the configuration directory: ${configuration_dir}."
sudo chmod --recursive 777 ${configuration_dir}/*
sudo chown tomcat:tomcat  --recursive ${configuration_dir}/*

echo
echo "Starting tomcat..."
echo

echo ${authorization} | sudo -S service tomcat9 start



