# Veterinarska ordinacija

Java desktop aplikacija za rad veterinarske ordinacije, organizovana kao klijent–server sistem. Projekat obuhvata domenski model za životinje, veterinare, stručne kvalifikacije, veterinarske usluge i izveštaje o pregledima.

Aplikacija je u razvoju. Trenutno je moguće pokrenuti serversku formu, pokrenuti i zaustaviti TCP server i kroz dugme **TEST** učitati sve vrste životinja iz MySQL baze. Klijentski deo i obrada većine operacija još nisu završeni.

## Struktura repozitorijuma

| Projekat | Namena |
| --- | --- |
| `VeterinarskaOrdinacijaZajednicki` | Domenske klase, interfejs `IAbstractDomainObject`, komunikacioni objekti `ClientRequest` i `ServerResponse`, statusi odgovora i konstante operacija. |
| `VeterinarskaOrdinacijaServer` | Swing administratorska forma, serverske niti, kontroler, sistemske operacije i JDBC pristup bazi. |
| `VeterinarskaOrdinacijaKlijent` | Osnova klijentske Swing aplikacije; početna klasa, forma, kontroler i sesija. |

Sva tri direktorijuma su zasebni NetBeans/Ant projekti. Server i klijent koriste zajednički projekat.

## Preduslovi

- JDK 26, prema trenutnim `javac.source` i `javac.target` podešavanjima projekata.
- Apache NetBeans sa podrškom za Java SE i Ant projekte, ili Apache Ant za izgradnju iz terminala.
- MySQL server sa pripremljenom bazom `veterinarska_ordinacija`.
- MySQL Connector/J: `VeterinarskaOrdinacijaServer/lib/mysql-connector-j-26.10.0.jar`.

Ako koristiš drugi JDK, uskladi Java Platform i Source/Binary Format u sva tri projekta. Niža verzija JDK-a ne može da gradi projekat podešen za Java 26 bez promene tih podešavanja.

## Podešavanje baze

Repozitorijum trenutno ne sadrži SQL skriptu za kreiranje ili popunjavanje baze. Pre pokretanja operacija potrebno je obezbediti postojeću šemu koja odgovara domenskim klasama.

Konekcija za trenutno povezanu operaciju definisana je u [ServerController.java](VeterinarskaOrdinacijaServer/src/rs/fon/veterinarska/server/controller/ServerController.java), prilikom kreiranja `DBConnectionFactory`. Prilagodi JDBC URL, korisničko ime i lozinku svom MySQL serveru, na primer:

```java
new DBConnectionFactory(
    "jdbc:mysql://localhost:3306/veterinarska_ordinacija",
    "korisnik",
    "lozinka"
)
```

Ovo je primer konfiguracije; trenutni kod koristi adresu baze u lokalnoj mreži. Forma **View → configuration** i klasa `Configuration` još nisu povezane sa kreiranjem ove konekcije.

Za dugme **TEST** potrebna je tabela `VrstaZivotinje` sa kolonama `idVrsta`, `naziv` i `napomene`. Prazna tabela daje praznu listu.

## Pokretanje u NetBeans-u

1. Izaberi **File → Open Project** i otvori sva tri projekta iz repozitorijuma.
2. Proveri izabrani JDK i reference ka zajedničkom projektu u svojstvima servera i klijenta.
3. U serverskom projektu proveri **Libraries**: referenca treba da pokazuje na JAR u direktorijumu `lib`. Ako imaš samo preuzeti Connector/J ZIP, prvo izdvoji JAR iz njega.
4. Podesi pristup bazi u `ServerController`.
5. Pokreni **Clean and Build** za zajednički projekat i server, pa **Run Project** za `VeterinarskaOrdinacijaServer`.
6. U serverskoj formi izaberi **Server → start** za pokretanje TCP servera na portu **3000**. Za zaustavljanje koristi **Server → stop**.
7. Klikni **TEST** za direktan poziv kontrolera i ispis vrsta životinja u NetBeans Output prozoru.

Dugme **TEST** direktno pristupa kontroleru i bazi, pa ne zahteva pokrenut TCP server i ne proverava mrežnu komunikaciju sa klijentom.

Početna klasa `VeterinarskaOrdinacijaKlijent` trenutno ima prazan `main`, pa pokretanje klijentskog projekta još ne otvara korisnički tok aplikacije.

## Izgradnja iz terminala

Komande se izvršavaju iz korena repozitorijuma, uz odgovarajući JDK na `PATH`:

```bash
ant -f VeterinarskaOrdinacijaZajednicki/build.xml clean jar
ant -f VeterinarskaOrdinacijaServer/build.xml clean jar
ant -f VeterinarskaOrdinacijaKlijent/build.xml clean jar
```

Pokretanje serverske forme:

```bash
ant -f VeterinarskaOrdinacijaServer/build.xml run
```

Swing forme zahtevaju grafičko okruženje.

## Organizacija serverskog koda

Trenutno povezan tok za učitavanje svih vrsta životinja je:

```text
ServerForm.TEST
  → ServerController.odgovoriKlijentu
  → SOVratiListuSvihVrstaZivotinja.execute
  → DBBroker.rowSelect
  → MySQL
```

`AbstractSO` upravlja životnim ciklusom konekcije i transakcijom: otvara konekciju, poziva validaciju i operaciju, potvrđuje uspešnu transakciju, vraća je pri grešci i zatvara konekciju. `DBBroker` gradi SQL koristeći metapodatke i mapiranje iz domenskih objekata.

TCP sloj koristi `StartServer` za prihvatanje konekcija i zaseban `ClientRequestHandler` za svakog klijenta. Zahtevi i odgovori se prenose preko Java objektnih tokova. `ClientRequestHandler` trenutno vraća poruku „Operacija jos nije implementirana.“; poziv kontrolera u tom toku još nije uključen. Postojanje klase sistemske operacije ne znači da je ona već dostupna kroz kontroler ili mrežu.

## Česti problemi

| Simptom | Provera |
| --- | --- |
| `No suitable driver found` | Proveri da je Connector/J JAR u runtime classpath-u serverskog projekta. ZIP sa JAR-om unutar njega nije dovoljan. |
| Greška povezivanja sa MySQL-om | Proveri JDBC adresu, dostupnost MySQL servera, port, korisnika i lozinku. |
| Tabela ili kolona ne postoji | Uporedi šemu baze sa nazivima u odgovarajućoj domenskoj klasi. |
| TCP server ne može da se pokrene | Proveri da li druga aplikacija ili ranije pokrenuta instanca zauzima port 3000. |
| Greška za Java source/target verziju | Uskladi instalirani JDK i podešavanja sva tri projekta. |

Dugme **TEST** ispisuje puni stack trace greške u konzolu i prekida dalju obradu ako poziv kontrolera ne uspe.
