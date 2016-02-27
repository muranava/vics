# canvass

Application to generate canvass cards and record canvassing activities.

## Stack

The UI is written in JS, HTML and CSS using AngularJS (v1.4) as a client side SPA.

The web app serving the client is written in Java. Spring Boot is the web framework.  Data between
the UI and server is REST over HTTP serialized as JSON.  PDFs are generated on the server and 
returned to the client as byte arrays.

A security system is implemented using Spring security and Redis for session state persistence.
The application should be deployed using TLS.

PostgreSQL is used for persistence.

## Build & Test

Install maven 3 and Java 8, then test and build the entire project from the root directory as follows

    mvn clean install

This will generate a ```/dist``` folder that contains all artefacts for deployment - minified javascript/html/css, java server as a fat jar and the database installation scripts that contain reference data.

## Project Modules

    * web-server - the main web application that provides data to the UI and manages users (Java/Spring Boot)
    * web-client - the user interface (AngularJS app)
    * common - utilities and language extensions that can be reused across all modules
    * common-dto - data transfer objects that can be shared between modules
    * test-support - test related utilities, matchers and builders
    * pdf-gen - code to generate PDFs from elector data
    * tools - scripts and tools for managing data and deploying the application

