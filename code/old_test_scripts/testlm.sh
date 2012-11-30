#!/usr/bin/env bash

CLASSPATH=.:~/cs475-machine_learning/commons-cli-1.2/commons-cli-1.2.jar:~/cs475-machine_learning/code/bin


JAVA=`which java`

$JAVA -cp $CLASSPATH cs475.TestLambdaMeans
