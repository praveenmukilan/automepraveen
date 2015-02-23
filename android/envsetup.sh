#rm -rf android/target/surefire-reports*
#rmdir "$HOME/APPIUM/APK"
#mkdir "$HOME/APPIUM/APK"
cd "$HOME/APPIUM/APK"
#ls *.apk | xargs rm -rf
#wget --no-check-certificate ${APK_URL}
export APK=$(echo "${APK_URL}"|sed -E "s/.*\/(.*\.apk)/\1/")
echo $APK
export APKPATH="$HOME/APPIUM/APK/${APK}" 
cd "$WORKSPACE/android"
echo "APKPATH=$APKPATH" > apkFile.properties

#Start Appium Server  /Applications/Appium.app/Contents/Resources/node/bin/
#/Applications/Appium.app/Contents/Resources/node/bin/node /Applications/Appium.app/Contents/Resources/node_modules/appium/bin/appium.js --address "127.0.0.1" --port "6723" --bootstrap-port 6724 --log "$HOME/APPIUM/AppiServer.log" --webhook "localhost:9876" --log-timestamp --local-timezone

#Start Emulator
echo "Start Emulator"
/Applications/Genymotion.app/Contents/MacOS/player --vm-name "SamsungGalaxyS4" &
