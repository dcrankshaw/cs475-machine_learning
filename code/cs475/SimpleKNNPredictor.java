package cs475;

import java.io.Serializable;
import java.util.*;

public class SimpleKNNPredictor extends KNNPredictor {

	private static final long serialVersionUID = 1L;

    public SimpleKNNPredictor(int numNeighbors) {
        super(numNeighbors);
    }

    public Label predict(Instance instance) {
        List<Neighbor> neighbors = findKNearestNeighbors(instance);
        double neighborSum = 0;
        for (Neighbor neighbor : neighbors) {
            RegressionLabel label = (RegressionLabel) neighbor.getNeighbor().getLabel();
            neighborSum += label.getLabel();
        }
        double prediction = (1 / (double) k_) * neighborSum;
        return new RegressionLabel(prediction);
    }

}
