Alexa learning
====
Application can be used for learning/memorizing sentences. It was created during Amazon
Alexa Hackathon in Warsaw, Poland at 09.04.2016. 
The application consists of:
* java backend that needs to be deployed on EC2 or some other hosting
* python script that needs to be deployed in AWS Lambda and is used as a proxy between Alexa and backend
* AngularJS frontend 
* utterances, slot types and intents (see interaction-model directory) that need to be defined in Amazon Alexa Skills Kit on developer.amazon.com (see [here]( https://developer.amazon.com/appsandservices/solutions/alexa/alexa-skills-kit/docs/developing-an-alexa-skill-as-a-lambda-function))

Local testing
====
You can test it on your local instance:

* git clone
* mvn spring-boot:run (in project root directory)
* open your browser and type http://localhost:8080
* you can simulate Alexa requests by sending POST requests to http://localhost:8080/api/echo

Each request's body should be in JSON format:
```json
{ 
    "method" : "{intentName}", 
    "parameters" : {
        "{parameter1Name}" : "{parameter1Value}", 
        "{parameter2Name}" : "{parameter2Value}" 
        ... 
    }
}
```

Lambda testing
====
Most convenient way to test lambda is by running test event in Lambda section of AWS. It doesn't require physical device (such as Echo) and is very effective for small changes in lambda (e.g. new exception handling), checking configuration and server responses.
Sample test event based on the one created in amazon.alexa.com used to check HowManyWordsIntent flow. 
Just paste it in AWS: Code->Actions->Configure Test Event and change applicationId or delete check in lambda.

```
{
 "session": {
   "sessionId": "SessionId.WHATEVER",
   "application": {
     "applicationId": "YOUR-APP-ID"
   },
   "user": {
     "userId": "USER-ID"
   },
   "new": true
 },
 "request": {
   "type": "IntentRequest",
   "requestId": "EdwRequestId.WHATEVER",
   "timestamp": "2016-04-09T12:44:40Z",
   "intent": {
     "name": "HowManyWordsIntent",
     "slots": {
       "Date": {
         "name": "Date",
         "value": "2016-04-11"
       }
     }
   },
   "locale": "en-US"
 },
 "version": "1.0"
}
```

Backend build and deployment
====

* mvn clean package (in project root directory)
* send target/alexa-learning-0.0.1-SNAPSHOT.jar to your hosting (for example via scp)
* install JRE 8 on your hosting
* execute following command "java -jar alexa-learning-0.0.1-SNAPSHOT.jar" 
