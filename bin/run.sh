#!/bin/bash

#######################################################################
# Globals definition
#######################################################################
WORK_DIR=/usr/share/mapr-apps/mapr-smart-home


# Check if 'DRILL_NODE' environment varaible set
if [ ! -z ${TSDB_NODE+x} ]; then # DRILL_NODE exists
    echo "OpenTSDB node: $TSDB_NODE"
else
    echo 'TSDB_NODE environment variable is not set. Please set it and rerun.'
    exit 1
fi

# Change permissions
sudo chown -R ${MAPR_CONTAINER_USER}:${MAPR_CONTAINER_GROUP} ${WORK_DIR}

java -jar ${WORK_DIR}/event-processor/target/scala-2.11/event-processor-assembly-0.1.0-SNAPSHOT.jar &
java -jar ${WORK_DIR}/webapp/target/scala-2.12/webapp-assembly-1.0-SNAPSHOT.jar &
java -jar ${WORK_DIR}/event-sink-mapr-db-json/target/scala-2.11/event-mapr-db-sink-assembly-0.1.0-SNAPSHOT.jar &
java -jar ${WORK_DIR}/event-sink-open-tsdb/target/scala-2.11/event-open-tsdb-sink-assembly-0.1.0-SNAPSHOT.jar ${TSDB_NODE} &
java -jar ${WORK_DIR}/event-generator/target/scala-2.11/event-generator-assembly-0.1.0-SNAPSHOT.jar &

( cd ${WORK_DIR}/web-client/ ; forever start node_modules/@angular/cli/bin/ng serve)

# TODO Start Grafana & Grafana MapR-DB Plugin

sleep infinity
