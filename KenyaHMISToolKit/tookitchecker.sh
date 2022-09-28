#!/bin/sh
 # Script used to read Property File
FILE_NAME=/opt/kehmisApplicationToolbox/application.properties
# Key in Property File
key="Name"

# Variable to hold the Property Value
prop_value=""

getProperty()
{
        prop_key=$1
        prop_value=`cat ${FILE_NAME} | grep ${prop_key} | cut -d'=' -f2`
}

getProperty ${key}
echo "Key = ${key} ; Value = " ${prop_value}
