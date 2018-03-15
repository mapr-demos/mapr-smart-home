#!/bin/bash

#######################################################################
# Initializes MapR-DB JSON Tables with test data
#######################################################################

mapr dbshell insert --table /apps/homes --value '{"_id":"553d1f54-8956-4452-9e98-b7981fa133da","name":"Smart\ Home\ 1","address":"Fake\ street,\ 1"}'

mapr dbshell insert --table /apps/sensors --value '{"_id":"501c21a6-829a-4b1a-84f1-2d2f3c161fe1","home_id":"553d1f54-8956-4452-9e98-b7981fa133da","name":"Boiler\ temperature\ sensor","conversions":["metrics.temperature\ >\ 50"]}'

mapr dbshell insert --table /apps/sensors --value '{"_id":"586a6d46-e891-4aeb-8f94-d4efac836df2","home_id":"553d1f54-8956-4452-9e98-b7981fa133da","name":"Complex\ multi-metric\ sensor","conversions":["metrics.brightness\ >\ 95","metrics.speed\ >\ 250"]}'
