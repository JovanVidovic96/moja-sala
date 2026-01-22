package mojasala.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import mojasala.model.sala.Sala;
import mojasala.repository.SalaRepository;

/*
 * SalaService predstavlja servisni sloj zadužen za poslovnu logiku
 * vezanu za sale.
 *
 * Uloga service sloja u arhitekturi:
 * - sadrži poslovna pravila i logiku aplikacije,
 * - koristi repository sloj za pristup podacima (CSV fajlovima),
 * - ne zna ništa o korisničkom interfejsu (meni klasama),
 * - služi kao posrednik između UI sloja i repository sloja.
 *
 * SalaService:
 * - omogućava dodavanje novih sala,
 * - omogućava pretragu sala po različitim kriterijumima
 *   (kapacitet, naziv, dostupnost u periodu),
 * - vraća kompletne podatke o salama.
 *
 * Komunikacija:
 * - koristi SalaRepository za čitanje i upis sala u CSV fajl,
 * - koristi ZakupService za proveru dostupnosti sala u određenom periodu.
 */

public class SalaService {
	//Repository zaduzen za trajno skladistenje sala (CSV)
    private SalaRepository salaRepository;
    //Inicijalizacija service-a i njegovih zavisnosti
    public SalaService() {
        this.salaRepository = new SalaRepository();
    }

    /*  DODAVANJE SALE */

    public boolean addSala(Sala novaSala) {
        for (Sala s : salaRepository.findAll()) {
            if (s.getNaziv().equalsIgnoreCase(novaSala.getNaziv())) {
                return false;
            }
        }
        salaRepository.save(novaSala);
        return true;
    }

    /* OSNOVNE METODE*/
    //vraca listu svih sala u sistemu
    public List<Sala> getAllSale() {
        return salaRepository.findAll();
    }
    //vraca salu po tacnom nazivu 
    public Sala getByNaziv(String naziv) {
        for (Sala s : getAllSale()) {
            if (s.getNaziv().equalsIgnoreCase(naziv)) {
                return s;
            }
        }
        return null;
    }

    /*  PRETRAGA PO KAPACITETU */

    // Sale koje imaju BAR trazeni broj mesta
    public List<Sala> findByMinKapacitet(int minKapacitet) {

        List<Sala> rezultat = new ArrayList<>();

        for (Sala s : salaRepository.findAll()) {
            if (s.getKapacitet() >= minKapacitet) {
                rezultat.add(s);
            }
        }

        return rezultat;
    }

    /* PRETRAGA PO NAZIVU */

    // Pretraga po delu naziva (case-insensitive)
    // vraca listu sala ciji naziv sadrzi zadati tekst
    public List<Sala> findByNazivContains(String deoNaziva) {

        List<Sala> rezultat = new ArrayList<>();

        if (deoNaziva == null || deoNaziva.isBlank()) {
            return rezultat;
        }

        String trazeno = deoNaziva.toLowerCase();

        for (Sala s : salaRepository.findAll()) {
            if (s.getNaziv().toLowerCase().contains(trazeno)) {
                rezultat.add(s);
            }
        }

        return rezultat;
    }

    /* PRETRAGA PO DOSTUPNOSTI */
    /*
     * Pretraga dostupnih sala za zadati vremenski period.
     * Dozvoljeni su samo buduci datumi.
     * Sala je dostupna ako nema zakupa koji se preklapa sa tim periodom.
     */
    public List<Sala> findAvailableSale(
            LocalDateTime pocetak,
            LocalDateTime kraj,
            ZakupService zakupService) {

        List<Sala> rezultat = new ArrayList<>();

        // samo buduci period
        if (pocetak.isBefore(LocalDateTime.now()) || !kraj.isAfter(pocetak)) {
            return rezultat;
        }

        for (Sala s : salaRepository.findAll()) {
            int slobodno = zakupService.getSlobodnaMesta(s, pocetak, kraj);

            // ako postoji bar jedno slobodno mesto, sala se smatra dostupnom
            if (slobodno > 0) {
                rezultat.add(s);
            }
        }

        return rezultat;
    }
}

