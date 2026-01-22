package mojasala.model.usluga;

import mojasala.model.enums.VrstaHrane;

public class PripremaHrane extends Usluga {
	
	private VrstaHrane vrsta;
	
	public PripremaHrane(VrstaHrane vrsta) {
		super("Priprema hrane");
		this.vrsta = vrsta;
		
	}

	@Override
	public double getCena() {
		return switch(vrsta) {
		case PREDJELO -> 250;
		case CORBA -> 200;
		case GLAVNO_JELO -> 350;
		case DEZERT -> 500;
		};
	}
	
	public VrstaHrane getVrstaHrane() {
		return vrsta;
	}
	
	@Override
	public String toCsv() {
		return "HRANA:" +vrsta.name();
	}
	
	public static PripremaHrane fromCsv(String s) {
		return new PripremaHrane(VrstaHrane.valueOf(s));
	}
}
