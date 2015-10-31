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
//	final static String testing_file = "src/Lenses.testing";
//	final static String training_file = "src/Lenses.training";
//	final static String crx_testing_file = "src/crx.data.processed.testing";
//	final static String crx_training_file = "src/crx.data.processed.training";
	final static String final_ending = ".final";
	public static void main(String[] args) throws Exception {

		int k =Integer.getInteger(args[0]);
		BufferedReader br_training = new BufferedReader(new FileReader(args[1]));
		BufferedReader br_testing = new BufferedReader(new FileReader(args[2]));

//		int k = 8;
//		BufferedReader br_training = new BufferedReader(new FileReader(training_file));
//		BufferedReader br_testing = new BufferedReader(new FileReader(testing_file));

		if(args[1].contains("crx")){ // hard coded for ease of assignment
			
			processCRX(br_training, br_testing,k,args[2]);
			
		}else{
			
			processLenses(br_training, br_testing,k,args[2]);
			
		}	
//			
//		processLenses(br_training, br_testing,k,testing_file);
//		
//		processCRX(br_training, br_testing,k,crx_testing_file);
	}

	
	// Processes Lens data and computes how accurate kNN is for a given k
	private static void processLenses(BufferedReader br_training, BufferedReader br_testing, int k, String file) throws Exception {
		ArrayList<Lens> training_list = new ArrayList<Lens>();
		PrintWriter out = new PrintWriter(file+"."+k+final_ending); // writing to a new file as I cant figure out how to overwrite a line in the same file
		
		String line = null; 
		while ((line = br_training.readLine()) != null) {
			String[] fields = line.split(",");
			training_list.add(new Lens(fields));
		}
		System.out.println("Training Size="+ training_list.size());
		br_training.close();

		while((line=br_testing.readLine()) != null){
			Lens testing_line = new Lens(line.split(","));
			Integer projected_label = computeLabelLens(testing_line, training_list, k);

			if(projected_label==testing_line.getLabel()){
				accuracy[POS_ACCURACY]= accuracy[POS_ACCURACY]+1;
			}else{ 
				accuracy[NEG_ACCURACY]= accuracy[NEG_ACCURACY]+1;

			}
			out.write(line+","+projected_label+"\n");
		}
		br_testing.close();
		
		out.write("With k="+k+" the accuracy was "+ ((double)accuracy[POS_ACCURACY]/(double)(accuracy[POS_ACCURACY]+(double)accuracy[NEG_ACCURACY]))*100.0 +"%.");
		out.close();
	}

	// Computes the Label for a given Lens
	private static Integer computeLabelLens(Lens testing_line, ArrayList<Lens> training_list, int k) {
		TreeMap<Double, Lens> nearest_Neighbors = new TreeMap<Double, Lens>();
		for(Lens element : training_list){
			double dist = testing_line.L2_Distance(element);
			if(nearest_Neighbors.size()<k){
				nearest_Neighbors.put(dist, element);
			}else if(dist<nearest_Neighbors.lastKey()){
				nearest_Neighbors.pollLastEntry(); // remove element with largest distance then add new, closer element
				nearest_Neighbors.put(dist, element);
			}
		}
		Collection<Lens> k_nearest=nearest_Neighbors.values();
		Iterator<Lens> k_nearest_iterator=k_nearest.iterator();
		int [] Lenses ={0,0,0,0};
		while(k_nearest_iterator.hasNext()){
			Lens neighbor=k_nearest_iterator.next();
			Lenses[neighbor.getLabel()]++;
		}

		int max =0;
		int index=0;
		for(int i=1;i<Lenses.length;i++){
			if(Lenses[i]>max){
				max=Lenses[i];
				index=i;
			}
		}
		return index;
	}

	// Processes CRX data and computes how accurate kNN is for a given k
	private static void processCRX(BufferedReader br_training, BufferedReader br_testing, int k, String file) throws Exception {
		ArrayList<CRX> training_list = new ArrayList<CRX>();
		PrintWriter out = new PrintWriter(file+"."+k+final_ending);
		
		String line = null; 
		while ((line = br_training.readLine()) != null) {
			String[] fields = line.split(",");
			training_list.add(new CRX(fields));
		}
		System.out.println("Training Size="+ training_list.size());
		br_training.close();

		while((line=br_testing.readLine()) != null){
			CRX testing_line = new CRX(line.split(","));
			String projected_label = computeLabelCRX(testing_line, training_list, k);

			if(projected_label.equals(testing_line.getLabel())){
				accuracy[POS_ACCURACY]= accuracy[POS_ACCURACY]+1;
			}else{ 
				accuracy[NEG_ACCURACY]= accuracy[NEG_ACCURACY]+1;

			}
			out.write(line+","+projected_label+"\n");
		}
		br_testing.close();
		
		out.write("With k="+k+" the accuracy was "+ ((double)accuracy[POS_ACCURACY]/(double)(accuracy[POS_ACCURACY]+(double)accuracy[NEG_ACCURACY]))*100.0 +"%.");
		out.close();

	}


	// Computes the label that training. Runs in N* log(N) time
	private static String computeLabelCRX(CRX testing_line, ArrayList<CRX> training_list, int k) {
		TreeMap<Double, CRX> nearest_Neighbors = new TreeMap<Double, CRX>();
		for(CRX element : training_list){
			double dist = testing_line.L2_Distance(element);
			if(nearest_Neighbors.size()<k){
				nearest_Neighbors.put(dist, element);
			}else if(dist<nearest_Neighbors.lastKey()){
				nearest_Neighbors.pollLastEntry(); // remove element with largest distance then add new, closer element
				nearest_Neighbors.put(dist, element);
			}
		}


		// Gets the k nearest neighbor tuples and count the number of "+" and "-" labels and return which ever one has a higher count
		// Breaks ties in Negative favor
		Collection<CRX> k_nearest=nearest_Neighbors.values();
		Iterator<CRX> k_nearest_iterator=k_nearest.iterator();
		int pos =0;
		int neg=0;
		while(k_nearest_iterator.hasNext()){
			CRX neighbor=k_nearest_iterator.next();
			if(neighbor.getLabel().equals(POS_LABEL)){
				pos++;
			}else{
				neg++;
			}
		}
		
		return pos>neg ? POS_LABEL : NEG_LABEL;
	}

}
