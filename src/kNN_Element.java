
public class kNN_Element {
	private String A1;
	private Double A2;
	private Double A3;
	private String A4;
	private String A5;
	private String A6;
	private String A7;
	private Double A8;
	private String A9;
	private String A10;
	private Double A11;
	private String A12;
	private String A13;
	private Double A14;
	private Double A15;
	private String Label;
	
	public kNN_Element(String[] fields){
		A1=(fields[0]);
		A2=Double.valueOf(fields[1]);
		A3=Double.valueOf(fields[2]);
		A4=fields[3];
		A5=fields[4];
		A6=fields[5];
		A7=fields[6];
		A8=Double.valueOf(fields[7]);
		A9=fields[8];
		A10=fields[9];
		A11=Double.valueOf(fields[10]);
		A12=fields[11];
		A13=fields[12];
		A14=Double.valueOf(fields[13]);
		A15=Double.valueOf(fields[14]);
		Label=fields[15];
	}

	public Double getA3() {
		return A3;
	}

	public void setA3(Double a3) {
		A3 = a3;
	}

	public String getA4() {
		return A4;
	}

	public void setA4(String a4) {
		A4 = a4;
	}

	public String getA5() {
		return A5;
	}

	public void setA5(String a5) {
		A5 = a5;
	}

	public String getA6() {
		return A6;
	}

	public void setA6(String a6) {
		A6 = a6;
	}

	public String getA7() {
		return A7;
	}

	public void setA7(String a7) {
		A7 = a7;
	}

	public String getA9() {
		return A9;
	}

	public void setA9(String a9) {
		A9 = a9;
	}

	public String getA10() {
		return A10;
	}

	public void setA10(String a10) {
		A10 = a10;
	}

	public Double getA11() {
		return A11;
	}

	public void setA11(Double a11) {
		A11 = a11;
	}

	public String getA12() {
		return A12;
	}

	public void setA12(String a12) {
		A12 = a12;
	}

	public String getA13() {
		return A13;
	}

	public void setA13(String a13) {
		A13 = a13;
	}

	public Double getA14() {
		return A14;
	}

	public void setA14(Double a14) {
		A14 = a14;
	}

	public Double getA15() {
		return A15;
	}

	public void setA15(Double a15) {
		A15 = a15;
	}

	public String getA1() {
		return A1;
	}

	public void setA1(String a1) {
		A1 = a1;
	}

	public Double getA2() {
		return A2;
	}

	public void setA2(Double a2) {
		A2 = a2;
	}

	public Double getA8() {
		return A8;
	}

	public void setA8(Double a8) {
		A8 = a8;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}
	
	public double L2_Distance(kNN_Element e1){
		
		return Math.sqrt(
				// Distance between Categorical / Binary Data
				e1.getA1().compareTo(this.A1)+
				e1.getA4().compareTo(this.A4)+
				e1.getA5().compareTo(this.A5)+
				e1.getA6().compareTo(this.A6)+
				e1.getA7().compareTo(this.A7)+
				e1.getA9().compareTo(this.A9)+
				e1.getA10().compareTo(this.A10)+
				e1.getA12().compareTo(this.A12)+
				e1.getA13().compareTo(this.A13)+
				
				//Distance between Numeric Data
				Math.pow(e1.getA2()-this.A2,2)+
				Math.pow(e1.getA3()-this.A3,2)+
				Math.pow(e1.getA8()-this.A8,2)+
				Math.pow(e1.getA11()-this.A11,2)+
				Math.pow(e1.getA14()-this.A14,2)+
				Math.pow(e1.getA15()-this.A15,2));
	}
	

}
