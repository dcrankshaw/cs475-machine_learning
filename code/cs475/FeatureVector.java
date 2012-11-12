package cs475;

import java.io.Serializable;
import java.util.*;

// Sparse vector implented as a Java HashMap
// NOTE All indices are 1-indexed
public class FeatureVector implements Serializable, Iterable<Feature> {

	private static final long serialVersionUID = 1L;
    private HashMap<Integer, Feature> vector;
    private int maxIndex_;
    private int numFeatures = 0;
    public FeatureVector() {
        vector = new HashMap<Integer, Feature>();
        maxIndex_ = 0;
    }

    public int dimensionality() {
        return maxIndex_;
    }

    public boolean isEmpty() {
        return (numFeatures == 0);
    }

    public void add(int index, double value) {
        Feature newFeature = new Feature(index, value);
        vector.put(index, newFeature);
        if (index > maxIndex_) {
            maxIndex_ = index;
        }
        ++numFeatures;
    }

    public double get(int index) {
        Feature feature = vector.get(index);
        if (feature == null) {
            return 0;
        } else {
            return feature.value_;
        }
    }

    // removes the feature at the given index
    // returns the removed feature, or null if the feature
    // was not there
    public Feature remove(int index) {
        Feature f = vector.remove(index);
        if (f != null) {
            numFeatures -= 1;
        }
        return f;
    }

    public Feature getFeature(int index) {
        return vector.get(index);
    }

    public Iterator<Feature> iterator() {
        return new FeatureVectorIterator();
    }

    public double computeDistance(FeatureVector other) {
        int largestIndex = maxIndex_;
        if (other.dimensionality() > largestIndex) {
            largestIndex = other.dimensionality();
        }
        double norm = 0;
        for (int i = 0; i <= largestIndex; ++i) {
            double diff = this.get(i) - other.get(i);
            norm += diff*diff;
        }
        return Math.sqrt(norm);
    }

    /*
    public double computeDistance(FeatureVector other) {
        double norm = 0;
        Iterator<Feature> thisIter = this.iterator();
        boolean updateThis = true;
        Iterator<Feature> otherIter = other.iterator();
        boolean updateOther = true;
        if (thisIter.hasNext() && otherIter.hasNext()) {
            Feature thisFeature = thisIter.next();
            updateThis = false;
            Feature otherFeature = otherIter.next();
            updateOther = false;
            while ((thisIter.hasNext() || !updateThis) && (otherIter.hasNext() || !updateOther)) {
                if (updateThis) {
                    thisFeature = thisIter.next();
                }
                if (updateOther) {
                    otherFeature = otherIter.next();
                }
                updateThis = true;
                updateOther = true;
                if (thisFeature.index_ == otherFeature.index_) {
                    double diff = thisFeature.value_ - otherFeature.value_;
                    norm += diff*diff;
                } else if (thisFeature.index_ < otherFeature.index_) {
                    norm += thisFeature.value_*thisFeature.value_;
                    while (thisIter.hasNext()) {
                        thisFeature = thisIter.next();
                        if (thisFeature.index_ == otherFeature.index_) {
                            double diff = thisFeature.value_ - otherFeature.value_;
                            norm += diff*diff;
                            break;
                        } else if (thisFeature.index_ > otherFeature.index_) {
                            updateThis = false;
                            break;
                        } else {
                            norm += thisFeature.value_*thisFeature.value_;
                        }
                    }
                } else if (otherFeature.index_ < thisFeature.index_) {
                    norm += otherFeature.value_*otherFeature.value_;
                    while (otherIter.hasNext()) {
                        otherFeature = otherIter.next();
                        if (thisFeature.index_ == otherFeature.index_) {
                            double diff = thisFeature.value_ - otherFeature.value_;
                            norm += diff*diff;
                            break;
                        } else if (thisFeature.index_ < otherFeature.index_) {
                            updateOther = false;
                            break;
                        } else {
                            norm += otherFeature.value_*otherFeature.value_;
                        }
                    }
                }

            }
            //will only go into one of these loops
            while (thisIter.hasNext() || !updateThis) {
                if (updateThis) {
                    thisFeature = thisIter.next();
                }
                updateThis = true;
                norm += thisFeature.value_*thisFeature.value_;
            }
            while (otherIter.hasNext() || !updateOther) {
                if (updateOther) {
                    otherFeature = otherIter.next();
                }
                updateOther = true;
                norm += otherFeature.value_*otherFeature.value_;
            }
        }
        return Math.sqrt(norm);
    }*/


    private class FeatureVectorIterator implements Iterator<Feature> {

        //private Iterator<Feature> internalIterator;
        private Iterator<Integer> internalIterator;
        private TreeSet<Integer> indices;

        public FeatureVectorIterator() {
            indices = new TreeSet<Integer>(vector.keySet());
            internalIterator = indices.iterator();
        }

        public boolean hasNext() {
            return internalIterator.hasNext();
        }

        public Feature next() {
            return vector.get(internalIterator.next());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        /*public void remove() {
            internalIterator.remove();
        }*/
    }
}
