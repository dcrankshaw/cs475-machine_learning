package cs475;

import java.util.*;
import java.util.Serializable;

// simple wrapper class to wrap a neighbor and a distance for KNN predictors

public class Neighbor {

    private Instance neighbor;
    private Instance test;
    private double distance;
    HashSet<Integer> seenFeatures;

    public Neighbor(Instance n, Instance t, HashSet<Integer> features) {
        neighbor = n;
        test = t;
        seenFeatures = features;
        distance = computeDistance(test.getFeatureVector(), neighbor.getFeatureVector());
    }
    
    public Instance getNeighbor() { return neighbor; }
    public Instance getTestInstance() { return test; }
    public double getDistance() { return distance; }



    public double computeDistance(FeatureVector test, FeatureVector train) {
        double norm = 0;
        Iterator<Feature> testIter = test.iterator();
        boolean updateTest = true;
        Iterator<Feature> trainIter = train.iterator();
        boolean updateTrain = true;
        if (testIter.hasNext() && trainIter.hasNext()) {
            Feature testFeature = testIter.next();
            updateTest = false;
            Feature trainFeature = trainIter.next();
            updateTrain = false;
            while ((testIter.hasNext() || !updateTest) && (trainIter.hasNext() || !updateTrain)) {
                if (updateTest) {
                    testFeature = testIter.next();
                }
                if (updateTrain) {
                    trainFeature = trainIter.next();
                }
                updateTest = true;
                updateTrain = true;
                if (testFeature.index_ == trainFeature.index_) {
                    double diff = testFeature.value_ - trainFeature.value_;
                    norm += diff*diff;
                } else if (testFeature.index_ < trainFeature.index_) {
                    if (seenFeatures.contains(testFeature.index_)) {
                        norm += testFeature.value_*testFeature.value_;
                    }
                    while (testIter.hasNext()) {
                        testFeature = testIter.next();
                        if (testFeature.index_ == trainFeature.index_) {
                            double diff = testFeature.value_ - trainFeature.value_;
                            norm += diff*diff;
                            break;
                        } else if (testFeature.index_ > trainFeature.index_) {
                            updateTest = false;
                            break;
                        } else {
                            if (seenFeatures.contains(testFeature.index_)) {
                                norm += testFeature.value_*testFeature.value_;
                            }
                        }
                    }
                } else if (trainFeature.index_ < testFeature.index_) {
                    norm += trainFeature.value_*trainFeature.value_;
                    while (trainIter.hasNext()) {
                        trainFeature = trainIter.next();
                        if (testFeature.index_ == trainFeature.index_) {
                            double diff = testFeature.value_ - trainFeature.value_;
                            norm += diff*diff;
                            break;
                        } else if (testFeature.index_ < trainFeature.index_) {
                            updateTrain = false;
                            break;
                        } else {
                            norm += trainFeature.value_*trainFeature.value_;
                        }
                    }
                }

            }
            //will only go into one of these loops
            while (testIter.hasNext() || !updateTest) {
                if (updateTest) {
                    testFeature = testIter.next();
                }
                updateTest = true;
                if (seenFeatures.contains(testFeature.index_)) {
                    norm += testFeature.value_*testFeature.value_;
                }
            }
            while (trainIter.hasNext() || !updateTrain) {
                if (updateTrain) {
                    trainFeature = trainIter.next();
                }
                updateTrain = true;
                norm += trainFeature.value_*trainFeature.value_;
            }
        }
        return Math.sqrt(norm);
    }

    public 


}
