#!/usr/bin/sh

BASE=~/cs475-machine_learning/image_data/

echo " colors noise 1 `./mrf_data_sets.sh $BASE/cs475.2_colors.noise1.bmp $BASE/cs475.2_colors.bmp`"
echo " colors noise 2 `./mrf_data_sets.sh $BASE/cs475.2_colors.noise2.bmp $BASE/cs475.2_colors.bmp`"

echo " dredze noise 1 `./mrf_data_sets.sh $BASE/dredze.greyscale.noisy1.bmp $BASE/dredze.greyscale.bmp`"
echo " dredze noise 2 `./mrf_data_sets.sh $BASE/dredze.greyscale.noisy2.bmp $BASE/dredze.greyscale.bmp`"

echo " easy 1 noise 1 `./mrf_data_sets.sh $BASE/easy.1.2_colors.noisy.bmp $BASE/easy.1.2_colors.bmp`"
echo " easy 1 noise 2 `./mrf_data_sets.sh $BASE/easy.1.2_colors.noisy2.bmp $BASE/easy.1.2_colors.bmp`"

echo " easy 2 noise 1 `./mrf_data_sets.sh $BASE/easy.2.2_colors.noisy.bmp $BASE/easy.2.2_colors.bmp`"
echo " easy 2 noise 2 `./mrf_data_sets.sh $BASE/easy.2.2_colors.noisy2.bmp $BASE/easy.2.2_colors.bmp`"

echo " hard 1 noise `./mrf_data_sets.sh $BASE/hard.1.2_colors.noisy.bmp $BASE/hard.1.2_colors.bmp`"
echo " hard 2 noise `./mrf_data_sets.sh $BASE/hard.2.2_colors.noisy.bmp $BASE/hard.2.2_colors.bmp`"
