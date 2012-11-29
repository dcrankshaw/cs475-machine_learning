#!/usr/bin/env bash

BASE=~/cs475-machine_learning/image_data

echo " colors noise 1 `./mrf_run.sh $BASE/cs475.2_colors.noise1 $BASE/cs475.2_colors`"
echo " colors noise 2 `./mrf_run.sh $BASE/cs475.2_colors.noise2 $BASE/cs475.2_colors`"
#
#echo " dredze noise 1 `./mrf_run.sh $BASE/dredze.greyscale.noisy1 $BASE/dredze.greyscale`"
#echo " dredze noise 2 `./mrf_run.sh $BASE/dredze.greyscale.noisy2 $BASE/dredze.greyscale`"

echo " easy 1 noise 1 `./mrf_run.sh $BASE/easy.1.2_colors.noisy $BASE/easy.1.2_colors`"
echo " easy 1 noise 2 `./mrf_run.sh $BASE/easy.1.2_colors.noisy2 $BASE/easy.1.2_colors`"

echo " easy 2 noise 1 `./mrf_run.sh $BASE/easy.2.2_colors.noisy $BASE/easy.2.2_colors`"
echo " easy 2 noise 2 `./mrf_run.sh $BASE/easy.2.2_colors.noisy2 $BASE/easy.2.2_colors`"

echo " hard 1 noise `./mrf_run.sh $BASE/hard.1.2_colors.noisy $BASE/hard.1.2_colors`"
echo " hard 2 noise `./mrf_run.sh $BASE/hard.2.2_colors.noisy $BASE/hard.2.2_colors`"
