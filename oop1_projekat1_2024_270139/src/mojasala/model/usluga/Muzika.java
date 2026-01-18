package mojasala.model.usluga;

public class Muzika extends Usluga {
	
	private boolean liveBand;
	
	public Muzika(boolean liveBand) {
		super("Muzika");
		this.liveBand = liveBand;
	}

	
	public double getCena() {
		return liveBand ? 30000 : 15000;
	}

}
