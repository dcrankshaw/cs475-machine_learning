package cs475;

import java.io.Serializable;
import java.util.List;

public class DecisionTreePredictor extends Predictor implements Serializable {
	private static final long serialVersionUID = 1L;

        private int maxDepth_;
        private Label majorityLabel_;

        public DecisionTreePredictor(int maxDepth) {
                maxDepth_ = maxDepth;
        }

	public void train(List<Instance> instances) {
                if (instances.size() == 0) {
                        throw new IllegalArgumentException("Cannot provide an empty set of training data.");
                }
               // Get majority label - tie goes to lowest indexed label
                majoritylabel_ = majorityLabel;
        }

        /**
          * @param usedFeatures The features that have already been split on
          *
          */
        private DecisionTreeNode BuildDecisionTree(List<Instance> data,
                                                   List<int> usedFeatures,
                                                   int depth) {
                // No Examples Base Case
                if (data.size == 0) {
                        //return node with majority label
                } else {
                        bool splitsPossible = false;
                        for (Instance instance : data) {
                                if (instance.getFeatureVector().dimensionality() > usedFeatures.size()) {
                                        splitsPossible = true;
                                        break; // TODO(crankshaw) only if we aren't going to do
                                                 // anything else with this loop
                                }
                        }
                        // No Further Splits Possible base case
                        if (!splitsPossible) {
                                //return majorityLabelFORDATAATNODE - tie goes to lowest indexed label
                        }
                        Set<Label> labels = labels(data);
                        // All Labels Agree base case
                        if (labels.size() == 1) {
                                //return (Label) labels.toArray()[0];
                        }

                        if (depth == maxDepth_) {
                                //return majorityLabelFORDATAATNODE - tie goes to lowest indexed label
                        }

                        // At this point all base cases have been handled,
                        // We now need to generate the probability distributions
                        //      - P(X)
                        //      - P(Y|X)

                        //Create P(X)
                        


                }
                
        }

        private Set<Label> labels(List<Instance> instances) {
                Set<Label> labels = new HashSet<Label>();
                // make sure that we override Equals, Hashcode for Label so that we
                // are getting the right comparisons for Set.add()
                for (Instance current : instances) {
                        labels.add(current.getLabel());
                }
        }
	
	public Label predict(Instance instance);
}
