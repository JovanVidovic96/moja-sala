package mojasala.model.usluga;

/*
 * Apstraktna klasa Usluga predstavlja osnovu za sve dodatne usluge
 * koje mogu biti uključene u zakup sale.
 *
 * Konkretne usluge (npr. muzika, priprema hrane, posluživanje)
 * nasleđuju ovu klasu i implementiraju način obračuna cene
 * kao i serijalizaciju u CSV format.
 */

public abstract class Usluga {

	protected String naziv;
	
	public Usluga(String naziv) {
		this.naziv = naziv;
	}
	
	public String getNaziv() {
		return naziv;
	}
	
	public abstract double getCena();
	
	public abstract String toCsv();
}

