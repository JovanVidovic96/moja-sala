package mojasala.model.usluga;

import mojasala.model.enums.UsluznoOsoblje;

public class Posluzivanje extends Usluga {
	
	private int brojOsoblja;
	private UsluznoOsoblje tip;
	

	public Posluzivanje(int brojOsoblja, UsluznoOsoblje tip) {
		super("Posluzivanje");
		this.brojOsoblja = brojOsoblja;
		this.tip = tip;
	}

	 public int getBrojOsoblja() {
	        return brojOsoblja;
	    }

	    public void setBrojOsoblja(int brojOsoblja) {
	        this.brojOsoblja = brojOsoblja;
	    }

	    public UsluznoOsoblje getTip() {
	        return tip;
	    }
	    
	    
	@Override
	public double getCena() {
		double cenaPoOsobi = switch (tip) {
		case KONOBAR -> 6000;
		case SERVIR -> 5000;
		case PERAC -> 5000;
		case CISTAC -> 3500;
		};
		return brojOsoblja * cenaPoOsobi;
	}
	
	@Override
	public String toCsv() {
		return "POSLUZIVANJE:" + tip.name() + "(" + brojOsoblja + ")";
	}
	
	public static Posluzivanje fromCsv(String s) {
		int o = s.indexOf(')');
		int z = s.indexOf('(');
		String tip = s.substring(0, o);
		int broj = Integer.parseInt(s.substring(o +1, z));
		return new Posluzivanje(broj, UsluznoOsoblje.valueOf(tip));
	}
	
}
