package mojasala.repository;

import java.util.ArrayList;
import java.util.List;
import mojasala.model.korisnik.Korisnik;
import mojasala.model.korisnik.Zakupac;
import mojasala.model.korisnik.Vlasnik;

/*
 * KorisnikRepository je konkretna implementacija CsvRepository klase
 * zadužena za rad sa korisnicima sistema.
 * 
 * Ovaj repozitorijum:
 * - čita i upisuje korisnike u CSV fajl "data/korisnici.csv"
 * - razlikuje tipove korisnika (Zakupac i Vlasnik)
 * - omogućava pretragu korisnika po username-u
 * - omogućava dodavanje i ažuriranje korisnika
 * 
 * Komunicira sa:
 * - model slojem (Korisnik, Zakupac, Vlasnik)
 * - CsvRepository i CsvUtil klasama za rad sa fajlovima
 */

public class KorisnikRepository extends CsvRepository<Korisnik> {
	
	//Konstruktor postavlja putanju do CSV fajla sa korisnicima
	public KorisnikRepository() {
		super("data/korisnici.csv");
		 
	}
	/*Ucitava sve korisnike iz CSV fajla
	 * Na osnovu prefiksa linije :
	 * "ZAKUPAC" -> kreira objekat Zakupac
	 * "VLASNIK" -> kreira objekat Vlasnik
	 * */
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
	
	//Metoda koja pronalazi korisnika po username-u, ako ne postoji vracamo null
	public Korisnik findByUsername(String username) {
		for(Korisnik k : findAll()) {
			if(k.getUsername().equals(username)) {
				return k;
			}
		}
		return null;
	}
	
	//Metoda koja cuva novog korisnika u CSV fajl, dodaje ga na kraj fajla
	public void save(Korisnik korisnik) {
		List<String> lines = readLines();
		lines.add(korisnik.toCsv());
		writeLines(lines);
	}
	
	//Metoda koja azurira postojeceg korisnika tako sto ako pronadje korisnika sa istim
	//usernameom, stari zapis zamenjuje novim (updated), dok ostali korisnici ostaju nepromenjeni
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
