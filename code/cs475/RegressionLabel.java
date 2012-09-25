package cs475;

import java.io.Serializable;

public class RegressionLabel extends Label implements Serializable {

        double label_;

	public RegressionLabel(double label) {
                label_ = label;
	}

        public double getLabel() {
                return label_;
        }

	@Override
	public String toString() {
                return Double.toString(label_);
	}

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof RegressionLabel)) { return false; }
            RegressionLabel otherLabel = (RegressionLabel) other;
            return (otherLabel.getLabel() == label_);
        }

        @Override
        public int hashCode() {
            Double labelDouble = new Double(label_);
            return labelDouble.hashCode();
        }

}
