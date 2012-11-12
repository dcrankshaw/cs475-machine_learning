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
    private int K;

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
        K = 1;
        clusterVectors.put(1, meanVector);
        // Initialize empty member list for single initial cluster
        clusterMembers.put(1, new ArrayList<Instance>());
        if (lambda == 0.0) {
            lambda = defaultLambda(instances, meanVector);
            System.out.println("lambda: " + lambda);
        }

        for (int i = 0; i < iterations; ++i) {
            assignExamplesToClusters(instances);
            updateMeanVectors();
            System.out.println("Finished iteration " + i);
        }
        // Don't need to keep entire training set in memory after done training
        clusterMembers.clear();
        System.out.println("K: " + K);
        int nonEmptyClusters = 0;
        for (int clusterIndex = 1; clusterIndex <= K; ++clusterIndex) {
            if (!clusterVectors.get(clusterIndex).isEmpty()) {
                nonEmptyClusters++;
            }
        }
        System.out.println("Nonempty clusters: " + nonEmptyClusters);
    }

    // E Step
    private void assignExamplesToClusters(List<Instance> instances) {
        //clear all member lists before updating so that each cluster gets assigned to
        //only it's current best cluster
        clusterMembers.clear();
        /*for (Integer clusterID : clusterMembers.keySet()) {
            clusterMembers.get(clusterID).clear();
        }*/
        for (Instance currentExample : instances) {
            int closestClusterIndex = -1;
            double smallestDistance = Double.MAX_VALUE;
            for (int clusterIndex = 1; clusterIndex <= K; ++clusterIndex) {
                FeatureVector cluster = clusterVectors.get(clusterIndex);
                if (cluster.isEmpty()) {
                    continue;
                }
                double dist = cluster.computeDistance(currentExample.getFeatureVector());
                if (dist < smallestDistance) {
                    closestClusterIndex = clusterIndex;
                    smallestDistance = dist;
                }
            }
            if (smallestDistance > lambda) {
                FeatureVector newVec = new FeatureVector();
                for (Feature f : currentExample.getFeatureVector()) {
                    newVec.add(f.index_, f.value_);
                }
                // Add new cluster
                clusterVectors.put(K + 1, newVec);
                List<Instance> currentClusterMembers = new ArrayList<Instance>();
                currentClusterMembers.add(currentExample);
                clusterMembers.put(K + 1, currentClusterMembers);
                // Update K
                K = K + 1;
            } else {
                List<Instance> currentClusterMembers = clusterMembers.get(closestClusterIndex);
                if (currentClusterMembers == null) {
                    currentClusterMembers = new ArrayList<Instance>();
                }
                currentClusterMembers.add(currentExample);
                clusterMembers.put(closestClusterIndex, currentClusterMembers);
            }
        }
    }

    // M step
    private void updateMeanVectors() {
        clusterVectors.clear();
        for (int ii = 1; ii <= K; ++ii) {
            List<Instance> members = clusterMembers.get(ii);
            FeatureVector newMeanVector = computeMeanVector(members);
            clusterVectors.put(ii, newMeanVector);
            //clusterVectors.set(ii, newMeanVector);
        }
    }

    // TODO This method works
    public static double defaultLambda(List<Instance> instances,
                                 FeatureVector meanVector) {
        double dist = 0;
        for (Instance i : instances) {
            dist += meanVector.computeDistance(i.getFeatureVector());
        }
        return dist / instances.size();
    }

    //private FeatureVector computeMeanVector(List<Instance> instances) {
    // TODO This method works
    public static FeatureVector computeMeanVector(List<Instance> currentInstances) {
        FeatureVector mean = new FeatureVector();
        if (currentInstances == null || currentInstances.size() == 0) {
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
        for (int clusterIndex = 1; clusterIndex <= K; ++clusterIndex) {
            FeatureVector cluster = clusterVectors.get(clusterIndex);
            if (cluster.isEmpty()) {
                continue;
            }
            double dist = cluster.computeDistance(instance.getFeatureVector());
            if (dist < smallestDistance) {
                smallestCluster = clusterIndex;
                smallestDistance = dist;
            }
        }
        return new ClassificationLabel(smallestCluster);
    }
}
