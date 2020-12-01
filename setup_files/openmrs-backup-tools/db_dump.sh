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

echo "passed details: dbname: $dbname user: $dbuser password: $dbpass dumpdir: $dumpdir"
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
rollbackDir=/opt/KenyaEMRAutoupdate/rollback/db
# Check destination directory exists and is writable
if [ -d "$rollbackDir" ] &&  [ -w "$rollbackDir" ]; then
	echo "Copying the backup file to the rollback directory"
	sudo cp $dumpfile $rollbackDir
	echo "Successfully copied the backup file to the rollback directory"
fi