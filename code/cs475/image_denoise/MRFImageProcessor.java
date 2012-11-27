package cs475.image_denoise;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import cs475.CommandLineUtilities;

public class MRFImageProcessor {
    double eta_;
    double beta_;
    int num_iterations_;
    int num_K_;
    double omega_;
    boolean second_level_;

    public MRFImageProcessor(
            double eta,
            double beta,
            double omega,
            int num_iter,
            int num_K,
            boolean use_second_level) {
        eta_ = eta;
        beta_ = beta;
        omega_ = omega;
        num_iterations_ = num_iter;
        num_K_ = num_K;
        second_level_ = use_second_level;
    }

    public int[][] denoisifyImage(int[][] image, int[][] IGNORE) {
        if (use_second_level_) {
            return denoisifyImageTwoLevels(image);
        } else {
            return denoisifyImageOneLevel(image);
        }
    }


    private int[][] denoisifyImageOneLevel(int[][] image) {
    
    }

    private int[][] denoisifyImageTwoLevels(int[][] image) {
    
    }













}
