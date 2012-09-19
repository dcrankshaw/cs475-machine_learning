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


}
