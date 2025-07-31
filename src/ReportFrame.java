import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class ReportFrame extends JFrame implements ActionListener {
	
	ExpenseCalculator ec;

	public ReportFrame(User currUser) {
		
		ec = new ExpenseCalculator(currUser);
		
		JFrame reportFrame = new JFrame("Generate Report");
		reportFrame.setSize(700, 500);
		reportFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		reportFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		// Panel to keep things centered
		JPanel centerLock = new JPanel();
		centerLock.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		reportFrame.add(centerLock);
		
		
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
		tablePane.setPreferredSize(new Dimension(400, 275));
		
		
		JPanel reportPanel = new JPanel();
		reportPanel.setPreferredSize(new Dimension(500,300));
		reportPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		reportPanel.add(tablePane);
		centerLock.add(reportPanel, constraints);
		
		String[] reportTypes = {
				"Full Report", "Income Report", "Expense Report"
		};
		JComboBox<String> typeSelector = new JComboBox<String>(reportTypes);
		typeSelector.setPreferredSize(new Dimension(400,25));
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
		closeButton.addActionListener(event -> reportFrame.dispose());
		
		buttonsPanel.add(importButton);
		buttonsPanel.add(exportButton);
		buttonsPanel.add(filterButton);
		buttonsPanel.add(closeButton);
		
		reportFrame.setVisible(true);
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
			return s.substring(0, s.length() - (decimalPlaces - 2));  // Gets rid of excess decimal places (FIXME: Doesn't round, just cuts off)
		
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
