# KenyaEMR-Autoupdate
### Overview
Provides a mechanism that works with the auto-update feature in the EMR to perform the following tasks once a release package is downloaded from github:
1. backup of db, modules, and web app in preparation of an upgrade
2. prepare rollback mechanism using the prepared backup in no.1 above
3. executes the upgrade script - copy web app, modules, and any metadata as provided in the downloaded release package
4. database backup scripts

### First-time setup
1. Download the repository files as a zip. (You can use git to clone the repository as well)
2. Extract the content of the downloaded package to the home directory, or any other preferred location.
3. Run the first_time_setup.sh script in the extracted directory using **bash first_time_setup.sh**. 
This will create KenyaEMRAutoupdate directory in the opt folder and copies all supporting files to the created directory.

### Usage
The setup process will provide for a KenyaEMR upgrade launcher on the desktop. 
- When a new release is updated in github, an admin user in the EMR is notified and a downloadable package (zipped) is provided. 
- A user downloads the package and extracts the contents which are then copied to the /opt/KenyaEMRAutoupdate/  
- Upgrade process is initiated by double-clicking the KenyaEMR upgrade launcher located at the desktop
- The launcher opens a terminal window where the user is guided through the upgrade process.

### Rollback
In the event a rollback is required after an upgrade process, please use the below steps:
1. Navigate to the /opt/KenyaEMRAutoupdate/rollback-tools directory on a terminal.
2. Execute/run the setup using **sh setup_script.sh**.


### Points to note
- Ensure the downloaded package is extracted successfully and the contents copied to /opt/KenyaEMRAutoupdate directory. The desktop launcher has been pre-configured to look into this directory for the process initiation
- The setup scripts have been pre-configured to work on Ubuntu. Other OS users may need to modify the scripts appropriately
- The setup scripts have been pre-configured for tomcat6. Setups with other versions of tomcat may need to change the scripts appropriately


