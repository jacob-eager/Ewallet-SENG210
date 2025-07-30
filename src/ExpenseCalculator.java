import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class ExpenseCalculator implements Expenser {
	public User userAtHand;

	public ExpenseCalculator(User user) {
		this.userAtHand = user;
	}

	public void addExpense(Expense Ex) {
		userAtHand.addSpending(Ex);
	}

	public void addMonthlyIncome(Wage W) {
		userAtHand.addIncome(W);
	}

	public void printFullReport() {
		System.out.println("<<Full Report>>");
		float totalExpense = 0;
		float totalIncome = 0;
		for (Expense s : userAtHand.getSpending()) {
			totalExpense += s.amount * s.yearlyFrequency; // calculate total expense based on frequency
		}
		for (Wage w : userAtHand.getIncome()) {
			totalIncome += w.amount;
		}

		printExpenseReport();
		System.out.println("Total yearly expenses: $" + totalExpense);

		printIncomeReport();
		for (String m : new String[] { "January", "February", "March", "April", "May", "June", "July", "August",
				"September", "October", "November", "December" }) {
			float monthlyIncome = 0;
			for (Wage w : userAtHand.getIncome()) {
				if (w.month.equals(m)) {
					monthlyIncome += w.amount;
				}
			}

			if (monthlyIncome != 0) { // only print if there is income(s) for the month
				System.out.println("Total income for " + m + ": $" + monthlyIncome);
			}
		}

		System.out.println("Total yearly income: $" + totalIncome);

		double totalSavings = (totalIncome - totalExpense);
		if (totalSavings >= 0) {
			System.out.println("Total savings: $" + totalSavings);
		} else {
			System.out.println("Total new debt: $" + totalSavings);
		}
	}

	public void printExpenseReport() {

		System.out.println("Expense report:");

		ArrayList<Expense> expenses = userAtHand.getSpending();

		for (int i = 0; i < expenses.size(); i++) {
			System.out.println("$" + expenses.get(i).amount + " from " + expenses.get(i).source
					+ " with a frequency of " + expenses.get(i).yearlyFrequency + " times a year.");
		}

	}

	public void printIncomeReport() {

		System.out.println("Income report:");

		ArrayList<Wage> income = userAtHand.getIncome();

		for (int i = 0; i < income.size(); i++) {
			System.out.println("$" + income.get(i).amount + " from " + income.get(i).source + " in the month of "
					+ income.get(i).month);
		}

	}

	public void printIncomeReportByType() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter income type: ");
		String type = scanner.nextLine();

		double total = 0;
		int count = 0;
		System.out.println("Type: " + type);

		for (int i = 0; i < userAtHand.getIncome().size(); i++) {
			if (userAtHand.getIncome().get(i).source.equalsIgnoreCase(type)) {
				System.out.println("Amount: $" + userAtHand.getIncome().get(i).amount + " in "
						+ userAtHand.getIncome().get(i).month);
				total += userAtHand.getIncome().get(i).amount;
				count++;
			}
		}

		System.out.println("Total income for " + type + ": $" + total + " over " + count + " months");
		scanner.close();
	}

	public void printExpenseByType() {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter expense type: ");
		String type = scanner.nextLine();

		double total = 0;
		System.out.println("Type: " + type);

		for (int i = 0; i < userAtHand.getSpending().size(); i++) {
			if (userAtHand.getSpending().get(i).source.equalsIgnoreCase(type)) {
				String freq = "";
				if (userAtHand.getSpending().get(i).yearlyFrequency == 1) {
					freq = "1 time a year";
				} else {
					freq = userAtHand.getSpending().get(i).yearlyFrequency + " times a year";
				}
				System.out.println("Amount: $" + userAtHand.getSpending().get(i).amount + ", " + freq);
				total += userAtHand.getSpending().get(i).amount * userAtHand.getSpending().get(i).yearlyFrequency
						/ 12.0;
			}
		}

		System.out.println("Total cost for " + type + ": $" + total + " every month");
		scanner.close();
	}

	public void exportReport(String reportTitle) {

		try {
			File report = new File(reportTitle + ".json");
			if (report.createNewFile()) {
				System.out.println(report.getName() + " created.");
			} else {
				System.out.println("File already exists");
			}

			FileWriter reportWriter = new FileWriter(reportTitle + ".json");

			switch (reportTitle.toLowerCase()) {

			case "expense":

				ArrayList<Expense> expenses = userAtHand.getSpending();

				for (int i = 0; i < expenses.size(); i++) {
					reportWriter.write("$" + expenses.get(i).amount + " from " + expenses.get(i).source
							+ " with a frequency of " + expenses.get(i).yearlyFrequency + " times a year.");
				}

				break;

			case "income":

				ArrayList<Wage> income = userAtHand.getIncome();

				for (int i = 0; i < income.size(); i++) {
					reportWriter.write("$" + income.get(i).amount + " from " + income.get(i).source
							+ " in the month of " + income.get(i).month);
				}

				break;

			}

			reportWriter.close();

			System.out.println("Report successfully exported.");

		}

		catch (IOException e) {
			System.out.println("Unexpected error occcured exporting file");
			e.getStackTrace();
		}

	}

	public Currency convertForeignCurrency(Currency C, double amount, Boolean toUSD) {
		Currency result = new Currency();

		if (toUSD) {
			result.name = "USD";
			result.rate = 1;
			System.out.println("Your balance in USD from " + C.name + ": " + amount * C.rate);
		} else {
			result.name = C.name;
			result.rate = C.rate;
			System.out.println("Your balance in " + C.name + " from USD: " + amount / C.rate);
		}

		return result;
	}

	public boolean loadExpenseFile(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");

				String source = parts[0];
				double amount = Double.parseDouble(parts[1]);
				int yearlyfrequency = Integer.parseInt(parts[2]);

				Expense expense = new Expense(source, amount, yearlyfrequency);
				userAtHand.getSpending().add(expense);
			}
			return true;
		} catch (IOException | NumberFormatException e) {
			System.out.println("Error loading expenses: " + e.getMessage());
			return false;
		}
	}

	public boolean loadIncomeFile(String filePath) {
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");

				String source = parts[0];
				double amount = Double.parseDouble(parts[1]);
				String Month = parts[2];

				Wage wage = new Wage(source, amount, Month);
				userAtHand.getIncome().add(wage);
			}
			return true;
		} catch (IOException | NumberFormatException e) {
			System.out.println("Error loading income: " + e.getMessage());
			return false;

		}
	}

	public int whenCanIBuy(String itemName, double price) {

		double waitTime = price / userAtHand.monthlySavings;

		System.out.println(
				"You will be able to buy a(n) " + itemName + " in " + (int) Math.round(waitTime) + " month(s).");

		return (int) Math.round(waitTime);
	}

	public void updateMonthlySavings() {
		float monthlyExpense = 0;
		float monthlyIncome = 0;
		for (Expense s : userAtHand.getSpending()) {
			if (s.yearlyFrequency >= 12) { // only consider monthly or biweekly expenses
				monthlyExpense += s.amount * (s.yearlyFrequency / 12); // calculate total expense based on frequency
			}
		}
		String curMonth = userAtHand.getIncome().get(userAtHand.getIncome().size() - 1).month;
		for (Wage w : userAtHand.getIncome()) {
			if (w.month.equals(curMonth)) {
				monthlyIncome += w.amount;
			}
		}

		userAtHand.setMonthlySavings(monthlyIncome - monthlyExpense);
	}
}
