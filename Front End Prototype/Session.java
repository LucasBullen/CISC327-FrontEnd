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


	private void deposit(){
		// do all the stuff
		System.out.println("Enter number of account to deposit into.");
		inputString = scan.nextLine();

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
			case "deposit":
				deposit();
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