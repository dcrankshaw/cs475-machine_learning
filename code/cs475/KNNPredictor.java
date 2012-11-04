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

    public List<Neighbor> findKNearestNeighbors(Instance current) {
        // Map of distance to Instance
        List<Neighbor> neighbors = new ArrayList<Neighbor>();
        Neighbor furthestNeighbor = null;
        for (Instance train : trainingInstances) {
            //TODO when running predictions on the training data we don't want to use
            //the instance to predict itself
            if (train.equals(current)) {
                continue;
            }
            Neighbor currentNeighbor = new Neighbor(train, current, seenFeatures);
            if (furthestNeighbor == null) {
                furthestNeighbor = currentNeighbor;
            }
            if (neighbors.size() < k_) {
                neighbors.add(currentNeighbor);
                if (furthestNeighbor.getDistance() < currentNeighbor.getDistance()) {
                    furthestNeighbor = currentNeighbor;
                }
            } else if (furthestNeighbor.getDistance() > currentNeighbor.getDistance()) {
                //replace furthest neighbor with new neighbor
                neighbors.remove(furthestNeighbor);
                neighbors.add(currentNeighbor);
                // find the new furthest neigbor
                furthestNeighbor = neighbors.get(0);
                for (Neighbor n : neighbors) {
                    if (furthestNeighbor.getDistance() < n.getDistance()) {
                        furthestNeighbor = n;
                    }
                }
            }
        }
        return neighbors;
    }

}
