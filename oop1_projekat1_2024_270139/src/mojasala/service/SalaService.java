package mojasala.service;

import java.util.List;
import java.util.ArrayList;

import mojasala.model.sala.Sala;
import mojasala.repository.SalaRepository;

public class SalaService {
	private SalaRepository salaRepository;

	public SalaService() {
		super();
		this.salaRepository = new SalaRepository();
	}
	
	public boolean addSala(Sala novaSala) {
		for(Sala s : salaRepository.findAll()) {
			if(s.getNaziv().equalsIgnoreCase(novaSala.getNaziv())) {
				return false;
			}
		}
		salaRepository.save(novaSala);
		return true;	
	}
	
	public List<Sala>getAllSale(){
		return salaRepository.findAll();
	}
	
	public List<Sala> findByMinKapacitet(int minKapacitet){
		List<Sala> rezultat = new ArrayList<>();
		
		for(Sala s : salaRepository.findAll()) {
			if(s.getKapacitet()>= minKapacitet) {
				rezultat.add(s);
			}
		}
		return rezultat;
	}
	
	
	
	
	
}
