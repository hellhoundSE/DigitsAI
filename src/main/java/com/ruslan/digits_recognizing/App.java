package com.ruslan.digits_recognizing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App 
{
	public static void main(String[] args) {
		
		//uploading training data
		ArrayList<double[]> allData = new ArrayList<double[]>();
		ArrayList<Integer> correctNumbers = new ArrayList<Integer>();
		readData(allData,correctNumbers,"src/main/digits.tra");
		
		// creating neuronNetwork object  
		//first parameter it is amount of perceptrons on hidden layer
		//second it is learning rate
		NeuralNetwork neuronNetwork = new NeuralNetwork(45,0.002);
		
		//training
		double generationAmount = 10000;
		for(int i = 0;i<generationAmount;i++) {
			for(int j = 0; j<allData.size();) {
				
				// data for training each time taken in a different order
				List<Integer> list= new ArrayList<Integer>();
				int index = (int)(Math.random()*allData.size());
				if(!list.contains(index)) {
					neuronNetwork.train(allData.get(j), correctNumbers.get(j));
					list.add(index);
					j++;
				}
			}
			
			    // just to know how many iterations left
				if(i %100 == 0) {
					System.out.println(i * 100 / generationAmount+" %"); 
				}
		}
		
		//uploading test data
		ArrayList<double[]> allData2 = new ArrayList<double[]>();
		ArrayList<Integer> correctNumbers2 = new ArrayList<Integer>();
		readData(allData2,correctNumbers2,"src/main/digits2.tes");

		double[] fromNetwork = {0,0,0,0,0,0,0,0,0,0};
		double[] real = {0,0,0,0,0,0,0,0,0,0};
		
		//recognizing test data
		double ok=0,notok=0;
		for(int j = 0;j < 20;j++) {
			for(int i = 0; i<allData2.size();i++) {
				int answer = neuronNetwork.recognize(allData2.get(i));	
				fromNetwork[answer]++;
				real[correctNumbers2.get(i)]++;
				if(answer == correctNumbers2.get(i))
					ok++;
				else
					notok++;
			}
		}

		// displays the percent correctness recognizing for testing data
		System.out.println("test % = "+(int)(ok/(ok+notok)*100));
		
		// displays the percent correctness for each class for testing data
		for(int i = 0;i< real.length;i++) {
			System.out.println(i+"  "+  (  fromNetwork[i]>real[i]  ?  real[i]/fromNetwork[i] : fromNetwork[i]/real[i]));
		}
		
		//recognizing training data
		fromNetwork = new double[10];
		real = new double[10];
		ok=0;
		notok=0;
		for(int j = 0;j < 20;j++) {
			for(int i = 0; i<allData.size();i++) {
				int answer = neuronNetwork.recognize(allData.get(i));		
				fromNetwork[answer]++;
				real[correctNumbers.get(i)]++;
				if(answer == correctNumbers.get(i))
					ok++;
				else
					notok++;
			}
		}
		// displays the percent correctness recognizing for training data
		System.out.println("training % = "+(int)(ok/(ok+notok)*100)); 
		
		// displays the percent correctness for each class for training data
		for(int i = 0;i< real.length;i++) {
			System.out.println(i+"  "+  (  fromNetwork[i]>real[i]  ?  real[i]/fromNetwork[i] : fromNetwork[i]/real[i]));
		}
	}

	//method to read data from file
	public static void readData(ArrayList<double[]> allData,ArrayList<Integer> correctNumbers,String path) {
		try(BufferedReader br = new BufferedReader(new FileReader(path)))
		{
		    String s;
		    while((s=br.readLine())!=null){
		        String[] tab = s.split(",");
		        double[] data = new double[64];
		        for(int i = 0;i<data.length;i++) {
		        	data[i] = Double.parseDouble(tab[i]);
		        }
		        allData.add(data);
		        correctNumbers.add(Integer.parseInt(tab[64]));
		    }
		}
		 catch(IOException ex) {
		    System.out.println(ex.getMessage());
		} 
	}

}
