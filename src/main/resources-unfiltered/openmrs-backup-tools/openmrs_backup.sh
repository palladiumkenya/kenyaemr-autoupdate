#!/bin/bash

authorization=$1
mysql_password=$2

# Get script directory
SCRIPT_PATH="${BASH_SOURCE[0]}";
echo ${authorization} | sudo -S echo $SCRIPT_PATH
#SCRIPT_PATH=/home/antony/Documents/Work/kenyaEMR/openmrs-backup-tools;
if ([ -h "${SCRIPT_PATH}" ]) then
  while([ -h "${SCRIPT_PATH}" ]) do SCRIPT_PATH=`readlink "${SCRIPT_PATH}"`; done
fi
pushd . > /dev/null
cd `dirname ${SCRIPT_PATH}` > /dev/null
SCRIPT_PATH=`pwd`;
popd  > /dev/null

# Load configuration values
. $SCRIPT_PATH/backup.conf

# Fail function to record error in syslog
fail() {
	logger -t $LOGGING_TAG -p local0.crit $1
	echo $1
	exit 1
}

# Check runtime properties file exists
if ! [ -e "$OPENMRS_PROP_FILE" ]; then
	fail "Specified OpenMRS runtime properties file does not exist"
fi

# Read properties from properties file

dbuser=root #`sed '/^\#/d' "$OPENMRS_PROP_FILE" | grep 'connection.username' | tail -n 1 | cut -d "=" -f2-`
dbpass=${mysql_password} # `sed '/^\#/d' "$OPENMRS_PROP_FILE" | grep 'connection.password' | tail -n 1 | cut -d "=" -f2-`
dburl=`sed '/^\#/d' "$OPENMRS_PROP_FILE" | grep 'connection.url' | tail -n 1 | cut -d "=" -f2-`

# Check properties could be read
if [ -z $dbuser ] || [ -z $dbpass ] || [ -z $dburl ]; then
	fail "Unable to read OpenMRS runtime properties"
dbuser='root'
dbpass= ${mysql_password}
dburl='localhost:3306'
fi



# Extract database name from connection URL
if [[ $dburl =~ /([a-zA-Z0-9_\-]+)\? ]]; then
	dbname=${BASH_REMATCH[1]}
else
	dbname="openmrs"
fi
# Dump the database

echo ${authorization} | sudo -S bash $SCRIPT_PATH/db_dump.sh $dbname $dbuser $dbpass $BACKUP_DEST_DIR


# Check dump was successful
if [ $? -eq 0 ]; then
	logger -t $LOGGING_TAG -p local0.info "Database dump successful"
else
	echo "Got an error: $?"	
	fail "Unable to dump database (name=$dbname, user=$dbuser)"
fi

# Cleanup old dumps
#bash $SCRIPT_PATH/rotate.sh $DAILY_KEEP_DAYS $WEEKLY_KEEP_WEEKS $MONTHLY_KEEP_MONTHS $BACKUP_DEST_DIR
