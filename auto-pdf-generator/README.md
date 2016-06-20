Purpose
=======

This project generates gotv PDFs of all electoral districts (wards) for backup purposes.

# Build executable jar

The following command creates a fat jar that can be executed from the command line

    mvn assembly:single

creates `pdf-generator.jar`

# Running the tool

    nohup java -DpafApiUrl=http://paf-elb/v1 -DpafApiToken=apitoken -DpdfServerUrl=http://pdf-server:18080/api/pdf -DwardsCsv=all-wards.csv -jar pdf-generator.jar &
