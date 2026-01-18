package mojasala.service;

import mojasala.model.korisnik.Korisnik;
import mojasala.model.korisnik.Vlasnik;
import mojasala.model.korisnik.Zakupac;
import mojasala.repository.KorisnikRepository;



public class AuthService {
	
	private KorisnikRepository korisnikRepository;
	
	public AuthService() {
		this.korisnikRepository=new KorisnikRepository();
	}
	
	public Korisnik login(String username, String password) {
		Korisnik korisnik = korisnikRepository.findByUsername(username);
		
		if(korisnik == null) {
			return null;
		}
		
		if(!korisnik.getPassword().equals(password)) {
			return null;
		}
		return korisnik;
	}
	
	public boolean registerZakupac(String username, String password, String telefon) {
		if(korisnikRepository.findByUsername(username) != null) {
			return false;
		}
		
		Zakupac z = new Zakupac(username, password, telefon, 0);
		korisnikRepository.save(z);
		return true;
	}
	
	public boolean registerVlasnik(String username, String password) {
		
		if (korisnikRepository.findByUsername(username) != null) {return false;}
		
		Vlasnik v = new Vlasnik (username, password);
		korisnikRepository.save(v);
		return true;
		
	}
	
	public void addFunds(Zakupac zakupac, double iznos) {
		if (iznos <= 0) {return;}
		
		zakupac.setSredstva(zakupac.getSredstva() + iznos);
		korisnikRepository.update(zakupac);
	}

}
