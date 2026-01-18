package mojasala.repository;

import java.util.ArrayList;
import java.util.List;

import mojasala.model.sala.Sala;

public class SalaRepository extends CsvRepository<Sala> {

	public SalaRepository() {
		super("data/sale.csv");
	}
	
	@Override
	public List<Sala>findAll(){
		List<Sala> sale = new ArrayList<>();
		
		for(String line : readLines()) {
			if(!line.isBlank()) {
				sale.add(Sala.fromCsv(line));
			}
		}
		return sale;
	}
	
	public void save (Sala sala) {
		List<String> lines = readLines();
		lines.add(sala.toCsv());
		writeLines(lines);
	}
	
}
