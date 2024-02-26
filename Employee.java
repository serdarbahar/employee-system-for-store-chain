
public class Employee extends Entry {

	public String job;
	public Branch branch;
	
	public int points=0;
	public int overallBonus=0;
	public int monthlyBonus=0;
	
	
	
	
	
	public Employee() {
		super();
	}
	
	
	
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public Branch getBranch() {
		return branch;
	}
	public void setBranch(Branch branch) {
		this.branch = branch;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
	public int hashCode() {
		int n;
		n = this.name.hashCode();
		return n;
	}
	
	
	
	
	
	
	
}
