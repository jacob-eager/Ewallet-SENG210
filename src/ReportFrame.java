import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class ReportFrame extends JFrame implements ActionListener {
	
	ExpenseCalculator ec;
	JComboBox<String> typeSelector;
	JPanel reportPanel;
	User currUser;

	public ReportFrame(User currUser) {
		
		this.currUser = currUser;
		
		ec = new ExpenseCalculator(currUser);
		
		this.setTitle("View Report");;
		this.setSize(700, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		// Panel to keep things centered
		JPanel centerLock = new JPanel();
		centerLock.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.insets = new Insets(5, 0, 5, 0);
		this.add(centerLock);
		
		
		
		
		reportPanel = new JPanel();
		reportPanel.setPreferredSize(new Dimension(500,300));
		reportPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		reportPanel.add(getIncomeTable());
		centerLock.add(reportPanel, constraints);
		
		String[] reportTypes = {
				"Full Report", "Income Report", "Expense Report"
		};
		typeSelector = new JComboBox<String>(reportTypes);
		typeSelector.setPreferredSize(new Dimension(400,25));
		typeSelector.addActionListener(this);
		constraints.gridy = 1;
		centerLock.add(typeSelector, constraints);
		
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.RED);
		constraints.gridy = 2;
		centerLock.add(buttonsPanel, constraints);
		
		JButton importButton = new JButton("Import");
		importButton.addActionListener(event -> ec.loadIncomeFile(getName()));
		JButton exportButton = new JButton("Export");
		exportButton.addActionListener(event -> ec.exportReport(getTitle()));
		JButton filterButton = new JButton("Filter");
		filterButton.addActionListener(event -> ec.printExpenseByType());
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(event -> this.dispose());
		
		buttonsPanel.add(importButton);
		buttonsPanel.add(exportButton);
		buttonsPanel.add(filterButton);
		buttonsPanel.add(closeButton);
		
		this.setVisible(true);
	}
	
	private static String formatMoney(double amount) {

		String s = Double.toString(amount);
		boolean afterDecimal = false;
		int decimalPlaces = 0;
		
		for (char c : s.toCharArray()) {
			if (c == '.') {
				afterDecimal = true;
				continue;
			}
			if (afterDecimal) {
				++decimalPlaces;
			}
		}
		
		switch (decimalPlaces) {
		
		case 1:
			return s + "0";
			
		case 2:
			return s;
			
		// > 2 decimal places
		default:
			return s.substring(0, s.length() - (decimalPlaces - 2));  // Gets rid of excess decimal places (TODO: Doesn't round, just cuts off)
		
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == typeSelector) {
			if (typeSelector.getSelectedItem().toString().equals("Full Report")) {
				reportPanel.removeAll();
				reportPanel.add(getFullReportTable());
				this.repaint();
				this.revalidate();
			}
			if (typeSelector.getSelectedItem().toString().equals("Income Report")) {
				reportPanel.removeAll();
				reportPanel.add(getIncomeTable());
				this.repaint();
				this.revalidate();
			}
			if (typeSelector.getSelectedItem().toString().equals("Expense Report")) {
				reportPanel.removeAll();
				reportPanel.add(getExpenseTable());
				this.repaint();
				this.revalidate();
			}
		}
		
	}
	
	private JScrollPane getExpenseTable() {
		String[] expenseTableColumnNames = {
				"Source",
				"Amount",
				"Frequency"
		};
		
		ArrayList<String[]> expenseTableDataArrayList = new ArrayList<String[]>();
		
		// Reads data from the current user's expenses and adds to array
		for (Expense expense : currUser.getSpending()) {
			String[] row = new String[3];
			row[0] = expense.source;
			row[1] = "$" + Double.toString(expense.amount);

			switch (expense.yearlyFrequency) {
			
			case 1:
				row[2] = "Yearly";
				break;
				
			case 12:
				row[2] = "Monthly";
				break;
				
			case 24:
				row[2] = "Biweekly";
				break;
				
			case 52:
				row[2] = "Weekly";
				break;
				
			default:
				row[2] = Integer.toString(expense.yearlyFrequency) + " times / year";
				break;
			}
			expenseTableDataArrayList.add(row);
		}
		
		String[][] expenseTableData = expenseTableDataArrayList.toArray(new String[0][0]);
		
		JTable expenseTable = new JTable(new ReportTableModel(expenseTableData, expenseTableColumnNames));
		
		JScrollPane tablePane = new JScrollPane(expenseTable);
		
		tablePane.setPreferredSize(new Dimension(300, 100));
				
		return tablePane;
	}
	
	private JScrollPane getIncomeTable() {
		
		String[] incomeTableColumnNames = {
				"Source",
				"Amount",
				"Month"
		};
		
		ArrayList<String[]> incomeTableDataArrayList = new ArrayList<String[]>();
		
		// Reads data from the current user's income and adds to array
		for (Wage incomeSource : currUser.getIncome()) {
			String[] row = new String[3];
			row[0] = incomeSource.source;
			row[1] = "$" + formatMoney(incomeSource.amount);
			row[2] = incomeSource.month;
			incomeTableDataArrayList.add(row);
		}
		
		// Converts to array for table model
		String[][] incomeTableData = incomeTableDataArrayList.toArray(new String[0][0]);
		
		JTable incomeTable = new JTable(new ReportTableModel(incomeTableData, incomeTableColumnNames));
		JScrollPane tablePane = new JScrollPane(incomeTable);
		tablePane.setBackground(Color.BLUE);
		tablePane.setPreferredSize(new Dimension(400, 200));
		
		return tablePane;
	}
	
	private JScrollPane getFullReportTable() {
		
		JScrollPane scrollPane = new JScrollPane();
		
		JPanel fullReport = new JPanel();
		fullReport.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		
		JLabel reportLabel = new JLabel("Full report for " + currUser.username + ":");
		constraints.gridx = 0;
		constraints.gridy = 0;
		fullReport.add(reportLabel, constraints);
		
		JLabel incomeLabel = new JLabel("Income: ");
		constraints.gridy = 1;
		fullReport.add(incomeLabel, constraints);
		
		constraints.gridy = 2;
		fullReport.add(getIncomeTable(), constraints);
		
		JLabel expenseLabel = new JLabel("Expenses: ");
		constraints.gridy = 3;
		fullReport.add(expenseLabel, constraints);
		
		constraints.gridy = 4;
		fullReport.add(getExpenseTable(), constraints);
		scrollPane.setViewportView(fullReport);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(475, 275));
		
		return scrollPane;
		
	}
	
	private void filterReport() {
		
	}
}
