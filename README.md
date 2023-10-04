KenyaHMIS Application Toolbox
=============================
A javaFX application designed to handle deployment of various project applications. 

This initial version still supports downloading and deployment of KenyaEMR with support for more applications in future releases

### Requirements
1. java 17 and above - bundled into the package. Copy into </opt/java/toolkit/>
2. openjfx 17.0.8 - bundled into the package. Copy into </opt/openjfx/>
3. maven

### Packaging
1. Clone the project into a directory of choice
2. Navigate to the project's root directory
3. If you dont have JDK 17, download it from https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.tar.gz and extract to </opt/java/toolkit/>
4. If you dont have JavaFX 17.0.8, download it from https://download2.gluonhq.com/openjfx/17.0.8/openjfx-17.0.8_linux-x64_bin-sdk.zip and extract to </opt/openjfx/>
5. If you dont have Maven, Install maven with <sudo apt update && sudo apt install maven>
6. Build using maven by running <./mvnw clean package> in the terminal
7. Navigate to the KenyaHMISToolKit directory
8. There is the kenyahmistoolkit.jar file. This needs to be uploaded for the release.
8. Run the packagedeb.sh <sudo ./packagedeb.sh> to build the .deb package. This can be used to deploy the toolkit on debian/ubuntu systems 
    NB: this gives you the kenyahmistoolkit-x.x.x.deb for the release. This needs to be uploaded for the release.
9. The build process also creates the following zipped files in the KenyaHMISToolKit directory:
        |-- openmrs-backup-tools.zip
        |-- rollback-tools.zip
        |-- scripts.zip
10. These zip files need to be uploaded during the release

This process uses maven shade plugin that provides a fat jar file that can be deployed. 

### RELEASE
1. KenyaEMR is normally released at: https://github.com/palladiumkenya/kenyahmis-releases/releases
    NB: This is where the toolkit looks for new KenyaEMR releases
    NB: Always tag the release with the version number of KenyaEMR e.g 18.6.2
        Folder structure of the release e.g for version 18.6.2:
            |--  KenyaEMR_18.6.2.zip
            |--  Kenyaemr.Release.Notes.v.18.6.2.pdf 

2. Toolkit is normally released at: https://github.com/palladiumkenya/kenyaemr-autoupdate/releases
    NB: This is where the toolkit looks for new KenyaEMR Toolkit releases
    NB: Always tag the release with the version number of Toolkit e.g 1.2.0
            Folder structure of the release e.g for version 1.2.0:
            |--  kenyahmistoolkit-1.2.0.deb  <-- debian package for ubuntu/debian based systems
            |--  kenyahmistoolkit.jar  <-- the latest jar file
            |--  openmrs-backup-tools.zip
            |--  rollback-tools.zip
            |--  scripts.zip

### Deployment

#### Required directory structure
The app is configured to use /opt/kehmisApplicationToolbox/Downloads directory for its operations. Create the directories using:

1. sudo mkdir /opt/kehmisApplicationToolbox/
2. sudo mkdir /opt/kehmisApplicationToolbox/Downloads
3. sudo chmod 777 -R /opt/kehmisApplicationToolbox/

You can launch the jar file using the below command. 

    java --module-path /opt/openjfx/lib --add-modules ALL-MODULE-PATH -jar /usr/share/kenyahmistoolkit/kenyahmistoolkit.jar

If your default java is not 17, please use the fully qualified path for java i.e. 

    /opt/java/toolkit/bin/java --module-path /opt/openjfx/lib --add-modules ALL-MODULE-PATH -jar /usr/share/kenyahmistoolkit/kenyahmistoolkit.jar

Or 
    Just use the desktop shortcut

Useful resources
- https://maven.apache.org/plugins/maven-shade-plugin/
- https://stackoverflow.com/questions/52130548/how-do-i-fix-javafx-runtime-components-are-missing
- https://openjfx.io/openjfx-docs/

#### References
```
REF: JavaFX - https://gluonhq.com/products/javafx/
     JavaFX - https://download2.gluonhq.com/openjfx/17.0.8/openjfx-17.0.8_linux-x64_bin-sdk.zip
REF: JDK 17 - https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.tar.gz
REF: KenyaEMR Release - https://github.com/palladiumkenya/kenyahmis-releases/releases/latest
REF: kenyahmistoolkit Release - https://github.com/palladiumkenya/kenyaemr-autoupdate/releases/latest
```

