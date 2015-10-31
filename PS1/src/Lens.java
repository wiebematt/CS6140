
public class Lens {
	private int age;
	private int prescr;
	private int astig;
	private int tear;
	private int Label;

	public Lens (String[] fields){
		this.age=Integer.valueOf(fields[0]);
		this.prescr=Integer.valueOf(fields[1]);
		this.astig=Integer.valueOf(fields[2]);
		this.tear=Integer.valueOf(fields[3]);
		this.Label = Integer.valueOf(fields[4]);
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getPrescr() {
		return prescr;
	}

	public void setPrescr(int prescr) {
		this.prescr = prescr;
	}

	public int getAstig() {
		return astig;
	}

	public void setAstig(int astig) {
		this.astig = astig;
	}

	public int getTear() {
		return tear;
	}

	public void setTear(int tear) {
		this.tear = tear;
	}

	public int getLabel() {
		return Label;
	}

	public void setLabel(int label) {
		Label = label;
	}

	public String toString(){
		return age+","+prescr+","+astig+","+tear+","+Label;
	}

	public double L2_Distance(Lens e1){
		return Math.sqrt(Math.pow(e1.getAge()-this.age,2)+
						 Math.pow(e1.getAstig()-this.astig,2)+
						 Math.pow(e1.getPrescr()-this.prescr,2)+
						 Math.pow(e1.getTear()-this.tear,2));
	}
}
