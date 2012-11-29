#!/usr/bin/env bash

# $1 is the input image
# $2 is the clean image to compare it to


JAVA=`which java`
CLASSPATH=.:~/cs475-machine_learning/commons-cli-1.2/commons-cli-1.2.jar:~/cs475-machine_learning/code/bin

#LOCAL
$JAVA -cp $CLASSPATH cs475.image_denoise.RemoveImageNoise -input_image $1.bmp \
    -output_image $1.out.bmp -eta 2.1 -beta 2.0 -num_iterations 20

$JAVA -cp $CLASSPATH cs475.image_denoise.CompareImages -image_a $1.out.bmp \
    -image_b $2.bmp


#NON-LOCAL
$JAVA -cp $CLASSPATH cs475.image_denoise.RemoveImageNoise -input_image $1.bmp \
    -output_image $1.out.nonlocal.bmp -eta 2.1 -beta 2.0 -num_iterations 20 -num_K 4 \
    -use_second_level true -omega 1.5 -

$JAVA -cp $CLASSPATH cs475.image_denoise.CompareImages -image_a $1.out.nonlocal.bmp \
    -image_b $2.bmp

