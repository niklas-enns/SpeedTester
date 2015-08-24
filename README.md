#SpeedTester

SpeedTester is a tool which tests the speed of your internet connection periodically by downloading a file and writes the results with timestamps into the file "results.txt".

If supported, SpeedTester can settle in the tray menu. I tried it with windows 7,8,10, openSuse and linux mint.
Otherwise, SpeedTester can run as a normal command-line application which can be called with java -jar SpeedTester.jar.

##Parameters:
The work of SpeedTester uses four parameters:
- size : The size of the file which is used to calculcate your connection speed
- interval: The download interval in minutes
- url: The target file url
- tray: Enables a GUI which is accessible through a tray icon

##Default values:
You don't have to declare these parameters. SpeedTester has built-in default values: 50MB size, 1 minute interval, a nice URL, enabled tray icon. For normal use-cases this should be fine. Just run SpeedTester and you will notice that "results.txt" will be contain a new result every minute.

If you want to change some parameters, you can just use the GUI or pass command-line arguments to the application.

##Arguments:
- size <size>: Target download size in MB
- interval <interval>: Download interval in minutes
- url <URL>: Target file URL. Works with http://, ftp:// and maybe some others which i havent't dried yet.
- tray [true|false]: Enables or disables the tray icon and GUI

##Example:
java -jar SpeedTester.jar -size 20 -interval 1 -url http://www.downloadhost.com/path/to/file.mp4 -tray true
