
public class Perceptron {
	
	double net;
	
	double error;
		
	public Perceptron() {
		net = 0;
		error = 0;
	}

	public void func() {
		net = 1/(1+Math.pow(Math.E,net*-1));
	}
	

}
