/*	
 *  Author: Lance Baker
 *  Student No: 3128034
 *  Date: 25-04-2011
 *  Description: 
 *  The class is a Command Line Interface for the application. It handles all data entries, 
 *  validation checking (with error handling), and uses the relating classes "Client" and "Account" for the 
 *  storing, management, and retrieval of such data. It is the only class that displays any output to the user,
 *  and is the singular starting and ending point for the application.
 */

import java.util.*;

public class CalculatorInterface {

	// The following constants are used for input prompts, and error messages relating to input given.
	private static final String SPACE = " ";
	private static final String INPUT_NAME_MSG = "Please enter your full name: ";
	private static final String INPUT_NAME_REGEX = "([a-zA-Z]+\\s[a-zA-Z]+)";
	private static final String INPUT_NAME_ERR = "Error: Please enter your first and last name (separated by a space)";
	private static final String INPUT_BOOLEAN_CRITERIA = "[Yes or No]: ";
	private static final String INPUT_BOOLEAN_YES = "Yes";
	private static final String INPUT_BOOLEAN_NO = "No";
	private static final String INPUT_BOOLEAN_ERR = "Error: Must input either Yes or No.";
	private static final String INPUT_INCOME_MSG = "Please enter your annual income: ";
	private static final String INPUT_RESIDENT_MSG = "Are you currently a resident? " + INPUT_BOOLEAN_CRITERIA;
	private static final String INPUT_LIVING_EXPENDITURE_MSG = "The amount (per week) used on living expenditures: ";
	private static final String INPUT_LIVING_EXPENDITURE_WARNING = "Warning: The amount given is more than your earnings. You will need to enter \n a new amount less than $%.2f, otherwise the program will be terminated.";
	private static final String INPUT_LIVING_EXPENDITURE_REENTER_MSG = "Would you like to enter a new living expenditure amount? " + INPUT_BOOLEAN_CRITERIA;
	private static final String INPUT_INVESTMENT_VALUE_MSG = "Please enter the investment amount per week: ";
	private static final String INPUT_INVESTMENT_VALUE_ERR = "Error: Your $%.2f investment exceeds your available funds of $%.2f.";
	private static final String INPUT_INVESTMENT_NOTE = "Please note: You currently have $%.2f invested in your other account.\n So therefore you cannot exceed $%.2f.";
	private static final String INPUT_INTEREST_RATE_MSG = "Please enter the interest rate percentage (between 1-100): ";
	private static final String INPUT_INTEREST_RATE_ERR = "Error: The interest rate must be between 1 and 100.";
	private static final String INPUT_INVESTMENT_LENGTH_MSG = "Please enter the investment length (# of weeks): ";
	private static final String ERROR_POSITIVE_AMOUNT_REQUIRED = "Error: The amount must be a positive number (and greater than zero).";
	private static final String ERROR_NOT_NUMERIC = "Error: Must only enter a numeric value.";
	private static final String ERROR_NOT_INTEGER = "Error: Must only enter a integer value.";
	
	// The following constants are used by the investment and exit menu.
	private static final String SEPARATOR = "---------------------";
	private static final String INVESTMENT_MENU_OPTION1 = "1: Edit account 1";
	private static final String INVESTMENT_MENU_OPTION2 = "2: Edit account 2";
	private static final String INVESTMENT_MENU_OPTION3 = "3: Display account 1";
	private static final String INVESTMENT_MENU_OPTION4 = "4: Display account 2";
	private static final String INVESTMENT_MENU_OPTION5 = "5: Exit";
	private static final String EXIT_MENU_OPTION1 = "1: Start the program again";
	private static final String EXIT_MENU_OPTION2 = "2: Calculate a new investment";
	private static final String EXIT_MENU_OPTION3 = "3: Exit";
	private static final String INPUT_MENU_OPTION = "Option #: ";
	private static final String INPUT_MENU_OPTION_ERR = "Error: Please enter a number corresponding to the options shown on the menu.";
	private static final String ACCOUNT_DOESNT_EXIST = "Account does not exist";
	private static final String TITLE_INVESTMENT_MENU = "Investment Menu";
	private static final String TITLE_EDIT_ACCOUNT1 = "Edit Account 1";
	private static final String TITLE_EDIT_ACCOUNT2 = "Edit Account 2";
	private static final String TITLE_DISPLAY_ACCOUNT1 = "Display Account 1";
	private static final String TITLE_DISPLAY_ACCOUNT2 = "Display Account 2";
	private static final String TITLE_EXIT_MENU = "Exit Menu";
	
	// Instance attributes.
	private Client client; // A reference to the Client object.
	private Scanner console;
	
	/**
	* The default constructor for the calculator interface. 
	* It will instantiate a new Client, and also a new Scanner object. Both are
	* assigned a reference to the instance variables.
	*/
	public CalculatorInterface() {
		this.client = new Client(); // Instantiates the Client.
		this.console = new Scanner(System.in); // Used for retrieving input from the user.
	}
	
	/**
	* The inputNumber method is used for retrieving a numeric value from the user. It is used by both the inputDouble, and also the 
	* inputInteger methods. It is designed generically to avoid the repeated logic that would have occurred if a separate method
	* was indeed created for retrieving an integer. The method receives both a message String, and also a boolean value indicating whether a
	* Double or an Integer is required. It will disallow anything else other than the desired numeric type to be entered, and reprompt the user to enter a 
	* valid type with a friendly error message.
	* @param message String
	* @param isDouble boolean
	* @return double - The received value. In the circumstance that it is an Integer, than the value will have a zero decimal, 
	*				   which can be cutoff by simply casting it to an int.
	*/
	private double inputNumber(String message, boolean isDouble) {
		double value = 0;
		do {
			try {
				System.out.println(SPACE); // creates a line space.
				System.out.print(message); // prompts the user with a message regarding the input desired.
				String input = this.console.next().trim(); // fetches a trimmed String from the user.
				// converts the String to either a Double or an Integer (depending on the isDouble boolean paramater received).
				value = ((isDouble)? Double.valueOf(input).doubleValue() : Integer.valueOf(input).intValue());
				// If the value is less than or equal to zero, then it will prompt a error message.
				if (value <= 0) {
					System.out.println(ERROR_POSITIVE_AMOUNT_REQUIRED);
				}
			// Catches any conversion exceptions, meaning that the user has entered something other than the seeked type. If a user tries to enter a double value
			// when the method is after an Integer, then a exception will be raised. The conversion ensures that either a double (being with or without the decimal) or a 
			// Integer value (without the decimal) is properly received.
			} catch (NumberFormatException ex) {
				// Displays an error message stating to either enter a numeric or an integer type. Since a double doesn't necessarily require a decimal
				// to be entered, it will be idenitifed to the user as just "numeric".
				System.out.println((isDouble)? ERROR_NOT_NUMERIC : ERROR_NOT_INTEGER);
			}
		// It will iterate until a value greater than zero is entered.
		} while (value <= 0);
		return value; // Returns the value, it is returned as a double but can be casted to an int. An integer will just have a zero decimal place.
	}
	
	/**
	* The inputDouble method is used throughout the cli in order to receive a double value from the user. 
	* It uses the inputNumber method in order to receive the double value. The method exists purely as a wrapper, 
	* and to eliminate the overhead of passing a boolean each time.
	* @param message String - The input message that you wish to be prompted to the user.
	* @return double
	*/
	private double inputDouble(String message) {
		return this.inputNumber(message, true);
	}
	
	/**
	* The inputInteger method is used throughout the cli in order to receive a integer value from the user.
	* It uses the inputNumber method in order to receive the integer value. Like the inputDouble method, it behaves as an
	* wrapper. It eliminates the need of passing the boolean, and also casting the retrieved double to an int.
	* @param message String - The input message that you wish to be prompted to the user.
	* @return int
	*/
	private int inputInteger(String message) {
		return (int)this.inputNumber(message, false);
	}
	
	/**
	* The inputBoolean method is method for retrieving an boolean value from the user. It prompts the received input message paramater, 
	* and only enables the user to input a Yes or No response. If a value other than Yes or No is received, it will display a friendly 
	* error message stating the required input, and re-prompt for input until a valid value is given. Once a valid value is received, it will
	* return a boolean indicating the response.
	* @param message String - The input message that you wish to be prompted to the user.
	* @return boolean - Either being true (Yes) or false (No).
	*/
	private boolean inputBoolean(String message) {
		String input;
		boolean value = false;
		do {
			System.out.println(SPACE);
			System.out.print(message);
			// Fetches a String input from the user.
			input = this.console.next();
			// Checks whether the received String is either equal to Yes or No (ignoring case).
			if (input.equalsIgnoreCase(INPUT_BOOLEAN_YES) || input.equalsIgnoreCase(INPUT_BOOLEAN_NO)) {
				// If the input received is valid, it will assign the boolean response from the equals test to the value variable.
				value = input.equalsIgnoreCase(INPUT_BOOLEAN_YES);
			} else {
				// Otherwise, it will show an friendly error message.
				System.out.println(INPUT_BOOLEAN_ERR);
			}
		// It will iterate until a valid input is given.
		} while (!(input.equalsIgnoreCase(INPUT_BOOLEAN_YES) || input.equalsIgnoreCase(INPUT_BOOLEAN_NO)));
		// Returns the answer as a boolean.
		return value;
	}
	
	/**
	* The inputString method is designed to be reusable, but is primarily intended to be used for retreiving the "full name" from the user.
	* It checks the input from the user against the received regular expression, and will display the received error message in the case that
	* the input doesn't match the criteria. It will iterate until a valid input has been given.
	* @param message String - The input message that you wish to be prompted to the user.
	* @param regex String - The regular expression will be used to validate the input.
	* @return String - The input that has been validated against the regular expression.
	*/
	private String inputString(String message, String regex, String error) {
		String input;
		do {
			System.out.println(SPACE);
			System.out.print(message);
			// Fetches the entire input line. 
			// It also trims the input to ensure there aren't any surrounding white spaces.
			input = this.console.nextLine().trim();
			// If the input doesn't match the received regular expression, then it will show the received error message.
			if (!input.matches(regex)) {
				System.out.println(error);
			}
		// Iterates until the input matches the regular expression.
		} while(!(input.matches(regex)));
		// Returns the validated input.
		return input;
	}
	
	/**
	* The convertToUpper method is designed to be reusable. 
	* It is used to capitalise each starting letter of each word in a String. Java (for some weird reason) doesn't contain 
	* a default method for doing such a thing, so therefore one had to be created in order to capitalise the received name. 
	* @param str String - The String that you desire to have the starting letter in each word to be capitalised.
	* @return String - The resulting capitalised String.
	*/
	private String convertToUpper(String str) {
		StringBuilder builder = new StringBuilder();
		// It first converts the String to lowercase, and is used in a
		// tokenizer to separate it into singular Strings based on spaces. 
		StringTokenizer tokens = new StringTokenizer(str.toLowerCase(), SPACE);
		// Iterates for each word.
		while(tokens.hasMoreTokens()) {
			String text = tokens.nextToken(); // Receives the next word.
			// Takes the starting letter of the word (using substring) and capitilises it using the toUpperCase method. It then
			// concatenates the remaining (lowercase) letters of the word by again using substring. The substring method is overloaded, and assumes
			// that in the case that a ending indice argument isn't received, it will go to last character.
			// Using the String builder, the word is then appended (also including a space).
			builder.append(text.substring(0, 1).toUpperCase() + text.substring(1) + SPACE);
		}
		// Returns a the capitalised String, and trims it (to take out the last space added from the previous iteration).
		return builder.toString().trim();
	}
	
	/**
	* The calcIncomeTax method uses the generic input methods for retreiving input from the user. It uses the setters on the client
	* object for storing the input to the instance fields, and uses some public methods to calculate the tax. It then displays the client in a println 
	* statement by using the overridden toString method (in which returns a String containing the formatted client data).
	*/
	private void calcIncomeTax() {
		// First, some constant values (relating for receiving an name) is passed into the inputString method. The given String is then passed into
		// the convertToUpper method, and the output is then assigned to the name using the setter method on the object.
		this.getClient().setName(this.convertToUpper(this.inputString(INPUT_NAME_MSG, INPUT_NAME_REGEX, INPUT_NAME_ERR)));
		// The inputDouble method is used to retreive a value for the gross salary. 
		this.getClient().setGrossSalary(this.inputDouble(INPUT_INCOME_MSG));
		// The inputBoolean method is used to determine whether the user is a resident or not.
		this.getClient().setResident(this.inputBoolean(INPUT_RESIDENT_MSG));
		this.getClient().calcTax(); // The tax is then calculated once all input has been received.
		this.getClient().calcMedicare(); // The medicare is also calculated.
		this.getClient().calcNetSalary(); // The net salary is then finally calculated once the tax is known.
		// The client object is then outputted in a println, by using the overridden toString.
		System.out.println(this.getClient()); 
	}
	
	/**
	* The livingExpenditure method uses the inputDouble for retreiving the weekly expenses from the user. In the circumstance that the value given is
	* greater than the weekly netsalary, then a warning message will be shown to the user. It will then prompt whether the user wants to reenter the value
	* by using the inputBoolean method. In the case that they wish to terminate the program, it will throw an empty Exception (that will be later caught) to 
	* end the program. It will iterate until the value given is less than the weekly expenses.
	* @throws Exception - An empty exception that will be later caught to end the program.
	*/
	private void livingExpenditure() throws Exception {
		do {
			System.out.println(SPACE);
			// It uses the inputDouble method for retrieving the weekly expenses.
			this.getClient().setWeeklyExpenses(this.inputDouble(INPUT_LIVING_EXPENDITURE_MSG));
			// If the expenses are greater than the weekly net salary
			if (this.getClient().getWeeklyExpenses() > this.getClient().getWeeklyNetSalary()) {
				// Shows a warning message.
				System.out.println(String.format(INPUT_LIVING_EXPENDITURE_WARNING, this.getClient().getWeeklyNetSalary()));
				// Asks whether the user wants to re-enter a new value. If not, then it will throw an empty exception 
				// that will later end the program.
				if (!this.inputBoolean(INPUT_LIVING_EXPENDITURE_REENTER_MSG)) {
					throw new Exception();
				}
			}
		// Iterates until the weekly expense value is less than the weekly net salary.
		} while(this.getClient().getWeeklyExpenses() > this.getClient().getWeeklyNetSalary());
	}
	
	/**
	* The investmentAmount method is used by the createAccount method. It prompts the user for the amount of money that they desire to 
	* invest (on a weekly basis) by using the inputDouble method. It then determines the amount of funds already invested in the other account
	* (if it exists), which is used to calculate the total investment. If the total investment is greater than the amount of weekly funds available
	* then it will show a error message stating that the desired investment has exceeded the available funds. If the other account exists, then it 
	* will also show a note mentioning the funds invested in the other account. It will iterate until a valid investment has been given.
	* @return double - The amount of money the client desires to invest.
	*/
	private double investmentAmount() {
		double investment = 0, totalInvestment = 0;
		do {
			// Fetches the desired investment from the user using the inputDouble method.
			investment = this.inputDouble(INPUT_INVESTMENT_VALUE_MSG);
			// Determines the amount of funds invested in the other account using a ternary statement. If the other account doesn't exist, the value will be zero.
			double otherInvestment = ((this.getClient().getAccount1() != null)? this.getClient().getAccount1().getAmount() : 
											((this.getClient().getAccount2() != null)? this.getClient().getAccount2().getAmount() : 0));
			// The total investment, in being the desired investment + the other investment.
			totalInvestment = investment + otherInvestment;
			// If the total investment is greater than the available funds (being the net salary - the living expenses).
			if (totalInvestment > this.getClient().getAvailableFunds()) {
				// It will then show a error message stating that the investment has exceeded the funds available.
				System.out.println(String.format(INPUT_INVESTMENT_VALUE_ERR, totalInvestment, this.getClient().getAvailableFunds()));
				// If the other investment exists, then it will show a note stating the funds invested in that account, and the remaining funds available.
				if (otherInvestment > 0) {
					System.out.println(String.format(INPUT_INVESTMENT_NOTE, otherInvestment, Math.abs(otherInvestment - this.getClient().getAvailableFunds())));
				}
			}
		// It will iterate until the total investment is less than the available funds.
		} while (totalInvestment > this.getClient().getAvailableFunds());
		// Returns the desired investment amount.
		return investment;
	}
	
	/**
	* The interestRate method is used by the createAccount method. It prompts the user for the interest rate percentage amount by using the
	* inputDouble method. If the input is outside of the percentage range, it will then show a friendly error message. It will iterate until
	* a value is received within the stated range.
	* @return double - The given interest rate.
	*/
	private double interestRate() {
		double interest = 0;
		do {
			interest = this.inputDouble(INPUT_INTEREST_RATE_MSG); 
			// If the value given is outside of the range 1 to 100, then it will display an error message
			// stating to only input a value within the said range.
			if (!(interest >= 1 && interest <= 100)) {
				System.out.println(INPUT_INTEREST_RATE_ERR);
			}
		// Iterates until the value given is within the correct range.
		} while(!(interest >= 1 && interest <= 100));
		return interest;
	}
	
	/**
	* The createAccount method is used to create a new Account object. It prompts the user for input using the relating methods, and assigns the 
	* data to the object using the setter methods.
	* @return Account - The newly created account object.
	*/
	private Account createAccount() {
		Account account = new Account(); // Instantiates a new Account.
		// Prompts the user for an investment amount using the investmentAmount method.
		// Assigns the amount to the account using the setter.
		account.setAmount(this.investmentAmount()); 
		// Prompts the user for an interest rate. Assigns the value to the account.
		account.setRate(this.interestRate());
		// Using the inputInteger method, it prompts the user to input a investment length.
		// Assigns the value to the account using the setter.
		account.setNumberOfWeeks(this.inputInteger(INPUT_INVESTMENT_LENGTH_MSG));
		return account; // Returns the newly created account.
	}
	
	/**
	* The displayAccount method receives an Account object and displays the output.
	* If the received account object is null, then it will show a message stating that
	* the account doesn't exist.
	* @param account Account - The account object desired to be displayed.
	*/
	private void displayAccount(Account account) {
		System.out.println(SPACE);
		if (account != null) {
			// Using the overridden toString method on the account object, it displays 
			// the returned String version of the object using a println statement. The toString method
			// returns an presentable text equivalent of the contents of the object's attributes. It could be either used
			// in this println for the CLI or within a JLabel in a GUI. It doesn't have to be used, but however
			// makes it easier for simple displaying purposes.
			System.out.println(account);
			System.out.println(SPACE);
			// The calcInvestment method is called, which returns a String containing the monthly investment outcomes for 
			// the entire duration of the investment length. It is displayed using a println.
			System.out.println(account.calcInvestment());
		} else {
			// Outputs a message to the user advising that the account doesn't exist.
			System.out.println(ACCOUNT_DOESNT_EXIST);
		}
	}
	
	/**
	* The restartInvestment method is used by the exitMenu method in order to restart the program from the livingExpenditure.
	* It will set null values to the accounts stored on the client, thus clearing the any existing data. In this method, it will
	* catch the Exception that could have been raised from the livingExpenditure method (which once caught means to exit the program).
	* @return boolean - It returns a boolean value indicating whether to continue executing, which if true will exit.
	*/
	private boolean restartInvestment() {
		try {
			// Prompts the user to enter their weekly expenses. Throws and exception to exit (see method for more clarification).
			this.livingExpenditure();
			// Clears the stored accounts by setting the reference to a null pointer, which will be cleared by the garbage collector.
			this.getClient().setAccount1(null);
			this.getClient().setAccount2(null);
			// Returns a boolean indicating whether the weekly expenses are equal to the weekly net salary. If it's equal, then there isn't any
			// money to invest, therefore returns a boolean true to end the program. Otherwise it will return false to continue.
			return (this.getClient().getWeeklyExpenses() == this.getClient().getWeeklyNetSalary());
		} catch(Exception ex) {};
		return true; // Will only be executed if an exception is thrown.
	}
	
	/**
	* The displayTitle method is used by both menus to display an title.
	* @param title String - The title that you wish to display.
	*/
	private void displayTitle(String title) {
		System.out.println(SEPARATOR);
		System.out.println(title);
		System.out.println(SEPARATOR);
	}
	
	/**
	* The exitMenu method is used by the mainMenu method to prompt further exit options for the user once they decide to end the program.
	* The method prompts a menu, and uses the inputInteger method in order to retrieve the desired option number. If the number entered is
	* greater than the amount of options available, then it will show an error message. It will iterate until a valid number is given.
	* @return boolean - It returns a boolean indicating whether to exit (true) or continue executing (false).
	*/
	private boolean exitMenu() {
		boolean exitFlag = true; // The exitFlag that will be eventually returned.
		int selection = 0; // The selection number entered.
		do {
			// Displays the Menu
			this.displayTitle(TITLE_EXIT_MENU);
			System.out.println(EXIT_MENU_OPTION1);
			System.out.println(EXIT_MENU_OPTION2);
			System.out.println(EXIT_MENU_OPTION3);
			// Uses the inputInteger method to retrieve the desired selection.
			selection = this.inputInteger(INPUT_MENU_OPTION);
			// If the selection is out of range, it shows an error message.
			if (selection > 3) {
				System.out.println(INPUT_MENU_OPTION_ERR);
			}
			// A switch statement based on the selection number.
			// If the selection is option 3: It will end the program.
			// It is not needed in the switch since the exitFlag is already true.
			switch (selection) {
				// If the selection is option 1: It will restart the program.
				case 1:
					// Sets the client attribute with a new instance.
					this.setClient(new Client());
					// An issue with the Scanner class. Since the calcIncomeTax method uses a nextLine call,
					// it must be first cleared of the spare carriage return sitting in the memory (after 
					// using other next method calls), in order to properly fetch another value.
					this.console.nextLine(); 
					// Calls the calcIncomeTax method.
					this.calcIncomeTax();
					// Calls the restartInvestment method, which will reprompt for the livingExpenditure, and
					// clears the stored accounts (even though its not required since its a new instance, but keep in mind
					// the method is reused again to just restart the investment). It then returns a boolean indicating to
					// exit or continue the program.
					exitFlag = this.restartInvestment();
					break;
				// If the selection is option 2: It will only restart the investment.
				case 2:
					// Calls the restartInvestment method, which will reprompt for the livingExpenditure, and
					// clears the stored accounts. It then returns a boolean indicating to exit or continue the program.
					exitFlag = this.restartInvestment();
					break;
			}
		// Iterates until a valid selection is entered.
		} while (selection > 3);
		return exitFlag; // Returns the exitFlag
	}
	
	/**
	* The mainMenu method is used by the run method and continues executing until the user has explicitly stated that they want to exit.
	* It is used to prompt the investment menu after the living expenditure has been given, and provides options for the user to edit (create)
	* the two accounts, display the two accounts, and also provides a exit facility that triggers the exitMenu method.
	* It displays the menu options to the user, and prompts for selection using the inputInteger method. If a selection has been given that is out
	* of range it will show an error message. It will iterate continuely until the exitFlag has a true value, in which is determined by the exitMenu.
	*/
	private void mainMenu() {
		// Iterates until the exitFlag has a true value.
		for (boolean exitFlag = false; (!exitFlag); ) {
			// Displays the menu options
			System.out.println(SPACE);
			this.displayTitle(TITLE_INVESTMENT_MENU);
			System.out.println(INVESTMENT_MENU_OPTION1);
			System.out.println(INVESTMENT_MENU_OPTION2);
			System.out.println(INVESTMENT_MENU_OPTION3);
			System.out.println(INVESTMENT_MENU_OPTION4);
			System.out.println(INVESTMENT_MENU_OPTION5);
			// Prompts for selection using the inputInteger method.
			int selection = this.inputInteger(INPUT_MENU_OPTION);
			// If the selection is out of range, it shows an error message.
			if (selection > 5) {
				System.out.println(INPUT_MENU_OPTION_ERR);
			}
			// Using a switch statement, it determines the action to take based on the selection.
			switch(selection) {
				// If the selection is option 1: It prompts to edit the contents of account1.
				case 1:
					this.displayTitle(TITLE_EDIT_ACCOUNT1);
					// Sets a null pointer reference to account1 using the setter.
					// This enables the ability to determine the other account in the investmentAccount method.
					this.getClient().setAccount1(null);
					// Prompts to create a new account using the createAccount method. Returns a new account object,
					// which is added to the client using the account1 setter.
					this.getClient().setAccount1(this.createAccount());
					break;
				// If the selection is option 2: It prompts to edit the contents of account2.
				case 2:
					this.displayTitle(TITLE_EDIT_ACCOUNT2);
					// Sets a null pointer to account2 using the setter.
					this.getClient().setAccount2(null);
					// Prompts to create a new account using the createAccount method. Returns a new account object,
					// which is added to the client using the account2 setter.
					this.getClient().setAccount2(this.createAccount());
					break;
				// If the selection is option 3: It then displays the contents of account1.	
				case 3:
					this.displayTitle(TITLE_DISPLAY_ACCOUNT1);
					// Retrieves the account1 object from the client, and passes it as an argument to the displayAccount method. 
					this.displayAccount(this.getClient().getAccount1());
					break;
				// If the selection is option 3: It then displays the contents of account2.	
				case 4:
					this.displayTitle(TITLE_DISPLAY_ACCOUNT2);
					// Retrieves the account2 object from the client, and passes it as an argument to the displayAccount method. 
					this.displayAccount(this.getClient().getAccount2());
					break;
				case 5:
					// Triggers the exitMenu. It returns a boolean indicating whether to exit or continue iterating the loop.
					exitFlag = this.exitMenu();
					break;
			}
		}
	}
	
	/**
	* The ability to modify the client outside of this class shouldn't be required.
	* Hence the reason it is private.
	*/
	private void setClient(Client client) {
		this.client = client;
	}
	
	/**
	* The ability to retrieve the client object outside of this class shouldn't be required.
	* Hence the reason it is private.
	*/
	private Client getClient() {
		return this.client;
	}
	
	/**
	* The run method is the starting and ending method for the program. It first calls the calcIncomeTax method, and then the livingExpenditure method. 
	* It catches the empty exception raised by the livingExpenditure method, which once caught will bypass the mainMenu and exit the program.
	* If the weekly expenses are not exactly equal to the weekly net salary, then it will call the mainMenu method which will continue iterating until exit.
	* The method is public, to enable the program to be instantiated and run() elsewhere.
	*/
	public void run() {
		try {
			// Calls the calcIncomeTax method.
			this.calcIncomeTax();
			// Calls the livingExpenditure method.
			this.livingExpenditure();
			// If the weekly expenses are not equal to the weekly net salary (therefore having money to invest)
			// it will call the mainMenu method.
			if (this.getClient().getWeeklyExpenses() != this.getClient().getWeeklyNetSalary()) {
				this.mainMenu(); 
			}
		} catch (Exception ex) {} // Catches the empty exception raised by the livingExpenditure method to end the program.
	}
	
	/**
	* The main method launches the program. It instantiates the CalculatorInterface class, and calls the run method on the object.
	*/
	public static void main(String[] args) {
		CalculatorInterface calc = new CalculatorInterface();
		calc.run(); // Starts the program.
	}
}