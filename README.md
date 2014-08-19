# Request

Provides a standard way to run and handle requests. It enables easy swapping out of request libraries using **RequestExecutors** 


## Getting Started

Including in your project: 

1. Clone this repo into /ProjectRoot/Libraries/
2. Add this line to your settings.gradle:

```groovy

  include ':Libraries:Request'

```

3. Add this line to your build.gradle:

```groovy

  dependencies {
    compile project(':Libraries:Request');
  }

```

## Usage

### Running a simple request

Requests "google.com" and expects to receive a JSON response.

```java

private RequestExecutor<String> mRequestExecutor = new VolleyExecutor();

private void someMethod(){
  new Request.Builder<String>(mRequestExecutor)
        .provider(new SimpleUrlProvider("http://www.google.com/")
        .responseHandler(new SimpleJsonResponseHandler())
        .execute(mRequestCallback);
}

private RequestCallback<JSONObject> mRequestCallback = new RequestCallback<JSONObject>() {
        @Override
        public void onRequestDone(JSONObject data) {
           // Success
        }

        @Override
        public void onRequestError(Throwable error, String stringError) {
            // There was an error, stringError is string rep of the error
        }
    };

```

