package cs475;

import java.io.Serializable;
import java.util.List;

public abstract class LinearThresholdPredictor implements Serializable {
	private static final long serialVersionUID = 1L;



	public void train(List<Instance> instances) {
            
        }
	
	public abstract Label predict(Instance instance);
}
