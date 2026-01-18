package mojasala.app.menu;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import mojasala.model.enums.UsluznoOsoblje;
import mojasala.model.enums.VrstaHrane;
import mojasala.model.korisnik.Zakupac;
import mojasala.model.sala.Sala;
import mojasala.model.usluga.Muzika;
import mojasala.model.usluga.Posluzivanje;
import mojasala.model.usluga.PripremaHrane;
import mojasala.model.usluga.Usluga;
import mojasala.service.AuthService;
import mojasala.service.SalaService;
import mojasala.service.ZakupService;


public class ZakupacMenu {
	
	private static Scanner sc = new Scanner(System.in);
	private static SalaService salaService = new SalaService();
	private static ZakupService zakupService = new ZakupService();
	private static AuthService authService = new AuthService();

	public static void show(Zakupac z) {
		while(true) {
			System.out.println("\n--- Zakupac ---");
			System.out.println("1. Prikaz slobodnih sala");
			System.out.println("2. Napravi zakup");
			System.out.println("3. Dodaj sredstva");
			System.out.println("0. Logout");
			
			int izbor = Integer.parseInt(sc.nextLine());
			
			switch (izbor) {
			case 1 -> prikaziSlobodneSale();
			case 2 -> napraviZakup(z); // treba proslediti argument z kada definisemo metodu
			case 3 -> dodajSredstva(z);
			case 0 -> {return;}
			}
		}
	}
	
	private static void prikaziSlobodneSale() {
		System.out.println("Unesite pocetak (format: yyyy-MM-ddTHH:mm) ");
		LocalDateTime pocetak = LocalDateTime.parse(sc.nextLine());
		System.out.println("Unesite kraj (format: yyyy-MM-ddTHH:mm) ");
		LocalDateTime kraj = LocalDateTime.parse(sc.nextLine());
		
		zakupService.findAvailableSale(pocetak, kraj).forEach(s -> System.out.println(s.getNaziv()));
	}


	private static void napraviZakup(Zakupac z) {

	    System.out.println("Unesite naziv sale:");
	    String nazivSale = sc.nextLine();

	    // Pretraga sale
	    Sala sala = null;
	    for (Sala s : salaService.getAllSale()) {
	        if (s.getNaziv().equalsIgnoreCase(nazivSale)) {
	            sala = s;
	            break;
	        }
	    }

	    if (sala == null) {
	        System.out.println("Sala ne postoji.");
	        return;
	    }

	    System.out.println("Unesite pocetak (format: yyyy-MM-ddTHH:mm):");
	    LocalDateTime pocetak = LocalDateTime.parse(sc.nextLine());

	    System.out.println("Unesite kraj (format: yyyy-MM-ddTHH:mm):");
	    LocalDateTime kraj = LocalDateTime.parse(sc.nextLine());

	    if (kraj.isBefore(pocetak) || kraj.equals(pocetak)) {
	        System.out.println("Kraj mora biti posle pocetka.");
	        return;
	    }

	    // dodatne usluge(opciono)
	    List<Usluga> usluge = new ArrayList<>();

	    // Muzika
	    System.out.println("Muzika?");
	    System.out.println("1. Da");
	    System.out.println("2. Ne");
	    if (Integer.parseInt(sc.nextLine()) == 1) {
	    	System.out.println("Tip muzike:");
	        System.out.println("1. Playlista");
	        System.out.println("2. Live band");

	        int izborMuzike = Integer.parseInt(sc.nextLine());

	        switch (izborMuzike) {
	            case 1 -> usluge.add(new Muzika(false)); // pustena muzika
	            case 2 -> usluge.add(new Muzika(true));  // live band
	            default -> System.out.println("Nepostojeca opcija.");
	        }
	    }

	    // Priprema hrane  
	    System.out.println("Priprema hrane?");
	    System.out.println("1. Da");
	    System.out.println("2. Ne");
	    if (Integer.parseInt(sc.nextLine()) == 1) {

	        boolean dalje = true;
	        while (dalje) {
	            System.out.println("Izaberite vrstu hrane:");
	            System.out.println("1. Predjelo");
	            System.out.println("2. Corba");
	            System.out.println("3. Glavno jelo");
	            System.out.println("4. Dezert");
	            System.out.println("0. Kraj izbora");

	            int izborHrane = Integer.parseInt(sc.nextLine());

	            switch (izborHrane) {
	                case 1 -> usluge.add(new PripremaHrane(VrstaHrane.PREDJELO));
	                case 2 -> usluge.add(new PripremaHrane(VrstaHrane.CORBA));
	                case 3 -> usluge.add(new PripremaHrane(VrstaHrane.GLAVNO_JELO));
	                case 4 -> usluge.add(new PripremaHrane(VrstaHrane.DEZERT));
	                case 0 -> dalje = false;
	                default -> System.out.println("Nepostojeca opcija.");
	            }
	        }
	    }

	    // Posluzivanje 
	    System.out.println("Posluzivanje?");
	    System.out.println("1. Da");
	    System.out.println("2. Ne");
	    if (Integer.parseInt(sc.nextLine()) == 1) {

	        boolean dalje = true;
	        while (dalje) {
	            System.out.println("Izaberite tip usluznog osoblja:");
	            System.out.println("1. Konobar");
	            System.out.println("2. Servir");
	            System.out.println("3. Perac");
	            System.out.println("4. Cistac");
	            System.out.println("0. Kraj izbora");

	            int izborOsoblja = Integer.parseInt(sc.nextLine());

	            if (izborOsoblja == 0) {
	                dalje = false;
	                continue;
	            }

	            System.out.print("Unesite broj osoblja: ");
	            int brojOsoblja = Integer.parseInt(sc.nextLine());

	            if (brojOsoblja <= 0) {
	                System.out.println("Broj mora biti veci od 0.");
	                continue;
	            }

	            switch (izborOsoblja) {
	                case 1 -> usluge.add(
	                        new Posluzivanje(brojOsoblja, UsluznoOsoblje.KONOBAR)
	                );
	                case 2 -> usluge.add(
	                        new Posluzivanje(brojOsoblja, UsluznoOsoblje.SERVIR)
	                );
	                case 3 -> usluge.add(
	                        new Posluzivanje(brojOsoblja, UsluznoOsoblje.PERAC)
	                );
	                case 4 -> usluge.add(
	                        new Posluzivanje(brojOsoblja, UsluznoOsoblje.CISTAC)
	                );
	                default -> System.out.println("Nepostojeca opcija.");
	            }
	        }
	    }

	    // kreiranje i placanje zakupa
	    boolean uspesno = zakupService.createAndPayZakup(
	            z,
	            sala,
	            pocetak,
	            kraj,
	            usluge
	    );

	    if (uspesno) {
	        System.out.println("Zakup je uspesno kreiran.");
	        System.out.println("Preostala sredstva: " + z.getSredstva());
	    } else {
	        System.out.println("Zakup nije moguce kreirati (termin zauzet ili nema dovoljno sredstava).");
	    }
	}


	private static void dodajSredstva(Zakupac z) {
		System.out.println("Trenutna sredstva: " + z.getSredstva());
		System.out.println("Unesite izmnos za dopunu: ");
		
		double iznos = Double.parseDouble(sc.nextLine());
		
		if(iznos <= 0) {
			System.out.println("Iznos mora biti veci od 0");
			return;
		}
		
		authService.addFunds(z, iznos);
		System.out.println("Sredstva uspesno dodata");
		System.out.println("Novo stanje na racunu: " + z.getSredstva());
		
	}

}
