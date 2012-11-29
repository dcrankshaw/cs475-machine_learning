#!/usr/bin/env bash

# $1 is the input image
# $2 is the clean image to compare it to


JAVA=`which java`
CLASSPATH=.:~/cs475-machine_learning/commons-cli-1.2/commons-cli-1.2.jar:~/cs475-machine_learning/code/bin

#LOCAL
$JAVA -cp $CLASSPATH cs475.image_denoise.RemoveImageNoise -input_image $1 \
    -output_image $1.out -eta 2.1 -beta 2.0 -num_iterations 20

$JAVA -cp $CLASSPATH cs475.image_denoise.CompareImages -image_a $1.out \
    -image_2 $2


#NON-LOCAL
$JAVA -cp $CLASSPATH cs475.image_denoise.RemoveImageNoise -input_image $1 \
    -output_image $1.out.nonlocal -eta 2.1 -beta 2.0 -num_iterations 20 -num_K 4 \
    -use_second_level true -omega 1.5 -

$JAVA -cp $CLASSPATH cs475.image_denoise.CompareImages -image_a $1.out.nonlocal \
    -image_2 $2

