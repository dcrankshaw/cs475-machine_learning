package cs475;

import java.io.Serializable;
import java.util.*;

public class GaussianKernelLogisticRegressionPredictor extends KernelLogisticRegressionPredictor {

    private double sigma;

    public GaussianKernelLogisticRegressionPredictor(int iters, double rate, double s) {
        super(iters, rate);
        sigma = s;
    }

    protected double computeKernel(FeatureVector first, FeatureVector second) {

        double norm = 0;
        Iterator<Feature> firstIter = first.iterator();
        boolean updateFirst = true;
        Iterator<Feature> secondIter = second.iterator();
        boolean updateSecond = true;
        if (firstIter.hasNext() && secondIter.hasNext()) {
            Feature firstFeature = firstIter.next();
            updateFirst = false;
            Feature secondFeature = secondIter.next();
            updateSecond = false;
            while ((firstIter.hasNext() || !updateFirst) && (secondIter.hasNext() || !updateSecond)) {
                if (updateFirst) {
                    firstFeature = firstIter.next();
                }
                if (updateSecond) {
                    secondFeature = secondIter.next();
                }
                updateFirst = true;
                updateSecond = true;
                if (firstFeature.index_ == secondFeature.index_) {
                    double diff = firstFeature.value_ - secondFeature.value_;
                    norm += diff*diff;
                } else if (firstFeature.index_ < secondFeature.index_) {
                    norm += firstFeature.value_*firstFeature.value_;
                    while (firstIter.hasNext()) {
                        firstFeature = firstIter.next();
                        if (firstFeature.index_ == secondFeature.index_) {
                            double diff = firstFeature.value_ - secondFeature.value_;
                            norm += diff*diff;
                            break;
                        } else if (firstFeature.index_ > secondFeature.index_) {
                            updateFirst = false;
                            break;
                        } else {
                            norm += firstFeature.value_*firstFeature.value_;
                        }
                    }
                } else if (secondFeature.index_ < firstFeature.index_) {
                    norm += secondFeature.value_*secondFeature.value_;
                    while (secondIter.hasNext()) {
                        secondFeature = secondIter.next();
                        if (firstFeature.index_ == secondFeature.index_) {
                            double diff = firstFeature.value_ - secondFeature.value_;
                            norm += diff*diff;
                            break;
                        } else if (firstFeature.index_ < secondFeature.index_) {
                            updateSecond = false;
                            break;
                        } else {
                            norm += secondFeature.value_*secondFeature.value_;
                        }
                    }
                }

            }
            //will only go into one of these loops
            while (firstIter.hasNext() || !updateFirst) {
                if (updateFirst) {
                    firstFeature = firstIter.next();
                }
                updateFirst = true;
                norm += firstFeature.value_*firstFeature.value_;
            }
            while (secondIter.hasNext() || !updateSecond) {
                if (updateSecond) {
                    secondFeature = secondIter.next();
                }
                updateSecond = true;
                norm += secondFeature.value_*secondFeature.value_;
            }
        }
        double exponent = -1.0*norm/(2*sigma*sigma);
        double kernel = Math.exp(exponent);
        return kernel;
    }
}

