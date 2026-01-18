package mojasala.service;

import mojasala.model.korisnik.Korisnik;
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
		
		Zakupac novi = new Zakupac(username, password, telefon, 0);
		korisnikRepository.save(novi);
		return true;
	}

}
