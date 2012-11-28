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
        int maxColorValue = ImageUtils.maxColorValue(image);
        // initialize hidden nodes
        int[][] hidden_nodes = new int[image.length][];
        for (int i = 0; i < image.length; ++i) {
            hidden_nodes[i] = new int[image[i].length];
            for (int j = 0; j < image[i].length; ++j) {
                hidden_nodes[i][j] = image[i][j];
            }
        }
        for (int iter = 0; iter < num_iterations_; ++iter) {
            float InitialEnergy = calculateTotalEnergy(hidden_nodes, image, greyscale);
            for (int ii = 0; ii < hidden_nodes.length; ++ii) {
                for (int jj = 0; jj < hidden_nodes[ii].length; ++jj) {
                    
                }
            }
        }

    }

    // This can be done locally without having to compute the total
    // energy. Just find the value that minizimizes the energy for the cliques that
    // this node is part of.
    private int findMinEnergy(int ii, int jj, int[][] hidden_nodes, int[][] image, int[][] greyscale) {
        int iMin = (ii - 1) < 0 ? 0 : ii - 1;
        int jMin = (jj - 1) < 0 ? 0 : jj - 1;
        int iMax = (ii + 1) < hidden_nodes.length ? (ii + 1) : hidden_nodes.length - 1;
        int jMax = (jj + 1) < hidden_nodes[0].length ? (jj + 1) : hidden_nodes[0].length - 1;
        if (!greyscale) {
            float curEnergy = 0;
            if ((ii - 1) >= 0) {
                
            }
            if ((jj - 1) >= 0) {
            
            }
            if ((ii + 1) < hidden_nodes.length) {
            
            }
            if ((jj + 1) < hidden_nodes[0].length) {
            
            }
        }
    }

    private float calculateTotalEnergy(int[][] hidden_nodes, int[][] image, boolean greyscale) {
        float energy = 0;
        // Calculate energy from horizontal cliques
        for (int row = 0; row < hidden_nodes.length; ++row) {
            for (int i = 0; int j = 1; j < hidden_nodes[row].length; ++i, ++j) {
                energy+= potentialHiddenHidden(hidden_nodes[row][i], hidden_nodes[row][j], greyscale);
            }
        }

        // Calculate energy from vertical cliques - all columns have same length
        for (int column = 0; column < hidden_nodes[0].length; ++column) {
            for (int i = 0; int j = 1; j < hidden_nodes.length; ++i, ++j) {
                energy+= potentialHiddenHidden(hidden_nodes[i][column], hidden_nodes[j][column], greyscale);
            }
        }

        // Calculate energy from hidden-observed cliques
        for (int i = 0; i < hidden_nodes.length; ++i) {
            for (int j = 0; j < hidden_nodes[i].length; ++j) {
                energy += potentialHiddenObserved(hidden_nodes[i][j], image[i][j], greyscale);
            }
        }
        return energy;
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
