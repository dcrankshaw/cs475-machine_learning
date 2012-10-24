package cs475;

import java.io.Serializable;
import java.util.*;

public class LinearKernelLogisticRegressionPredictor extends KernelLogisticRegressionPredictor {

    private double sigma;

    public LinearKernelLogisticRegressionPredictor(int iters, double rate, double s) {
        super(iters, rate);
        sigma = s;
    }

    protected double computeKernel(FeatureVector first, FeatureVector second) {
        double norm = 0;
        Iterator<Feature> firstIter = first.iterator();
        boolean updateFirst = true;
        Iterator<Feature> secondIter = second.iterator();
        boolean updateSecond = true;
        Feature firstFeature = null;
        Feature secondFeature = null;
        while(firstIter.hasNext() || secondIter.hasNext()) {
            if (updateFirst) {
                firstFeature = firstIter.next();
            }
            if (updateSecond) {
                secondFeature = secondIter.next()
            }

        }











        double kernel = 0;
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
                    kernel += firstFeature.value_ * secondFeature.value_;
                } else if (firstFeature.index_ < secondFeature.index_) {
                    while (firstIter.hasNext()) {
                        firstFeature = firstIter.next();
                        if (firstFeature.index_ == secondFeature.index_) {
                            kernel += firstFeature.value_ * secondFeature.value_;
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
                            kernel += firstFeature.value_ * secondFeature.value_;
                            break;
                        } else if (secondFeature.index_ > firstFeature.index_) {
                            updateSecond = false;
                            break;
                        }
                    }
                }
            }
        }
        return kernel;
    }
}

