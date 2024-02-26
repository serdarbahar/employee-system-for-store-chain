import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class project2 {

	public static void main(String[] args) throws IOException {
		
		
		
		String initialFile = args[0];
		String inputFile = args[1];
		String outputFile = args[2];


		String filePath = outputFile;
		FileWriter fileWriter = new FileWriter(filePath);



		//FIRST INPUT FILE
		String filePath1 = initialFile;
		File file1 = new File(filePath1);
		Scanner scanner1 = new Scanner(file1);


		ArrayList<Branch> branches = new ArrayList<>();


		HashTable<Branch> branchHash = new HashTable<>();


		
		while (scanner1.hasNextLine()) {

			String line = scanner1.nextLine();
			String[] array = line.split(", ");

			String city = array[0]; String district = array[1]; String name = array[2]; String job = array[3];
			String branchString = city + ", " + district;

			Branch branch = new Branch();
			branch.setName(branchString);

			Employee employee = new Employee();
			employee.setJob(job); employee.setName(name);


			boolean isBranchAdded = false;

			for (Branch b: branches) 
				if (b.name.equals(branchString) ) {
					branch = b;
					isBranchAdded = true;
					break;
				}


			if (isBranchAdded) {
				branch.employeeHash.insert(employee);
				employee.setBranch(branch);
			}

			else {
				branches.add(branch);
				branchHash.insert(branch);
				branch.employeeHash.insert(employee);
				employee.setBranch(branch);

			}

			if (employee.job.equals("COURIER")) 
				branch.numCourier++;

			if (employee.job.equals("CASHIER")) 
				branch.numCashier++;

			if (employee.job.equals("COOK")) 
				branch.numCook++;


			if (employee.job.equals("MANAGER")) {
				branch.manager = employee;
			}

		}







		//SECOND INPUT FILE

		String filePath2 = inputFile;
		File file2 = new File(filePath2);
		Scanner scanner2 = new Scanner(file2);

		String[] months = {"January:", "February:","March:",
				"April:","May:","June:","July:","August:","September:","October:",
				"November:","December:"};

		while (scanner2.hasNextLine()) {
			String line = scanner2.nextLine();
			if (Arrays.asList(months).contains(line)) {

				for (Branch b: branches)
					b.monthlyBonus = 0;

			}
			else {

				String[] arrayTask = line.split(": ");
				String task = arrayTask[0];


				if (task.equals("PERFORMANCE_UPDATE")) {


					String[] array = arrayTask[1].split(", ");
					String city = array[0]; String district = array[1]; String name = array[2]; 
					int promoPoints = Integer.parseInt(array[3]);
					String branchString = city + ", " + district;

					Branch branch = branchHash.find(branchString);


					Employee e = branch.employeeHash.find(name);





					if (e==null) {
						fileWriter.write("There is no such employee.\n");
					}


					else {


						//Updating employee's promotion points and bonuses
						int promo;
						int bonus;

						if (promoPoints>=0) {
							promo = promoPoints / 200;
							bonus = promoPoints % 200;
							if (bonus == 200) {
								bonus = 0;
							}
						}

						else {
							promoPoints = Math.abs(promoPoints);
							promo = -1*(promoPoints / 200);
							bonus = 0;
						}

						e.points += promo; branch.monthlyBonus += bonus; branch.overallBonus += bonus;


						branch.checkForPromoQueue(e);
						
						if (e.points<=-5 && !e.job.equals("MANAGER")) {
							if (!branch.dismissalList.contains(e)) {
								branch.dismissalList.add(e);
							}
						}
						
						if (e.points>-5  && !e.job.equals("MANAGER")) {
							if (branch.dismissalList.contains(e)) {
								branch.dismissalList.remove(e);
							}
						}

						
						if (e.job.equals("CASHIER")) {
							Employee promoted = branch.checkForCashtoCookPromotion();
							if (promoted != null) {
								fileWriter.write(promoted.name + " is promoted from Cashier to Cook.\n");
								promoted.job = "COOK";
								branch.checkForPromoQueue(e);
							}
							
							
					
						}

						else if (e.job.equals("COOK")) {


							Employee promoted = branch.checkForCooktoManPromotion();

							if (promoted != null) {
								fileWriter.write(branch.manager.name + " is dismissed from branch: " + district + ".\n");
								fileWriter.write(promoted.name + " is promoted from Cook to Manager.\n");
								promoted.job = "MANAGER";
								branch.manager = promoted;
							}
						}



						else if (e.job.equals("MANAGER")) {


							Employee promoted = branch.checkDismissalforManager();


							if (promoted != null) {

								fileWriter.write(branch.manager.name + " is dismissed from branch: " + district + ".\n");
								fileWriter.write(promoted.name + " is promoted from Cook to Manager.\n");	


								promoted.job = "MANAGER";
								branch.manager = promoted;
							}
						}


						//check dismissals	
						for (int i = 0; i<branch.dismissalList.size(); i++) {
							Employee dismissed = branch.checkDismissalForSub(branch.dismissalList.get(i));
							if (dismissed != null) {
								String[] l = branch.name.split(" ");
								String district1 = l[1];
								fileWriter.write(dismissed.name + " is dismissed from branch: " + district1 + ".\n");
								branch.dismissalList.remove(i);
								i--;

							}

						}
						
						
						
						
						
						//check dismissal for manager
						if (branch.manager.points<=-5) {
							
							
							Employee promoted = branch.checkDismissalforManager();


							if (promoted != null) {

								fileWriter.write(branch.manager.name + " is dismissed from branch: " + district + ".\n");
								fileWriter.write(promoted.name + " is promoted from Cook to Manager.\n");	


								promoted.job = "MANAGER";
								branch.manager = promoted;
							}
							
							
						}





					}
				}

				if (task.equals("ADD")) {

					String[] array = arrayTask[1].split(", ");
					String city = array[0]; String district = array[1]; String name = array[2]; String job = array[3];
					String branchString = city + ", " + district;

					Branch branch = branchHash.find(branchString);

					Employee employee = new Employee(); employee.name = name; employee.job = job; employee.branch = branch;


					Employee isAdded = branch.employeeHash.find(name);
					if (isAdded != null)
						fileWriter.write("Existing employee cannot be added again.\n");

					else {


						branch.employeeHash.insert(employee);


						if (job.equals("CASHIER")) 
							branch.numCashier++;

						if (job.equals("COOK")) 
							branch.numCook++;

						if (job.equals("COURIER")) 
							branch.numCourier++;

						//check if any promotion is possible
						if (job.equals("CASHIER")) {
							Employee promoted = branch.checkForCashtoCookPromotion();
							if (promoted != null) {
								fileWriter.write(promoted.name + " is promoted from Cashier to Cook.\n");

								promoted.job = "COOK";
								
								branch.checkForPromoQueue(promoted);
							}
							
							
							
						}

						if (job.equals("COOK") && branch.manager.points<=-5) {
							Employee promoted = branch.checkForCooktoManPromotion();
							if (promoted != null) {
								fileWriter.write(branch.manager.name + " is dismissed from branch: " + district + ".\n");
								fileWriter.write(promoted.name + " is promoted from Cook to Manager.\n");

								branch.manager = promoted;
								promoted.job = "MANAGER";
							}
						}

						//check for any dismissals 
						//check dismissals	
						for (int i = 0; i<branch.dismissalList.size(); i++) {
							Employee dismissed = branch.checkDismissalForSub(branch.dismissalList.get(i));
							if (dismissed != null) {
								String[] l = branch.name.split(" ");
								String district1 = l[1];
								fileWriter.write(dismissed.name + " is dismissed from branch: " + district1 + ".\n");
								branch.dismissalList.remove(i);
								i--;
							}
							
						}
						
						
						//check dismissal for manager
						if (branch.manager.points<=-5) {
							
							
							Employee promoted = branch.checkDismissalforManager();


							if (promoted != null) {

								fileWriter.write(branch.manager.name + " is dismissed from branch: " + district + ".\n");
								fileWriter.write(promoted.name + " is promoted from Cook to Manager.\n");	


								promoted.job = "MANAGER";
								branch.manager = promoted;
							}
						}
						
						

					}
				}



				else if (task.equals("LEAVE")) {

					String[] array = arrayTask[1].split(", ");
					String city = array[0]; String district = array[1]; String name = array[2]; 
					String branchString = city + ", " + district;

					Branch branch = branchHash.find(branchString);
					Employee e = branch.employeeHash.find(name);
					if (e == null) {
						fileWriter.write("There is no such employee.\n");
					}

					else {
						if (   (e.job.equals("COURIER") && branch.numCourier>1) || 
								(e.job.equals("CASHIER") && branch.numCashier>1) ||
								(e.job.equals("COOK") && branch.numCook>1)
								) {


							Employee e1 = (Employee) branch.employeeHash.remove(name);

							if (e1 == null)
								fileWriter.write("There is no such employee.\n");

							else {
								fileWriter.write(e.name + " is leaving from branch: " + district + ".\n");



								if (e.job.equals("CASHIER")) {
									branch.numCashier--;
									branch.promoToCook.remove(e);
								}

								if (e.job.equals("COOK")) {
									branch.numCook--;
									branch.promoToManager.remove(e);
								}

								if (e.job.equals("COURIER"))
									branch.numCourier--;
							}

						}

						else if (e.job.equals("MANAGER")) {

							if (branch.promoToManager.size()!=0 && branch.numCook>1) {
								
								


								
								Employee newManager = branch.promoToManager.poll();
								
								newManager.job = "MANAGER";
								newManager.points -= 10;
								branch.numCook--;
								branch.manager = newManager;
								branch.employeeHash.remove(e.name);

								fileWriter.write(e.name + " is leaving from branch: " + district + ".\n");
								fileWriter.write(newManager.name + " is promoted from Cook to Manager.\n");
	

							}

							else {
								//cannot leave, give bonus
								if (e.points>-5) {
									branch.monthlyBonus += 200; branch.overallBonus += 200;
								}
							}


						}

						else {
							if (e.points>-5)  {
								branch.monthlyBonus += 200; branch.overallBonus += 200;
							}
						}
					}
				}



				else if (task.equals("PRINT_MANAGER")) {

					String[] array = arrayTask[1].split(", ");
					String city = array[0]; String district = array[1];
					String branchString = city + ", " + district;

					Branch branch = branchHash.find(branchString);

					fileWriter.write("Manager of the " + district + " branch is " + branch.manager.name + ".\n");

				}

				else if (task.equals("PRINT_MONTHLY_BONUSES")) {

					String[] array = arrayTask[1].split(", ");
					String city = array[0]; String district = array[1];
					String branchString = city + ", " + district;

					Branch branch = branchHash.find(branchString);


					fileWriter.write("Total bonuses for the " + district + " branch this month are: " + branch.monthlyBonus + "\n");

				}

				else if (task.equals("PRINT_OVERALL_BONUSES")) {

					String[] array = arrayTask[1].split(", ");
					String city = array[0]; String district = array[1];
					String branchString = city + ", " + district;

					Branch branch = branchHash.find(branchString);


					fileWriter.write("Total bonuses for the " + district + " branch are: " + branch.overallBonus + "\n");

				}






			}



		}




		fileWriter.close();
		scanner1.close();
		scanner2.close();
		
		
		
	}
}






