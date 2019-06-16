package com.ruslan.digits_recognizing;

public class NeuralNetwork {
	
	
	double[][] hidden;
	
	double[][] output;
	
	double[][] weights01;
	
	double[][] weights12;
	
	double rate;
	
	
	public NeuralNetwork(int numOfHiddenPerceptrons, double rate) {
		
		this.rate = rate;
		
		hidden = new double[numOfHiddenPerceptrons][2];	
		output = new double[10][2];
		
		//initialization of weights between input and hidden layers
		weights01 = new double[numOfHiddenPerceptrons][64];	
		for(int i = 0;i < weights01.length; i++) {
			for(int j = 0;j < weights01[i].length; j++) {
				weights01[i][j] = Math.random()*0.4+0.1;
			}
		}
		
		//initialization of weights between hidden and output layers
		weights12 = new double[10][numOfHiddenPerceptrons];
		for(int i = 0;i < weights12.length; i++) {
			for(int j = 0;j < weights12[i].length; j++) {
				weights12[i][j] = Math.random()*0.4+0.1;
			}
		}	
	}

	public void train(double[] data, Integer correct) {

		double[] answers = {0,0,0,0,0,0,0,0,0,0};
		answers[correct] = 1;
			
		findNETs(data);
		
		changeWeights(data,answers);
	}

		

	private void changeWeights(double[] data, double[] answers) {
		
		//finding error for output layer
		for(int i = 0;i< output.length;i++) {
			output[i][1] = answers[i] - output[i][0];
		}
		
		//finding error for hidden layer
		for(int i = 0; i < hidden.length;i++) {
			hidden[i][1]=0;
			for(int j = 0; j < output.length;j++) {
				hidden[i][1] += output[j][1] * weights12[j][i];
			}
		}
		
		//changing weights between hidden and output layer
		for(int i = 0;i< weights12.length;i++) {
			for(int j = 0;j< weights12[i].length;j++) {
				weights12[i][j] += rate * output[i][1] * hidden[j][0] *output[i][0]*(1 - output[i][0]);
			}		
		}
		
		//changing weights between input and hidden layer
		for(int i = 0;i< weights01.length;i++) {
			for(int j = 0;j< weights01[i].length;j++) {
				weights01[i][j] +=  rate * hidden[i][1] *data[j] * hidden[i][0]*(1 - hidden[i][0]);
			}		
		}
		
	}

	private void findNETs(double[] input) {
		
		normalize(input);
		
		//finding NETs on hidden layer
		for(int i = 0;i<hidden.length;i++) {
			hidden[i][0] = 0;
			for(int j = 0;j<input.length;j++) {
				hidden[i][0]+=input[j]*weights01[i][j]; // sum of all inputs multiplied by weights
			}
			hidden[i][0] = 1/(1+Math.pow(Math.E,-hidden[i][0])); // sigmoid function

		}
		
		//finding NETs on output layer
		for(int i = 0;i<output.length;i++) {
			output[i][0] = 0;
			for(int j = 0;j<hidden.length;j++) {
				output[i][0]+=hidden[j][0]*weights12[i][j]; // sum of all hidden perceptrons  multiplied by weights
			}
			output[i][0] = 1/(1+Math.pow(Math.E,-output[i][0])); // sigmoid function
		}
		
		
	}
	
	// normalization of data using   x = ( x - average value ) / standard deviation(sigma) 
	public void normalize(double[] data) {
		
		//avenge value estimation
		double avgsum=0;
		for(int i = 0;i<data.length;i++) {
			avgsum+= data[i];
		}
		avgsum /= data.length;
		
		//sigma estimation 
		double sigma = 0;
		for(int i = 0;i<data.length;i++) {
			sigma+= Math.pow(data[i] - avgsum,2);
		}
		sigma/=data.length;
		sigma = Math.pow(sigma, 0.5);
		
		//normalize data
		for(int i = 0;i<data.length;i++) {
			data[i]= (data[i] - avgsum)/sigma;
			data[i] = 1/(1+Math.pow(Math.E,-data[i]));
		}
		
	}
	
	//main method to recognizing
	public int recognize(double[] data) {
		findNETs(data);
		
		//finding output with the highest value
		double max = output[0][0];
		int index = 0;
		for(int i = 1;i<output.length;i++) {
			if(output[i][0] > max) {
				max = output[i][0];
				index = i;
			}	
		}
		
		// return recognized answer
		return index;
	}

	//show all weights ( for debugging ) 
	public void showWeights() {
		System.out.println("Weights 0 - 1");
		for(int i = 0;i< weights01.length;i++) {
			for(int j = 0;j< weights01[i].length;j++) {
				System.out.println(weights01[i][j]);
			}	
			System.out.println("\n");
		}
		
		System.out.println("Weights 1 - 2");
		for(int i = 0;i< weights12.length;i++) {
			for(int j = 0;j< weights12[i].length;j++) {
				System.out.println(weights12[i][j]);
			}	
			System.out.println("\n");
		}
		
	}

	//show all NETs ( for debugging ) 
	public void showNets() {
		for(int i = 0;i< hidden.length;i++) {
			System.out.println(hidden[i][0]);
		}
		System.out.println();
		for(int i = 0;i< output.length;i++) {
			System.out.println(output[i][0]);
		}
	}

}
