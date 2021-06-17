#!/bin/bash
set -x
GOAL=${1:-migrate}
SCHEME=${2:-p27}

echo "RUNNING FLYWAY FOR ${SCHEME}"
SCHEMA_OWNER="CPUI_DB_OWNER"
SCHEMA_USER="CPUI_DB_USER"

FLYWAY_PARAMS="-schemas=$SCHEMA_OWNER,$SCHEMA_USER"
FLYWAY_PLACEHOLDERS="-placeholders.schemaOwner=$SCHEMA_OWNER -placeholders.schemaUser=$SCHEMA_USER -placeholders.project.version=local-version"

docker run --rm --network host -v /"${PWD}"/database/migrations/data/${SCHEME}:/flyway/sql/data/ -v /"${PWD}"/database/migrations/common:/flyway/sql/common/ flyway/flyway -url=jdbc:oracle:thin:@127.0.0.1:1521/PDB112 -user='SYS as SYSDBA' -password=Password123 $FLYWAY_PARAMS $FLYWAY_PLACEHOLDERS $GOAL
