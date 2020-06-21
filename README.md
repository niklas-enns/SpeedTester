# SpeedTester

SpeedTester is a tool which tests the speed of your internet connection periodically by downloading a file and measuring the elapsed time

SpeedTester can run as a normal command-line application which can be called with ``java -jar SpeedTester.jar``.

## Configuration Parameters:
* size: The download size of the file which is used to calculcate your connection speed, in MB
  * default: 50MB
* interval: The download interval in minutes
  * default: 1min
* url: The target file url
  * no default value
  * can also be set via env DOWNLOAD_URL

## Example:
``
java -jar SpeedTester.jar -size 20 -interval 1 -url http://www.downloadhost.com/path/to/file.mp4
``

## Build
### Build and Push to docker hub via jib
Set envs for maven
* REGISTRY_USERNAME
* REGISTRY_PASSWORD

`compile jib:build`

### Run as docker image
For example `docker run -p8080:7000 -e DOWNLOAD_URL=<url>`