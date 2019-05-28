
public class NN {
	
	Perceptron[] firstHiddenLayer;
	
	Perceptron[] secondHiddenLayer;
	
	Perceptron[] outputLayer;
	
	double[][] weight01;
	
	double[][] weight12;
	
	double[][] weight23;
	
	double coef;
	
	double plus = 0.1;
	
	public NN(int firstHiddenLayerSize,int secondHiddenLayerSize, double coef) {
		
		this.coef = coef;
		
		firstHiddenLayer = new Perceptron[firstHiddenLayerSize];
		for(int i = 0;i< firstHiddenLayerSize;i++) {
			firstHiddenLayer[i] = new Perceptron();
		}
		
		secondHiddenLayer = new Perceptron[secondHiddenLayerSize];
		for(int i = 0;i< secondHiddenLayerSize;i++) {
			secondHiddenLayer[i] = new Perceptron();
		}
		
		outputLayer = new Perceptron[10];
		for(int i = 0;i< outputLayer.length;i++) {
			outputLayer[i] = new Perceptron();
		}
		
		weight01 = new double[firstHiddenLayerSize][64+1];
		weight12 = new double[secondHiddenLayerSize][firstHiddenLayerSize+1];
		weight23 = new double[outputLayer.length][secondHiddenLayerSize+1];
		
		for(int i = 0; i<weight01.length;i++) {
			for(int j = 0; j<weight01[i].length; j++) {
				weight01[i][j] = Math.random();
			}
		}
		for(int i = 0; i<weight12.length;i++) {
			for(int j = 0; j<weight12[i].length; j++) {
				weight12[i][j] = Math.random();
			}
		}
		for(int i = 0; i<weight23.length;i++) {
			for(int j = 0; j<weight23[i].length; j++) {
				weight23[i][j] = Math.random();
			}
		}
		
	}
	
	public void train(double[] data,int correct) {
		//normalize input data
		normalize(data);
		
		//move forward
		calcNET(data);
		
		
		double[] answerSet = {0,0,0,0,0,0,0,0,0,0};
		answerSet[correct] = 1;

		
		
		//updating weight
		for(int i = 0;i<weight23.length;i++) {
			double wtf = outputLayer[i].net * (1 - outputLayer[i].net) * ( answerSet[i] - outputLayer[i].net);
			for(int j = 0;j<weight23[i].length-1;j++) {
				//http://www.aiportal.ru/articles/neural-networks/back-propagation.html
				weight23[i][j] += coef*wtf*secondHiddenLayer[j].net;  
			}
			weight23[i][weight23[i].length-1] += coef*wtf;  
		}
		for(int i = 0;i<weight12.length;i++) {
			double wtf = secondHiddenLayer[i].net * (1 - secondHiddenLayer[i].net);
			for(int j = 0;j<weight12[i].length-1;j++) {
				weight12[i][j] += coef*wtf*firstHiddenLayer[j].net;  
			}
			weight12[i][weight12[i].length-1] += coef*wtf;  
		}
		
		for(int i = 0;i<weight01.length;i++) {
			double wtf = firstHiddenLayer[i].net * (1 - firstHiddenLayer[i].net);
			for(int j = 0;j<weight01[i].length-1;j++) {
				weight01[i][j] += coef*wtf*data[j];  
			}
			weight01[i][weight01[i].length-1] += coef*wtf;  

		}
		
	}
	
	public void recognize(double[] data) {
		calcNET(data);
		int answer = findAnswer(); 
		System.out.println("Answer is "+answer+"\n");
		
		for(int i = 0;i<outputLayer.length;i++) {
			System.out.println(i+" - "+outputLayer[i].net);
		}
	}
	
	public void normalize(double[] data) {
		double sumavg=0;
		for(int i = 0;i<data.length;i++) {
			sumavg+= data[i];
		}
		sumavg /= data.length;
		
		double sigma = 0;
		for(int i = 0;i<data.length;i++) {
			sigma+= Math.pow(data[i] - sumavg,2);
		}
		sigma/=data.length;
		sigma = Math.pow(sigma, 0.5);
		for(int i = 0;i<data.length;i++) {
			data[i]= (data[i] - sumavg)/sigma;
		}
	}

	public int findAnswer() {
		int max = 0;
		double value = outputLayer[0].net;
		
		for(int i = 1; i<outputLayer.length ;i++) {
			if(value < outputLayer[i].net) {
				value = outputLayer[i].net;
				max = i;
			}
		}
		return max;
	}
	
	public void calcNET(double[] data) {
		for(int i = 0;i< firstHiddenLayer.length;i++) {
			firstHiddenLayer[i].net = 0;
			for(int j = 0;j< data.length;j++) {
				firstHiddenLayer[i].net+= data[j] * weight01[i][j];
			}
			firstHiddenLayer[i].net+=weight01[i][weight01[i].length-1];
			firstHiddenLayer[i].func();
		}
		
		for(int i = 0;i< secondHiddenLayer.length;i++) {
			secondHiddenLayer[i].net = 0;
			for(int j = 0;j< firstHiddenLayer.length;j++) {

				secondHiddenLayer[i].net+= firstHiddenLayer[j].net * weight12[i][j];
			}
			secondHiddenLayer[i].net+=weight12[i][weight12[i].length-1];
			secondHiddenLayer[i].func();
		}
		
		for(int i = 0;i< outputLayer.length;i++) {
			outputLayer[i].net = 0;
			for(int j = 0;j< secondHiddenLayer.length;j++) {
				outputLayer[i].net+= secondHiddenLayer[j].net * weight23[i][j];
			}
			outputLayer[i].net+=weight23[i][weight23[i].length-1];
			outputLayer[i].func();
		}
	}
	
}
