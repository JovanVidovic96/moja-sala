package mojasala.repository;

import java.util.ArrayList;
import java.util.List;

import mojasala.model.sala.Sala;

/*
 * SalaRepository je sloj za pristup podacima (repository) zadužen za rad sa
 * entitetom Sala.
 * 
 * Ova klasa:
 * - komunicira sa CSV fajlom (data/sale.csv),
 * - pretvara redove iz fajla u objekte klase Sala,
 * - omogućava čuvanje i čitanje sala iz trajne memorije.
 * 
 * Repository ne sadrži poslovnu logiku – njegova uloga je isključivo
 * čitanje i upis podataka.
 */

public class SalaRepository extends CsvRepository<Sala> {
	
	//konstruktor postavlja putanju CSVa fajla
	public SalaRepository() {
		super("data/sale.csv");
	}
	
	//ucitavanje svih sala iz CSVa
	@Override
	public List<Sala>findAll(){
		List<Sala> sale = new ArrayList<>();
		
		for(String line : readLines()) {
			if(!line.isBlank()) { //ovim if-om preskacemo prazne redove ako postoje 
				sale.add(Sala.fromCsv(line));
			}
		}
		return sale;
	}
	
	//dodavanje nove sale u CSV fajl
	public void save (Sala sala) {
		List<String> lines = readLines(); //Ucitavanje postojecih linija u csvu
		lines.add(sala.toCsv()); //dodavanje nove linije sa informacijama o dodatoj sali
		writeLines(lines);		//Upis nazac u CSV
	}
	
}
