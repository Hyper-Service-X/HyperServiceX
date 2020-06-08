#!/bin/sh
echo "Executing auto deployment script.."
app=$1
current_timestamp=$2
userDir="/home/ec2-user"
tomcatDir="$userDir/tomcat-$app"

webappsDir="$tomcatDir/webapps/"
archivedDir="$userDir/archived/$current_timestamp/$app/"
newWarFileDir="$userDir/war_files/$current_timestamp/$app/"
echo $newWarFileDir

source ~/.bash_profile


#Create the archivedDir, copy war file, deployment files
mkdir -p  $archivedDir
cp -r  $webappsDir* $archivedDir

#Stop the server if running
if pgrep -U ec2-user -f  $tomcatDir;
 then pkill -U ec2-user -f  $tomcatDir;
else
 echo "App not running";
fi

#Remove the files
rm -rf  $webappsDir$app*

#Copy war file to tomcat
cp $newWarFileDir* $webappsDir

#start the tomcat
"$tomcatDir/bin/startup.sh"


