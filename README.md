#SpeedTester

SpeedTester is a tool which tests the speed of your internet connection periodically by downloading a file and measuring the elapsed time. Then, the results will be listed with according timestamps in the file "results.txt".

![alt tag](https://github.com/niklasu/SpeedTester/blob/master/Screenshot.png)

If supported, SpeedTester can settle in the tray menu. It should work with Windows 7,8,10, openSuse and Linux Mint.
Otherwise, SpeedTester can run as a normal command-line application which can be called with ``java -jar SpeedTester.jar``. Depending on your OS configuration, a (double-)click on the jar file will also execute it.

##Parameters:
The operation of SpeedTester is controlled by four parameters
* size: The download size of the file which is used to calculcate your connection speed, in MB
 * default: 50MB
* interval: The download interval in minutes
 * default: 1min
* url: The target file url
 * (default: [link](http://ftp.halifax.rwth-aachen.de/opensuse/distribution/13.2/iso/openSUSE-13.2-DVD-i586.iso))
* tray: Enables a GUI which is accessible through a tray icon
 * default: enabled

If you want to change some parameters, you can just use the GUI via the tray icon or pass command-line arguments to the application.


##Example:
``
java -jar SpeedTester.jar -size 20 -interval 1 -url http://www.downloadhost.com/path/to/file.mp4 -tray true
``
