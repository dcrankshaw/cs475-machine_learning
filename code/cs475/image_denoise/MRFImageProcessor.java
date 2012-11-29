package cs475.image_denoise;


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

    private int[][] updateK(int[][] hidden_nodes, boolean greyscale, int color1, int color2) {
        int maxKI = (hidden_nodes.length - 1) / num_K_;
        int maxKJ = (hidden_nodes[0].length - 1) / num_K_;
        int[][] k_nodes = new int[maxKI + 1][maxKJ + 1];
        int[][] k_nodes_members = new int[maxKI + 1][maxKJ + 1];
        if (greyscale) {
            for (int ii = 0; ii < hidden_nodes.length; ++ii) {
                for (int jj = 0; jj < hidden_nodes[0].length; ++jj) {
                    int ki = ii / num_K_;
                    int kj = jj / num_K_;
                    k_nodes[ki][kj] += hidden_nodes[ii][jj];
                    k_nodes_members[ki][kj] += 1;
                }
            }
            for (int ki = 0; ki < k_nodes.length; ++ki) {
                for (int kj = 0; kj < k_nodes[0].length; ++kj) {
                        double kavg = (double) k_nodes[ki][kj] / (double) k_nodes_members[ki][kj];
                        int kavgInt = (int) Math.round(kavg);
                        k_nodes[ki][kj] = kavgInt;
                }
            }
        }
        else {
            for (int ii = 0; ii < hidden_nodes.length; ++ii) {
                for (int jj = 0; jj < hidden_nodes[0].length; ++jj) {
                    int ki = ii / num_K_;
                    int kj = jj / num_K_;
                    if (hidden_nodes[ii][jj] == color1) {
                        k_nodes[ki][kj] += 1;
                    } else {
                        if (hidden_nodes[ii][jj] != color2) {
                            System.out.println("bad color matching. grep this ksdfkjdhsjfk");
                        }
                        k_nodes[ki][kj] -= 1;
                    }
                }
            }
            for (int ki = 0; ki < k_nodes.length; ++ki) {
                for (int kj = 0; kj < k_nodes[0].length; ++kj) {
                    if (k_nodes[ki][kj] >= 0) {
                        k_nodes[ki][kj] = color1;
                    } else {
                        k_nodes[ki][kj] = color2;
                    }
                }
            }

        }
        return k_nodes;
    }

    private static int[][] copyArray(int[][] image) {
        int[][] hidden_nodes = new int[image.length][];
        for (int i = 0; i < image.length; ++i) {
            hidden_nodes[i] = new int[image[i].length];
            for (int j = 0; j < image[i].length; ++j) {
                hidden_nodes[i][j] = image[i][j];
            }
        }
        return hidden_nodes;
    }


    public int[][] denoisifyImage(int[][] image, int[][] IGNORE) {
        boolean greyscale = (ImageUtils.countColors(image, false) > 2);
        int maxColorValue = ImageUtils.maxColor(image);
        int color1 = 0;
        int color2 = 0;
        if (!greyscale) {
            color1 = image[0][0];
            for (int ii = 0; ii < image.length; ++ii) {
                boolean done = false;
                for (int jj = 0; jj < image[0].length; ++jj) {
                    if (image[ii][jj] != color1) {
                        color2 = image[ii][jj];
                        done = true;
                        break;
                    }
                }
                if (done) break;
            }
        }
        if (color1 > color2) {
            int temp = color1;
            color1 = color2;
            color2 = temp;
        }

        // initialize hidden nodes
        int[][] hidden_nodes = copyArray(image);
        int[][] k_nodes = null;
        if (second_level_) {
            k_nodes = updateK(hidden_nodes, greyscale, color1, color2);
        }
        if (greyscale) {
            // For grey images
            for (int iter = 0; iter < num_iterations_; ++iter) {
                //double InitialEnergy = calculateTotalEnergy(hidden_nodes, image, greyscale);
                int[][] newHiddenNodes = new int[hidden_nodes.length][hidden_nodes[0].length];
                for (int ii = 0; ii < hidden_nodes.length; ++ii) {
                    for (int jj = 0; jj < hidden_nodes[ii].length; ++jj) {
                        newHiddenNodes[ii][jj] = findMinEnergyGrey(ii, jj, hidden_nodes, image, maxColorValue, k_nodes); 
                    }
                }
                hidden_nodes = newHiddenNodes;
                if (second_level_) {
                    k_nodes = updateK(hidden_nodes, greyscale, color1, color2);
                }

            }
        } else {
            // for black and white images
            for (int iter = 0; iter < num_iterations_; ++iter) {
                //double InitialEnergy = calculateTotalEnergy(hidden_nodes, image, greyscale);
                int[][] newHiddenNodes = new int[hidden_nodes.length][hidden_nodes[0].length];
                for (int ii = 0; ii < hidden_nodes.length; ++ii) {
                    for (int jj = 0; jj < hidden_nodes[ii].length; ++jj) {
                        newHiddenNodes[ii][jj] = findMinEnergyBW(ii, jj, hidden_nodes, image, k_nodes, color1, color2); 
                    }
                }
                hidden_nodes = newHiddenNodes;
                if (second_level_) {
                    k_nodes = updateK(hidden_nodes, greyscale, color1, color2);
                }
            }
        }
        return hidden_nodes;
    }

    // This can be done locally without having to compute the total
    // energy. Just find the value that minizimizes the energy for the cliques that
    // this node is part of.
    private int findMinEnergyBW(int ii, int jj, int[][] hidden_nodes, int[][] image, int[][] k_nodes, int color1, int color2) {
        double color1Energy = 0;
        double color2Energy = 0;
        if ((ii - 1) >= 0) {
            color1Energy += potentialHiddenHidden(color1, hidden_nodes[ii-1][jj], false); 
            color2Energy += potentialHiddenHidden(color2, hidden_nodes[ii-1][jj], false); 
        }
        if ((jj - 1) >= 0) {
            color1Energy += potentialHiddenHidden(color1, hidden_nodes[ii][jj-1], false); 
            color2Energy += potentialHiddenHidden(color2, hidden_nodes[ii][jj-1], false); 
        }
        if ((ii + 1) < hidden_nodes.length) {
            color1Energy += potentialHiddenHidden(color1, hidden_nodes[ii+1][jj], false); 
            color2Energy += potentialHiddenHidden(color2, hidden_nodes[ii+1][jj], false); 

        }
        if ((jj + 1) < hidden_nodes[0].length) {
            color1Energy += potentialHiddenHidden(color1, hidden_nodes[ii][jj+1], false); 
            color2Energy += potentialHiddenHidden(color2, hidden_nodes[ii][jj+1], false); 
        }
        color1Energy += potentialHiddenObserved(color1, image[ii][jj], false);
        color2Energy += potentialHiddenObserved(color2, image[ii][jj], false);
        if (second_level_) {
            int ki = ii / num_K_;
            int kj = jj / num_K_;
            color1Energy += potentialHiddenZ(color1, k_nodes[ki][kj], false);
            color2Energy += potentialHiddenZ(color2, k_nodes[ki][kj], false);
        }
        if (color2Energy < color1Energy) {
            return color2;
        } else {
            return color1;
        }
    }

    private int findMinEnergyGrey(int ii, int jj, int[][] hidden_nodes, int[][] image, int maxColorVal, int[][] k_nodes) {
        double minEnergy = Float.MAX_VALUE;
        int minColor = 0;
        for (int color = 0; color <= maxColorVal; ++color) {
            double curEnergy = 0;
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
            if (second_level_) {
                int ki = ii / num_K_;
                int kj = jj / num_K_;
                curEnergy += potentialHiddenZ(hidden_nodes[ii][jj], k_nodes[ki][kj], true);
            }
            if (minEnergy > curEnergy) {
                minEnergy = curEnergy;
                minColor = color;
            }
        }
        return minColor;
    }

    // Don't need
    /*
    private double calculateTotalEnergy(int[][] hidden_nodes, int[][] image, boolean greyscale) {
        double energy = 0;
        // Calculate energy from horizontal cliques
        for (int row = 0; row < hidden_nodes.length; ++row) {
            for (int i = 0, j = 1; j < hidden_nodes[row].length; ++i, ++j) {
                energy+= potentialHiddenHidden(hidden_nodes[row][i], hidden_nodes[row][j], greyscale);
            }
        }

        // Calculate energy from vertical cliques - all columns have same length
        for (int column = 0; column < hidden_nodes[0].length; ++column) {
            for (int i = 0, j = 1; j < hidden_nodes.length; ++i, ++j) {
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
    */


    private double potentialHiddenHidden(int xi, int xj, boolean greyscale) {
        if (greyscale) {
            double potential = (Math.log(Math.abs(xj - xi) + 1) - 1) * beta_;
            return potential;
        } else {
            if (xi == xj)
                return -1 * beta_;
            else
                return beta_;
        }
    }

    private double potentialHiddenObserved(int xi, int yi, boolean greyscale) {
        if (greyscale) {
            double potential = (Math.log(Math.abs(xi - yi) + 1) - 1) * eta_;
            return potential;
        } else {
            if (xi == yi)
                return -1 * eta_;
            else
                return eta_;
        }
    }

    private double potentialHiddenZ(int xi, int zi, boolean greyscale) {
        if (greyscale) {
            double potential = (Math.log(Math.abs(xi - zi) + 1) - 1) * omega_;
            return potential;
        } else {
            if (xi == zi)
                return -1 * omega_;
            else
                return omega_;
        }
    }

}
