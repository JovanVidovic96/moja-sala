package mojasala.model.usluga;

public abstract class Usluga {

	protected String naziv;
	
	public Usluga(String naziv) {
		this.naziv = naziv;
	}
	
	public String getNaziv() {
		return naziv;
	}
	
	public abstract double getCena();
}
