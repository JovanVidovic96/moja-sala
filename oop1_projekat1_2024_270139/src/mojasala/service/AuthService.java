package mojasala.service;

import mojasala.model.korisnik.Korisnik;
import mojasala.model.korisnik.Vlasnik;
import mojasala.model.korisnik.Zakupac;
import mojasala.repository.KorisnikRepository;

/*
 * AuthService predstavlja servisni sloj zadužen za autentifikaciju
 * i registraciju korisnika u sistemu.
 * 
 * Service sloj:
 * - sadrži poslovnu logiku aplikacije,
 * - koristi repository sloj za pristup podacima,
 * - ne komunicira direktno sa CSV fajlovima niti sa korisničkim interfejsom.
 * 
 * AuthService:
 * - omogućava prijavu (login) korisnika,
 * - omogućava registraciju zakupaca i vlasnika,
 * - omogućava dodavanje sredstava zakupcu.
 * 
 * Komunikacija:
 * - prema dole: koristi KorisnikRepository za čitanje i upis podataka,
 * - prema gore: koriste ga meni klase (LoginMenu, ZakupacMenu, VlasnikMenu).
 */

public class AuthService {
	
	// Repository za rad sa korisnicima (CSV skladiste)
	private KorisnikRepository korisnikRepository;
	//Inicijalizacija service-a i njegovih zavisnosti
	public AuthService() {
		this.korisnikRepository=new KorisnikRepository();
	}
	
	// Prijava korisnika na osnovu korisnickog imena i lozinke
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
	
	//Metoda za registraciju novog zakupca
	public boolean registerZakupac(String username, String password, String telefon) {
		if(korisnikRepository.findByUsername(username) != null) {
			return false;
		}
		//kreiranje Zakupca sa pocetnim sredstvima 0
		Zakupac z = new Zakupac(username, password, telefon, 0);
		korisnikRepository.save(z);
		return true;
	}
	
	//Registracija vlasnika , koristi se samo jednom radi cistije Platforma
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
