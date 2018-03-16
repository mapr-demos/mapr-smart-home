#!/bin/bash

#######################################################################
# Globals definition
#######################################################################
STREAM_PATH=/apps/smart-home-stream
EVENTS_TOPIC_NAME=events
NOTIFICATIONS_TOPIC_NAME=notifications

HOMES_TABLE_PATH=/apps/homes
EVENTS_TABLE_PATH=/apps/events
SENSORS_TABLE_PATH=/apps/sensors


#######################################################################
# Functions definition
#######################################################################

function print_usage() {
    cat <<EOM
Usage: $(basename $0) [-r|--recreate] [-h|--help]
Options:
    --recreate  When specified, MapR-DB JSON Tables and Stream will be recreated.

    --help      Prints usage information.
EOM
}

function create_table() {

    local TABLE_PATH=$1
    local RECREATE=$2

    EXISTS=0
    maprcli table info -path ${TABLE_PATH} > /dev/null
    EXISTS=$?

    if [ "$EXISTS" -eq 0 ] && [ "$RECREATE" -eq 0 ]; then
        echo "error: Table '$TABLE_PATH' already exists. Exiting. "
        echo "Please, specify '[-r|--recreate]' if you want to recreate it."
        exit 1
    fi

    maprcli table delete -path ${TABLE_PATH} > /dev/null
    maprcli table create -path ${TABLE_PATH} -tabletype json
    OUT=$?
    if [ $OUT -eq 0 ];then
        echo "Table '${TABLE_PATH}' successfully created!"
    else
        echo "error: Errors occurred while creating '$TABLE_PATH' table. Exiting."
        exit 1
    fi
}

function create_stream() {

    local STREAM_PATH=$1
    local RECREATE=$2

    EXISTS=0
    maprcli stream info -path ${STREAM_PATH} > /dev/null
    EXISTS=$?

    if [ "$EXISTS" -eq 0 ] && [ "$RECREATE" -eq 0 ]; then
        echo "error: Stream '$STREAM_PATH' already exists. Exiting. "
        echo "Please, specify '[-r|--recreate]' if you want to recreate it."
        exit 1
    fi

    maprcli stream delete -path ${STREAM_PATH} > /dev/null
    maprcli stream create -path ${STREAM_PATH}
    OUT=$?
    if [ $OUT -eq 0 ];then
        echo "Stream '$STREAM_PATH' successfully created!"
    else
        echo "error: Errors occurred while creating '$STREAM_PATH' stream. Exiting."
        exit 1
    fi
}

function create_topic() {
    local STREAM_PATH=$1
    local TOPIC_NAME=$2

    maprcli stream topic create -path ${STREAM_PATH} -topic ${TOPIC_NAME}

    OUT=$?
    if [ $OUT -eq 0 ];then
        echo "Topic '$STREAM_PATH:$TOPIC_NAME' successfully created!"
    else
        echo "error: Errors occurred while creating '$STREAM_PATH:$TOPIC_NAME' topic. Exiting."
        exit 1
    fi
}

function change_stream_permissions() {
    local STREAM_PATH=$1
    maprcli stream edit -path ${STREAM_PATH} -produceperm p -consumeperm p -topicperm p -copyperm p -adminperm p
}

function change_table_permissions() {
    local TABLE_PATH=$1
    maprcli table cf edit -path ${TABLE_PATH} -cfname default -readperm p -writeperm p -traverseperm  p
}

#######################################################################
# Parse options
#######################################################################

OPTS=`getopt -o hr --long help,recreate -n 'init-cluster.sh' -- "$@"`
eval set -- "$OPTS"

RECREATE=0
while true ; do
    case "$1" in
        -r|--recreate) RECREATE=1 ; shift ;;
        -h|--help) print_usage ; exit 0 ;;
        --) shift ; break ;;
        *) break ;;
    esac
done

#######################################################################
# Create MapR-DB tables
#######################################################################
create_table ${HOMES_TABLE_PATH} ${RECREATE}
create_table ${EVENTS_TABLE_PATH} ${RECREATE}
create_table ${SENSORS_TABLE_PATH} ${RECREATE}

#######################################################################
# Change MapR-DB Tables permissions
#######################################################################
change_table_permissions ${HOMES_TABLE_PATH}
change_table_permissions ${EVENTS_TABLE_PATH}
change_table_permissions ${SENSORS_TABLE_PATH}

#######################################################################
# Create MapR-DB Stream and topics
#######################################################################
create_stream ${STREAM_PATH} ${RECREATE}

create_topic ${STREAM_PATH} ${EVENTS_TOPIC_NAME}
create_topic ${STREAM_PATH} ${NOTIFICATIONS_TOPIC_NAME}

#######################################################################
# Change MapR-DB Stream permissions
#######################################################################
change_stream_permissions ${STREAM_PATH}
