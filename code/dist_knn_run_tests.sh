#!/usr/bin/env bash

# $1 is the classifier

datasets=( easy medium hard nasdaq )

# CLASSIFIER=$1
CLASSIFIER=knn_distance
TRAIN=./dist_knn_train.sh
TEST=./dist_knn_test.sh

for i in "${datasets[@]}"
do
#  echo "Train `${datasets[$i]}` : `$TRAIN $CLASSIFIER ${datasets[$i]}`"
#  echo "Test `${datasets[$i]}` : `$TEST $CLASSIFIER ${datasets[$i]}`"
  echo "Train $i : `$TRAIN $CLASSIFIER $i`"
#  echo "Test $i : `$TEST $CLASSIFIER $i`"
done

echo

for i in "${datasets[@]}"
do
#  echo "Train `${datasets[$i]}` : `$TRAIN $CLASSIFIER ${datasets[$i]}`"
#  echo "Test `${datasets[$i]}` : `$TEST $CLASSIFIER ${datasets[$i]}`"
#  echo "Train $i : `$TRAIN $CLASSIFIER $i`"
  echo "Test $i : `$TEST $CLASSIFIER $i`"
done
