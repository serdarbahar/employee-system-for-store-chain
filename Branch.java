import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Branch extends Entry {

	
	public HashTable<Employee> employeeHash = new HashTable<Employee>();

	public int numCashier=0;
	public int numCook=0;
	public int numCourier=0;
	
	public Queue<Employee> promoToCook = new LinkedList<>();
	public Queue<Employee> promoToManager = new LinkedList<>();
	
	public Employee manager;
	
	public int monthlyBonus;
	public int overallBonus;
	
	public ArrayList<Employee> dismissalList = new ArrayList<>();
	
	
	
	public void checkForPromoQueue(Employee e) {
		
		
		//if too slow, use memory
		if (e.job.equals("CASHIER")) {
			if (e.points>=3 && !promoToCook.contains(e)) {
				promoToCook.add(e);
			}
			else if (e.points<3 && promoToCook.contains(e)) {
				promoToCook.remove(e);
			}
		}
		
		if (e.job.equals("COOK")) {
			if (e.points>=10 && !promoToManager.contains(e)) {
				promoToManager.add(e);
				
	
				
			}
			else if (e.points<10 && promoToManager.contains(e)) {
				promoToManager.remove(e);
	
			}
			
		}
		
	
	}
	
	
 	public Employee checkForCooktoManPromotion() {
		if (numCook>1 && promoToManager.size()!=0 && manager.points<=-5) {
			Employee promoted = promoToManager.poll();
			employeeHash.remove(manager.name);
			promoted.job = "MANAGER"; numCook--; 
			promoted.points -= 10;
			return promoted;
		}
		
		return null;
	}
	
	
	public Employee checkForCashtoCookPromotion() {
		if (numCashier>1 && promoToCook.size()!=0) {
			Employee promoted = promoToCook.poll();
			promoted.job = "COOK"; numCashier--; numCook++;
			promoted.points -= 3;
			return promoted;
		}
		return null;
	}
	
	
	public Employee checkDismissalForSub(Employee e) {
		if (e.job.equals("COURIER")) {
			if (numCourier>1 && e.points<=-5) {
				numCourier--;
				employeeHash.remove(e.name);
		
				return e;	
			}
		}

		if (e.job.equals("CASHIER")) {
			if (numCashier>1 && e.points<=-5) {
				numCashier--;
				employeeHash.remove(e.name);
	
				return e;	
			}
		}
		
		if (e.job.equals("COOK")) {
			if (numCook>1 && e.points<=-5) {
				numCook--;
				employeeHash.remove(e.name);
	
				return e;	
			}
		}
		return null;
	}
	
	
	public Employee checkDismissalforManager() {
		if (manager.points<=-5 && promoToManager.size()!=0 && numCook>1) {
			Employee promoted = checkForCooktoManPromotion();
			return promoted;
		}
		
		return null;
	}
	
	
	//
	
	
	
	
	
	
	
	
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
