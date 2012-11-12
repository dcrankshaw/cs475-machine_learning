package cs475;

import java.io.Serializable;
import java.util.*;

// Sparse vector implented as a Java TreeMap
// NOTE All indices are 1-indexed
public class FeatureVector implements Serializable, Iterable<Feature> {

	private static final long serialVersionUID = 1L;
    private TreeMap<Integer, Feature> vector;
    private int maxIndex_;
    private int numFeatures = 0;
    public FeatureVector() {
        vector = new TreeMap<Integer, Feature>();
        maxIndex_ = 0;
    }

    public int dimensionality() {
        return maxIndex_;
    }

    public boolean isEmpty() {
        return (numFeatures == 0);
    }

    public Set<Integer> getIndices() {
        return vector.keySet();
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
                    updateThis = false;
                }
                if (updateOther) {
                    otherFeature = otherIter.next();
                    updateOther = false;
                }
                if (thisFeature.index_ == otherFeature.index_) {
                    double diff = thisFeature.value_ - otherFeature.value_;
                    norm += diff*diff;
                    updateThis = true;
                    updateOther = true;
                } else if (thisFeature.index_ < otherFeature.index_) {
                    norm += thisFeature.value_*thisFeature.value_;
                    updateThis = true;
                    while (thisIter.hasNext() || !updateThis) {
                        if (updateThis) {
                            thisFeature = thisIter.next();
                            updateThis = false;
                        }
                        if (thisFeature.index_ == otherFeature.index_) {
                            double diff = thisFeature.value_ - otherFeature.value_;
                            norm += diff*diff;
                            updateThis = true;
                            updateOther = true;
                            break;
                        } else if (thisFeature.index_ > otherFeature.index_) {
                            break;
                        } else {
                            norm += thisFeature.value_*thisFeature.value_;
                            updateThis = true;
                        }
                    }
                } else if (otherFeature.index_ < thisFeature.index_) {
                    norm += otherFeature.value_*otherFeature.value_;
                    updateOther = true;
                    while (otherIter.hasNext() || !updateOther) {
                        if (updateOther) {
                            otherFeature = otherIter.next();
                            updateOther = false;
                        }
                        if (thisFeature.index_ == otherFeature.index_) {
                            double diff = thisFeature.value_ - otherFeature.value_;
                            norm += diff*diff;
                            updateOther = true;
                            updateThis = true;
                            break;
                        } else if (thisFeature.index_ < otherFeature.index_) {
                            break;
                        } else {
                            norm += otherFeature.value_*otherFeature.value_;
                            updateOther = true;
                        }
                    }
                }

            }
            //will only go into one of these loops
            while (thisIter.hasNext() || !updateThis) {
                if (updateThis) {
                    thisFeature = thisIter.next();
                    updateThis = false;
                }
                norm += thisFeature.value_*thisFeature.value_;
                updateThis = true;
            }
            while (otherIter.hasNext() || !updateOther) {
                if (updateOther) {
                    otherFeature = otherIter.next();
                    updateOther = false;
                }
                norm += otherFeature.value_*otherFeature.value_;
                updateOther = true;
            }
        }
        return Math.sqrt(norm);
    }


/*
    public double computeDistance(FeatureVector second) {
        int largest_index = this.dimensionality();
        if (second.dimensionality() > largest_index) {
            largest_index = second.dimensionality();
        }
        HashSet<Integer> indices = new HashSet<Integer>(this.getIndices());
        Set<Integer> extraIndices = second.getIndices();
        for (Integer index : extraIndices) {
            indices.add(index);
        }
        double norm = 0;
        for (Integer ind : indices) {
            double diff = this.get(ind) - second.get(ind);
            norm += diff*diff;
        }
        return Math.sqrt(norm);
    }*/


    private class FeatureVectorIterator implements Iterator<Feature> {
        private Iterator<Feature> internalIterator;

        public FeatureVectorIterator() {
            internalIterator = vector.values().iterator();
        }

        public boolean hasNext() {
            return internalIterator.hasNext();
        }

        public Feature next() {
            return internalIterator.next();
        }

        public void remove() {
            internalIterator.remove();
        }
    }
}
