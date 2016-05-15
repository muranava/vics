#!/usr/bin/env bash

ELB_DEV="https://WebApp-57219498.eu-west-1.elb.amazonaws.com:18080/api/canvass"
ELB_STAGING="https://WebApp-2103587711.eu-west-1.elb.amazonaws.com:18080/api/canvass"
ELB_PROD="https://WebApp-731548696.eu-west-1.elb.amazonaws.com:18080/api/canvass"

rm -rf ../dist/client-dev
rm -rf ../dist/client-staging
rm -rf ../dist/client-prod

cp -r ../dist/client ../dist/client-dev
cp -r ../dist/client ../dist/client-staging
cp -r ../dist/client ../dist/client-prod

sed -i '' "s;CANVASS_API_ELB_ENDPOINT;$ELB_DEV;g" ../dist/client-dev/scripts/scripts.*.js
sed -i '' "s;CANVASS_API_ELB_ENDPOINT;$ELB_STAGING;g" ../dist/client-staging/scripts/scripts.*.js
sed -i '' "s;CANVASS_API_ELB_ENDPOINT;$ELB_PROD;g" ../dist/client-prod/scripts/scripts.*.js
