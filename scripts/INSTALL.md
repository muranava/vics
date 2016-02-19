# Installation and runtime

**This guide needs elaboration**

## database

Execute the database install script to build the tables and insert the reference data (wards, constituencies and user)

```
$ ./install-db.sh
```

## web-client

Deploy the ```static``` directory into a file server such as nginx or haproxy

## web-server

Execute the ```run-web-server.sh``` script to launch the java web server application.

