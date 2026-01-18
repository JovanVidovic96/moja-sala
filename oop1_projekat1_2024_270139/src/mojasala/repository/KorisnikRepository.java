package mojasala.repository;

import java.util.ArrayList;
import java.util.List;
import mojasala.model.korisnik.Korisnik;
import mojasala.model.korisnik.Zakupac;
import mojasala.model.korisnik.Vlasnik;

public class KorisnikRepository extends CsvRepository<Korisnik> {
	
	public KorisnikRepository() {
		super("data/korisnici.csv");
		 
	}

	@Override
	public List<Korisnik> findAll() {
		List<Korisnik> korisnici = new ArrayList<>();
		for(String line : readLines()) {
			if(line.startsWith("ZAKUPAC")) {
				korisnici.add(Zakupac.fromCsv(line));
			}else if(line.startsWith("VLASNIK")) {
				korisnici.add(Vlasnik.fromCsv(line));
			}
		}
		return korisnici;
	}
	public Korisnik findByUsername(String username) {
		for(Korisnik k : findAll()) {
			if(k.getUsername().equals(username)) {
				return k;
			}
		}
		return null;
	}
	
	public void save(Korisnik korisnik) {
		List<String> lines = readLines();
		lines.add(korisnik.toCsv());
		writeLines(lines);
	}
	
	
	public void update (Korisnik updated) {
		List<Korisnik> korisnici = findAll();
		List<String> lines = new ArrayList<>();
		
		for (Korisnik k : korisnici) {
			if(k.getUsername().equals(updated.getUsername())) {
				lines.add(updated.toCsv()); //zamenjuje staru verziju korisnika sa updateovanom
			} else {
				lines.add(k.toCsv());
			}
		}
		writeLines(lines);
	}
	
	
	
	

}
