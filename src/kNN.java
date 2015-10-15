import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public class kNN {

	/**
	 * @param k testing.file training.file
	 */
	
	// Distance for Non Numeric Data 
	final static int POS_ACCURACY = 0;
	final static int NEG_ACCURACY = 1;
	final static String POS_LABEL = "+";
	final static String NEG_LABEL = "-";
	
	final static int[] accuracy = new int[2];
	final static String testing_file = "src/crx.data.processed.testing";
	final static String training_file = "src/crx.data.processed.training";
	
	public static void main(String[] args) throws Exception {
				
		//int k =Integer.getInteger(args[0]);
		//BufferedReader br_training = new BufferedReader(new FileReader(args[1]));
		//BufferedReader br_testing = new BufferedReader(new FileReader(args[2]));
		int k= 10;
		
		BufferedReader br_training = new BufferedReader(new FileReader(training_file));
		BufferedReader br_testing = new BufferedReader(new FileReader(testing_file));
		ArrayList<kNN_Element> training_list = new ArrayList<kNN_Element>();
		
		String line = null; 
		while ((line = br_training.readLine()) != null) {
			String[] fields = line.split(",");
			training_list.add(new kNN_Element(fields));
		}
		System.out.println("training_list.size()="+ training_list.size());
		br_training.close();
		
		while((line=br_testing.readLine()) != null){
			kNN_Element testing_line = new kNN_Element(line.split(","));
			String projected_label = computeLabel(testing_line, training_list, k);
			if(projected_label.equals(testing_line.getLabel())){
				accuracy[POS_ACCURACY]= accuracy[POS_ACCURACY]+1;
			}else{ 
				accuracy[NEG_ACCURACY]= accuracy[NEG_ACCURACY]+1;
					
			}
		}
		br_testing.close();
		
		System.out.println("With k="+k+" the accuracy was "+ (accuracy[POS_ACCURACY]/(accuracy[POS_ACCURACY]+accuracy[NEG_ACCURACY]))*100.0 +"%.");
	}

	// Computes the label that training
	private static String computeLabel(kNN_Element testing_line, ArrayList<kNN_Element> training_list, int k) {
		TreeMap<Double, kNN_Element> nearest_Neighbors = new TreeMap<Double, kNN_Element>();
		for(kNN_Element element : training_list){
			double dist = testing_line.L2_Distance(element);
			if(nearest_Neighbors.size()<k){
				nearest_Neighbors.put(dist, element);
			}else if(dist<nearest_Neighbors.lastKey()){
				nearest_Neighbors.pollLastEntry(); // remove element with largest distance then add new, closer element
				nearest_Neighbors.put(dist, element);
			}
		}
		//System.out.println(nearest_Neighbors.size()); treeMap is always size 10
		
		// Gets the k nearest neighbor tuples and count the number of "+" and "-" labels and return which ever one has a higher count
		Collection<kNN_Element> k_nearest=nearest_Neighbors.values();
		Iterator<kNN_Element> k_nearest_iterator=k_nearest.iterator();
		int pos =0;
		int neg=0;
		while(k_nearest_iterator.hasNext()){
			kNN_Element neighbor=k_nearest_iterator.next();
			if(neighbor.getLabel().equals(POS_LABEL)){
				pos++;
			}else{
				neg++;
			}
		}
		return pos>neg ? POS_LABEL : NEG_LABEL;
	}

}
