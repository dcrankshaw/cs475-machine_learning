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
        boolean greyscale = (ImageUtils.countColors(image) > 2);
        // initialize hidden nodes
        int[][] hidden_nodes = new int[image.length][];
        for (int i = 0; i < image.length; ++i) {
            hidden_nodes[i] = new int[image[i].length];
            for (int j = 0; j < image[i].length; ++j) {
                hidden_nodes[i][j] = image[i][j];
            }
        }
        for (int iter = 0; iter < num_iterations_; ++iter) {
            for (int ii = 0; ii < hidden_nodes.length; ++ii) {
                for (int jj = 0; jj < hidden_nodes[ii].length; ++jj) {
                    
                }
            }
        }

    }

    private potentialHiddenHidden(int xi, int xj, boolean greyscale) {
        if (greyscale) {
            double potential = (Math.log(Math.abs(xj - xi) + 1) - 1) * beta_;
        } else {
            if (xi == xj)
                return -1 * beta_;
            else
                return beta_;
        }
    }

    private potentialHiddenObserved(int xi, int yi, boolean greyscale) {
        if (greyscale) {
            double potential = (Math.log(Math.abs(xi - yi) + 1) - 1) * beta_;
        } else {
            if (xi == yi)
                return -1 * beta_;
            else
                return beta_;
        }
    }

    private int[][] denoisifyImageTwoLevels(int[][] image) {

    }













}
