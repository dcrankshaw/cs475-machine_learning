#!/usr/bin/env bash

# $1 is classifier
# $2 is dataset (speech, bio, etc.)

JAVA=`which java`

# EXTRA_FILES=~/cs475-machine_learning/hw1/01_all_extra_files_1/
CLASSPATH=.:~/cs475-machine_learning/commons-cli-1.2/commons-cli-1.2.jar:~/cs475-machine_learning/code/bin

$JAVA -cp $CLASSPATH cs475.Classify -mode train -algorithm $1 \
  -model_file model.$1.$2 -data ~/cs475-machine_learning/data/$2.train \
  -k_ensemble 5 -ensemble_learning_rate 0.1 -ensemble_training_iterations 5
