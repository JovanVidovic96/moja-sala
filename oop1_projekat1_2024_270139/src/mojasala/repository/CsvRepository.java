package mojasala.repository;

import java.util.List;
import mojasala.database.CsvUtil;


/*
 * Apstraktna klasa CsvRepository predstavlja osnovu za sve repozitorijume u projektu.
 * 
 * Repository sloj služi kao posrednik između:
 * - poslovne logike (Service sloj)
 * - i trajnog skladištenja podataka (CSV fajlovi).
 * 
 * CsvRepository:
 * - zna putanju do CSV fajla
 * - omogućava čitanje i upis redova u fajl
 * - ne zna konkretan tip objekta (generički tip T)
 * 
 * Konkretne repozitorijumske klase (npr. SalaRepository, ZakupRepository, KorisnikRepository)
 * nasleđuju ovu klasu i implementiraju metodu findAll(), gde se vrši:
 * - parsiranje CSV zapisa
 * - kreiranje konkretnih objekata modela
 */

public abstract class CsvRepository <T>{
	protected String filePath;

	public CsvRepository(String filePath) {
		super();
		this.filePath = filePath;
	}
	
 
	//Citanje svih linija iz CSV-a, koristim CsvUtil kao pomocnu klasu za rad sa fajlovima
	protected List<String> readLines(){
		return CsvUtil.read(filePath);
	}
	
	public void writeLines(List<String> lines) {
		CsvUtil.write(filePath, lines);
	}
	
	//Apstraktna metoda koju implementiraju konkretni repozitorijumi i vraca listu objekata ucitanih iz CSV-a
	public abstract List<T> findAll();
}
