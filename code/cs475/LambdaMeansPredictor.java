package cs475;

import java.io.Serializable;
import java.util.*;

public class LambdaMeansPredictor extends Predictor {
    private static final long serialVersionUID = 1L;
    private double lambda;
    private int iterations;
    // A list of mean FeatureVectors representing the clusters
    private HashMap<Integer, FeatureVector> clusterVectors;
    private HashMap<Integer, List<Instance>> clusterMembers;

    public LambdaMeansPredictor(double _lambda, int numIterations) {
        lambda = _lambda;
        iterations = numIterations;
        clusterVectors = new HashMap<Integer, FeatureVector>();
        clusterMembers = new HashMap<Integer, List<Instance>>();
    }

    public void train(List<Instance> instances) {

        // Initialize
        FeatureVector meanVector = computeMeanVector(instances);
        /*for (Feature f : meanVector) {
            System.out.print(f + " ");
        }
        System.out.println();*/
        clusterVectors.put(0, meanVector);
        // Initialize empty member list for single initial cluster
        clusterMembers.put(0, new ArrayList<Instance>());
        if (lambda == 0.0) {
            lambda = defaultLambda(instances, meanVector);
            System.out.println("lambda: " + lambda);
        }

        for (int i = 0; i < iterations; ++i) {
            assignExamplesToClusters(instances);
            updateMeanVectors();
        }
        // Don't need to keep entire training set in memory after done training
        clusterMembers.clear();
    }

    // E Step
    private void assignExamplesToClusters(List<Instance> instances) {
        //clear all member lists before updating so that each cluster gets assigned to
        //only it's current best cluster
        for (Integer clusterID : clusterMembers.keySet()) {
            clusterMembers.get(clusterID).clear();
        }
        for (Instance currentExample : instances) {
            int smallestCluster = -1;
            double smallestDistance = Double.MAX_VALUE;
            for (int clusterIndex = 0; clusterIndex < clusterVectors.size(); ++clusterIndex) {
                FeatureVector cluster = clusterVectors.get(clusterIndex);
                double dist = cluster.computeDistance(currentExample.getFeatureVector());
                if (dist < smallestDistance) {
                    smallestCluster = clusterIndex;
                    smallestDistance = dist;
                }
            }
            if (smallestDistance > lambda) {
                // TODO I think this is unnecessary
                FeatureVector newVec = new FeatureVector();
                for (Feature f : currentExample.getFeatureVector()) {
                    newVec.add(f.index_, f.value_);
                }
                clusterVectors.put(clusterVectors.size(), newVec);
                List<Instance> currentClusterMembers = new ArrayList<Instance>();
                currentClusterMembers.add(currentExample);
                clusterMembers.put(clusterMembers.size(), currentClusterMembers);
                if (clusterMembers.size() != clusterVectors.size()) {
                    System.out.println("Cluster Members size != cluster vectors size");
                }
            } else {
                clusterMembers.get(smallestCluster).add(currentExample);
            }
        }
    }

    // M step
    private void updateMeanVectors() {
        if (clusterMembers.size() != clusterVectors.size()) {
            System.out.println("Cluster Members size != cluster vectors size");
        }

        clusterVectors.clear();
        for (int index = 0; index < clusterMembers.size(); ++index) {
            List<Instance> members = clusterMembers.get(index);
            FeatureVector newMeanVector = computeMeanVector(members);
            clusterVectors.put(index, newMeanVector);
            //clusterVectors.set(index, newMeanVector);
        }
    }

    private double defaultLambda(List<Instance> instances,
                                 FeatureVector meanVector) {
        double dist = 0;
        for (Instance i : instances) {
            dist += meanVector.computeDistance(i.getFeatureVector());
        }
        return dist / instances.size();
    }

    //private FeatureVector computeMeanVector(List<Instance> instances) {
    public static FeatureVector computeMeanVector(List<Instance> currentInstances) {
        FeatureVector mean = new FeatureVector();
        if (currentInstances.size() == 0) {
            return mean;
        }
        for (Instance current : currentInstances) {
            for (Feature feature : current.getFeatureVector()) {
                int ind = feature.index_;
                double currentValue = mean.get(ind);
                mean.add(ind, currentValue + feature.value_);
                //mean.add(ind, mean.get(ind) + feature.value_);
            }
        }
        double N = (double) currentInstances.size();
        for (Feature feature : mean) {
            feature.value_ = feature.value_ / N;
        }
        return mean;
    }

    public Label predict(Instance instance) {
        int smallestCluster = -1;
        double smallestDistance = Double.MAX_VALUE;
        for (int clusterIndex = 0; clusterIndex < clusterVectors.size(); ++clusterIndex) {
            FeatureVector cluster = clusterVectors.get(clusterIndex);
            double dist = cluster.computeDistance(instance.getFeatureVector());
            if (dist < smallestDistance) {
                smallestCluster = clusterIndex;
                smallestDistance = dist;
            }
        }
        return new ClassificationLabel(smallestCluster);
    }

}
