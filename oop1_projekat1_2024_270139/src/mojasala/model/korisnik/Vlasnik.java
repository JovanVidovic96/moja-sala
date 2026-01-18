package mojasala.model.korisnik;

import mojasala.database.CsvSerializable;

public class Vlasnik extends Korisnik implements CsvSerializable {

	public Vlasnik(String username, String password) {
		super(username, password);
	}
	
	public String toCsv() {
		return "VLASNIK," + username + "," + password;
	}
	
	public static Vlasnik fromCsv(String line) {
		String[] p = line.split(",");
		return new Vlasnik(
				p[1],
				p[2]);
	}
}
