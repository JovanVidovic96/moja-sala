package mojasala.model.korisnik;

import mojasala.database.CsvSerializable;

public class Zakupac extends Korisnik implements CsvSerializable {
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
	
	public void dodajSredstva(double iznos) {
		if(iznos > 0) {
			sredstva += iznos;
		}
	}
}
