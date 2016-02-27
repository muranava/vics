#!/usr/bin/env bash

echo "Creating database and tables..."
psql -U postgres -f create.sql --quiet
echo "Creating constituencies..."
psql -U postgres -f constituencies.sql --quiet
echo "Creating wards..."
psql -U postgres -f wards.sql --quiet
echo "Creating users..."
psql -U postgres -f users.sql --quiet
echo "Done."
