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
                if (index_ < other.index_) {
                        return -1;
                } else if (index_ == other.index_) {
                        return 0;
                } else {
                        return 1;
                }
        }

        public String toString() {
            return index_ + ":" + value_;
        }

}
