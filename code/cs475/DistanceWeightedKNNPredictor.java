package cs475;

import java.io.Serializable;
import java.util.*;

public class DistanceWeightedKNNPredictor extends KNNPredictor {

    public DistanceWeightedKNNPredictor(int numNeighbors) {
        super(numNeigbors);
    }

    public Label predict(Instance instance) {
        List<Neighbor> neighbors = findKNearestNeighbors(instance);
        double lambdaDenom = 0;
        for (Neighbor neighbor : neighbors) {
            lambdaDenom +=  1.0 / (1.0 + neighbor.getDistance());
        }
        double neighborSum = 0;
        for (Neighbor neighbor : neighbors) {
            double lambda = (1.0 / (1.0 + neighbor.getDistance())) / lambdaDenom;
            neighborSum += (lambda * neighbor.getNeighbor().getLabel().getLabel());
        }
        return new RegressionLabel(neighborSum);
    }

}
