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

	@Override
	public double getCena() {
		double cenaPoOsobi = switch (tip) {
		case KONOBAR -> 3000;
		case SERVIR -> 2500;
		case PERAC -> 2000;
		case CISTAC -> 1800;
		};
		return brojOsoblja * cenaPoOsobi;
	}
}
