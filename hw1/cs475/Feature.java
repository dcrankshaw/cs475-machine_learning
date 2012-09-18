package cs475;

import java.io.Serializable;


public class Feature implements Comparable<Feature>, Serializable {
        public int index_;
        public double value_;

        public Feature(int index, double value) {
                index_ = index;
                value_ = value;
        }

        public int compareTo(Feature other) {
                return index_.compareTo(other.index_);
        }

}
