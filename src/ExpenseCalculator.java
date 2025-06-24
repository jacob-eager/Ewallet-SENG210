import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class ExpenseCalculator implements Expenser {
    public User userAtHand;

    public ExpenseCalculator(User user) {
        this.userAtHand = user;
    }

    public void addExpense(Expense Ex) {

    }

    public void addMonthlyIncome(Wage W) {

    }

    public void PrintFullreport() {

    }

    public void PrintExpensereport() {

    }

    public void PrintIncomereport() {

    }

    public void PrintIncomereportbyType() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter income type: ");
        String type = scanner.nextLine();

        double total = 0;
        System.out.println("Type: " + type);
        
        for (int i = 0; i < userAtHand.Income.size(); i++) {
            if (userAtHand.Income.get(i).source.equalsIgnoreCase(type)) {
                System.out.println("Amount: $" + userAtHand.Income.get(i).amount + " in " + userAtHand.Income.get(i).Month);
                total += userAtHand.Income.get(i).amount;
            }
        }

        System.out.println("Total income for " + type + ": $" + total + " over " + userAtHand.Income.size() + " months");
    }

    public void PrintExpensebyType() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter expense type: ");
        String type = scanner.nextLine();
        
        double total = 0;
        System.out.println("Type: " + type);
        
        for (int i = 0; i < userAtHand.Spending.size(); i++) {
            if (userAtHand.Spending.get(i).source.equalsIgnoreCase(type)) {
                String freq = "";
                if (userAtHand.Spending.get(i).yearlyfrequency == 1) {freq = "1 time a year";}
                else {freq = userAtHand.Spending.get(i).yearlyfrequency + " times a year";}
                System.out.println("Amount: $" + userAtHand.Spending.get(i).amount + ", " + freq);
                total += userAtHand.Spending.get(i).amount * userAtHand.Spending.get(i).yearlyfrequency / 12.0;
            }
        }

        System.out.println("Total cost for " + type + ": $" + total + " every month");
    }

    public void exportReport(String reportTitle) {
      
    }

    public Currency convertForeignCurrency(Currency C, double amount) {
        return C; //Placeholder return statement
    }

    public boolean loadExpenseFile(String filePath) {
        return False; //Placeholder return statement
    }

    public boolean loadIncomeFile(String filePath) {
        return False; //Placeholder return statement
    }

    public int whenCanIBuy(String itemname, double price) {
        return -1; //Placeholder return statement
    }

    public void updateMonthlySavings() {

    }
}
