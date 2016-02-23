# canvass

Application to generate canvass cards and record canvassing activities.

## Stack

The UI is written in JS, HTML and CSS using AngularJS (v1.4) as a client side SPA.

The web app serving the client is written in Java. Spring Boot is the web framework.  Data between
the UI and server is REST over HTTP serialized as JSON.  PDFs are generated on the server and 
returned to the client as byte arrays.

PostgreSQL is used for persistence.

## Build & Test

Install maven 3 and Java 8, then test and build the entire project from the root directory as follows

    mvn clean install

This will generate a ```/dist``` folder that contains all artefacts for deployment - minified javascript/html/css, java server as a fat jar
and the database installation scripts that contain reference data

## Project Modules

    * common - utilities and language extensions that can be reused across all modules
    * common-dto - data transfer objects that can be shared between modules
    * test-support - test related utilities, matchers and builders
    * pdf-gen - code to generate PDFs from elector data
    * web-server - the main web application that provides data to the UI and manages users (Java/Spring Boot)
    * tools - scripts and tools around for managing data and deploying the application
    * web-client - the user interface (AngularJS)

## Electoral Roll Number (ERN)

The electoral roll number is the combination of the following:

```
{Polling district}-{Elector ID}-{Elector Suffix}
```

