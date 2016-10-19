// login - start a Front End session (processing day)
// logout - end a Front End session
// create - create a new account (privileged transaction)
// delete - delete an existing account (privileged transaction)
// deposit - deposit to an account (ATM transaction)
// withdraw - withdraw from an account (ATM transaction)
// transfer - transfer between accounts (ATM transaction)
import java.util.stream.Stream;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

/**
* The Main class contains the outer run loop of the ATM program. It is not
* designed to stop. This class handles the login command, but all other commands
* including logout are handled by <code>Session.java</code>.
*
* After login, until logout is performed the run cycle is carried through
* a variety of functions inside <code>Session.java</code>.
*/
public class Main {

	/**
	* @param args[0] Name of valid accounts list (do not add file extension)
	* @param args[1] Name of transaction summary file (do not add extension)
	*/
	public static void main(String[] args) {
		String valName = args[0];
		String tsfFileName = args[1];
		Scanner scan = new Scanner(System.in);
		String inputString;
		// This is used to handle the case where we generate multiple TSF files.
		// The naming scheme is TSF.txt, TSF2.txt, TSF3.txt, ...
		Integer tsfVersion = 0;

		for (;;) {
			inputString = scan.nextLine();
			if (inputString.equals("login")) {
				System.out.println("Select mode: atm or agent.");
				inputString = scan.nextLine();
				if (inputString.equals("atm") || inputString.equals("agent")) {
				 	Session session = new Session(inputString, tsfFileName, tsfVersion, valName);
				 	session.route();
					tsfVersion += 1;
				} else {
					System.out.println("Unrecognized mode. Login cancelled.");
				}
			} else {
				System.out.println("Unrecognized command. Remember all commands must be in lowercase.");
			}
		}
	}
}
