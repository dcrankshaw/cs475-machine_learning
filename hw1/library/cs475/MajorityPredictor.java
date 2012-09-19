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
                HashMap<Integer, Integer> labelCounts = new HashMap<Integer, Integer>();
                for (Instance current : instances) {
                        ClassificationLabel currentLabel = (ClassificationLabel) current.getLabel();
                        int labelValue =  currentLabel.getLabel();
                        if (labelCounts.containsKey(labelValue)) {
                                labelCounts.put(labelValue, labelCounts.get(labelValue) + 1);
                        } else {
                                labelCounts.put(labelValue, 1);
                        }
                }
                Vector<Integer> maxLabels = new Vector<Integer>();
                // Hacky way to find the max count but should work
                int maxCount = 0;
                for (int count : labelCounts.values()) {
                        maxCount = count > maxCount ? count : maxCount;
                }
                //System.out.println("Max count: " + maxCount);

                for (Integer label : labelCounts.keySet()) {
                       if (labelCounts.get(label) == maxCount) {
                                maxLabels.add(label);
                       }
                }
                int labelIndex = 0;
                if (maxLabels.size() > 1) {
                        System.out.println("Breaking tie.");
                        Random rand = new Random();
                        labelIndex = rand.nextInt(maxLabels.size());
                }
                majorityLabel_ = new ClassificationLabel(maxLabels.get(labelIndex));
                
        }
	
	public Label predict(Instance instance) {
                return majorityLabel_;
        }
}
