#!/usr/bin/env bash

ProductionEnvironment=NO

if [ "$ProductionEnvironment" = "YES" ] ; then
    exit 0
else
    echo "Creating database and tables..."
    psql -U postgres -f db/create.sql --quiet
    echo "Creating constituencies..."
    psql -U postgres -f db/constituencies.sql --quiet
    echo "Creating wards..."
    psql -U postgres -f db/wards.sql --quiet
    echo "Creating users..."
    psql -U postgres -f db/users.sql --quiet
    echo "Done."
fi