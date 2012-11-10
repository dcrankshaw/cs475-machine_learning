#!/usr/bin/env bash

# $1 is classifier
# $2 is dataset (speech, bio, etc.)

JAVA=`which java`

#EXTRA_FILES=~/cs475-machine_learning/hw1/01_all_extra_files_1/
DATA_LOC=~/cs475-machine_learning/regression_data
CLASSPATH=.:~/cs475-machine_learning/commons-cli-1.2/commons-cli-1.2.jar:~/cs475-machine_learning/code/bin

$JAVA -cp $CLASSPATH cs475.Classify -mode test \
  -model_file model.$1.$2 -data $DATA_LOC/$2.dev \
  -predictions_file predictions.$1.$2 -algorithm $1

