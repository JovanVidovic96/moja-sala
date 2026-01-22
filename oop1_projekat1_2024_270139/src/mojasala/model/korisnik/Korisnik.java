package mojasala.model.korisnik;

import mojasala.database.CsvSerializable;

/*
 * Apstraktna klasa Korisnik predstavlja osnovni model korisnika sistema.
 * 
 * Ova klasa sadrži zajednička polja i ponašanja za sve tipove korisnika
 * (npr. Zakupac, Vlasnik).
 * 
 * Klasa implementira CsvSerializable interfejs, što znači da svaki konkretan
 * korisnik mora omogućiti serijalizaciju u CSV format.
 */

public abstract class Korisnik implements CsvSerializable{
	protected String username;
	protected String password;
	
	public Korisnik(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
