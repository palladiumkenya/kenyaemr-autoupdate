#!/bin/sh
 
PACKAGE_NAME="kenyahmistoolkit"
PACKAGE_VERSION="1.1.7"
SOURCE_DIR=$PWD
TEMP_DIR="/tmp"
 
mkdir -p $TEMP_DIR/debian/DEBIAN
mkdir -p $TEMP_DIR/debian/lib
mkdir -p $TEMP_DIR/debian/usr/games
mkdir -p $TEMP_DIR/debian/usr/share/applications
mkdir -p $TEMP_DIR/debian/usr/share/$PACKAGE_NAME
mkdir -p $TEMP_DIR/debian/usr/share/doc/$PACKAGE_NAME
mkdir -p $TEMP_DIR/debian/usr/share/common-licenses/$PACKAGE_NAME
 
echo "Package: $PACKAGE_NAME" > $TEMP_DIR/debian/DEBIAN/control
echo "Version: $PACKAGE_VERSION" >> $TEMP_DIR/debian/DEBIAN/control
cat control >> $TEMP_DIR/debian/DEBIAN/control
cat preinst >> $TEMP_DIR/debian/DEBIAN/preinst
#cat postinst >> $TEMP_DIR/debian/DEBIAN/postinst
 
cp *.desktop $TEMP_DIR/debian/usr/share/applications/
cp copyright $TEMP_DIR/debian/usr/share/common-licenses/$PACKAGE_NAME/ # results in no copyright warning
cp copyright $TEMP_DIR/debian/usr/share/doc/$PACKAGE_NAME/ # results in obsolete location warning

chmod 0644 $TEMP_DIR/debian/usr/share/doc/$PACKAGE_NAME/
chmod 0644 $TEMP_DIR/debian/usr/share/common-licenses/$PACKAGE_NAME/
chmod 0775 $TEMP_DIR/debian/DEBIAN/preinst
#chmod 0775 $TEMP_DIR/debian/DEBIAN/postinst
 
cp *.jar $TEMP_DIR/debian/usr/share/$PACKAGE_NAME/
cp $PACKAGE_NAME $TEMP_DIR/debian/usr/games/

cp -r toolkitdependecies $TEMP_DIR/debian/usr/share/$PACKAGE_NAME/
 
echo "$PACKAGE_NAME ($PACKAGE_VERSION) trusty; urgency=low" > changelog
echo "  * Rebuild" >> changelog
echo " -- Serge Helfrich <helfrich@xs4all.nl>  `date -R`" >> changelog
gzip -9c changelog > $TEMP_DIR/debian/usr/share/doc/$PACKAGE_NAME/changelog.gz
 
cp *.png $TEMP_DIR/debian/usr/share/$PACKAGE_NAME/
chmod 0644 $TEMP_DIR/debian/usr/share/$PACKAGE_NAME/*png
 
PACKAGE_SIZE=`du -bs $TEMP_DIR/debian | cut -f 1`
PACKAGE_SIZE=$((PACKAGE_SIZE/1024))
echo "Installed-Size: $PACKAGE_SIZE" >> $TEMP_DIR/debian/DEBIAN/control
 
chown -R root $TEMP_DIR/debian/
chgrp -R root $TEMP_DIR/debian/
 
cd $TEMP_DIR/
dpkg --build debian
mv debian.deb $SOURCE_DIR/$PACKAGE_NAME-$PACKAGE_VERSION.deb
rm -r $TEMP_DIR/debian
