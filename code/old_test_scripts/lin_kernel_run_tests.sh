#!/usr/bin/env bash

# $1 is the classifier

#datasets[0]='bio'
#datasets[1]='easy'
#datasets[2]='finance'
#datasets[3]='hard'
#datasets[4]='nlp'
#datasets[5]='speech'
#datasets[6]='vision'
datasets=( circle bio finance nlp speech vision hard easy)
#datasets=( circle bio finance speech vision hard easy)
#datasets=(nlp)

# CLASSIFIER=$1
CLASSIFIER=logistic_regression_linear_kernel
TRAIN=./lin_kernel_train.sh
TEST=./lin_kernel_test.sh

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
