package mojasala.model.zakup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import mojasala.database.CsvSerializable;
import mojasala.model.enums.UsluznoOsoblje;
import mojasala.model.enums.VrstaHrane;
import mojasala.model.usluga.Muzika;
import mojasala.model.usluga.Posluzivanje;
import mojasala.model.usluga.PripremaHrane;
import mojasala.model.usluga.Usluga;

/*
 * Klasa Zakup predstavlja jedan zakup sale.
 * 
 * Sadrži informacije o:
 * - sali koja je zakupljena
 * - zakupcu koji je napravio zakup
 * - vremenskom periodu zakupa
 * - broju osoba
 * - ceni i statusu plaćanja
 * - dodatnim uslugama uključenim u zakup
 * 
 * Klasa podržava čuvanje i učitavanje podataka iz CSV fajla.
 */

public class Zakup implements CsvSerializable {

    private String nazivSale;
    private String usernameZakupca;
    private LocalDateTime pocetakZakupa;
    private LocalDateTime krajZakupa;
    private int brojOsoba;
    private double cena;
    private boolean placen;
    private List<Usluga> usluge;

    /* PRAZAN KONSTRUKTOR */
    //Koristi se pri ucitavanju podataka iz CSV fajla
    public Zakup() {
        this.usluge = new ArrayList<>();
        this.placen = false;
    }

    /* GLAVNI KONSTRUKTOR */
    //Koristi se pri kreiranju novog zakupa u toku rada programa
    public Zakup(String nazivSale,
                 String usernameZakupca,
                 LocalDateTime pocetakZakupa,
                 LocalDateTime krajZakupa,
                 int brojOsoba,
                 double cena,
                 boolean placen,
                 List<Usluga> usluge) {

        this.nazivSale = nazivSale;
        this.usernameZakupca = usernameZakupca;
        this.pocetakZakupa = pocetakZakupa;
        this.krajZakupa = krajZakupa;
        this.brojOsoba = brojOsoba;
        this.cena = cena;
        this.placen = placen;
        this.usluge = usluge != null ? usluge : new ArrayList<>();
    }

    /* GETTERI / SETTERI */

    public String getNazivSale() {
        return nazivSale;
    }

    public String getUsernameZakupca() {
        return usernameZakupca;
    }

    public LocalDateTime getPocetakZakupa() {
        return pocetakZakupa;
    }

    public LocalDateTime getKrajZakupa() {
        return krajZakupa;
    }

    public int getBrojOsoba() {
        return brojOsoba;
    }

    public double getCena() {
        return cena;
    }

    public boolean isPlacen() {
        return placen;
    }

    public void setPlacen(boolean placen) {
        this.placen = placen;
    }

    public List<Usluga> getUsluge() {
        return usluge;
    }

    /* CSV */

    @Override
    public String toCsv() {
        return nazivSale + "," +
               usernameZakupca + "," +
               pocetakZakupa + "," +
               krajZakupa + "," +
               brojOsoba + "," +
               cena + "," +
               placen + "," +
               uslugeToCsv();
    }

    public static Zakup fromCsv(String line) {
        String[] p = line.split(",", 8);

        // podrška za STARE CSV zapise koji nemaju polje "placen"
        if (p.length != 7 && p.length != 8) {
            throw new IllegalArgumentException(
                    "Neispravan CSV zapis za Zakup: " + line
            );
        }

        Zakup z = new Zakup();
        z.nazivSale = p[0];
        z.usernameZakupca = p[1];
        z.pocetakZakupa = LocalDateTime.parse(p[2]);
        z.krajZakupa = LocalDateTime.parse(p[3]);
        z.brojOsoba = Integer.parseInt(p[4]);
        z.cena = Double.parseDouble(p[5]);

        if (p.length == 8) {
            z.placen = Boolean.parseBoolean(p[6]);
            z.usluge = parseUsluge(p[7]);
        } else {
            z.placen = false;
            z.usluge = parseUsluge(p[6]);
        }

        return z;
    }

    /* SERIALIZACIJA USLUGA */

    private String uslugeToCsv() {
        if (usluge == null || usluge.isEmpty()) return "";

        List<String> hrana = new ArrayList<>();
        List<String> muzika = new ArrayList<>();
        List<String> posluzivanje = new ArrayList<>();
        
        //Grupisanje usluga po tipu
        for (Usluga u : usluge) {
            if (u instanceof PripremaHrane ph) {
                hrana.add(ph.getVrstaHrane().name());
            } else if (u instanceof Muzika m) {
                muzika.add(m.isLiveBand() ? "LIVE" : "PLAYLISTA");
            } else if (u instanceof Posluzivanje p) {
                posluzivanje.add(
                        p.getTip().name() + "(" + p.getBrojOsoblja() + ")"
                );
            }
        }
        
        //Spajanje usluga u jedan string
        List<String> sekcije = new ArrayList<>();
        if (!hrana.isEmpty())
            sekcije.add("HRANA:" + String.join("|", hrana));
        if (!muzika.isEmpty())
            sekcije.add("MUZIKA:" + String.join("|", muzika));
        if (!posluzivanje.isEmpty())
            sekcije.add("POSLUZIVANJE:" + String.join("|", posluzivanje));

        return String.join(";", sekcije);
    }

    /* PARSIRANJE USLUGA */
    
    //Parsira CSV zapis usluga i vraca listu sa tipom Usluga 
    private static List<Usluga> parseUsluge(String s) {
        List<Usluga> rezultat = new ArrayList<>();
        if (s == null || s.isBlank()) return rezultat;

        String[] sekcije = s.split(";");

        for (String sekcija : sekcije) {
            String[] delovi = sekcija.split(":");
            if (delovi.length != 2) continue;

            String tip = delovi[0];
            String[] stavke = delovi[1].split("\\|");

            switch (tip) {
                case "HRANA" -> {
                    for (String h : stavke) {
                        rezultat.add(
                                new PripremaHrane(VrstaHrane.valueOf(h))
                        );
                    }
                }
                case "MUZIKA" -> {
                    for (String m : stavke) {
                        rezultat.add(
                                new Muzika(m.equals("LIVE"))
                        );
                    }
                }
                case "POSLUZIVANJE" -> {
                    for (String p : stavke) {
                        int o = p.indexOf('(');
                        int z = p.indexOf(')');

                        if (o == -1 || z == -1) continue;

                        String naziv = p.substring(0, o);
                        int broj = Integer.parseInt(
                                p.substring(o + 1, z)
                        );

                        rezultat.add(
                                new Posluzivanje(
                                        broj,
                                        UsluznoOsoblje.valueOf(naziv)
                                )
                        );
                    }
                }
            }
        }
        return rezultat;
    }
}




