[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Broker-red.svg?style=flat)](https://android-arsenal.com/details/1/1256) [![Raizlabs Repository](http://img.shields.io/badge/Raizlabs%20Repository-1.1.0-blue.svg?style=flat)](https://github.com/Raizlabs/maven-releases)

# Broker

A façade between executing requests and creating them. The library provides a wrapper for creating requests, but delegates the actual execution to ```RequestExecutors``` to enable a unified API for requests. It also utilizes **annotation processing** when creating REST interfaces to simplify code you write dramatically. 

## Getting Started

### Remotely

Add the repo as a maven url to your classpath:

```

buildscript {
      repositories {
        maven { url "https://raw.github.com/Raizlabs/maven-releases/master/releases" }
      }
}

```

Add this line to your build.gradle, using the [apt plugin](https://bitbucket.org/hvisser/android-apt) and the 
[AARLinkSources](https://github.com/xujiaao/AARLinkSources) plugin:

```groovy

dependencies {
  apt 'com.raizlabs.android:Broker-Compiler:1.1.0'
  aarLinkSources 'com.raizlabs.android:Broker-Compiler:1.1.0:sources@jar'
  compile 'com.raizlabs.android:Broker-Core:1.1.0'
  aarLinkSources 'com.raizlabs.android:Broker-Core:1.1.0:sources@jar'
  compile 'com.raizlabs.android:Broker: 1.1.0'
  aarLinkSources 'com.raizlabs.android:Broker:1.1.0:sources@jar'

}


```

#### Volley Support

To use the provided ```VolleyExecutor```, add these lines:

```java

      compile 'com.raizlabs.android:Broker-Volley:1.1.0'
      aarLinkSources 'com.raizlabs.android:Broker-Volley:1.1.0:sources@jar'

```

#### WebServiceManager

To use ```WebServiceManager``` ([repo](https://github.com/Raizlabs/RZAndroidWebServiceManager)), add these lines:

```java

      compile 'com.raizlabs.android:Broker-WebServiceManager:1.1.0'
      aarLinkSources 'com.raizlabs.android:Broker-WebServiceManager:1.1.0:sources@jar'

```

### Locally: 

Add the ```request_project_prefix``` to your ```gradle.properties``` file, to elminate the need to fork and change the build.gradle of the project.

Add these lines to your build.gradle:

```groovy

  dependencies {
    apt project(request_project_prefix + "Broker-Compiler")
    compile project(request_project_prefix + "Broker-Core")
    compile project(request_project_prefix + "Broker")
  }

```

## Usage

### Configuration

You will need to extend the ```Application``` class for proper configuration:

```java

public class ExampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // replace sharedExecutor with the default executor you wish to use
        RequestConfig.init(this, sharedExecutor);
    }
}

```

In order to set up the library properly, you will need to use one of the following for the shared ```requestExecutor```:
  1. ```VolleyExecutor``` from ```Broker-Volley```
  2. ```WebServiceManagerExecutor``` from ```Broker-WebServiceManager```
  3. A custom ```RequestExecutor```


Lastly, add the definition to the manifest (with the name that you chose for your custom application):

```xml

<application
  android:name="{packageName}.ExampleApplication"
  ...>
</application>

```

### Request and Request.Builder

A ```Request``` requires the following:

1. ```UrlProvider``` : an interface that makes it easy to specify a method, base url, and end url. Simple implementation is the ```SimpleUrlProvider```. 
2. ```RequestExecutor``` : actually handles the request and is up to the executor how the request is run. Constructing one without one specified will run the shared executor from ```RequestConfig```.
3. A predefined ``ResponseType`` that must match the ```ResponseHandler```'s return type.

Supports:

1. Custom contentTypes
2. Adding a body to the request
3. Url Params
4. Request headers
5. Adding metadata to attach to the specific request
6. Multipart data
7. Downloading files to a predetermined location.
8. Priority

### REST Interfaces

This library supports annotation processing for generating ```$RestService``` classes that contain all of the code of constructing the intended request. This enables the call to these interfaces as fast as possible.

Note: Each REST interface must be an __interface__ since the generated code has a base class.

Features:
  1. Define own ```RequestExecutor```
  2. Define a shared ```ResponseHandler```, otherwise use a shared map of them.
  3. BaseUrl as a string or resource id.

#### Simple example

For this example we use dummy data and define 3 annotations:

```@RestService```: Specify either a baseUrl or baseUrlResId for the service
```@RequestExecutor```: Tells what ```RequestExecutor``` to create for this service to use. The default is a shared ```RequestConfig.getSharedExecutor()```. 
```@ResponseHandler```: Defines the default ```ResponseHandler``` class to use if none is specified for each ```@Method```.

```java

@RestService(baseUrl = "https://www.google.com")
@RequestExecutor(VolleyExecutor.class)
@ResponseHandler(SimpleJsonResponseHandler.class)
public interface SimpleRestService {

}

```

Next we define each ```@Method``` we want to generate from this interface. 

```@Method```: Defines a url (that's appended to the end of the base url), what HTTP method to use (use ```@Method``` constants), and static HTTP headers. If we place any string within it enclosed by "{}" it becomes an ```@Endpoint``` variable with the same name. 

Within the function parameters, we must use annotations to specify what parameter goes where:

```@Endpoint```: Marks the parameter as corresponding to a bracket-enclosed piece of the ```@Method``` url. This enables dynamic endpointing. The name of the variable MUST match the name in the URL. 

```@Header```: The parameter is a request header with a specified static key. 

```@Metadata```: passes the parameter as a tag or piece of information about the request. Can only be one per method.

```@Param```: Marks the parameter as a url parameter where the value is URL encoded by default. 

```@ResponseHandler```:  allows for a different response handler for this method than the default in the class. **Warning:** using non-default will create a new one for each specified using reflection. 

```@Body```: The parameter is the body to the request. It must be a String, ```InputStream```, or ```File```. 

```@Part```: The part of a multipart request. If ```isFile()``` true, the variable its associated with becomes the path, otherwise it is the text value of the part.

```java

    public static final String POSTS = "posts";

    public static final String COMMENTS = "comments";


    @Method(url = POSTS)
    public void fetchPostsByUserId(@Param("userId") long userID,
                                   RequestCallback<JSONArray> requestCallback);

    @Method(url = POSTS)
    public void fetchAllPosts(JsonArrayCallback requestCallback);

    @Method(url = COMMENTS)
    public void fetchAllComments(JsonArrayCallback callback);

    @Method(url = "/{firstLevel}/{secondLevel}/{thirdLevel}")
    public void fetchData(@Endpoint String firstLevel, @Endpoint String secondLevel, @Endpoint String thirdLevel,
                          RequestCallback<JSONArray> jsonArrayRequestCallback);

    @Method(url = POSTS + "/{userId}", method = Method.PUT)
    @ResponseHandler(SimpleJsonResponseHandler.class)
    public Request<JSONObject> updateCommentsWithUserId(@Body String putData, @Endpoint String userId, RequestCallbackAdapter<JSONObject> requestCallback);

    @Method(url = "/{firstLevel}/{secondLevel}/{thirdLevel}")
    @ResponseHandler(SimpleJsonResponseHandler.class)
    public Request<JSONObject> getFetchDataRequest(@Endpoint String firstLevel, @Endpoint String secondLevel, @Endpoint String thirdLevel);

    @Method(url = COMMENTS)
    public Request.Builder<JSONObject> getCommentsRequestBuilder();

    @Method(url = COMMENTS)
    public Request<JSONArray> getPostsByUserIdParamRequest(@Param("userId") long userId, @Param("id") long id);

    @Method(url = COMMENTS)
    public void postCommentData(@Part(name = "image", isFile = true) String imageFilePath, @Part(name = "caption") String caption);
}

```

### UrlProvider
It enables enums and other classes to provide a url for the request in a standardized fashion.

#### Example

This is an example, where we define a base url that the other enum objects use in combination with its own defined endpoint. Works very well with REST APIs, or swapping between different endpoints.

```java

private enum AppUrlProvider implements UrlProvider {

        DEV {
            @Override
            public String getUrl() {
                return "dev/config/appConfig.json";
            }
        },
        LIVE {
            @Override
            public String getUrl() {
                return "live/config/appConfig.json";
            }
        };

        @Override
        public String getBaseUrl() {
            return "https://www.someurl.com/";
        }

        @Override
        public Request.Method getMethod() {
            return Request.Method.GET;
        }


    }

```

### RequestCallback

The main response interface that gets called when the request finishes. ```onRequestDone(ResponseType response)``` is for a success and ```onRequestError(Throwable error, String stringError)``` is meant for failures. The parameter of this callback **MUST** match the return type of the ```ResponseHandler```.

#### Example

```java

private RequestCallback<AppConfig> mRequestCallback = new RequestCallback<AppConfig>() {
        @Override
        public void onRequestDone(AppConfig appFeatureControl) {
            if(appFeatureControl != null) {
                Toast.makeText(MainActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRequestError(Throwable error, String stringError) {
            Toast.makeText(MainActivity.this, stringError, Toast.LENGTH_SHORT).show();
        }
    };

```

### RequestExecutor

This the main interface by which a request is executed. Any library that we use for networking, we should create a **Request** executor to plug into this module.

Override the ```execute(Request request)``` method and handle the data that is passed in through the **Request** object.

### ResponseHandlers

ResponseHandlers define how to convert a response into another. It's ```ResponseType``` should match the ```RequestCallback```'s type-param. 

#### Example

Using our other library, [Parser](https://github.com/Raizlabs/Parser) (which also uses annotation processing, for parsing data into objects) this response handler converts the string data into an ```AppConfig``` object. 

Example of converting a string into an ```AppConfig``` object. 

```java

private class ParserResponseHandler implements ResponseHandler<String, AppConfig> {

        @Override
        public AppConfig processResponse(String s) {
                return ParserHolder.parse(AppConfig.class, new JSONObject(s));
        }
    }

```
