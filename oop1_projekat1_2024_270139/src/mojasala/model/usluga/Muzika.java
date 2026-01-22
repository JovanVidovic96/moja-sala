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
	
	public boolean isLiveBand() {
		return liveBand;
	}
	
	@Override
	public String toCsv() {
		return "MUZIKA:" +(liveBand ? "LIVE" : "Playlist-a");
	}
	
	public static Muzika fromCsv(String s) {
		return new Muzika(s.equals("LIVE"));
	}
}
