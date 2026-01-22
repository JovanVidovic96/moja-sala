package mojasala.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


/*
 * CsvUtil je pomoćna (utility) klasa zadužena za rad sa CSV fajlovima.
 * 
 * Ova klasa omogućava:
 * - čitanje svih linija iz CSV fajla
 * - upis liste linija u CSV fajl
 * 
 * Klasa je statička po nameni (nema stanje), i koristi se unutar
 * CSV repozitorijuma za rad sa fajl sistemom.
 */

public class CsvUtil {
	public static List<String> read(String path){
		try {
			if(!Files.exists(Path.of(path))) {
				Files.createFile(Path.of(path));
				}
			return Files.readAllLines(Path.of(path));
		}catch (IOException e){
			return new ArrayList<>();
		}
		
	}
	
	public static void write(String path, List<String> lines) {
		try {
			Files.write(Path.of(path), lines);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}
