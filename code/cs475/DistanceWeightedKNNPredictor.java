package cs475;

import java.io.Serializable;
import java.util.*;

public class DistanceWeightedKNNPredictor extends KNNPredictor {

	private static final long serialVersionUID = 1L;

    public DistanceWeightedKNNPredictor(int numNeighbors) {
        super(numNeighbors);
    }

    public Label predict(Instance instance) {
        List<Neighbor> neighbors = findKNearestNeighbors(instance);
        double lambdaDenom = 0;
        for (Neighbor neighbor : neighbors) {
            lambdaDenom +=  1.0 / (1.0 + neighbor.getDistance());
        }
        double neighborSum = 0;
        for (Neighbor neighbor : neighbors) {
            RegressionLabel label = (RegressionLabel) neighbor.getNeighbor().getLabel();
            double lambda = (1.0 / (1.0 + neighbor.getDistance())) / lambdaDenom;
            neighborSum += (lambda * label.getLabel());
        }
        return new RegressionLabel(neighborSum);
    }

}
