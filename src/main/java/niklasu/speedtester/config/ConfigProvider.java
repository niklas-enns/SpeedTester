package niklasu.speedtester.config;

public record ConfigProvider(long size, int interval, String url, String influxHost, String influxToken, String influxOrg, String influxBucket) {
}
