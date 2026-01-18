package mojasala.model.zakup;

import java.time.LocalDateTime;

import mojasala.database.CsvSerializable;

public class Zakup implements CsvSerializable {
	
	private String nazivSale;
	private String usernameZakupca;
	private LocalDateTime pocetakZakupa;
	private LocalDateTime krajZakupa;
	private double cena;
	
	
	public Zakup(String nazivSale, String usernameZakupca, LocalDateTime pocetakZakupa, LocalDateTime krajZakupa,
			double cena) {
		super();
		this.nazivSale = nazivSale;
		this.usernameZakupca = usernameZakupca;
		this.pocetakZakupa = pocetakZakupa;
		this.krajZakupa = krajZakupa;
		this.cena = cena;
	}

	public String getNazivSale() {
		return nazivSale;
	}
	
	public String getUsernameZakupca() {
		return usernameZakupca;
	}


	public LocalDateTime getPocetakZakupa() {
		return pocetakZakupa;
	}


	public LocalDateTime getKrajZakupa() {
		return krajZakupa;
	}


	public double getCena() {
		return cena;
	}
	
	
	@Override
	public String toCsv() {
		return nazivSale + "," +
				   usernameZakupca + "," +
		           pocetakZakupa + "," +
		           krajZakupa + "," +
		           cena;
	}
	
	public static Zakup fromCsv(String line) {
		String[] p = line.split(",");
		return new Zakup(
				p[0],
				p[1],
				LocalDateTime.parse(p[2]),
				LocalDateTime.parse(p[3]),
				Double.parseDouble(p[4])
				);
	}
	
}
