import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class ReportFrame extends JFrame implements ActionListener {
	
	ExpenseCalculator ec;
	JPanel reportPanel;
	User currUser;
	JComboBox<String> typeSelector, filterBy, category;
	JFrame filterDialog;
	JButton generateReport, importButton, exportButton;

	public ReportFrame(User currUser) {
		
		this.currUser = currUser;
		
		ec = new ExpenseCalculator(currUser);
		
		this.setTitle("View Reports");;
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
		reportPanel.add(getFullReportTable());
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
		// buttonsPanel.setBackground(Color.RED);
		constraints.gridy = 2;
		centerLock.add(buttonsPanel, constraints);
		
		importButton = new JButton("Import");
		importButton.addActionListener(this);
		exportButton = new JButton("Export");
		exportButton.addActionListener(this);
		JButton filterButton = new JButton("Filter");
		filterButton.addActionListener(event -> filterReport());
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
		
		s = "$" + s;
		
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
		if (e.getSource() == filterBy) {
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(getFilters());
			category.setModel(model);
			this.repaint();
			this.revalidate();
		}
		if (e.getSource() == generateReport) {
			
			reportPanel.removeAll();
			
			String[] incomeTableColumnNames = {
					"Source",
					"Amount",
					"Month"
			};
			
			String[] expenseTableColumnNames = {
					"Source",
					"Amount",
					"Frequency"
			};
			
			switch (filterBy.getSelectedItem().toString()) {
			
			case "Expense Source":
				
				ArrayList<String[]> expenseTableDataArrayList = new ArrayList<String[]>();
				
				for (Expense expense : currUser.getSpending()) {
					
					if (expense.source.equals(category.getSelectedItem().toString())) {
						String[] row = new String[3];
						row[0] = expense.source;
						row[1] = formatMoney(expense.amount);
						row[2] = Expense.yearlyFrequencyKey.getOrDefault(expense.yearlyFrequency, 
								Integer.toString(expense.yearlyFrequency) + " times / year");
	
						expenseTableDataArrayList.add(row);
					}
				}
				
				String[][] expenseTableData = expenseTableDataArrayList.toArray(new String[0][0]);
				
				JTable expenseTable = new JTable(new ReportTableModel(expenseTableData, expenseTableColumnNames));
				
				JScrollPane tablePane = new JScrollPane(expenseTable);
				
				tablePane.setPreferredSize(new Dimension(400, 200));
				
				reportPanel.add(tablePane);
				this.repaint();
				this.revalidate();
				break;
				
			case "Expense Frequency":
				
				expenseTableDataArrayList = new ArrayList<String[]>();
				
				for (Expense expense : currUser.getSpending()) {
					
					if (Expense.yearlyFrequencyKey.getOrDefault(expense.yearlyFrequency,
							Integer.toString(expense.yearlyFrequency) + " times / year").
							equals(category.getSelectedItem().toString())) {
						String[] row = new String[3];
						row[0] = expense.source;
						row[1] = formatMoney(expense.amount);
						row[2] = Expense.yearlyFrequencyKey.getOrDefault(expense.yearlyFrequency, 
								Integer.toString(expense.yearlyFrequency) + " times / year");
	
						expenseTableDataArrayList.add(row);
					}
				}
				
				expenseTableData = expenseTableDataArrayList.toArray(new String[0][0]);
				
				expenseTable = new JTable(new ReportTableModel(expenseTableData, expenseTableColumnNames));
				
				tablePane = new JScrollPane(expenseTable);
				
				tablePane.setPreferredSize(new Dimension(400, 200));
				
				reportPanel.add(tablePane);
				this.repaint();
				this.revalidate();
				break;
				
			case "Income Source":
				
				ArrayList<String[]> incomeTableDataArrayList = new ArrayList<String[]>();
				
				// Reads data from the current user's income and adds to array
				for (Wage incomeSource : currUser.getIncome()) {
					if (incomeSource.source.equals(category.getSelectedItem())) {
						String[] row = new String[3];
						row[0] = incomeSource.source;
						row[1] = formatMoney(incomeSource.amount);
						row[2] = incomeSource.month;
						incomeTableDataArrayList.add(row);
					}
				}
				
				// Converts to array for table model
				String[][] incomeTableData = incomeTableDataArrayList.toArray(new String[0][0]);
				
				JTable incomeTable = new JTable(new ReportTableModel(incomeTableData, incomeTableColumnNames));
				tablePane = new JScrollPane(incomeTable);
				tablePane.setBackground(Color.BLUE);
				tablePane.setPreferredSize(new Dimension(400, 200));
				
				reportPanel.add(tablePane);
				this.repaint();
				this.revalidate();
				break;
				
			case "Income Month":
				
				incomeTableDataArrayList = new ArrayList<String[]>();
				
				// Reads data from the current user's income and adds to array
				for (Wage incomeSource : currUser.getIncome()) {
					if (incomeSource.month.equals(category.getSelectedItem())) {
						String[] row = new String[3];
						row[0] = incomeSource.source;
						row[1] = formatMoney(incomeSource.amount);
						row[2] = incomeSource.month;
						incomeTableDataArrayList.add(row);
					}
				}
				
				// Converts to array for table model
				incomeTableData = incomeTableDataArrayList.toArray(new String[0][0]);
				
				incomeTable = new JTable(new ReportTableModel(incomeTableData, incomeTableColumnNames));
				tablePane = new JScrollPane(incomeTable);
				tablePane.setBackground(Color.BLUE);
				tablePane.setPreferredSize(new Dimension(400, 200));
				
				reportPanel.add(tablePane);
				this.repaint();
				this.revalidate();
				break;
			}
			filterDialog.dispose();
		}
		if (e.getSource() == importButton) {
			importReport();
		}
		if (e.getSource() == exportButton) {
			ec.exportReport(typeSelector.getSelectedItem().toString());
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
			row[1] = formatMoney(expense.amount);
			row[2] = Expense.yearlyFrequencyKey.getOrDefault(expense.yearlyFrequency, 
					Integer.toString(expense.yearlyFrequency) + " times / year");

			expenseTableDataArrayList.add(row);
		}
		
		String[][] expenseTableData = expenseTableDataArrayList.toArray(new String[0][0]);
		
		JTable expenseTable = new JTable(new ReportTableModel(expenseTableData, expenseTableColumnNames));
		
		JScrollPane tablePane = new JScrollPane(expenseTable);
		
		tablePane.setPreferredSize(new Dimension(400, 200));
				
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
			row[1] = formatMoney(incomeSource.amount);
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
		
		filterDialog = new JFrame("Filter");
		filterDialog.setSize(400,200);
		filterDialog.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		filterBy = null;
		category = null; 
		
		switch (typeSelector.getSelectedItem().toString()) {
		
		case "Full Report":
			String[] allCategories = {"Expense Source", "Expense Frequency", "Income Source", "Income Month"};
			filterBy = new JComboBox<String>(allCategories);
			filterBy.addActionListener(this);
			filterDialog.add(filterBy);
			category = new JComboBox<String>(getFilters());
			filterDialog.add(category);
			break;
			
		case "Income Report":
			String[] incomeCategories = {"Income Source", "Income Month"};
			filterBy = new JComboBox<String>(incomeCategories);
			filterBy.addActionListener(this);
			filterDialog.add(filterBy);
			category = new JComboBox<String>(getFilters());
			filterDialog.add(category);
			break;
			
		case "Expense Report":
			String[] expenseCategories = {"Expense Source", "Expense Frequency"};
			filterBy = new JComboBox<String>(expenseCategories);
			filterBy.addActionListener(this);
			filterDialog.add(filterBy);
			category = new JComboBox<String>(getFilters());
			filterDialog.add(category);
			break;

		}
		
		generateReport = new JButton("Generate");
		generateReport.addActionListener(this);
		
		filterDialog.add(generateReport);
		filterDialog.setVisible(true);
	}
	
	private String[] getFilters() {
		
		ArrayList<String> filters = new ArrayList<String>();
		
		switch (filterBy.getSelectedItem().toString()) {
		
		case "Expense Source":
			for (Expense e : currUser.getSpending()) {
				if (!filters.contains(e.source)) {
					filters.add(e.source);
				}
			}
			break;
			
		case "Expense Frequency":
			for (Expense e : currUser.getSpending()) {
				if (!filters.contains(Integer.toString(e.yearlyFrequency))) {
					filters.add(Expense.yearlyFrequencyKey.getOrDefault(e.yearlyFrequency, 
							Integer.toString(e.yearlyFrequency) + " times / year")); 
				}
			}
			break;
			
		case "Income Source":
			for (Wage w : currUser.getIncome()) {
				if (!filters.contains(w.source)) {
					filters.add(w.source); 
				}
			}
			break;
			
		case "Income Month":
			for (Wage w : currUser.getIncome()) {
				if (!filters.contains(w.month)) {
					filters.add(w.month); 
				}
			}
			break;
		}
		return filters.toArray(new String[0]);
	}
	
	private void importReport() {
		try {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Comma-separated value files (.csv)", "csv");
			fileChooser.setFileFilter(filter);
			int selection = fileChooser.showOpenDialog(null);
			File selectedFile;
			ArrayList<Wage> incomeList = new ArrayList<Wage>();
			ArrayList<Expense> expenseList = new ArrayList<Expense>();

			if (selection == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile().getAbsoluteFile();

				FileInputStream fileByteStream = new FileInputStream(selectedFile);
				Scanner inFS = new Scanner(fileByteStream);

				String labels = inFS.nextLine();

				// Income report
				if (labels.contains("month")) {
					while (inFS.hasNextLine()) {
						String[] line = Arrays.asList(inFS.nextLine().split(",")).toArray(new String[3]);
						
						// Stops reading if expense report is detected
						if (line[0].equals("yearly_frequency") || line[1].equals("yearly_frequency")
								|| line[2].equals("yearly_frequency")) {
							labels = "category,amount,yearly_frequency\n";
							break;
							
						}
						incomeList.add(new Wage(line[0], Double.parseDouble(line[1]), line[2]));
					}
				}
				// Expense report
				if (labels.contains("yearly_frequency")) {
					while (inFS.hasNextLine()) {
						
						String[] line = Arrays.asList(inFS.nextLine().split(",")).toArray(new String[3]);
						expenseList.add(new Expense(line[0], Double.parseDouble(line[1]), Integer.parseInt(line[2])));
						
					}
				}
				
				// Displays data if there is any
				if (incomeList.size() == 0 && expenseList.size() == 0) {
					JOptionPane.showMessageDialog(this, "File not readable! Please select a new file.", "Warning", 
							JOptionPane.WARNING_MESSAGE,null);
				}
				else {
					JFrame externalReportFrame = new JFrame("External Report");
					JTextPane textArea = new JTextPane();
					String textContents = "<table>";
					
					if (incomeList.size() != 0) {
						textContents += "<tr><th>Source</th><th>Amount</th><th>Month</th></tr>";
					}
					for (Wage w : incomeList) {
						textContents += "<tr><td>" + w.source + "</td><td>" + w.amount + "</td><td>" + w.month + "</td></tr>\n";
					}
					
					if (expenseList.size() != 0) {
						textContents += "<tr><th>Source</th><th>Amount</th><th>Month</th></tr>";
					}
					for (Expense e : expenseList) {
						textContents += "<tr><td>" + e.source + "</td><td>" + e.amount + "</td><td>" + e.yearlyFrequency + "</td></tr>\n";
					}
					
					textContents += "</table>";
					
					textArea.setContentType("text/html");
					textArea.setText(textContents);
					textArea.setEditable(false);
					externalReportFrame.add(textArea);
					externalReportFrame.setSize(300, 300);
					externalReportFrame.setVisible(true);
				}
				inFS.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
}


