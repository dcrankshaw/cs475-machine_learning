package cs475;

import java.util.*;
import java.io.Serializable;


public class DecisionTreeNode implements Serializable {
        
        private Label label_;
        public bool isLeaf;
        private FeatureTest featureTest_;
        public DecisionTreeNode leftChild_;
        public DecisionTreeNode rightChild_;

        public DecisionTreeNode

        
        //Figure out how to encapsulate a feature test
        // A feature test is constructed with a feature index, a threshhold value,
        // and two DecisionTreeNodes (the left and right children). It evaluates the
        // test based on being given a FeatureVector and returns the corresponding node.


}
