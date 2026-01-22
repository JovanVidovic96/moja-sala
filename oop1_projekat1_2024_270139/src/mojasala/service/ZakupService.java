package mojasala.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import mojasala.model.korisnik.Zakupac;
import mojasala.model.sala.Sala;
import mojasala.model.usluga.Muzika;
import mojasala.model.usluga.Posluzivanje;
import mojasala.model.usluga.PripremaHrane;
import mojasala.model.usluga.Usluga;
import mojasala.model.zakup.Zakup;
import mojasala.repository.KorisnikRepository;
import mojasala.repository.SalaRepository;
import mojasala.repository.ZakupRepository;

/*
 * ZakupService predstavlja servisni sloj zadužen za kompletnu poslovnu
 * logiku vezanu za zakupe sala.
 *
 * Uloga ZakupService-a u arhitekturi:
 * - upravlja kreiranjem zakupa,
 * - proverava dostupnost sala i slobodna mesta,
 * - obračunava cenu zakupa prema specifikaciji,
 * - omogućava plaćanje zakupa,
 * - vraća neplaćene i istorijske zakupe zakupca.
 *
 * Komunikacija:
 * - koristi ZakupRepository za čuvanje i čitanje zakupa (CSV fajl),
 * - koristi SalaRepository za podatke o salama,
 * - koristi KorisnikRepository za ažuriranje sredstava zakupca,
 * - koristi se iz UI sloja (ZakupacMenu).
 */

public class ZakupService {
	//repo za rad sa zakupima
    private ZakupRepository zakupRepository;
    //repo za pristup salama
    private SalaRepository salaRepository;
    //repo za update korisnika(sredstva zakupca)
    private KorisnikRepository korisnikRepository;

    /* KONSTRUKTOR */
    // inicijalizacija service-a i njegovih zavisnosti 
    public ZakupService() {
        this.zakupRepository = new ZakupRepository();
        this.salaRepository = new SalaRepository();
        this.korisnikRepository = new KorisnikRepository();
    }

    /* POMOCNE METODE */
    //metoda koja proverava da li se vremenski intervali preklapaju
    private boolean isOverlapping(
            LocalDateTime start1,
            LocalDateTime end1,
            LocalDateTime start2,
            LocalDateTime end2) {

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    /* SLOBODNA MESTA 
     * 
     * metoda koja racuna broj slobodnih mesta u sali, za zadati vremenski period
     * parametri: sala za koju proveravamo dostupnost, pocetak  i kraj perioda zakupa
     * vraca broj slobodnih mesta*/
    
    public int getSlobodnaMesta(
            Sala sala,
            LocalDateTime pocetak,
            LocalDateTime kraj) {

        int zauzeto = 0;

        for (Zakup z : zakupRepository.findAll()) {
            if (z.getNazivSale().equalsIgnoreCase(sala.getNaziv())
                    && isOverlapping(
                        z.getPocetakZakupa(),
                        z.getKrajZakupa(),
                        pocetak,
                        kraj)) {

                zauzeto += z.getBrojOsoba();
            }
        }

        return Math.max(sala.getKapacitet() - zauzeto, 0);
    }

    /* PRETRAGA SALA 
     * Vraca opis sala koje imaju slobodna mesta u zadatom periodu*/

    public List<String> findSaleWithFreeSeats(
            LocalDateTime pocetak,
            LocalDateTime kraj) {

        List<String> rezultat = new ArrayList<>();

        for (Sala s : salaRepository.findAll()) {
            int slobodno = getSlobodnaMesta(s, pocetak, kraj);
            if (slobodno > 0) {
                rezultat.add(
                        s.getNaziv() + " (slobodna mesta: " + slobodno + ")"
                );
            }
        }

        return rezultat;
    }

    /* KREIRANJE ZAKUPA (BEZ PLACANJA) 
     * zakup se cuva kao neplacen
     * return je true ako je uspesno kreiran, false ako nema dovoljno mesta*/

    public boolean createZakup(
            Zakupac zakupac,
            Sala sala,
            LocalDateTime pocetak,
            LocalDateTime kraj,
            int brojOsoba,
            List<Usluga> usluge) {

        int slobodno = getSlobodnaMesta(sala, pocetak, kraj);
        if (brojOsoba <= 0 || brojOsoba > slobodno) {
            return false;
        }

        Zakup zakup = new Zakup(
                sala.getNaziv(),
                zakupac.getUsername(),
                pocetak,
                kraj,
                brojOsoba,
                0,          // cena se racuna tek pri placanju
                false,      // neplacen
                usluge
        );

        zakupRepository.save(zakup);
        return true;
    }

    /* PLACANJE ZAKUPA 
     * vrsi se placanje izabranog zakupa
     * skida sredstva i oznacava zakup kao placen*/

    public boolean platiZakup(
            Zakupac zakupac,
            Zakup zakup,
            Sala sala) {

        double cena = izracunajCenuZakupa(zakup, sala);

        if (zakupac.getSredstva() < cena) {
            return false;
        }

        // skidanje sredstava
        zakupac.setSredstva(zakupac.getSredstva() - cena);
        korisnikRepository.update(zakupac);

        // azuriranje zakupa (oznacavamo kao placen)
        List<Zakup> svi = zakupRepository.findAll();
        List<String> noviCsv = new ArrayList<>();

        for (Zakup z : svi) {
            if (z.getUsernameZakupca().equals(zakup.getUsernameZakupca())
                    && z.getNazivSale().equals(zakup.getNazivSale())
                    && z.getPocetakZakupa().equals(zakup.getPocetakZakupa())
                    && !z.isPlacen()) {

                z = new Zakup(
                        z.getNazivSale(),
                        z.getUsernameZakupca(),
                        z.getPocetakZakupa(),
                        z.getKrajZakupa(),
                        z.getBrojOsoba(),
                        cena,
                        true,
                        z.getUsluge()
                );
            }

            noviCsv.add(z.toCsv());
        }

        zakupRepository.writeLines(noviCsv);
        return true;
    }

    /* RACUNANJE CENE ZAKUPA (PO SPECIFIKACIJI) 
     * u obracun ulazi broj osoba, rasveta, dodatne usluge, trajanje zakupa*/

    public double izracunajCenuZakupa(Zakup zakup, Sala sala) {

        double suma = 0;

        // BROJ OSOBA * 35
        suma += zakup.getBrojOsoba() * 35;

        // RASVETA
        if (sala.isRasveta()) {
            suma += 100;
        }

        long minuti = Duration.between(
                zakup.getPocetakZakupa(),
                zakup.getKrajZakupa()
        ).toMinutes();

        for (Usluga u : zakup.getUsluge()) {

            if (u instanceof PripremaHrane ph) {
                suma += switch (ph.getVrstaHrane()) {
                    case PREDJELO -> 250;
                    case CORBA -> 200;
                    case GLAVNO_JELO -> 350;
                    case DEZERT -> 500;
                };
            }

            if (u instanceof Posluzivanje p) {
                int koef = switch (p.getTip()) {
                    case KONOBAR -> 4;
                    case SERVIR -> 2;
                    case PERAC -> 2;
                    case CISTAC -> 3;
                };
                suma += p.getBrojOsoblja() * koef;
            }

            if (u instanceof Muzika) {
                suma += minuti * 2;
            }
        }

        return suma;
    }

    /* NEPLACENI ZAKUPI – SVI, BEZ OBZIRA NA DATUM */

    public List<Zakup> getNeplaceniZakupi(Zakupac zakupac) {

        List<Zakup> rezultat = new ArrayList<>();

        for (Zakup z : zakupRepository.findAll()) {
        	
        	
            if (z.getUsernameZakupca().equals(zakupac.getUsername())
                    && !z.isPlacen()) {
                rezultat.add(z);
            }
        }

        return rezultat;
    }
    /*ISTORIJA ZAKUPA
     * vraca istorju ulogovanog korisnika
     * u zavisnosti od unosa vraca komplet istoriju zakupa
     * ili u rasponu datuma koji su uneti*/
    public List<Zakup> getIstorijaZakupa(Zakupac zakupac, LocalDateTime odDatuma, LocalDateTime doDatuma){
    	List<Zakup> rezultat = new ArrayList<>();
    	
    	for (Zakup z : zakupRepository.findAll()) {
    		
    		if(!z.getUsernameZakupca().equals(zakupac.getUsername())) {
    			continue;
    		}
    		// svi zakupi od -1 do 1
    		if(odDatuma == null && doDatuma == null) {
    			rezultat.add(z);
    			continue;
    		}
    		
    		//filtriranje po opsegu datuma
    		boolean posleOd = z.getPocetakZakupa().isEqual(odDatuma) || z.getPocetakZakupa().isAfter(odDatuma);
    		boolean preDo = z.getKrajZakupa().isEqual(doDatuma) || z.getKrajZakupa().isBefore(doDatuma);
    		
    		if (posleOd && preDo) {
    			rezultat.add(z);
    		}
    	}
    	return rezultat;	
    }
}



