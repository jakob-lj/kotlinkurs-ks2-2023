# Kotlinkurs 2023

Velkommen til kotlinkurs! I da skal du få utvikle en applikasjon for å delete inn
biler slik vi har gjort for å organisere KS2.

Vi står ovenfor et stort problem; vi må fordele sjåfører og passasjerer
i biler, men vi finner ikke noe godt system for å organisere bilene med
god struktur. I tillegg er det MYE jobb å estimere kostnader samt legge til
sjåfører hos HYRE.

Derfor trenger vi din hjelp til å utvikle et system som hjelper oss å gjøre nettopp dette!

## Om utviklingen

Siden dette er et kotlinkurs har vi forsøkt å skrive kurset så framework-uavhengig som mulig.
Det betyr at alt som ligger i [bff](src/main/kotlin/no/liflig/ks2kurs/bff)-pakken er forhåndsutfylt og ikke
trenger å endres.
Det som ligger i [common](src/main/kotlin/no/liflig/ks2kurs/common) er klasser vi bruker for å sette opp appen, og litt
div
nice to have.

Din oppgave er å fullføre service layer-utviklingen i appen. Dette gjøres
i [services](src/main/kotlin/no/liflig/ks2kurs/services)-pakken.

For å gjøre oppgaven litt lettere har vi skrevet en del TODOs i koden der vi tror det kan være nyttig å skrive slik
kode. Men vi har
også laget stegvis oppgaver her dersom du ønsker det.

Dersom du vil teste om du har implementert koden riktig kan du
kjøre [CreateCarAndDriverTest](src/test/kotlin/no/liflig/ks2kurs/bff/CreateCarAndDriverTest.kt)

## Løsningsforslag

Er du helt blank? Sjekk ut branchen [solution](/jakob-lj/koltinkurs-ks2-2023/tree/solution) for å se hvordan vi har
implementert dette.

# Oppgaver!

# Step 1: Utvid Car og fiks immutability issuet (Car.kt)

- [ ] Done

Fiks imutability issuet med Car slik at regNr ikke kan endres på et Car-object

Utvide Car-klassen til å ha:

- [ ] Passasjer kapasitet
- [ ] en liste over sjåfører (PersonId)
- [ ] en listen med passasjerer (PersonId)
- [ ] biltype (CarType)
- [ ] lag en getter for å finne antall ledige plasser igjen i bilen

# Step 2 (Car Service):

- [ ] Done

Utvid Car service til å oppfylle forretningskravene:

## 2.1 Crate car

- [ ] Sjekk at bilen du ønsker å registrere ikke finnes fra før
- [ ] Persister bilen med Car Repository

## 2.2 Edit

- [ ] Fjern !! etter man har gettet-en car og håndter feilen så bruker får rett feilmelding
- [ ] Sjekk at det ikke er mulig å endre regnummer til (en annen) bil som ikke finnes allerede
- [ ] persister endringer på en car

Vi kommer tilbake til resten av metodene i Car service etter vi har implementert ferdig Person-konseptet siden Car er
dependent på Person for å legge til disse funksjonene.

Det skal nå være mulig å kjøre [CarTest](src/test/kotlin/no/liflig/ks2kurs/bff/CarTest.kt).

# Step 3: Utvide Person klassen (Person.kt)

- [ ] Done

- [ ] Utvide Person til å ha firstname og lastname i stedet for kun name. (Vi skal ikke endre api-et så
  CreateOrEditPersonRequest skal fremdeles sende inn bare name. Dette skal vi håndtere i Person service i step 4) La
  firstName og lastName begge være name frem til du kommer til Step 4 og skal håndtere dette
- [ ] Lag en getter for full name
- [ ] Utvide Person til å ha birthDay (LocalDate)

# Step 4: Implementert en service for Person (PersonService.kt)

- [ ] Done

Vi trenger å implementere en service for å håndtere det å opprette og gjøre endringer på personer

## Step 4.1: Create person

- [ ] Skriv en extention function som henter fornavn (alle navn som ikke er sist) fra en String
- [ ] Skriv en extention function som henter etternavn (siste navn) fra en String
- [ ] Brukt extension functions over for å persistere fornavn og etternavn riktig
- [ ] Persister personen til databasen

## Step 4.2: Edit person

- [ ] Sjekk om personen finnes og throw PersonError med riktig PersonErrorCode hvis ikke
- [ ] Persister endringene i databasen med personRepository

## Step 4.3 getByFilter

For å ikke liste aller persons, har vi laget en "getByFilter"-metode som sender inn et PersonServiceListFilter-object
man kan bruke til å filtrere.

- [ ] Hent alle personer fra basen
- [ ] Bruk kotlin sin innebygde collection-støtte for å mappe fra VersionedEntity til Person
- [ ] Bruk kotlin sin innebygde collection-støttet til å filtere bort de personene man ikke ønsker

Merk!! I en "vanlig" applikasjon vil man la basen gjøre mest mulig av denne filtreringen, vi unngår dette her så dere
skal få jobbe med kotlin.

# Step 5: Vi utvider Car aggregatet til å kunne legge til sjåfører (Car.kt)

- [ ] Done

En ting er sikkert: vi trenger å gjøre endringer på Car-objekter. Merk: Her jobber vi kun med Person-id-er. Å validere
om en person har rettigheter til å bli lagt til som sjåfør (har gyldig førerkort) er en service layer constraint og skal
legges til i step 6.
For å gjøre disse endringene så lett som mulig ønsker vi at entiteten "Car" eier endringer slik at vi bare kaller
endringer på entiteten selv når vi ønsker å gjøre disse.
Implementert derfor funksjoner i Car som følger følgende forretningsbehov med tilhørende regler:

## Step 5.1: Legge til sjåfører

- [ ] Lag en metode for å legge til en referanse til en driver (PersonId) i listen over drivers
- [ ] Det må være et ledig sete i bilen
- [ ] Driveren kan ikke være passenger fra før
- [ ] Driveren kan ikke være driver fra før

## Step 5.2: Legge til passasjerer

- [ ] Lag en metode for å legge til en referanse til en passasjer (PersonId) i listen i listen over passasjerer
- [ ] Det må være et ledig sete i bilen
- [ ] Passasjeren kan ikke være passenger fra før
- [ ] Passasjeren kan ikke være driver fra før

# Step 6: Vi trenger å gjøre ferdig funksjoner i car servicen vår (CarService.kt)

- [ ] Done

## Step 6.1 addDriver

Utvid metoden til å følge følgende forretningsbehov

- [ ] Car må finnes
- [ ] Person må finnes
- [ ] Person må ha gyldig sertifikat
- [ ] Person må ha fylt mist 23 år

## Step 6.2 addPassenger

Utvid metoden til å følge følgende forretningsbehov

- [ ] Car må finnes
- [ ] Person må finnes
- [ ] Gjør endringer på Car-objectet og persister disse

## Step 6.3 remove passenger

Implementer metode for å fjerne passasjer fra Car og deretter oppdatere persisteringen av Car-objectet

## Step 6.4 remove driver

Implementer metoden for å fjerne driver fra Car og deretter oppdater persisteringen av Car-objectet

## Getting started

### Tool dependencies

You need to install:

- Docker
- docker-compose
- Maven (or run maven through IntelliJ)
- JDK 17
  - `brew tap homebrew/cask-versions` and then`brew install --cask temurin17`

### Running the application

1. Run the app

- Start `docker-compose`:
   ```shell
   docker-compose -f docker-compose.yml up -d 
   ```

2. run Main.kt

You can test the API with [src/test/http/health.http](src/test/http/health.http)

### Running tests

```shell
mvn verify
```

Add `-DskipTests` to `mvn` to disable all tests.  
Add `-DskipITs` to only disable integration tests.

### Linting

Only lint: `mvn ktlint:check`

`mvn ktlint:ktlint` to create a report in `target/site/ktlint.html`.

Fix: `mvn ktlint:format`

