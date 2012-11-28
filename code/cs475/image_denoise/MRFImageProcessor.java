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

  private int[][] updateK(int[][] hidden_nodes, boolean greyscale) {

    int maxKI = (hidden_nodes.length - 1) / num_K_;
    int maxKJ = (hidden_nodes[0].length - 1) / num_K_;
    int[][] k_nodes = new int[maxKI + 1][maxKJ + 1];
    int[][] k_nodes_members = new int[maxKI + 1][maxKJ + 1];
    for (int ii = 0; ii < hidden_nodes.length; ++ii) {
      for (int jj = 0; jj < hidden_nodes[0].length; ++jj) {
        int ki = ii / num_K_;
        int kj = jj / num_K_;
        k_nodes[ki][kj] += hidden_nodes[ii][jj];
        k_nodes_members[ki][kj] += 1;
      }
    }
    for (int ki = 0; ki k_nodes.length; ++ki) {
      for (int kj = 0; kj < k_nodes[0].length; ++kj) {
        if (greyscale) {
          float kavg = (float) k_nodes[ki][kj] / (float) k_nodes_members[ki][kj];
          int kavgInt = Math.round(kavg);
          k_nodes[ki][kj] = kavgInt;
        } else {
          int pixelVal = Math.abs(hidden_nodes[0]);
          if (k_nodes[ki][kj] > 0) {
            k_nodes[ki][kj] = pixelVal;
          } else {
            k_nodes[ki][kj] = -1 * pixelVal;
          }

        }
      }
    }
  }

  private static int[][] copyArray(int[][] image) {
    int[][] hidden_nodes = new int[image.length][];
    for (int i = 0; i < image.length; ++i) {
      hidden_nodes[i] = new int[image[i].length];
      for (int j = 0; j < image[i].length; ++j) {
        hidden_nodes[i][j] = image[i][j];
      }
    }
  }


  private int[][] denoisifyImage(int[][] image, int[][] IGNORE) {
    boolean greyscale = (ImageUtils.countColors(image) > 2);
    int maxColorValue = ImageUtils.maxColorValue(image);

    // initialize hidden nodes
    int[][] hidden_nodes = copyArray(image);
    if (use_second_level_) {
      k_nodes = updateK(hidden_nodes, greyscale);
    }
    int[][] k_nodes = null;
    if (greyscale) {
      // For grey images
      for (int iter = 0; iter < num_iterations_; ++iter) {
        //float InitialEnergy = calculateTotalEnergy(hidden_nodes, image, greyscale);
        int[][] newHiddenNodes = new int[hidden_nodes.length][hidden_nodes[0].length];
        for (int ii = 0; ii < hidden_nodes.length; ++ii) {
          for (int jj = 0; jj < hidden_nodes[ii].length; ++jj) {
            newHiddenNodes[ii][jj] = findMinEnergyGrey(ii, jj, hidden_nodes, image, maxColorValue, k_nodes); 
          }
        }
        hidden_nodes = newHiddenNodes;
        if (use_second_level_) {
          k_nodes = updateK(hidden_nodes);
        }

      }
    } else {
      // for black and white images
      for (int iter = 0; iter < num_iterations_; ++iter) {
        //float InitialEnergy = calculateTotalEnergy(hidden_nodes, image, greyscale);
        int[][] newHiddenNodes = new int[hidden_nodes.length][hidden_nodes[0].length];
        for (int ii = 0; ii < hidden_nodes.length; ++ii) {
          for (int jj = 0; jj < hidden_nodes[ii].length; ++jj) {
            newHiddenNodes[ii][jj] = findMinEnergyBW(ii, jj, hidden_nodes, image, k_nodes); 
          }
        }
        hidden_nodes = newHiddenNodes;
        if (use_second_level_) {
          k_nodes = updateK(hidden_nodes);
        }
      }
    }
  }

  // This can be done locally without having to compute the total
  // energy. Just find the value that minizimizes the energy for the cliques that
  // this node is part of.
  private int findMinEnergyBW(int ii, int jj, int[][] hidden_nodes, int[][] image) {
    int iMin = (ii - 1) < 0 ? 0 : ii - 1;
    int jMin = (jj - 1) < 0 ? 0 : jj - 1;
    int iMax = (ii + 1) < hidden_nodes.length ? (ii + 1) : hidden_nodes.length - 1;
    int jMax = (jj + 1) < hidden_nodes[0].length ? (jj + 1) : hidden_nodes[0].length - 1;
    float curEnergy = 0;
    float oppEnergy = 0;
    if ((ii - 1) >= 0) {
      curEnergy += potentialHiddenHidden(hidden_nodes[ii][jj], hidden_nodes[ii-1][jj], false); 
      oppEnergy += potentialHiddenHidden(-1*hidden_nodes[ii][jj], hidden_nodes[ii-1][jj], false); 
    }
    if ((jj - 1) >= 0) {
      curEnergy += potentialHiddenHidden(hidden_nodes[ii][jj], hidden_nodes[ii][jj-1], false); 
      oppEnergy += potentialHiddenHidden(-1*hidden_nodes[ii][jj], hidden_nodes[ii][jj-1], false); 
    }
    if ((ii + 1) < hidden_nodes.length) {
      curEnergy += potentialHiddenHidden(hidden_nodes[ii][jj], hidden_nodes[ii+1][jj], false); 
      oppEnergy += potentialHiddenHidden(-1*hidden_nodes[ii][jj], hidden_nodes[ii+1][jj], false); 

    }
    if ((jj + 1) < hidden_nodes[0].length) {
      curEnergy += potentialHiddenHidden(hidden_nodes[ii][jj], hidden_nodes[ii][jj+1], false); 
      oppEnergy += potentialHiddenHidden(-1*hidden_nodes[ii][jj], hidden_nodes[ii][jj+1], false); 
    }
    curEnergy += potentialHiddenObserved(hidden_nodes[ii][jj], image[ii][jj], false);
    oppEnergy += potentialHiddenObserved(-1*hidden_nodes[ii][jj], image[ii][jj], false);
    if (use_second_level_) {


    }
    if (oppEnergy < curEnergy) {
      return -1*hidden_nodes[ii][jj];
    } else {
      return hidden_nodes[ii][jj];
    }
  }

  private int findMinEnergyGrey(int ii, int jj, int[][] hidden_nodes, int[][] image, int maxColorVal) {
    int iMin = (ii - 1) < 0 ? 0 : ii - 1;
    int jMin = (jj - 1) < 0 ? 0 : jj - 1;
    int iMax = (ii + 1) < hidden_nodes.length ? (ii + 1) : hidden_nodes.length - 1;
    int jMax = (jj + 1) < hidden_nodes[0].length ? (jj + 1) : hidden_nodes[0].length - 1;
    float minEnergy = Float.MAX_VALUE;
    int minColor = 0;
    for (int color = 0; color <= maxColorVal; ++color) {
      float curEnergy = 0;
      if ((ii - 1) >= 0) {
        curEnergy += potentialHiddenHidden(color, hidden_nodes[ii-1][jj], true); 
      }
      if ((jj - 1) >= 0) {
        curEnergy += potentialHiddenHidden(color, hidden_nodes[ii][jj-1], true); 
      }
      if ((ii + 1) < hidden_nodes.length) {
        curEnergy += potentialHiddenHidden(color, hidden_nodes[ii+1][jj], true); 

      }
      if ((jj + 1) < hidden_nodes[0].length) {
        curEnergy += potentialHiddenHidden(color, hidden_nodes[ii][jj+1], true); 
      }
      curEnergy += potentialHiddenObserved(hidden_nodes[ii][jj], image[ii][jj], true);
      if (use_second_level_) {


      }
      if (minEnergy > curEnergy) {
        minEnergy = curEnergy;
        minColor = color;
      }
    }
    return minColor;
  }

  // Don't need
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


  private float potentialHiddenHidden(int xi, int xj, boolean greyscale) {
    if (greyscale) {
      double potential = (Math.log(Math.abs(xj - xi) + 1) - 1) * beta_;
    } else {
      if (xi == xj)
        return -1 * beta_;
      else
        return beta_;
    }
  }

  private float potentialHiddenObserved(int xi, int yi, boolean greyscale) {
    if (greyscale) {
      double potential = (Math.log(Math.abs(xi - yi) + 1) - 1) * beta_;
    } else {
      if (xi == yi)
        return -1 * beta_;
      else
        return beta_;
    }
  }

  private float potentialHiddenZ(int xi, int zi, boolean greyscale) {
    if (greyscale) {
      double potential = (Math.log(Math.abs(xi - zi) + 1) - 1) * omega_;
    } else {
      if (xi == zi)
        return -1 * omega_;
      else
        return omega_;
    }
  }

  private int[][] denoisifyImageTwoLevels(int[][] image) {

  }













}
