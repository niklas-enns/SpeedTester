# SpeedTester

SpeedTester is a tool which tests the speed of your internet connection periodically by downloading a file and measuring the elapsed time

SpeedTester can run as a normal command-line application which can be called with ``java -jar SpeedTester.jar``.

## Configuration Parameters:
* `size`: The download size of the file which is used to calculcate your connection speed, in MB
  * default: 50MB
* `interval`: The download interval in minutes
  * default: 1min
* `url`: The target file url
  * no default value
* `influx-host`: if set, measurements will be pushed to an influxDB
    * db-name: `influx`
    * measurement: `speed`
    * value: `value`

## Example:
``
java -jar SpeedTester.jar -size 20 -interval 1 -url http://www.downloadhost.com/path/to/file.mp4
``

### InfluxDB & Grafana

Based on [DockerGrafanaInfluxKit](https://github.com/BushnevYuri/DockerGrafanaInfluxKit)

* `cd infra && docker-compose up -d`
* visit `http://localhost:3000/` and login with `admin:admin`
* import the dashboard located at `infra/grafana/SpeedTester-dashboard.json`
* select InfluxDB as data source

`docker-compose up -d ` will create a volume where influxDB and Grafana persist data. In order to stop influxDB and Grafana, run `docker-compose down`.
However, this will leave the created volumes. You can remove them by searching them via `docker volume ls` and then remove via `docker volume rm <volume_name>` 