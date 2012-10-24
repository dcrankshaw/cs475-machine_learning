package cs475;

import java.io.Serializable;
import java.util.*;

public class PolynomialKernelLogisticRegressionPredictor extends KernelLogisticRegressionPredictor {

    private double exponent;
    public PolynomialKernelLogisticRegressionPredictor(int iters, double rate, double exp) {
        super(iters, rate);
        exponent = exp;
    }

    protected double computeKernel(FeatureVector first, FeatureVector second) {
        double dotprod = 0;
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
                    dotprod += firstFeature.value_ * secondFeature.value_;
                } else if (firstFeature.index_ < secondFeature.index_) {
                    while (firstIter.hasNext()) {
                        firstFeature = firstIter.next();
                        if (firstFeature.index_ == secondFeature.index_) {
                            dotprod += firstFeature.value_ * secondFeature.value_;
                            break;
                        } else if (firstFeature.index_ > secondFeature.index_) {
                            updateFirst = false;
                            break;
                        }
                    }
                } else if (secondFeature.index_ < firstFeature.index_) {
                    while (secondIter.hasNext()) {
                        secondFeature = secondIter.next();
                        if (secondFeature.index_ == firstFeature.index_) {
                            dotprod += firstFeature.value_ * secondFeature.value_;
                            break;
                        } else if (secondFeature.index_ > firstFeature.index_) {
                            updateSecond = false;
                            break;
                        }
                    }
                }
            }
        }
        double kernel = Math.pow(dotprod + 1.0, exponent);
        return kernel;
    }
}

