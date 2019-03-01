package niklasu.speedtester.downloader

class DownloadException : Exception {

    constructor(s: String) : super(s) {}

    constructor(message: String, e: Exception) : super(message, e) {}
}
