package mojasala.app.menu;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
import mojasala.model.zakup.Zakup;
import mojasala.service.AuthService;
import mojasala.service.SalaService;
import mojasala.service.ZakupService;

/*
 * ZakupacMenu predstavlja konzolni meni za korisnika tipa Zakupac.
 * Omogućava pretragu sala, kreiranje zakupa, plaćanje zakupa,
 * dopunu sredstava i prikaz istorije zakupa za ulogovanog zakupca.
 */
public class ZakupacMenu {

    // Scanner za unos sa konzole
    private static final Scanner sc = new Scanner(System.in);

    // Servisi koji sadrže poslovnu logiku
    private static final SalaService salaService = new SalaService();
    private static final ZakupService zakupService = new ZakupService();
    private static final AuthService authService = new AuthService();

    // Glavni meni zakupca
    public static void show(Zakupac z) {
        while (true) {
            System.out.println("\n--- Zakupac ---");
            System.out.println("1. Pretraga sala");
            System.out.println("2. Napravi zakup");
            System.out.println("3. Dodaj sredstva");
            System.out.println("4. Plati zakup");
            System.out.println("5. Istorija zakupa");
            System.out.println("0. Logout");

            int izbor = ucitajInt("Izbor: ", 0, 5);

            switch (izbor) {
                case 1 -> pretragaSalaMenu();
                case 2 -> napraviZakup(z);
                case 3 -> dodajSredstva(z);
                case 4 -> platiZakupMenu(z);
                case 5 -> istorijaZakupa(z);
                case 0 -> { return; }
            }
        }
    }

    // Podmeni za različite tipove pretrage sala
    private static void pretragaSalaMenu() {

        System.out.println("\n--- Pretraga sala ---");
        System.out.println("1. Po dostupnosti (period)");
        System.out.println("2. Po nazivu");
        System.out.println("3. Po broju mesta");
        System.out.println("0. Nazad");

        int izbor = ucitajInt("Izbor: ", 0, 3);

        switch (izbor) {
            case 1 -> pretragaPoDostupnosti();
            case 2 -> pretragaPoNazivu();
            case 3 -> pretragaPoBrojuMesta();
            case 0 -> { }
        }
    }

    // Pretraga sala koje su dostupne u zadatom vremenskom periodu
    private static void pretragaPoDostupnosti() {

        LocalDateTime pocetak = ucitajDatum("Unesite pocetak (yyyy-MM-ddTHH:mm)");
        if (pocetak == null) return;

        LocalDateTime kraj = ucitajDatum("Unesite kraj (yyyy-MM-ddTHH:mm)");
        if (kraj == null) return;

        List<Sala> rezultat =
                salaService.findAvailableSale(pocetak, kraj, zakupService);

        if (rezultat.isEmpty()) {
            System.out.println("Nema dostupnih sala za dati period.");
            return;
        }

        rezultat.forEach(System.out::println);
    }

    // Pretraga sala po delu naziva
    private static void pretragaPoNazivu() {

        System.out.print("Unesite deo naziva sale: ");
        String naziv = sc.nextLine();

        List<Sala> rezultat = salaService.findByNazivContains(naziv);

        if (rezultat.isEmpty()) {
            System.out.println("Nema sala sa datim nazivom.");
            return;
        }

        rezultat.forEach(System.out::println);
    }

    // Pretraga sala po minimalnom kapacitetu
    private static void pretragaPoBrojuMesta() {

        int min = ucitajInt("Unesite minimalan broj mesta: ", 1, Integer.MAX_VALUE);

        List<Sala> rezultat = salaService.findByMinKapacitet(min);

        if (rezultat.isEmpty()) {
            System.out.println("Nema sala sa trazenim kapacitetom.");
            return;
        }

        rezultat.forEach(System.out::println);
    }

    // Kreiranje novog zakupa
    private static void napraviZakup(Zakupac z) {

        Sala sala = izaberiSalu();
        if (sala == null) return;

        LocalDateTime pocetak = ucitajDatum("Unesite pocetak (yyyy-MM-ddTHH:mm)");
        if (pocetak == null) return;

        LocalDateTime kraj = ucitajDatum("Unesite kraj (yyyy-MM-ddTHH:mm)");
        if (kraj == null) return;

        if (!kraj.isAfter(pocetak)) {
            System.out.println("Kraj mora biti posle pocetka.");
            return;
        }

        int slobodno = zakupService.getSlobodnaMesta(sala, pocetak, kraj);
        System.out.println("Slobodna mesta u sali: " + slobodno);

        int brojOsoba = ucitajInt("Unesite broj osoba: ", 1, slobodno);

        // Lista dodatnih usluga koje zakupac bira
        List<Usluga> usluge = new ArrayList<>();
        odaberiMuziku(usluge);
        odaberiHranu(usluge);
        odaberiPosluzivanje(usluge);

        boolean uspesno = zakupService.createZakup(
                z, sala, pocetak, kraj, brojOsoba, usluge
        );

        if (!uspesno) {
            System.out.println("Zakup nije moguce kreirati.");
            return;
        }

        System.out.println("Zakup je uspesno kreiran (NEPLACEN).");

        int izbor = ucitajInt(
                "Da li zelite da platite odmah?\n1. Da\n2. Ne\nIzbor: ",
                1, 2
        );

        if (izbor == 1) {
            platiZakupMenu(z);
        }
    }

    // Meni za plaćanje neplaćenih zakupa
    private static void platiZakupMenu(Zakupac z) {

        List<Zakup> neplaceni = zakupService.getNeplaceniZakupi(z);

        if (neplaceni.isEmpty()) {
            System.out.println("Nemate neplacenih zakupa.");
            return;
        }

        System.out.println("\nNeplaceni zakupi:");
        for (int i = 0; i < neplaceni.size(); i++) {
            Zakup zak = neplaceni.get(i);
            System.out.println(
                    (i + 1) + ". " +
                    zak.getNazivSale() + " | " +
                    zak.getPocetakZakupa() + " - " +
                    zak.getKrajZakupa()
            );
        }

        int izbor = ucitajInt(
                "Izaberite zakup za placanje: ",
                1, neplaceni.size()
        );

        Zakup izabrani = neplaceni.get(izbor - 1);
        Sala sala = salaService.getByNaziv(izabrani.getNazivSale());

        boolean uspesno = zakupService.platiZakup(z, izabrani, sala);

        if (uspesno) {
            System.out.println("Placanje uspesno.");
            System.out.println("Preostala sredstva: " + z.getSredstva());
        } else {
            System.out.println("Nemate dovoljno sredstava.");
        }
    }

    // Izbor sale na osnovu naziva
    private static Sala izaberiSalu() {
        System.out.print("Unesite naziv sale: ");
        String naziv = sc.nextLine();

        for (Sala s : salaService.getAllSale()) {
            if (s.getNaziv().equalsIgnoreCase(naziv)) {
                return s;
            }
        }

        System.out.println("Sala ne postoji.");
        return null;
    }

    // Dopuna sredstava zakupca
    private static void dodajSredstva(Zakupac z) {
        System.out.println("Trenutna sredstva: " + z.getSredstva());

        while (true) {
            try {
                System.out.print("Unesite iznos za uplatu (0 = nazad): ");
                String unos = sc.nextLine().trim();

                if (unos.equals("0")) return;

                double iznos = Double.parseDouble(unos);

                if (iznos <= 0) {
                    System.out.println("Iznos mora biti veci od 0.");
                    continue;
                }

                authService.addFunds(z, iznos);

                System.out.println("Sredstva uspesno uplacena.");
                System.out.println("Novo stanje na racunu: " + z.getSredstva());
                return;

            } catch (NumberFormatException e) {
                System.out.println("Unos mora biti broj.");
            }
        }
    }

    // Učitavanje celobrojne vrednosti sa ograničenjem
    private static int ucitajInt(String poruka, int min, int max) {
        while (true) {
            try {
                System.out.print(poruka);
                int v = Integer.parseInt(sc.nextLine());
                if (v < min || v > max) continue;
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Unos mora biti broj.");
            }
        }
    }

    // Učitavanje datuma i vremena u ISO formatu
    private static LocalDateTime ucitajDatum(String poruka) {
        while (true) {
            System.out.print(poruka + " (0 = nazad): ");
            String unos = sc.nextLine().trim();

            if (unos.equals("0")) return null;

            try {
                return LocalDateTime.parse(unos);
            } catch (DateTimeParseException e) {
                System.out.println("Pogresan format datuma.");
            }
        }
    }

    // Odabir muzike kao dodatne usluge
    private static void odaberiMuziku(List<Usluga> usluge) {
        int odgovor = ucitajInt("Muzika?\n1. Da\n2. Ne\nIzbor: ", 1, 2);
        if (odgovor == 2) return;

        int izbor = ucitajInt(
                "Tip muzike:\n1. Playlista\n2. Live band\nIzbor: ",
                1, 2
        );

        usluge.add(new Muzika(izbor == 2));
    }

    // Odabir hrane kao dodatne usluge
    private static void odaberiHranu(List<Usluga> usluge) {
        int odgovor = ucitajInt("Priprema hrane?\n1. Da\n2. Ne\nIzbor: ", 1, 2);
        if (odgovor == 2) return;

        System.out.println("""
                Izaberite vrste hrane (odvojene zarezom):
                1. Predjelo
                2. Corba
                3. Glavno jelo
                4. Dezert
                5. Sve navedeno
                """);

        List<Integer> izbori = parsirajBrojeve(sc.nextLine(), 1, 5);

        if (izbori.contains(5)) {
            dodajSvuHranu(usluge);
            return;
        }

        for (int i : izbori) {
            usluge.add(new PripremaHrane(VrstaHrane.values()[i - 1]));
        }
    }

    // Dodavanje svih vrsta hrane
    private static void dodajSvuHranu(List<Usluga> usluge) {
        for (VrstaHrane v : VrstaHrane.values()) {
            usluge.add(new PripremaHrane(v));
        }
    }

    // Odabir posluživanja
    private static void odaberiPosluzivanje(List<Usluga> usluge) {
        int odgovor = ucitajInt("Posluzivanje?\n1. Da\n2. Ne\nIzbor: ", 1, 2);
        if (odgovor == 2) return;

        System.out.print("Unesite posluzivanje (npr. konobar(2), servur(2), cistac(5), perac(1)): ");
        usluge.addAll(parsirajPosluzivanje(sc.nextLine()));
    }

    // Parsiranje unosa posluživanja
    private static List<Posluzivanje> parsirajPosluzivanje(String unos) {
        List<Posluzivanje> rezultat = new ArrayList<>();

        for (String deo : unos.split(",")) {
            int o = deo.indexOf('(');
            int z = deo.indexOf(')');

            String naziv = deo.substring(0, o).trim().toLowerCase();
            int broj = Integer.parseInt(deo.substring(o + 1, z));

            rezultat.add(new Posluzivanje(broj, mapirajOsoblje(naziv)));
        }
        return rezultat;
    }

    // Mapiranje tekstualnog unosa na enum UsluznoOsoblje
    private static UsluznoOsoblje mapirajOsoblje(String naziv) {
        return switch (naziv) {
            case "konobar" -> UsluznoOsoblje.KONOBAR;
            case "servir" -> UsluznoOsoblje.SERVIR;
            case "perac" -> UsluznoOsoblje.PERAC;
            case "cistac" -> UsluznoOsoblje.CISTAC;
            default -> throw new IllegalArgumentException("Nepoznata usluga.");
        };
    }

    // Parsiranje liste brojeva iz stringa
    private static List<Integer> parsirajBrojeve(String unos, int min, int max) {
        List<Integer> rezultat = new ArrayList<>();
        for (String d : unos.split(",")) {
            int v = Integer.parseInt(d.trim());
            if (v < min || v > max) throw new IllegalArgumentException();
            if (!rezultat.contains(v)) rezultat.add(v);
        }
        return rezultat;
    }

    // Prikaz istorije zakupa za ulogovanog zakupca
    private static void istorijaZakupa(Zakupac z) {

        System.out.println("Unesite period za istoriju zakupa");
        System.out.println("Za kompletnu istoriju unesite: -1 i 1");
        System.out.println("Format datuma: dd-MM-yyyy");

        System.out.print("Od (dd-MM-yyyy ili -1): ");
        String odUnos = sc.nextLine().trim();

        System.out.print("Do (dd-MM-yyyy ili 1): ");
        String doUnos = sc.nextLine().trim();

        LocalDateTime odDatuma = null;
        LocalDateTime doDatuma = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (!odUnos.equals("-1") && !doUnos.equals("1")) {
            try {
                LocalDate od = LocalDate.parse(odUnos, formatter);
                LocalDate doD = LocalDate.parse(doUnos, formatter);

                if (doD.isBefore(od)) {
                    System.out.println("DO datum mora biti posle OD datuma.");
                    return;
                }

                odDatuma = od.atStartOfDay();
                doDatuma = doD.atTime(LocalTime.MAX);

            } catch (Exception e) {
                System.out.println("Pogresan format datuma.");
                return;
            }
        }

        List<Zakup> zakupi = zakupService.getIstorijaZakupa(z, odDatuma, doDatuma);

        if (zakupi.isEmpty()) {
            System.out.println("U datom periodu nema zakupa.");
            return;
        }

        System.out.println("\n--- Istorija zakupa ---");

        List<Sala> sale = salaService.getAllSale();

        for (int i = 0; i < zakupi.size(); i++) {
            Zakup zak = zakupi.get(i);

            Sala sala = null;
            for (Sala s : sale) {
                if (s.getNaziv().equalsIgnoreCase(zak.getNazivSale())) {
                    sala = s;
                    break;
                }
            }

            System.out.println(
                    "ID: " + (i + 1) +
                    " | " + zak.getPocetakZakupa().toLocalDate() +
                    " - " + zak.getKrajZakupa().toLocalDate() +
                    " | Sala: " + zak.getNazivSale() +
                    " | Kapacitet: " +
                    (sala != null ? sala.getKapacitet() : "N/A") +
                    " | Broj osoba: " + zak.getBrojOsoba()
            );
        }
    }
}




