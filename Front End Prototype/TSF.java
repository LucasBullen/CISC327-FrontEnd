import java.nio.charset.Charset;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

class TSF {
	String fileName;
	ArrayList<String> info;

	public TSF (String fileName) {
		this.fileName = fileName;
		info = new ArrayList<String>();
	}

	public Boolean logCreate(String accountNumber, String accountName) {
		return info.add(String.format("CR %s 00000000 000 %s", accountNumber, accountName));
	}

	public Boolean logDelete(String accountNumber, String accountName) {
		return info.add(String.format("DL %s 00000000 000 %s", accountNumber, accountName));
	}

	public Boolean logTransfer(String accountNumber1, String accountNumber2, String accountName) {
		return info.add(String.format("TR %s %s 000 %s", accountNumber1, accountNumber2, accountName));
	}

	public Boolean logWithdraw(String accountNumber, String amount) {
		return info.add(String.format("WD %s 00000000 %s ***", accountNumber, amount));
	}

	public Boolean logLogout() {
		info.add(String.format("ES  00000000 00000000 000 ***"));
		System.out.println(writeToFile());
		return true;
	}

	public Boolean logDeposit(String accountNumber, String amount) {
		return info.add(String.format("DE %s 00000000 %03d ***", accountNumber, Integer.parseInt(amount)));
	}

	public Boolean writeToFile() {
		Path file = Paths.get(fileName);

		try {
			Files.write(file, info, Charset.forName("UTF-8"));
		} catch (IOException e) {
			System.out.println("Error: " + e);
			return false;
		}

		return true;
	}
}