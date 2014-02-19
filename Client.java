/*
 *  Author: Lance Baker
 *  Student No: 3128034
 *  Date: 25-04-2011
 *  Description: 
 *  The class is used to store client related income-based data, and calculate the
 *  tax rate in which applies to that client's income & residential status (using the public 
 *  methods available). It also has a overridden toString method which is used to retrieve 
 *  preformatted String output that can be optionally used for basic displaying purposes.
 *  The class also contains two Account datatype reference attributes (account1, account2) in which 
 *  are both used for storing their investment Account objects. It doesn't do any management/ handling
 *  of the Account objects, but provides getter & setter methods so the handling can be done externally to
 *  the class.
 */

import java.text.NumberFormat;
import java.text.DecimalFormat;

public class Client {

	// The following constants are used for the resident tax calculations.
	private static final int RESIDENT_TAXABLE_INCOME1 = 6000;
	private static final int RESIDENT_TAXABLE_INCOME2 = 37000;
	private static final double RESIDENT_TAXABLE_INCOME2_RATE = 0.15;
	private static final int RESIDENT_TAXABLE_INCOME3 = 80000;
	private static final int RESIDENT_TAXABLE_INCOME3_TAX = 4650;
	private static final double RESIDENT_TAXABLE_INCOME3_RATE = 0.30;
	private static final int RESIDENT_TAXABLE_INCOME4 = 180000;
	private static final int RESIDENT_TAXABLE_INCOME4_TAX = 17550;
	private static final double RESIDENT_TAXABLE_INCOME4_RATE = 0.37;
	private static final int RESIDENT_TAXABLE_INCOME5_TAX = 54550;
	private static final double RESIDENT_TAXABLE_INCOME5_RATE = 0.45;
	private static final int MEDICARE_LEVY = 20000;
	private static final double MEDICARE_LEVY_RATE = 0.015;
	
	// The following constants are used for the nonresident tax calculations.
	private static final int NONRESIDENT_TAXABLE_INCOME1 = 37000;
	private static final double NONRESIDENT_TAXABLE_INCOME1_RATE = 0.29;
	private static final int NONRESIDENT_TAXABLE_INCOME2 = 80000;
	private static final int NONRESIDENT_TAXABLE_INCOME2_TAX = 10730;
	private static final double NONRESIDENT_TAXABLE_INCOME2_RATE = 0.30;
	private static final int NONRESIDENT_TAXABLE_INCOME3 = 180000;
	private static final int NONRESIDENT_TAXABLE_INCOME3_TAX = 23630;
	private static final double NONRESIDENT_TAXABLE_INCOME3_RATE = 0.37;
	private static final int NONRESIDENT_TAXABLE_INCOME4_TAX = 60630;
	private static final double NONRESIDENT_TAXABLE_INCOME4_RATE = 0.45;
	
	// Some miscellaneous constants.
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");
	private static final int WEEKS_PER_YEAR = 52;
	
	// The following constants are used for formatting purposes by the toString method.
	private static final String LABEL_NAME = "Name: ";
	private static final String LABEL_NET_SALARY = "Net Salary";
	private static final String LABEL_PER_WEEK = "Per Week: ";
	private static final String LABEL_PER_YEAR = "Per Year: ";
	private static final String LABEL_TAX_PAID = "Tax Paid";
	private static final String LABEL_MEDICARE = "Medicare Levy " + LABEL_PER_YEAR;
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String EMPTY_STRING = "";

	// Instance attributes.
	private String name;
	private Account account1, account2;
	private double grossSalary;
	private double netSalary;
	private boolean resident;
	private double tax;
	private double medicare;
	private double weeklyExpenses;
	
	/**
	* The default constructor. It chains with initial values to the second constructor.
	*/
	public Client() {
		this(EMPTY_STRING, 0);
	}
	
	/**
	* The second constructor accepts the client name, and grossSalary. It chains to the main constructor
	* passing a default "resident" value of true.
	* @param name String - The full name of the client.
	* @param grossSalary double - The gross salary.
	*/
	public Client(String name, double grossSalary) {
		this(name, grossSalary, true);
	}
	
	/**
	* The main constructor for the Client. It receives the three parameters that are used for calculating the tax.
	* @param name String - The full name of the client.
	* @param grossSalary double - The gross salary.
	* @param resident boolean - A boolean value indicating whether the client is a resident or not.
	*/
	public Client(String name, double grossSalary, boolean resident) {
		this.setName(name);
		this.setGrossSalary(grossSalary);
		this.setResident(resident);
	}
	
	/**
	* The calcTaxFormula method is used by the calcTax method to calculate the income tax based on a set of parameters. It is a separate
	* method to avoid the repeated logic experienced throughout the conditions. It receives the taxableIncome (in being the last amount of 
	* the previous tax bracket) which is then subtracted from the gross salary, in order to get the range for the taxable income. It is then multiplied
	* by the taxableRate variable. The taxAmount is just an additional tax amount that is added to the other calculated amount.
	* @param taxableIncome double - The last amount of the previous tax bracket.
	* @param taxableRate double - The tax rate for each dollar over the taxableIncome bracket.
	* @param taxAmount double - An additional tax amount that is added to the calculated value.
	*/
	private double calcTaxFormula(double taxableIncome, double taxableRate, double taxAmount) {
		return (((this.getGrossSalary() - taxableIncome) * taxableRate) + taxAmount);
	}
	
	public void setName(String name) {
		this.name = name;	
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setAccount1(Account account1) {
		this.account1 = account1;
	}
	
	public Account getAccount1() {
		return this.account1;
	}
	
	public void setAccount2(Account account2) {
		this.account2 = account2;
	}
	
	public Account getAccount2() {
		return this.account2;
	}
	
	public void setGrossSalary(double grossSalary) {
		this.grossSalary = grossSalary;
	}
	
	public double getGrossSalary() {
		return this.grossSalary;
	}
	
	public void setNetSalary(double netSalary) {
		this.netSalary = netSalary;
	}
	
	public double getNetSalary() {
		return this.netSalary;
	}
	
	/**
	* The getWeeklyNetSalary method is used to retrieve the net salary for each week. It is calculated by dividing the annual amount by
	* the amount of weeks in the year, and then rounded to two decimal places.
	* @return double - The weekly net salary rounded to two decimal places.
	*/
	public double getWeeklyNetSalary() {
		// Performs the calculation, then formats the result with the DecimalFormat utility class instance. It then converts that 
		// resulting String back to a double using the parseDouble method.
		return Double.parseDouble(DECIMAL_FORMAT.format((this.getNetSalary() /WEEKS_PER_YEAR)));
	}
	
	public void setResident(boolean resident) {
		this.resident = resident;
	}
	
	public boolean getResident() {
		return this.resident;
	}
	
	public void setTax(double tax) {
		this.tax = tax;
	}
	
	public double getTax() {
		return this.tax;
	}
	
	/**
	* The getWeeklyTax method is used to retrieve the weekly amount of tax being paid. It is calculated based on the annual tax, that
	* is then divided by the amount of weeks in a year. The result is then rounded to two decimal places.
	* @return double - The weekly tax rounded to two decimal places.
	*/
	public double getWeeklyTax() {
		// Performs the calculation, then formats the result with the DecimalFormat utility class instance. It then converts that 
		// resulting String back to a double using the parseDouble method.
		return Double.parseDouble(DECIMAL_FORMAT.format((this.getTax() /WEEKS_PER_YEAR)));
	}
	
	public void setMedicare(double medicare) {
		this.medicare = medicare;
	}
	
	public double getMedicare() {
		return this.medicare;
	}
	
	public void setWeeklyExpenses(double weeklyExpenses) {
		this.weeklyExpenses = weeklyExpenses;
	}
	
	public double getWeeklyExpenses() {
		return this.weeklyExpenses;
	}
	
	/**
	* The getAvailableFunds method is used to retrieve the funds that are left after the weekly expenses have been deducted.
	* It rounds the amount to two decimal places (which enables for the figure to be compared with other values easier).
	* @return double - The remaining funds rounded to two decimal places.
	*/
	public double getAvailableFunds() {
		// Performs the calculation, then formats the result with the DecimalFormat utility class instance. It then converts that 
		// resulting String back to a double using the parseDouble method.
		return Double.parseDouble(DECIMAL_FORMAT.format((this.getWeeklyNetSalary() - this.getWeeklyExpenses())));
	}
	
	/**
	* The calcTax method is used to calculate the income tax; it performs the tax calculation based on their residency and gross salary.
	* The result is then assigned to the tax instance attribute via the setter. It uses the partner method calcTaxFormula, passing in the
	* different arguments for each condition, which is done to avoid repeated logic.
	*/
	public void calcTax() {
		double tax = 0;
		// If the user is a resident, then it will complete the income calculations based on the tax rates for residents.
		if (this.getResident()) {
			// If the income is less than or equal to $6,000
			if (this.getGrossSalary() <= RESIDENT_TAXABLE_INCOME1) {
				tax = 0; // Then the tax would be nil.
			// Otherwise, if the income is less than or equal to $37,000
			} else if (this.getGrossSalary() <= RESIDENT_TAXABLE_INCOME2) {
				// Then the tax would be 15c for each $1 over $6,000.
				tax = this.calcTaxFormula(RESIDENT_TAXABLE_INCOME1, RESIDENT_TAXABLE_INCOME2_RATE, 0);
			// Otherwise, if the income is less than or equal to $80,000
			} else if (this.getGrossSalary() <= RESIDENT_TAXABLE_INCOME3) {
				// Then the tax would be $4,650 plus 30c for each $1 over $37,000.
				tax = this.calcTaxFormula(RESIDENT_TAXABLE_INCOME2, RESIDENT_TAXABLE_INCOME3_RATE, RESIDENT_TAXABLE_INCOME3_TAX);
			// Otherwise, if the income is less than or equal to $180,000
			} else if (this.getGrossSalary() <= RESIDENT_TAXABLE_INCOME4) {
				// Then the tax would be $17,550 plus 37c for each $1 over $80,000
				tax = this.calcTaxFormula(RESIDENT_TAXABLE_INCOME3, RESIDENT_TAXABLE_INCOME4_RATE, RESIDENT_TAXABLE_INCOME4_TAX);
			// Otherwise, the income would be over $180,000
			} else {
				// Therefore, the tax would be $54,550 plus 45c for each $1 over $180,000
				tax = this.calcTaxFormula(RESIDENT_TAXABLE_INCOME4, RESIDENT_TAXABLE_INCOME5_RATE, RESIDENT_TAXABLE_INCOME5_TAX);
			}				
		// Otherwise it will do the income calculations based on the tax rates for nonresidents.
		} else {
			// If the income is less than or equal to $37,000
			if (this.getGrossSalary() <= NONRESIDENT_TAXABLE_INCOME1) {
				// Then the tax would be 29c for each $1.
				tax = this.calcTaxFormula(0, NONRESIDENT_TAXABLE_INCOME1_RATE, 0);
			// Otherwise, if the income is less than or equal to $80,000
			} else if (this.getGrossSalary() <= NONRESIDENT_TAXABLE_INCOME2) {
				// Then the tax would be $10,730 plus 30c for each $1 over $37,000.
				tax = this.calcTaxFormula(NONRESIDENT_TAXABLE_INCOME1, NONRESIDENT_TAXABLE_INCOME2_RATE, NONRESIDENT_TAXABLE_INCOME2_TAX);
			// Otherwise, if the income is less than or equal to $180,000
			} else if (this.getGrossSalary() <= NONRESIDENT_TAXABLE_INCOME3) {
				// Then the tax would be $23,630 plus 37c for each $1 over $80,000
				tax = this.calcTaxFormula(NONRESIDENT_TAXABLE_INCOME2, NONRESIDENT_TAXABLE_INCOME3_RATE, NONRESIDENT_TAXABLE_INCOME3_TAX);
			// Otherwise, the income would be over $180,000
			} else {
				// Therefore, the tax would be $60,630 plus 45c for each $1 over $180,000
				tax = this.calcTaxFormula(NONRESIDENT_TAXABLE_INCOME3, NONRESIDENT_TAXABLE_INCOME4_RATE, NONRESIDENT_TAXABLE_INCOME4_TAX);
			}
		}
		this.setTax(tax);
	}
	
	/**
	* The calcMedicare method will only calculate the medicare tax if the client is a resident, and also if the client earns more than the medicare
	* levy threshold. The calculation is done by multiplying the gross salary with the medicare levy rate. It sets the result to the medicare attribute
	* via its setter.
	*/
	public void calcMedicare() {
		// If the client is a resident it will calculate the medicare tax.
		if (this.getResident()) {
			// If the income is greater than or equal to the medicare levy amount (being $20,000) then 
			// the medicare tax is calculated at 1.5% of the taxable income. It sets the calculated value
			// to the medicare attribute via the setter method.
			this.setMedicare((this.getGrossSalary() >= MEDICARE_LEVY) ? 
				(this.getGrossSalary() * MEDICARE_LEVY_RATE) : 0);
		}
	}
	
	/**
	* The calcNetSalary method is used to calculate the net salary after the income and medicare tax have been calculated. It sets the result to
	* the netSalary attribute via its setter.
	*/
	public void calcNetSalary() {
		this.setNetSalary((this.getGrossSalary() - this.getTax()) - this.getMedicare());
	}
	
	/**
	* The toString method is overridden from the super Object, and
	* enables a predefined way for (optionally) retrieving the contents of the Object's attributes for simple presentation purposes.
	* @return String - A text version containing the attribute data.
	*/
	public String toString() {
		// Instantiates a StringBuilder which is used to concatenate the output.
		StringBuilder builder = new StringBuilder();
		// The NumberFormat utility class returns a currency instance,
		// which is used to format the currency values.
		NumberFormat currency = NumberFormat.getCurrencyInstance();
		// The client's full name.
		builder.append(NEW_LINE);  
		builder.append(LABEL_NAME); 
		builder.append(this.getName());
		builder.append(NEW_LINE);  
		
		// The net salary heading.
		builder.append(NEW_LINE);  
		builder.append(LABEL_NET_SALARY); 
		builder.append(NEW_LINE); 
		// The weekly net salary amount.
		builder.append(LABEL_PER_WEEK); 
		builder.append(currency.format(this.getWeeklyNetSalary()));
		builder.append(NEW_LINE);  
		// The annual net salary amount.
		builder.append(LABEL_PER_YEAR); 
		builder.append(currency.format(this.getNetSalary()));
		builder.append(NEW_LINE); 
		
		// The tax paid heading
		builder.append(NEW_LINE); 
		builder.append(LABEL_TAX_PAID); 
		builder.append(NEW_LINE); 
		// The weekly tax being paid.
		builder.append(LABEL_PER_WEEK); 
		builder.append(currency.format(this.getWeeklyTax()));
		builder.append(NEW_LINE); 
		// The annual tax being paid.
		builder.append(LABEL_PER_YEAR); 
		builder.append(currency.format(this.getTax()));
		builder.append(NEW_LINE); 
		
		// The medicare tax.
		builder.append(NEW_LINE); 
		builder.append(LABEL_MEDICARE); 
		builder.append(currency.format(this.getMedicare()));
		
		return builder.toString(); // Returns a String.
	}	
}