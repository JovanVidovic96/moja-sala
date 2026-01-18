package mojasala.model.zakup;

import java.time.LocalDateTime;

import mojasala.database.CsvSerializable;
import java.util.List;
import mojasala.model.usluga.Usluga;

public class Zakup implements CsvSerializable {
	
	private String nazivSale;
	private String usernameZakupca;
	private LocalDateTime pocetakZakupa;
	private LocalDateTime krajZakupa;
	private double cena;
	private List<Usluga> usluge;
	
	
	//konstruktor za CSV koji nema usluge
	public Zakup(String nazivSale, String usernameZakupca, LocalDateTime pocetakZakupa, LocalDateTime krajZakupa, double cena) {
		this.nazivSale = nazivSale;
		this.usernameZakupca = usernameZakupca;
		this.pocetakZakupa = pocetakZakupa;
		this.krajZakupa = krajZakupa;
		this.cena = cena;
		this.usluge = List.of(); // list.of dodeljuje polju praznu nepromenljivu listu
	}
	
	// konstruktor za runtime(sa uslugama)
	public Zakup(String nazivSale, String usernameZakupca, LocalDateTime pocetakZakupa, LocalDateTime krajZakupa,
			double cena, List<Usluga> usluge) {
		super();
		this.nazivSale = nazivSale;
		this.usernameZakupca = usernameZakupca;
		this.pocetakZakupa = pocetakZakupa;
		this.krajZakupa = krajZakupa;
		this.cena = cena;
		this.usluge = usluge;
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
	
	public List<Usluga> getUsluge(){
		return usluge;
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
