package mojasala.model.korisnik;

import mojasala.database.CsvSerializable;



/*
 * Klasa Zakupac predstavlja korisnika sistema koji iznajmljuje sale.
 * 
 * Zakupac nasleđuje apstraktnu klasu Korisnik i poseduje dodatne podatke
 * kao što su broj telefona i raspoloživa sredstva.
 * Implementira CsvSerializable kako bi se njegovi podaci mogli trajno
 * čuvati u CSV fajlu.
 */



public class Zakupac extends Korisnik implements CsvSerializable {
	//telefon je String tip zbog mogucnosti da broj pocne sa 0 ili +381
	private String telefon;
	private double sredstva;
	
	
	public Zakupac(String username, String password, String telefon, double sredstva) {
		super(username, password);
		this.telefon = telefon;
		this.sredstva = sredstva;
	}
	
	@Override
	public String toCsv() {
		return "ZAKUPAC," + username + "," + password + "," +
	               telefon + "," + sredstva;
	}
	
	public static Zakupac fromCsv(String line) {
		String[] p = line.split(",");
		return new Zakupac(
				p[1],
				p[2],
				p[3],
				Double.parseDouble(p[4])
						);
				
	}

	public double getSredstva() {
		return sredstva;
	}
	
	
	public void setSredstva(double sredstva) {
		this.sredstva = sredstva;
	}

	public void dodajSredstva(double iznos) {
		if(iznos > 0) {
			sredstva += iznos;
		}
	}
}
