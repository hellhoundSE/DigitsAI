import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		ArrayList<double[]> allData = new ArrayList<double[]>();
		ArrayList<Integer> correctNumbers = new ArrayList<Integer>();
		
		readData(allData,correctNumbers,"C:\\Users\\ruslan\\Desktop\\digits.tra");

//		for(int i = 0;i<5;i++) {
//			System.out.println(allData.get(i)+" == "+correctNumbers.get(i));
//		}

		
		NN neuronNetwork = new NN(30,15,0.05);
		for(int i = 0;i<1000;i++) {
			for(int j = 0; j<allData.size();j++) {
					neuronNetwork.train(allData.get(j), correctNumbers.get(j));
			}
			if(i %100 == 0) {
				System.out.println(i);
			}
		}
		System.out.println("thats all\n");
		
		neuronNetwork.recognize(allData.get(2));

	}
	
	
	
	
	
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
		 catch(IOException ex){
		             
		    System.out.println(ex.getMessage());
		} 
	}

}
