#!/usr/bin/env bash

# $1 is the classifier

#datasets=( bio finance nlp speech vision hard easy)
#datasets=(bio easy finance hard speech speech.mc vision nlp)
datasets=(bio finance nlp speech.mc speech easy hard vision)

# CLASSIFIER=$1
CLASSIFIER=lambda_means
TRAIN=./kmeans_train.sh
TEST=./kmeans_test.sh

for i in "${datasets[@]}"
do
  echo "Train $i : `$TRAIN $CLASSIFIER $i`"
done

echo

for i in "${datasets[@]}"
do
  echo "$i"
  echo "`$TEST $CLASSIFIER $i`"
done
