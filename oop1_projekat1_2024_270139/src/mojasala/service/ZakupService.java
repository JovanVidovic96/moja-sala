package mojasala.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

 
import mojasala.model.korisnik.Zakupac;
import mojasala.model.sala.Sala;
import mojasala.model.zakup.Zakup;
import mojasala.repository.KorisnikRepository;
import mojasala.repository.SalaRepository;
import mojasala.repository.ZakupRepository;
import mojasala.model.usluga.Usluga;

public class ZakupService {
	
	private ZakupRepository zakupRepository;
	private SalaRepository salaRepository;
	private KorisnikRepository korisnikRepository;
	
	
	public ZakupService() {
		super();
		this.zakupRepository = new ZakupRepository();
		this.salaRepository = new SalaRepository();
	}
	
	private boolean isOverlapping(LocalDateTime start1,	LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
		 return start1.isBefore(end2) && start2.isBefore(end1);
	}
	
	public boolean isSalaAvailable(String nazivSale, LocalDateTime pocetak, LocalDateTime kraj) {
		
		for(Zakup z : zakupRepository.findAll()) {
			if(z.getNazivSale().equalsIgnoreCase(nazivSale)) {
				if(isOverlapping(z.getPocetakZakupa(),z.getKrajZakupa(),pocetak,kraj)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public List<Sala> findAvailableSale(LocalDateTime pocetak, LocalDateTime kraj){
		
		List<Sala> dostupneSale = new ArrayList<>();
		
		for(Sala s : salaRepository.findAll()) {
			if(isSalaAvailable(s.getNaziv(),pocetak,kraj)) {
				dostupneSale.add(s);
			}	
		}
		return dostupneSale;
	}
	
	public boolean createZakup(Zakup zakup) {
		
		if(!isSalaAvailable(zakup.getNazivSale(),zakup.getPocetakZakupa(),zakup.getKrajZakupa())) {
			return false;
		}
		zakupRepository.save(zakup);
		return true;
	}
	
	private long calculateDurationHours(LocalDateTime pocetak, LocalDateTime kraj) {
		return Duration.between(pocetak, kraj).toHours();
	}
	
	public double calculateCena(Sala sala, LocalDateTime pocetak, LocalDateTime kraj) {
		
		long sati = calculateDurationHours(pocetak,kraj);
		double osnovnaCena = 50;
		return sati * sala.getKapacitet() * osnovnaCena;
	}
	
	private double calculateUslugeCena(List<Usluga> usluge) {
		double suma = 0;
		
		for(Usluga u : usluge) {
			suma+= u.getCena();
		}
		return suma;
	}
	
	public boolean createAndPayZakup(Zakupac zakupac, Sala sala, LocalDateTime pocetak, LocalDateTime kraj, List<Usluga> usluge) {
		
		//provera termina
		if(!isSalaAvailable(sala.getNaziv(), pocetak, kraj)) {return false;}
		
		double osnovnaCena = calculateCena(sala, pocetak, kraj);
		double cenaUsluga = calculateUslugeCena(usluge);
		double ukupnaCena = osnovnaCena + cenaUsluga;
		
		
		//provera da li zakupac ima dovoljno sredstava
		if(zakupac.getSredstva() < ukupnaCena) {return false;}
		
		//skidanje sredstava sa racuna zakupca
		zakupac.setSredstva(zakupac.getSredstva() - ukupnaCena);
		
		//kreiranje zakupa
		Zakup zakup = new Zakup(
				sala.getNaziv(),
				zakupac.getUsername(),
				pocetak,
				kraj,
				ukupnaCena,
				usluge);
		//snimanje zakupa
		zakupRepository.save(zakup);
		// updateovanje korisnika		
		korisnikRepository.update(zakupac);
		
		return true;
	}
	
	 
}
