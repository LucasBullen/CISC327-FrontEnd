import java.util.stream.Stream;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

/**
* This class represents the contents of a valid accounts list file.
* The account numbers are stored in an ArrayList of Strings.
* The class allows for searching and removing of accounts in the list, but
* has no function for adding an account to the list because that feature is
* unnecessary.
*/
class ValidAccountsList {
	String fileName;
	ArrayList<String> accountsList;

 /**
	* Constructor.
	* @param	fileName the name of the file to load account numbers from. This
	*                  should include the file extension.
	*/
	public ValidAccountsList (String fileName) {
		this.fileName = fileName;
		load();
	}

 /**
	* Search function
	* @param  accountNumber	the account number to look for
	* @return               the index of the account number (-1 if not found)
	*/
	public Integer search (String accountNumber) {
		return accountsList.indexOf(accountNumber);
	}

	/**
	* Remove function
	* @param  accountNumber the account number to remove
	* @return               true if account number existed before removal
	*/
	public Boolean remove (String accountNumber) {
		return accountsList.remove(accountNumber);
	}

  /**
	* Reads the file contents into the list of valid accounts.
	* @return true if file was successfully read.
	*/
	public Boolean load() {
		accountsList = new ArrayList<String>();
		// take filename and reload
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
      		stream.forEach(line -> accountsList.add(line));
		} catch(IOException|NumberFormatException e) {
			System.out.println("Error: "+e);
			return false;
		}

		System.out.println(accountsList);
		return true;
	}

  /**
	* Override of toString function for printing to console.
	* @return the ArrayList of Strings containing account numbers
	*/
	public String toString () {
		return accountsList.toString();
	}
}
