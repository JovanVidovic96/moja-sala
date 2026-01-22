package mojasala.model.korisnik;

import mojasala.database.CsvSerializable;

/*
 * Klasa Vlasnik predstavlja korisnika sistema koji ima ulogu vlasnika sala.
 * 
 * Vlasnik je tip korisnika koji nasleđuje apstraktnu klasu Korisnik i
 * implementira CsvSerializable kako bi se njegovi podaci mogli čuvati
 * i učitavati iz CSV fajla.
 */

public class Vlasnik extends Korisnik implements CsvSerializable {

	public Vlasnik(String username, String password) {
		super(username, password);
	}
	
	//serijalizacija objekta Vlasnik u CSV format u formatu koji smo odredili
	public String toCsv() {
		return "VLASNIK," + username + "," + password;
	}
	
	//Kreiranje objekta iz CSV zapisa, ocekivani format je VLASNIK, username, password
	public static Vlasnik fromCsv(String line) {
		String[] p = line.split(",");
		return new Vlasnik(
				p[1],
				p[2]);
	}
}
