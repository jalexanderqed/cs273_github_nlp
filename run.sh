#!/bin/bash

mvn compile && mvn exec:java -Dexec.mainClass="edu.ucsb.cs273.Main" -Dexec.args="$1 $2 $3 $4 $5"
