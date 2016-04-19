# vics

Voter Intention Collection System. An application to generate canvass cards and record canvass activities.

## Stack

The UI is written in JS, HTML and CSS using AngularJS (v1.4) as a client side SPA.

The web app serving the client is written in Java. Spring Boot is the web framework.  Data between
the UI and server is REST over HTTP serialized as JSON.  PDFs are generated on the server and 
returned to the client as byte arrays.

A security system is implemented using Spring security and Redis for session state persistence.
The application should be deployed using TLS.

PostgreSQL is used for persistence.

## Build & Test

Install redis and ensure the redis server is running locally. This is needed to run integration tests (it would be nicer to use the embedded redis server for testing...)

    redis-server
    
Install postgres and create a database locally called ```canvassapp``` with username ```postgres``` and password ```postgres```

Install maven 3 and Java 8, then test and build the entire project from the root directory as follows

    mvn clean install

This will generate a ```/dist``` folder that contains all artefacts for deployment - minified javascript/html/css, java server as a fat jar and the database installation scripts that contain reference data.

## Project Modules

    * web-server - the main web application that provides data to the UI and manages users (Java/Spring Boot)
    * web-client - the user interface (AngularJS app)
    * common - utilities and language extensions that can be reused across all modules
    * paf-stub - mocks the upstream paf api. Can be executed from tests and run standalone
    * tools - scripts and tools for managing data and deploying the application
    * perf-tests - Gatling performance test scenarios

## AWS deployment

CodeDeploy is used to deploy the server side of the app to a deployment group with auto scaling.
The client side is deployed into an S3 bucket and Cloudfront is used to manage serving the static content.

* Server side deployment - to deploy a new revision of the web api, simply generate the app by running the maven build, then
upload the built tar in the CodeDeploy UI (the maven build will auto deploy to an S3 bucket called 993854-maven). After uploading
the tar, the app will be scaled out to all EC2 instances.

* Client side deployment - simply upload the built dist/client to the 993854-staticobjects S3 bucket (I plan to get jenkins to automate this task...)
