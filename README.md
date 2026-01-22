# Moja Sala – Sistem za upravljanje zakupom sala

Ovaj projekat predstavlja konzolnu Java aplikaciju za upravljanje salama i njihovim zakupom.
Aplikacija omogućava registraciju i prijavu korisnika, pretragu sala, kreiranje zakupa,
upravljanje dodatnim uslugama i evidenciju plaćanja, uz trajno čuvanje podataka u CSV fajlovima.

Projekat je razvijen u skladu sa principima objektno-orijentisanog programiranja (OOP).

---

## Funkcionalnosti

### Korisnici
- Registracija zakupca
- Registracija vlasnika
- Login sistema

### Zakupac može:
- Pretraživati sale:
  - po dostupnosti za vremenski period
  - po nazivu
  - po broju mesta
- Napraviti zakup sale za budući period
- Dodati dodatne usluge:
  - muzika (playlista ili live band)
  - priprema hrane
  - posluživanje
- Izabrati da li će zakup platiti odmah ili naknadno
- Uplatiti sredstva na račun
- Platiti neplaćene zakupe
- Pregledati istoriju zakupa za izabrani period ili kompletnu istoriju

### Sistem automatski:
- Proverava dostupnost sala
- Računa cenu zakupa prema specifikaciji
- Sprečava preklapanje zakupa
- Čuva sve podatke u CSV fajlovima

---

## Obračun cene zakupa

Cena zakupa se računa po formuli:

- BROJ OSOBA × 35
- + RASVETA × 100 (ako postoji)
- + HRANA (zbir izabranih stavki)
- + POSLUŽIVANJE (broj osoblja × koeficijent)
- + MUZIKA (trajanje u minutima × 2)

U cenu se uključuju samo usluge koje su izabrane.

---



### Arhitektura

Aplikacija koristi slojevitu arhitekturu:

- **Menu sloj** – komunikacija sa korisnikom (konzola)
- **Service sloj** – poslovna logika
- **Repository sloj** – rad sa CSV fajlovima
- **Model sloj** – domen objekti



---

## Tehnologije

- Java (JDK 17+)
- Eclipse IDE
- Git & GitHub
- CSV fajlovi za perzistenciju podataka

---

## Pokretanje aplikacije

1. Otvoriti projekat u Eclipse IDE
2. Pokrenuti `Platforma` (main klasa)
3. Rad sa aplikacijom se vrši putem konzole

---

## Autor

- Ime i prezime: **Jovan Vidović**
- Projekat iz predmeta: **Objektno orijentisano programiranje**
- Akademska godina: **2025/2026**

---
