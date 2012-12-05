#!/usr/bin/env bash

# $1 is the input


JAVA=`which java`
CLASSPATH=.:~/cs475-machine_learning/commons-cli-1.2/commons-cli-1.2.jar:~/cs475-machine_learning/code/bin

$JAVA -cp $CLASSPATH cs475.sum_product.SumProductTester $1
