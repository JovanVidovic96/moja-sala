package mojasala.model.sala;

import mojasala.database.CsvSerializable;

public class Sala implements Stanje, CsvSerializable {
	private String naziv;
	private double povrsina;
	private int kapacitet;
	private boolean klimatizovana;
	private boolean rasveta;
	
	
	
	public Sala(String naziv, double povrsina, int kapacitet, boolean klimatizovana, boolean rasveta) {
		super();
		this.naziv = naziv;
		this.povrsina = povrsina;
		this.kapacitet = kapacitet;
		this.klimatizovana = klimatizovana;
		this.rasveta = rasveta;
	}

	@Override
	public void proveriStanje() {
		System.out.println(
				"Sala " + naziv + " sa ukupno " + kapacitet + " mesta "
				+ (klimatizovana ? "je" : "nije") + " klimatizovana i " +
						(rasveta ? "ima" : "nema") + " rasvetu");

	}
	
	@Override
	public String toCsv() {
		return naziv + "," +
		           povrsina + "," +
		           kapacitet + "," +
		           klimatizovana + "," +
		           rasveta;
	}
	
	public static Sala fromCsv(String line) {
		String[] p = line.split(",");
		
		return new Sala(
				p[0],
				Double.parseDouble(p[1]),
				Integer.parseInt(p[2]),
				Boolean.parseBoolean(p[3]),
				Boolean.parseBoolean(p[4])
				);
				
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public double getPovrsina() {
		return povrsina;
	}

	public void setPovrsina(double povrsina) {
		this.povrsina = povrsina;
	}

	public int getKapacitet() {
		return kapacitet;
	}

	public void setKapacitet(int kapacitet) {
		this.kapacitet = kapacitet;
	}

	public boolean isKlimatizovana() {
		return klimatizovana;
	}

	public void setKlimatizovana(boolean klimatizovana) {
		this.klimatizovana = klimatizovana;
	}

	public boolean isRasveta() {
		return rasveta;
	}

	public void setRasveta(boolean rasveta) {
		this.rasveta = rasveta;
	}
	
	@Override
	public String toString() {
	    return "Sala: " + naziv +
	           ", kapacitet: " + kapacitet +
	           ", klimatizacija: " + (klimatizovana ? "da" : "ne") +
	           ", rasveta: " + (rasveta ? "da" : "ne");
	}

}
