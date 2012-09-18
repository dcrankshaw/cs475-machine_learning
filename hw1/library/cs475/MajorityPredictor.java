package cs475;

import java.io.Serializable;
import java.util.List;
import java.util.*;

public class MajorityPredictor extends Predictor implements Serializable {
        
        Label majorityLabel_ = null;

	private static final long serialVersionUID = 1L;

        public MajorityPredictor() {
                majorityLabel_ = null;
        }

        // TODO what if instances.size() == 0??????
	public void train(List<Instance> instances) {
                HashMap<Label, Integer> labelCounts = new HashMap<Label, Integer>();
                for (Instance current : instances) {
                        Label currentLabel = current.getLabel();
                        if (labelCounts.containsKey(currentLabel)) {
                                labelCounts.put(currentLabel, labelCounts.get(currentLabel) + 1);
                        } else {
                                labelCounts.put(currentLabel, 1);
                        }
                }
                Vector<Label> maxLabels = new Vector<Label>();
                // Hacky way to find the max count but should work
                int maxCount = 0;
                for (int count : labelCounts.values()) {
                        maxCount = count > maxCount ? count : maxCount;
                }

                for (Label label : labelCounts.keySet()) {
                       if (labelCounts.get(label) == maxCount) {
                                maxLabels.add(label);
                       }
                }
                int labelIndex = 0;
                if (maxLabels.size() > 1) {
                        Random rand = new Random();
                        labelIndex = rand.nextInt(maxLabels.size());
                }
                majorityLabel_ = maxLabels.get(labelIndex);
                
        }
	
	public Label predict(Instance instance) {
                return majorityLabel_;
        }
}
