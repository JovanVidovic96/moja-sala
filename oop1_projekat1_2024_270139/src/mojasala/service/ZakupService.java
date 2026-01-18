package mojasala.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import mojasala.model.sala.Sala;
import mojasala.model.zakup.Zakup;
import mojasala.repository.SalaRepository;
import mojasala.repository.ZakupRepository;

public class ZakupService {
	
	private ZakupRepository zakupRepository;
	private SalaRepository salaRepository;
	
	
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
	
	
}
