package niklasu.speedtester.downloader

/**
 * Created by enzo on 24.08.2015.
 */
class DownloadException : Exception {

    constructor(s: String) : super(s) {}

    constructor(message: String, e: Exception) : super(message, e) {}
}
