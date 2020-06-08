#!/bin/sh
echo "Executing auto deployment script.."
app=$1
current_timestamp=$3
version=$2
userDir="/home/ec2-user"
webappsDir="$userDir/apps/xb"

"$webappsDir/$app/bin/$app" stop || true
echo "Waiting 30 seconds to close the services"
sleep 10
echo "$webappsDir/$app/bin/$app" status


archivedDir="$userDir/archived/$current_timestamp/$app/"
newZipFile="$userDir/releases/$current_timestamp/$app-$version.zip"
echo $newZipFile

mkdir "$webappsDir" || true

#Remove the files
rm -rf  $webappsDir/$app*


unzip "$newZipFile" -d "$webappsDir"
mv "$webappsDir/$app-$version" "$webappsDir/$app"


source ~/.bash_profile


#Copy war file to tomcat
#cp $newZipFileDir* $webappsDir

#start the tomcat
"$webappsDir/$app/bin/$app" start
"$webappsDir/$app/bin/$app" status


