import java.util.Scanner;

class Session {
	private String inputString;
	Scanner scan = new Scanner(System.in);
	String user; //atm or agent
	ValidAccountsList accountsList;
	TSF tsf;

	public Session(String user, Integer tsfVersion){
		this.user = user;
		accountsList = new ValidAccountsList("accounts.txt");
		if (tsfVersion.equals(0)) {
			tsf = new TSF("TSF.txt");
		} else {
			tsf = new TSF("TSF" + tsfVersion + ".txt");
		}
		System.out.println("Logged into " + user + " mode.");
	}

	private void logout () {
		tsf.logLogout();
		System.out.println("Logged out.");
	}

	private void create () {
		// do all the stuff
		String accountNumber = "";
		String accountName = "";

		System.out.println("Enter account number to create.");
		inputString = scan.nextLine();

		if (inputString.length() != 8 || inputString.charAt(0) == '0'){
			System.out.println("Incorrect account number format (8 digits, no leading zeros). Create cancelled.");
		} else if (accountsList.search(inputString) != -1){
			System.out.println("Account already exists. Create cancelled.");
		} else if (user.equals("agent")){
			System.out.println("Cannot access create feature in atm mode.");
		} else {
			accountNumber = inputString;
			System.out.println("Enter name of new account.");
			inputString = scan.nextLine();

			if (inputString.length() < 3 || inputString.length() > 30 || inputString.charAt(0) == ' '
				|| inputString.charAt(inputString.length() - 1) == ' ') {
				System.out.println("Incorrect account name format (3 to 30 characters, no leading or trailing spaces). Create cancelled.");
			}

			accountName = inputString;
		}

		System.out.println("Account successfully created. Will update after synchronizing with Back Office.");

		// add to TSF file
		tsf.logCreate(accountNumber, accountName);
		route();
	}

	private void delete(){
		String accountNumber = "";
		String accountName = "";

		System.out.println("Enter account number to delete.");
		inputString = scan.nextLine();

		if (user.equals("agent")){
			System.out.println("Cannot access create feature in atm mode.");
		} else {
			accountNumber = inputString;
			System.out.println("Enter name of account to delete.");
			inputString = scan.nextLine();
			accountName = inputString;
		}
		accountsList.remove(accountNumber);
		System.out.println("Account successfully deleted.");

		// add to TSF file
		tsf.logDelete(accountNumber, accountName);
		route();
	}

	private void deposit(){
		// do all the stuff
		System.out.println("Enter number of account to deposit into.");
		inputString = scan.nextLine();

		route();
	}

	private void withdraw(){
		String accountNumber = "";
		String withdrawAmount = "";

		System.out.println("Enter account number to delete.");
		inputString = scan.nextLine();

		if (inputString.length() != 8 || inputString.charAt(0) == '0'){
			System.out.println("Incorrect account number format (8 digits, no leading zeros). Withdraw cancelled.");
		} else {
			accountNumber = inputString;
			System.out.println("Enter amount to withdraw.");
			inputString = scan.nextLine();
			withdrawAmount = inputString;
		}
		System.out.println("Withdraw successful.");

		// add to TSF file
		tsf.logWithdraw(accountNumber, withdrawAmount);
		route();
	}

	private void transfer(){
		String accountNumberOne = "";
		String accountNumberTwo = "";
		String transferAmount = "";

		System.out.println("Enter first account number.");
		inputString = scan.nextLine();

		if (inputString.length() != 8 || inputString.charAt(0) == '0'){
			System.out.println("Incorrect account number format (8 digits, no leading zeros). Withdraw cancelled.");
		} else {
			accountNumberOne = inputString;
			System.out.println("Enter second account number.");
			inputString = scan.nextLine();

			if (inputString.length() != 8 || inputString.charAt(0) == '0'){
				System.out.println("Incorrect account number format (8 digits, no leading zeros). Withdraw cancelled.");
			} else {
				accountNumberTwo = inputString;
				System.out.println("Enter amount to transfer.");
				inputString = scan.nextLine();
				transferAmount = inputString;
			}
		}
		System.out.println("Transfer successful.");

		// add to TSF file
		tsf.logWithdraw(accountNumberOne, accountNumberTwo, transferAmount);
		route();
	}

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