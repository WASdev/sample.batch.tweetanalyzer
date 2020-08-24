# TweetAnalyzer sample for Liberty Batch

TweetAnalyzer is a fun sample application demonstrating Liberty's implementation of the Batch Programming Model in Java EE 7, as specified by JSR 352. 

TweetAnalyzer lets you capture Tweets about a topic and batch process them to analyze their sentiment and popularity. After processing, the results are readily viewable in the browser. The default topic is the New York City MTA, since it is an eternally popular social media topic with a high entertainment factor.

## !! Disclaimer/Warning - Profanity Potential !! 
Twitter users express a wide spectrum of emotions, often using rather colorful language.  Since this application involves ingesting the contents of the internet, the material may not always be PG-rated, and may include obscene, offensive language.

## Setting Up the API Keys

### Twitter API

To access the Twitter API, you will need to set up credentials. 

https://developer.twitter.com/en/apply-for-access

#### Access from sample

Store these in a file named **twitter4j.properties** in any directory you like.  You will reference these later, via the "snatcher" utility.  

It should look like:

```
debug=false
tweetModeExtended=true
oauth.consumerKey=...
oauth.consumerSecret=...
oauth.accessToken=...
oauth.accessTokenSecret=...
jsonStoreEnabled=true
```


### Watson API

To access the Watson Natural Language Understanding API, you will need to set up another set of credentials.

https://www.ibm.com/watson/developercloud/natural-language-understanding/api/v1/#authentication

https://console.bluemix.net/docs/services/watson/getting-started-credentials.html#creating-credentials

#### Access from sample

Store these in a file named **watson.properties** in any directory you like.  You will reference these later as a batch "job property".

It should look like:

```
version=2018-03-16
username=...
password=...
```


## Collect the Tweets

Use the included "snatcher" utility.

### Prepare

Since this will write tweets to the **snatcher/tweets** folder, we can keep things simpler by deleting this first.

```rm -r snatcher/tweets``` 

### Build and run

First switch to the **snatcher** directory:

```cd snatcher``` 

then build and run with:

```mvn clean compile exec:java -Dexec.args="/path/to/twitter4j.properties"```

This will use Twitter4J to start up a stream listening to Twitter for Tweets about the New York City subway system.

The tweet snatcher will save incoming tweets that match your desired criteria, writing them to the **snatcher/tweets** folder in a file named by date and time. 
Every minute, it will roll over to a new file.  A dot is printed for each tweet captured.
 
Allow the snatcher to run for as long as you please, then hit *Return/Enter* to stop.  Note that the snatcher might not immediately stop.  A new tweet needs to be captured to 'wake up' the snatcher so it can notice the enter.
Rename/copy the folder somewhere (which you will reference later).

You are now ready to proceed to the batch process/visualization step. 

### Custom tags

To filter with other tags, follow the properties file name with a space and a comma separated (no spaces) list of tags.

```mvn exec:java -Dexec.args="/path/to/twitter.properties  #myTopic,mytopic"``` 


## Process the Tweets

### Parameterize the job

Edit the job XML to point to each of the 'tweets' directory of saved tweet files captured by the "snatcher" and to your **watson.properties** file of Watson API credentials.

In [TweetFileProcessing.xml](src/main/resources/META-INF/batch-jobs/TweetFileProcessing.xml) edit these two job properties within the job XML:

```
    <properties>
        <property name="defaultInputDir" value="/my/tweets"/>
        <property name="defaultWatsonPropFile" value="/my/watson.properties"/>
    </properties>
```

The **defaultInputDir** property should point to a directory of tweet files captured by the "snatcher" utility. (Unzip into a directory if you had earlier zipped them up.)  


### Run the job

Switch to the repository root directory (make sure you're not still in the **snatcher** directory) and type: 

```mvn install```  

Followed by:

```mvn liberty:run```

The application includes a "startup EJB" (impemented via [ControllerBean.java](src/main/java/com/ibm/websphere/sample/startup/ControllerBean.java)) which runs the job when the application starts.

Watch the job begin execution on app startup.

Wait for the batch processing to complete. 

## Display the Tweets

Now that the tweets have been batch-processed, open your browser to:
[localhost:9080/web](http://localhost:9080/web).

Admire your tweets!


## More Info

To change the UI and build the changes, see [here](docs/ModifyingTuningSample.md)


Please refer to the [ci.maven](https://github.com/WASdev/ci.maven) repository for documentation about using the Liberty Maven Plug-in.

# Notice

© Copyright IBM Corporation 2018.

# License

```text
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````
