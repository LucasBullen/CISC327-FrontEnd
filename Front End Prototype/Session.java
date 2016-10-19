import java.util.Scanner;


/**
* This class contains the methods for performing logout,
* create, delete, deposit, withdraw, and transfer functions.
*
* Also contains the recursive calling that keeps a user logged
* in and able to perform multiple functions.
*/
class Session {
	private String inputString;
	Scanner scan = new Scanner(System.in);
	String user; //atm or agent
	ValidAccountsList accountsList;
	TSF tsf;

	/*
	* constructor
	*
	* @param user       atm or agent
	* @param tsfName    name of transaction summary file, not including extension
	* @param tsfVersion the current TSF that is being created
	* @param valName    name of verified accounts list file, not including extension
	*/
	public Session(String user, String tsfName, Integer tsfVersion, String valName){
		this.user = user;
		accountsList = new ValidAccountsList(valName + ".txt");
		if (tsfVersion.equals(0)) {
			tsf = new TSF(tsfName + ".txt");
		} else {
			tsf = new TSF(tsfName + tsfVersion + ".txt");
		}
		System.out.println("Logged into " + user + " mode.");
	}

	/*
	* function to confirm the validitiy of an account number
	* @param  inputString account number in question
	* @param  addText     additional text added to the end of error message
	* @return             true if the account number is correctly formatted
	*/
	public Boolean checkAccountNumber(String inputString, String addText) {
		if (inputString.length() != 8 || inputString.charAt(0) == '0'){
			System.out.print("Incorrect account number format (8 digits, no leading zeros). " + addText);
			return false;
		}
		return true;
	}

	/*
	* function to confirm the validitiy of an account name
	* @param  inputString account name in question
	* @param  addText     additional text added to the end of error message
	* @return             true if the account name is correctly formatted
	*/
	public Boolean checkAccountName(String inputString, String addText) {
		if (inputString.length() < 3 || inputString.length() > 30 || inputString.charAt(0) == ' ' || inputString.matches("^.*[^a-zA-Z0-9 ].*$")){
			System.out.print("Incorrect account name format. " + addText);
			return false;
		}
		return true;
	}

	/*
	* function to confirm the validitiy of an amount of money
	* @param  inputString ammount in question
	* @param  addText     additional text added to the end of error message
	* @return             true if the amount is correctly formatted
	*/
	public Boolean checkAmount(String inputString, String addText) {
		if (inputString.length() < 3 || inputString.length() > 8 || inputString.matches("^.*[^0-9 ].*$")){
			System.out.print("Incorrect amount format. " + addText);
			return false;
		}
		return true;
	}

	/*
	* Ends a session and creates the TSF
	*/
	private void logout () {
		tsf.logLogout();
		System.out.println("Logged out.");
	}

	/*
	* Creates a new account.
	* Checks to make sure the account does not already exist and that the
	* user is in agent mode.
	*
	* Does not update Valid Accounts List because no further operations are
	* valid on this account until the BackEnd has handled the create.
	*/
	private void create () {
		// do all the stuff
		String failedText = "Create cancelled.";
		String accountNumber = "";
		String accountName = "";

		System.out.println("Enter account number to create.");
		inputString = scan.nextLine();

		if (!checkAccountNumber(inputString, failedText)){
			route();
		} else if (accountsList.search(inputString) != -1){
			System.out.println("Account already exists. Create cancelled.");
			route();
		} else if (user.equals("agent")){
			System.out.println("Cannot access create feature in atm mode.");
			route();
		}

		accountNumber = inputString;

		System.out.println("Enter name of new account.");
		inputString = scan.nextLine();

		if (!checkAccountName(inputString, failedText)) {
			route();
		}

		accountName = inputString;
		System.out.println("Account successfully created. Will update after synchronizing with Back Office.");
		// add to TSF file
		tsf.logCreate(accountNumber, accountName);

		route();
	}

  /**
	* Function to make an account delete request.
	*
	* Does not confirm that the account name and number match because it is
	* handled by the BackEnd.
	*
	* The number is removed from the Valid Accounts List because no further
	* transactions should be performed on this account after this point.
	*
	* If the account number and name do not match, users will still not be able
	* to perform actions on the account until logging in again and receiving a
	* new Valid Accounts List.
	*/
	private void delete(){
		String failedText = "Delete cancelled.";
		String accountNumber = "";
		String accountName = "";

		if (user.equals("agent")){
			System.out.println("Cannot access create feature in atm mode.");
			route();
		}

		System.out.println("Enter account number to delete.");
		inputString = scan.nextLine();
		accountNumber = inputString;
		System.out.println("Enter name of account to delete.");
		inputString = scan.nextLine();
		accountName = inputString;
		accountsList.remove(accountNumber);
		System.out.println("Account successfully deleted.");

		// add to TSF file
		tsf.logDelete(accountNumber, accountName);

		route();
	}

  /**
	* Function to make a deposit request.
  *
	* Checks that the account number exists and that the amount to deposit is a
	* valid amount.
	*/
	private void deposit(){
		String failedText = "Deposit cancelled.";
		String accountNumber = "";
		String withdrawAmount = "";

		System.out.println("Enter account number to deposit to.");
		inputString = scan.nextLine();

		if (!checkAccountNumber(inputString, failedText)){
			route();
		}

		accountNumber = inputString;
		System.out.println("Enter amount to deposit.");
		inputString = scan.nextLine();

		if (!checkAmount(inputString, failedText)){
			route();
		}

		depositAmount = inputString;
		System.out.println("Deposit successful.");
		// add to TSF file
		tsf.logDeposit(accountNumber, depositAmount);

		route();
	}

  /**
	* Function to make a withdraw request.
	* Checks for a valid account number and valid amount to withdraw.
	*/
	private void withdraw(){
		String failedText = "Withdraw cancelled.";
		String accountNumber = "";
		String withdrawAmount = "";

		System.out.println("Enter account number to withdraw from.");
		inputString = scan.nextLine();

		if (!checkAccountNumber(inputString, failedText)){
			route();
		}

		accountNumber = inputString;
		System.out.println("Enter amount to withdraw.");
		inputString = scan.nextLine();

		if (!checkAmount(inputString, failedText)){
			route();
		}

		withdrawAmount = inputString;
		System.out.println("Withdraw successful.");
		// add to TSF file
		tsf.logWithdraw(accountNumber, withdrawAmount);

		route();
	}

  /**
	* Function to request transfer of money between two accounts.
	* Checks each account name and the amount for validity.
	*/
	private void transfer(){
		String failedText = "Transfer cancelled.";
		String accountNumberOne = "";
		String accountNumberTwo = "";
		String transferAmount = "";

		System.out.println("Enter first account number.");
		inputString = scan.nextLine();

		if (!checkAccountNumber(inputString, failedText)){
			route();
		}
		accountNumberOne = inputString;
		System.out.println("Enter second account number.");
		inputString = scan.nextLine();

		if (!checkAccountNumber(inputString, failedText)){
			route();
		}

		accountNumberTwo = inputString;
		System.out.println("Enter amount to transfer.");
		inputString = scan.nextLine();

		if (!checkAmount(inputString, failedText)){
			route();
		}

		transferAmount = inputString;

		System.out.println("Transfer successful.");

		// add to TSF file
		tsf.logTransfer(accountNumberOne, accountNumberTwo, transferAmount);

		route();
	}

  /**
	* This function handles the flow of the program. Any valid command will call
	* this function on completion in order to keep the program running, with the
	* exception of the logout command which will return to the loop inside of
	* Main.java and await a login command.
	*/
	public void route(){
		String inputString = scan.nextLine();
		switch(inputString){
			case "logout":
				logout();
				break;
			case "create":
				create();
				break;
			case "delete":
				delete();
				break;
			case "deposit":
				deposit();
				break;
			case "withdraw":
				withdraw();
				break;
			case "transfer":
				transfer();
				break;
			case "q":
				return;
			default:
				System.out.println("Unrecognized command. Remember all commands must be in lowercase.");
				route();
				break;
		}
	}
}
