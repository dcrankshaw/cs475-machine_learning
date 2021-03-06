#!/usr/bin/env bash

# $1 is the classifier

#datasets=( bio finance nlp speech vision hard easy)
datasets=(bio easy finance hard speech vision nlp)

# CLASSIFIER=$1
CLASSIFIER=instance_bagging
TRAIN=./instbag_train.sh
TEST=./instbag_test.sh

for i in "${datasets[@]}"
do
  echo "Train $i : `$TRAIN $CLASSIFIER $i`"
done

echo

for i in "${datasets[@]}"
do
  echo "Test $i : `$TEST $CLASSIFIER $i`"
done
