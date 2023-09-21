#!/bin/sh
 
PACKAGE_NAME="kenyahmistoolkit"
PACKAGE_VERSION="1.0.2"
SOURCE_DIR=$PWD
TEMP_DIR="/tmp/kenyahmistoolkit"
 
mkdir -p $TEMP_DIR/debian/DEBIAN
mkdir -p $TEMP_DIR/debian/lib
mkdir -p $TEMP_DIR/debian/usr/games
mkdir -p $TEMP_DIR/debian/usr/share/applications
mkdir -p $TEMP_DIR/debian/usr/share/$PACKAGE_NAME
mkdir -p $TEMP_DIR/debian/usr/share/doc/$PACKAGE_NAME
mkdir -p $TEMP_DIR/debian/usr/share/common-licenses/$PACKAGE_NAME

### Download and Inject the JDK
rm -rf $TEMP_DIR/debian/opt/java/toolkit/
mkdir -p $TEMP_DIR/debian/opt/java/toolkit/

if [ -e "jdk-17_linux-x64_bin.tar.gz" ]; then
    # If the jdk exists, just extract it
    echo "JDK file exists. Now extracting ..."
    # move file to destination
    mv jdk-17_linux-x64_bin.tar.gz $TEMP_DIR/debian/opt/java/toolkit/
    # tar -xvf jdk-17_linux-x64_bin.tar.gz -C $TEMP_DIR/debian/opt/java/toolkit/
    # extract it
    cd $TEMP_DIR/debian/opt/java/toolkit/ && tar -xvf jdk-17_linux-x64_bin.tar.gz --strip-components=1 --wildcards '*/' && cd $SOURCE_DIR
    rm -rf $TEMP_DIR/debian/opt/java/toolkit/jdk-17_linux-x64_bin.tar.gz
    chmod -R 777 $TEMP_DIR/debian/opt/
else
    # if the jdk doesnt exist, we attempt to download it
    wget -O jdk-17_linux-x64_bin.tar.gz https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.tar.gz

    if [ $? -eq 0 ] && [ -e "jdk-17_linux-x64_bin.tar.gz" ]; then
        echo "JDK Download successful. Now extracting ..."
        # tar -xvf jdk-17_linux-x64_bin.tar.gz -C $TEMP_DIR/debian/opt/java/toolkit/
        # move file to destination
        mv jdk-17_linux-x64_bin.tar.gz $TEMP_DIR/debian/opt/java/toolkit/
        # extract it
        cd $TEMP_DIR/debian/opt/java/toolkit/ && tar -xvf jdk-17_linux-x64_bin.tar.gz --strip-components=1 --wildcards '*/' && cd $SOURCE_DIR
        rm -rf $TEMP_DIR/debian/opt/java/toolkit/jdk-17_linux-x64_bin.tar.gz
        chmod -R 777 $TEMP_DIR/debian/opt/
    else
        echo "JDK Download failed. No need to proceed"
        # Exit with code 11 -- Failed to install JDK
        exit 11
    fi
fi

echo "SUCCESS: JDK Setup Complete"

### Download and Inject the JavaFX SDK
rm -rf $TEMP_DIR/debian/opt/openjfx/
mkdir -p $TEMP_DIR/debian/opt/openjfx/

if [ -e "openjfx-17.0.8_linux-x64_bin-sdk.zip" ]; then
    # If the JavaFX SDK exists, just extract it
    echo "JavaFX SDK file exists. Now extracting ..."
    # move file to destination
    # mv openjfx-17.0.8_linux-x64_bin-sdk.zip $TEMP_DIR/debian/opt/openjfx/
    # unzip openjfx-17.0.8_linux-x64_bin-sdk.zip -d $TEMP_DIR/debian/opt/openjfx/
    mkdir tmp
    unzip openjfx-17.0.8_linux-x64_bin-sdk.zip -d tmp/
    mv tmp/*/* $TEMP_DIR/debian/opt/openjfx/
    rm -rf tmp
    chmod -R 777 $TEMP_DIR/debian/opt/
else
    # if the JavaFX SDK doesnt exist, we attempt to download it
    wget -O openjfx-17.0.8_linux-x64_bin-sdk.zip https://download2.gluonhq.com/openjfx/17.0.8/openjfx-17.0.8_linux-x64_bin-sdk.zip

    if [ $? -eq 0 ] && [ -e "openjfx-17.0.8_linux-x64_bin-sdk.zip" ]; then
        echo "JavaFX SDK Download successful. Now extracting ..."
        # unzip openjfx-17.0.8_linux-x64_bin-sdk.zip -d $TEMP_DIR/debian/opt/openjfx/
        mkdir tmp
        unzip openjfx-17.0.8_linux-x64_bin-sdk.zip -d tmp/
        mv tmp/*/* $TEMP_DIR/debian/opt/openjfx/
        rm -rf tmp
        rm -rf openjfx-17.0.8_linux-x64_bin-sdk.zip
        chmod -R 777 $TEMP_DIR/debian/opt/
    else
        echo "JavaFX SDK Download failed. No need to proceed"
        # Exit with code 12 -- Failed to install java FX
        exit 12
    fi
fi

echo "SUCCESS: JavaFX Setup Complete"

 
echo "Package: $PACKAGE_NAME" > $TEMP_DIR/debian/DEBIAN/control
echo "Version: $PACKAGE_VERSION" >> $TEMP_DIR/debian/DEBIAN/control
cat control >> $TEMP_DIR/debian/DEBIAN/control
cat preinst >> $TEMP_DIR/debian/DEBIAN/preinst
cat postinst >> $TEMP_DIR/debian/DEBIAN/postinst
 
cp *.desktop $TEMP_DIR/debian/usr/share/applications/
cp copyright $TEMP_DIR/debian/usr/share/common-licenses/$PACKAGE_NAME/ # results in no copyright warning
cp copyright $TEMP_DIR/debian/usr/share/doc/$PACKAGE_NAME/ # results in obsolete location warning

chmod 0644 $TEMP_DIR/debian/usr/share/doc/$PACKAGE_NAME/
chmod 0644 $TEMP_DIR/debian/usr/share/common-licenses/$PACKAGE_NAME/
chmod 0775 $TEMP_DIR/debian/DEBIAN/preinst
chmod 0775 $TEMP_DIR/debian/DEBIAN/postinst
 
cp *.jar $TEMP_DIR/debian/usr/share/$PACKAGE_NAME/
cp $PACKAGE_NAME $TEMP_DIR/debian/usr/games/

#cp -r toolkitdependecies $TEMP_DIR/debian/usr/share/$PACKAGE_NAME/
 
echo "$PACKAGE_NAME ($PACKAGE_VERSION) trusty; urgency=low" > changelog
echo "  * Rebuild" >> changelog
echo " -- Patrick Waweru <patrick.waweru@thepalladiumgroup.com>  `date -R`" >> changelog
gzip -9c changelog > $TEMP_DIR/debian/usr/share/doc/$PACKAGE_NAME/changelog.gz
 
cp *.png $TEMP_DIR/debian/usr/share/$PACKAGE_NAME/
chmod 0644 $TEMP_DIR/debian/usr/share/$PACKAGE_NAME/*.png
 
PACKAGE_SIZE=`du -bs $TEMP_DIR/debian | cut -f 1`
PACKAGE_SIZE=$((PACKAGE_SIZE/1024))
echo "Installed-Size: $PACKAGE_SIZE" >> $TEMP_DIR/debian/DEBIAN/control
 
chown -R root $TEMP_DIR/debian/
chgrp -R root $TEMP_DIR/debian/
 
cd $TEMP_DIR/
dpkg --build debian
mv debian.deb $SOURCE_DIR/$PACKAGE_NAME-$PACKAGE_VERSION.deb
rm -rf $TEMP_DIR/debian
