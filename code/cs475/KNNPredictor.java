package cs475;

import java.io.Serializable;
import java.util.*;

public abstract class KNNPredictor extends Predictor {
	private static final long serialVersionUID = 1L;
    
    protected List<Instance> trainingInstances;
    protected int k_;
    protected HashSet<Integer> seenFeatures;

    public KNNPredictor(int numNeighbors) {
        k_ = numNeighbors;
        seenFeatures = new HashSet<Integer>();
    }

	public void train(List<Instance> instances) {
        trainingInstances = instances;
        // Find all seen features in dataset
        for (Instance instance : instances) {
            for (Feature feature : instance.getFeatureVector()) {
                seenFeatures.add(feature.index_);
            }
        }
    }

    public abstract Label predict(Instance instance);

    public TreeMap<Instance, Double> findKNearestNeighbors(Instance current) {
        // Map of distance to Instance
        TreeMap<Double, Instance> neighbors = new TreeMap<Double, Instance>();
        for (Instance train : trainingInstances) {
            //TODO when running predictions on the training data we don't want to use
            //the instance to predict itself
            double distance = computeDistance(current.getFeatureVector(), train.getFeatureVector());
            if (neighbors.size() < k_) {
                neighbors.put(new Double(distance), train);
            } else if (neighbors.lastKey() > distance) {
                neighbors.remove(neighbors.lastKey());
                neighbors.put(new Double(distance), train);
            }
        }
        return neighbors;
    }

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


}
