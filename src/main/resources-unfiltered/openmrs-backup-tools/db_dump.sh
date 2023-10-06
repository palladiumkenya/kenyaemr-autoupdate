#!/bin/bash

#
# Dumps a database. 
#
# Usage: db_dump <dbname> <dbuser> <dbpassword> <dumpdir>
#

# Gather script arguments
dbname=$1
dbuser=$2
dbpass=$3
dumpdir=$4

# Check destination directory exists and is writable
if ! [ -d "$dumpdir" ] || ! [ -w "$dumpdir" ]; then
	echo "Dump directory does not exist or is not writable"
	exit 1
fi

# Determine whether this is daily/weekly/monthly based on current date
dayofmonth=`date +%d` # 01-31
dayofweek=`date +%u` # 1-7 (Monday-Sunday)

if [ $dayofmonth == "01" ]; then
	dumpfilename="${dbname}-`date '+%Y-%m-%d_%H:%M:%S'`-monthly.gz"
elif [ $dayofweek == "1" ]; then
	dumpfilename="${dbname}-`date '+%Y-%m-%d_%H:%M:%S'`-weekly.gz"
else
	dumpfilename="${dbname}-`date '+%Y-%m-%d_%H:%M:%S'`-daily.gz"
fi 

dumpfile="$dumpdir/$dumpfilename"

# Delete dump file if it already exists
if [ -e $dumpfile ]; then
	sudo rm $dumpfile
fi

# Dump OpenMRS database and gzip result
sudo mysqldump -u$dbuser -p$dbpass $dbname | gzip -c > $dumpfile


# Check dump was successful
if [ ${PIPESTATUS[0]} -ne 0 ]; then
	echo "MySQL dump failed"
	exit 1
fi

# Copy the new file to the rollback directory

rollback=/opt/kehmisApplicationToolbox/rollback/
rollbackDir=/opt/kehmisApplicationToolbox/rollback/db
modules=/opt/kehmisApplicationToolbox/rollback/modules
frontend=/opt/kehmisApplicationToolbox/rollback/frontend
webapps=/opt/kehmisApplicationToolbox/rollback/webapps

#Create rollback
if [[ ! -e $rollback ]]; then
    mkdir $rollback
elif [[ ! -d $rollback ]]; then
    echo "$rollback already exists but is not a directory" 1>&2
fi

#Create db
if [[ ! -e $rollbackDir ]]; then
    mkdir $rollbackDir
elif [[ ! -d $rollbackDir ]]; then
    echo "$rollbackDir already exists but is not a directory" 1>&2
fi

#Create Modules
if [[ ! -e $modules ]]; then
    mkdir $modules
elif [[ ! -d $modules ]]; then
    echo "$modules already exists but is not a directory" 1>&2
fi

#Create Frontend
if [[ ! -e $frontend ]]; then
    mkdir $frontend
elif [[ ! -d $frontend ]]; then
    echo "$frontend already exists but is not a directory" 1>&2
fi

#Create Webapps
if [[ ! -e $webapps ]]; then
    mkdir $webapps
elif [[ ! -d $webapps ]]; then
    echo "$webapps already exists but is not a directory" 1>&2
fi

sudo chmod -r 777  $rollbackDir
echo " Assigning Rights to $rollbackDir directory" 1>&2

# Check destination directory exists and is writable
if [ -d "$rollbackDir" ] &&  [ -w "$rollbackDir" ]; then
	echo "Copying the backup file to the rollback directory"
	sudo cp $dumpfile $rollbackDir
	sudo cp /var/lib/OpenMRS/modules/*.omod $modules
	sudo cp -rR /var/lib/OpenMRS/frontend/* $frontend
	sudo cp /var/lib/tomcat9/webapps/openmrs.war $webapps
	echo "Successfully copied the backup file to the rollback directory"
fi