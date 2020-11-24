# KenyaEMR-Autoupdate
###Overview
Provides packaging of KenyaEMR releases and ability to run an upgrade from a desktop launcher/shortcut.
It requires provision for the following stubs:
1. web application
2. modules
3. dictionary
4. database backup scripts

###Setup
1. Clone the repository using git
2. cd into the cloned repository i.e. cd kenyaemr-autoupdate
3. Run the first_time_setup.sh script using bash first_time_setup.sh

###Usage
The setup process copies files to appropriate locations in the file system. It adds a desktop launcher which on click executes, on a terminal, a setup_script.sh at /opt/KenyaEMRAutoupdate directory. 
Please note that altering the location of the setup_script.sh, and any other files within the directory, will affect the functioning of the auto-update feature.

