#!/usr/bin/env bash

# $1 is classifier
# $2 is mode (train or test)
# $3 is dataset (speech, bio, etc.)

JAVA=`which java`

EXTRA_FILES=~/cs475-machine_learning/hw1/01_all_extra_files_1/
CLASSPATH=.:~/cs475-machine_learning/commons-cli-1.2/commons-cli-1.2.jar:$EXTRA_FILES/library.jar

$JAVA -cp $CLASSPATH cs475.Classify -mode $2 -algorithm $1 \
  -model_file model.$1 -data $EXTRA_FILES/$3.train

