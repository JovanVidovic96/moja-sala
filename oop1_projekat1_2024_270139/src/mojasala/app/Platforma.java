package mojasala.app;

import java.time.LocalDateTime;

import mojasala.model.korisnik.Korisnik;
import mojasala.model.korisnik.Zakupac;
import mojasala.model.sala.Sala;
import mojasala.model.zakup.Zakup;
import mojasala.repository.KorisnikRepository;
import mojasala.repository.SalaRepository;
import mojasala.repository.ZakupRepository;
import mojasala.service.AuthService;
import mojasala.service.SalaService;
import mojasala.service.ZakupService;

public class Platforma {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		
		 
		
		ZakupService zakupService = new ZakupService();

		LocalDateTime od = LocalDateTime.of(2026, 1, 20, 18, 0);
		LocalDateTime doo = LocalDateTime.of(2026, 1, 20, 23, 0);

		System.out.println(
		    zakupService.findAvailableSale(od, doo)
		        .stream()
		        .map(Sala::getNaziv)
		        .toList()
		);
	}

}
