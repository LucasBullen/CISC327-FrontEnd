// login - start a Front End session (processing day)
// logout - end a Front End session
// create - create a new account (privileged transaction)
// delete - delete an existing account (privileged transaction) gl
// deposit - deposit to an account (ATM transaction) gd
// withdraw - withdraw from an account (ATM transaction) dl
// transfer - transfer between accounts (ATM transaction) lg
// document gd
// comments dl
// cleanup lg

import java.util.stream.Stream;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;


public class Main {
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		String inputString;
		Integer tsfVersion = 0;

		for (;;) {
			inputString = scan.nextLine();
			if (inputString.equals("login")) {
				System.out.println("Select mode: atm or agent.");
				inputString = scan.nextLine();
				if (inputString.equals("atm") || inputString.equals("agent")) {
				 	Session session = new Session(inputString, tsfVersion);
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
