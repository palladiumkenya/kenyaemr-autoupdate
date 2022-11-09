KenyaHMIS Application Toolbox
=============================
A javaFX application designed to handle deployment of various project applications. 

This initial version still supports downloading and deployment of KenyaEMR with support for more applications in future releases

### Requirements
1. java 11 and above
2. openjfx 11 - install using *sudo apt install openjfx*
3. maven

### Packaging
1. Clone the project into a directory of choice
2. Navigate to the project's root directory
3. Build using maven by running *mvn package* in the terminal
4. Copy the applicationtoolkit-1.0-SNAPSHOT-jar-with-dependencies.jar file from target to kenyaemr-autoupdate/KenyaHMISToolKit 
5. Rename the file to kenyahmistoolkit.jar
6. Open the packagedeb.sh using an editor
7. Change PACKAGE_VERSION="x.x" in line 4 to represent the version you are building. 

This process uses maven shade plugin that provides a fat jar file that can be deployed. 


### Deployment

#### Required directory structure
The app is configured to use /opt/kehmisApplicationToolbox/Downloads directory for its operations. Create the directories using:

1. sudo mkdir /opt/kehmisApplicationToolbox/
2. sudo mkdir /opt/kehmisApplicationToolbox/Downloads
3. sudo chmod 777 -R /opt/kehmisApplicationToolbox/

You can launch the jar file using the below command. 

    java --module-path /usr/share/openjfx/lib --add-modules ALL-MODULE-PATH -jar Untitled.jar

If your default java is not 11, please use the fully qualified path for java i.e. 

    /usr/lib/jvm/java-1.11.0-openjdk-amd64/bin/java --module-path /usr/share/openjfx/lib --add-modules ALL-MODULE-PATH -jar applicationtoolkit-1.0-SNAPSHOT.jar

Useful resources
- https://maven.apache.org/plugins/maven-shade-plugin/
- https://stackoverflow.com/questions/52130548/how-do-i-fix-javafx-runtime-components-are-missing
- https://openjfx.io/openjfx-docs/



