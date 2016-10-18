import java.util.stream.Stream;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Files;

class ValidAccountsList {
	String fileName;
	ArrayList<String> accountsList;

	public ValidAccountsList (String fileName) {
		this.fileName = fileName;
		load();
	}

	public Integer search (String accountNumber) {
		return accountsList.indexOf(accountNumber);
	}

	// true if removed the element
	public Boolean remove (String accountNumber) {
		return accountsList.remove(accountNumber);
	}

	public Boolean load() {
		accountsList = new ArrayList<String>();
		// take filename and reload
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
      		stream.forEach(line -> accountsList.add(line));
		} catch(IOException|NumberFormatException e) {
			System.out.println("whoops: "+e);
			return false;
		}

		System.out.println(accountsList);
		return true;
	}

	public String toString () {
		return accountsList.toString();
	}
}