import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class ExpenseCalculator implements Expenser {
    public User userAtHand;

    public ExpenseCalculator(User user) {
        this.userAtHand = user;
    }

    public void addExpense(Expense Ex) {
    	userAtHand.getSpending().add(Ex);
    }

    public void addMonthlyIncome(Wage W) {
    	userAtHand.getIncome().add(W);
    }

    public void PrintFullreport() {

    }

    public void PrintExpensereport() {

        System.out.println("Expense report:");

        ArrayList<Expense> expenses = userAtHand.getSpending();

        for (int i = 0; i < expenses.size(); i++) {
            System.out.println("$" + expenses.get(i).amount + " from " + expenses.get(i).source + " with a frequency of " + expenses.get(i).yearlyfrequency + " times a year.");
        }

    }

    public void PrintIncomereport() {

        System.out.println("Income report:");

        ArrayList<Wage> income = userAtHand.getIncome();

        for (int i = 0; i < income.size(); i++) {
            System.out.println("$" + income.get(i).amount + " from " + income.get(i).source + " in the month of " + income.get(i).Month);
        }

    }

    public void PrintIncomereportbyType() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter income type: ");
        String type = scanner.nextLine();

        double total = 0;
        System.out.println("Type: " + type);
        
        for (int i = 0; i < userAtHand.getIncome().size(); i++) {
            if (userAtHand.getIncome().get(i).source.equalsIgnoreCase(type)) {
                System.out.println("Amount: $" + userAtHand.getIncome().get(i).amount + " in " + userAtHand.getIncome().get(i).Month);
                total += userAtHand.getIncome().get(i).amount;
            }
        }

        System.out.println("Total income for " + type + ": $" + total + " over " + userAtHand.getIncome().size() + " months");
    }

    public void PrintExpensebyType() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter expense type: ");
        String type = scanner.nextLine();
        
        double total = 0;
        System.out.println("Type: " + type);
        
        for (int i = 0; i < userAtHand.getSpending().size(); i++) {
            if (userAtHand.getSpending().get(i).source.equalsIgnoreCase(type)) {
                String freq = "";
                if (userAtHand.getSpending().get(i).yearlyfrequency == 1) {freq = "1 time a year";}
                else {freq = userAtHand.getSpending().get(i).yearlyfrequency + " times a year";}
                System.out.println("Amount: $" + userAtHand.getSpending().get(i).amount + ", " + freq);
                total += userAtHand.getSpending().get(i).amount * userAtHand.getSpending().get(i).yearlyfrequency / 12.0;
            }
        }

        System.out.println("Total cost for " + type + ": $" + total + " every month");
    }

    public void exportReport(String reportTitle) {
      
        

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
        return false; //Placeholder return statement
    }

    public boolean loadIncomeFile(String filePath) {
        return false; //Placeholder return statement
    }

    public int whenCanIBuy(String itemname, double price) {
        return -1; //Placeholder return statement
    }

    public void updateMonthlySavings() {

    }
}
