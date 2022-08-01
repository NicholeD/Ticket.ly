#!/bin/bash
failures=0
trap 'failures=$((failures+1))' ERR
./gradlew ticketsystem-integration-task1
./gradlew ticketsystem-integration-task2
./gradlew ticketsystem-integration-task3
./gradlew ticketsystem-integration-task4
./gradlew ticketsystem-integration-task5
./gradlew ticketsystem-integration-task6
if ((failures == 0)); then
  echo "Success"
else
  echo "$failures failures"
  exit 1
fi