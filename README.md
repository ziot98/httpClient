# httpClient
A program that can test Java HTTP communication using the command line.
It also shows TLS version and CipherSuite.
It can be executed as an argument whether to receive a response as a Json String or Base64.
It can be executed through HttpExecutor for convenient GUI-based execution.(https://github.com/ziot98/httpExecutor)
* Command Line Execute Example : java -jar C:\httpclient-jdk1.8-jar-with-dependencies.jar {\"METHOD\":\"POST\",\"HEADER\":{\"Content-Type\":\"application\/json\"},\"BODY\":\"{  \\\"testKey\\\" : \\\"testValue\\\"}\",\"URL\":\"https:\/\/testapi.com:443\/api\/test\/json.do\"} useBase64


## Requirements
* Java 6 or higher