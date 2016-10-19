import java.nio.charset.Charset;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

/**
* This class handles writing to the Transaction Summary File whenever a logout
* is performed. It maintains a list of strings, one for each line of the file to
* write, and then writes them all at once to the file on logout.
*
* The name chosen for the file is based on the number of transaction summary
* files previously generated. This information is passed in by Main.java because
* individual instances of TSF are deleted after writing to file.
*/
class TSF {
	String fileName;
	ArrayList<String> info; // Contents to write to file

	public TSF (String fileName) {
		this.fileName = fileName;
		info = new ArrayList<String>();
	}

  /**
	* Records a create command in the correct format using the account number and
	* account name provided.
	*/
	public void logCreate(String accountNumber, String accountName) {
		info.add(String.format("CR %s 00000000 000 %s", accountNumber, accountName));
	}

	/**
	* Records a delete command in the correct format using the account number and
	* account name provided.
	*/
	public void logDelete(String accountNumber, String accountName) {
		info.add(String.format("DL %s 00000000 000 %s", accountNumber, accountName));
	}

	/**
	* Records a transfer command in the correct format using the account number of
	* the account to transfer from, followed by the account number of the account
	* to transfer to and the amount to transfer.
	*/
	public void logTransfer(String accountNumber1, String accountNumber2, String amount) {
		info.add(String.format("TR %s %s 000 %s", accountNumber1, accountNumber2, amount));
	}

	/**
	* Records a withdraw command in the correct format using the account number
	* and amount provided.
	*/
	public void logWithdraw(String accountNumber, String amount) {
		info.add(String.format("WD %s 00000000 %s ***", accountNumber, amount));
	}

	/**
	* Records a logout command in the correct format and then writes the TSF to
	* disk.
	*/
	public void logLogout() {
		info.add(String.format("ES  00000000 00000000 000 ***"));
		System.out.println(writeToFile());
	}

	/**
	* Records a deposit command in the correct format using the account number and
	* amount provided.
	*/
	public void logDeposit(String accountNumber, String amount) {
		return info.add(String.format("DE %s 00000000 %03d ***", accountNumber, Integer.parseInt(amount)));
	}

	/**
	* This function takes the filename given on creation of the TSF instance and
	* writes the file contents. The assumption is that the TSF instance will not
	* be used after this point and will be discarded.
	*/
	public void writeToFile() {
		Path file = Paths.get(fileName);

		try {
			Files.write(file, info, Charset.forName("UTF-8"));
		} catch (IOException e) {
			System.out.println("Error: " + e);
		}
	}
}
