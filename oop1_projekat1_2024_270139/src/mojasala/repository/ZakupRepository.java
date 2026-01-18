package mojasala.repository;

import java.util.ArrayList;
import java.util.List;

import mojasala.model.zakup.Zakup;

public class ZakupRepository extends CsvRepository<Zakup>{
	
	public ZakupRepository() {
		super("data/zakupi.csv");
	}
	
	@Override
	public List<Zakup> findAll(){
		List<Zakup> zakupi = new ArrayList<>();
		
		for(String line : readLines()) {
			if(!line.isBlank()) {
				zakupi.add(Zakup.fromCsv(line));
			}
		}
		return zakupi;
	}
	
	public void save(Zakup zakup) {
		List<String> lines = readLines();
		lines.add(zakup.toCsv());
		writeLines(lines);
	}
}
