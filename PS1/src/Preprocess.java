import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

public class Preprocess {

	// indices for feature properties
	final static int POS_SUM = 0;
	final static int NEG_SUM = 1;
	final static int POS_COUNT = 2;
	final static int NEG_COUNT = 3;
	final static int POS_MISSING_COUNT = 4;
	final static int NEG_MISSING_COUNT = 5;
	// for normalization
	final static int TOTAL_SUM = 6;
	final static int TOTAL_SUM_SQUARES = 7;
	final static int MEAN = 0;
	final static int STDDEV = 1;
	// index for the instance label
	final static int LABEL = 15;
	// for label conditioning
	final static int NEGATIVE = 0;
	final static int POSITIVE = 1;
	//final static String testing_file = "src/crx.data.testing";
	//final static String training_file = "src/crx.data.training";
	
	public static void main(String[] args) throws Exception {

		// these are for the categorical/binary data
		HashMap<String,Integer[]> A1 = new HashMap<String,Integer[]>();
		HashMap<String,Integer[]> A4 = new HashMap<String,Integer[]>();
		HashMap<String,Integer[]> A5 = new HashMap<String,Integer[]>();
		HashMap<String,Integer[]> A6 = new HashMap<String,Integer[]>();
		HashMap<String,Integer[]> A7 = new HashMap<String,Integer[]>();
		HashMap<String,Integer[]> A9 = new HashMap<String,Integer[]>();
		HashMap<String,Integer[]> A10 = new HashMap<String,Integer[]>();
		HashMap<String,Integer[]> A12 = new HashMap<String,Integer[]>();
		HashMap<String,Integer[]> A13 = new HashMap<String,Integer[]>();
		
		// these are for continuous data
		double[] A2 = new double[8];
		double[] A3 = new double[8];
		double[] A8 = new double[8];
		double[] A11 = new double[8];
		double[] A14 = new double[8];
		double[] A15 = new double[8];
		
		// read in all of the 
		BufferedReader br = new BufferedReader(new FileReader(args[0]));
		//BufferedReader br = new BufferedReader(new FileReader(training_file));
		
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] fields = line.split(",");
			String label = fields[LABEL];
			addHistogram(A1, fields[0], label);
			addContinuous(A2, fields[1], label);
			addContinuous(A3, fields[2], label);
			addHistogram(A4, fields[3], label);
			addHistogram(A5, fields[4], label);
			addHistogram(A6, fields[5], label);
			addHistogram(A7, fields[6], label);
			addContinuous(A8, fields[7], label);
			addHistogram(A9, fields[8], label);
			addHistogram(A10, fields[9], label);
			addContinuous(A11, fields[10], label);
			addHistogram(A12, fields[11], label);
			addHistogram(A13, fields[12], label);
			addContinuous(A14, fields[13], label);
			addContinuous(A15, fields[14], label);
		}
		br.close();
		
		String[][] plurality = new String[LABEL][2];
		double[][] mean = new double[LABEL][2];
		plurality[0] = computePlurality(A1);
		mean[1] = computeLabelMean(A2);
		mean[2] = computeLabelMean(A3);
		plurality[3] = computePlurality(A4);
		plurality[4] = computePlurality(A5);
		plurality[5] = computePlurality(A6);
		plurality[6] = computePlurality(A7);
		mean[7] = computeLabelMean(A8);
		plurality[8] = computePlurality(A9);
		plurality[9] = computePlurality(A10);
		mean[10] = computeLabelMean(A11);
		plurality[11] = computePlurality(A12);
		plurality[12] = computePlurality(A13);
		mean[13] = computeLabelMean(A14);
		mean[14] = computeLabelMean(A15);

		double[][] scaling = new double[LABEL][2];
		scaling[1] = computeScaling(A2);
		scaling[2] = computeScaling(A3);
		scaling[7] = computeScaling(A8);
		scaling[10] = computeScaling(A11);
		scaling[13] = computeScaling(A14);
		scaling[14] = computeScaling(A15);

		// uncomment if you want to see the missing value imputations
		/*
		for (int i = 0; i < plurality.length; i++) {
			if (plurality[i][0] != null) {
				System.out.println(plurality[i][0] + "," + plurality[i][1]);
			}
			else {
				System.out.println(mean[i][0] + "," + mean[i][1]);
			}
		}
		*/
		
		processFile(args[0], plurality, mean, scaling);
		processFile(args[1], plurality, mean, scaling);
		//processFile(training_file, plurality, mean, scaling);
		//processFile(testing_file, plurality, mean, scaling);
	}
	
	final static void processFile(String file, String[][] plurality, 
			double[][] mean, double[][] scaling) throws Exception {
		int splitIdx = file.lastIndexOf('.');
		String filename = file.substring(0, splitIdx) + 
				".processed" + file.substring(splitIdx);
		PrintWriter out = new PrintWriter(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] fields = line.split(",");
			String label = fields[LABEL];
			for (int i = 0; i < fields.length - 1; i++) {
				if (plurality[i][0] == null) {	// continuous feature
					double value = 0.0;
					if (fields[i].equals("?")) {
						value = mean[i][label.equals("+") ? POSITIVE : NEGATIVE];
					}
					else {
						value = Double.parseDouble(fields[i]);
					}
					value = (value - scaling[i][MEAN]) / scaling[i][STDDEV];
					out.print(value);
				}
				else {	// categorical
					if (fields[i].equals("?")) {
						out.print(plurality[i][label.equals("+") ? POSITIVE : NEGATIVE]);
					}
					else {
						out.print(fields[i]);
					}
				}
				out.print(",");
			}
			out.println(label);
		}
		br.close();
		out.close();		
	}
	
	final static double[] computeLabelMean(double[] a) {
		double[] result = new double[2];
		result[NEGATIVE] = a[NEG_SUM] / a[NEG_COUNT];
		result[POSITIVE] = a[POS_SUM] / a[POS_COUNT];
		return result;
	}

	final static double[] computeScaling(double[] a) {
		double[] result = new double[2];
		double n = a[POS_COUNT] + a[NEG_COUNT];
		result[MEAN] = a[TOTAL_SUM] / n;
		result[STDDEV] = Math.sqrt((a[TOTAL_SUM_SQUARES] - 
				(a[TOTAL_SUM] * a[TOTAL_SUM]) / n)/(n - 1));
		return result;
	}
	
	final static String[] computePlurality(HashMap<String,Integer[]> h) {
		String[] result = new String[2];
		int count[] = new int[2];
		count[NEGATIVE] = Integer.MIN_VALUE;
		count[POSITIVE] = Integer.MIN_VALUE;
		for (String key : h.keySet()) {
			Integer[] c = h.get(key);
			if (!key.equals("?") && (c[NEGATIVE] != null) && 
					(c[NEGATIVE] > count[NEGATIVE])) {
				result[NEGATIVE] = key;
				count[NEGATIVE] = c[NEGATIVE];
			}
			if (!key.equals("?") && (c[NEGATIVE] != null) && 
					(c[POSITIVE] > count[POSITIVE])) {
				result[POSITIVE] = key;
				count[POSITIVE] = c[POSITIVE];
			}
		}
		return result;
	}
	
	final static String computeUnifiedPlurality(HashMap<String,Integer[]> h) {
		String result = null;
		int count = Integer.MIN_VALUE;
		for (String key : h.keySet()) {
			Integer[] c = h.get(key);
			int sum = c[0] + c[1];
			if (!key.equals("?") && (sum > count)) {
				result = key;
				count = sum;
			}
		}
		return result;
	}
	
	final static void addContinuous(double[] a, String value, String label) {
		if (value.equals("?")) {
			if (label.equals("+")) {
				a[POS_MISSING_COUNT]++;				
			}
			else if (label.equals("-")) {
				a[NEG_MISSING_COUNT]++;
			}
		}
		else {
			double v = Double.parseDouble(value);
			if (label.equals("+")) {
				a[POS_SUM] += v;
				a[POS_COUNT]++;
			}
			else if (label.equals("-")) {
				a[NEG_SUM] += v;
				a[NEG_COUNT]++;
			}
			a[TOTAL_SUM] += v;
			a[TOTAL_SUM_SQUARES] += v * v;
		}
	}
	
	final static void addHistogram(HashMap<String,Integer[]> h, String value, String label) {
		Integer[] count = h.get(value);
		if (count == null) {
			count = new Integer[2];
		}
		if (label.equals("+")) {
			if (count[POSITIVE] == null) {
				count[POSITIVE] = new Integer(0);
			}
			count[POSITIVE]++;
		}
		else if (label.equals("-")) {
			if (count[NEGATIVE] == null) {
				count[NEGATIVE] = new Integer(0);
			}
			count[NEGATIVE]++;
		}
		h.put(value, count);
	}
}
