package niklasu.speedtester.config

class ValidationException : Exception {

    constructor(s: String) : super(s) {}

    constructor(message: String, e: Exception) : super(message, e) {}
}
