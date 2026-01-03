Abysalto – Java Junior Tehnički Zadatak

Ovo je rješenje tehničkog zadatka za Junior Java Developer poziciju.
Aplikacija predstavlja jednostavan sustav za upravljanje narudžbama restorana, implementiran unutar postojećeg Spring Boot kostura.

Aplikacija omogućuje:
 - dodavanje novih narudžbi
 - pregled svih postojećih narudžbi
 - promjenu statusa narudžbe
 - automatski izračun ukupnog iznosa narudžbe
 - sortiranje narudžbi prema ukupnom iznosu

Tehnologije:
 - Java 17+
 - Spring Boot
 - Spring Data JDBC
 - Spring Security (Basic Auth)
 - H2 in-memory baza podataka
 - Swagger / OpenAPI
 - Maven

Pokretanje aplikacije:

Preduvjeti:
 - Java 17+
 - Maven

Koraci:
1. U terminalu unijeti: mvn spring-boot:run ili pokrenuti iz Spring Boot Dashboarda
2. Otići na poveznicu http://localhost:8080/ping/pong u browseru radi testiranja
3. Unijeti javne korisničke podatke za testiranje:
   - username: user
   - password: password
4. Otići na poveznicu http://localhost:8080/swagger-ui/index.html za pokretanje Swagger UI-a
5. Inicijalizirati podatke POST /init-data/ - Bez ovog koraka orders endpointi neće raditi ispravno.

Nakon izvršavanja ovih koraka sve funkcionalnosti aplikacije bi trebale biti dostupne.

Napomena:

Projekt koristi H2 in-memory bazu što znači da se podaci brišu pri ponovnom pokretanju aplikacije pa je svaki put potrebno pozvati:

POST /init-data/

Create order:

POST /orders

Request body (primjer):
```json
{
  "order": {
    "buyerId": 1,
    "orderStatus": "WAITING_FOR_CONFIRMATION",
    "orderTime": "2026-01-03T16:30:00",
    "paymentOption": "CASH",
    "deliveryAddressId": 1,
    "contactNumber": "091000001",
    "currency": "EUR",
    "note": "Sa kukuruzom"
  },
  "items": [
    {
      "name": "Piletina u tijestu",
      "quantity": 3,
      "price": 4.50
    },
    {
      "name": "Cockta",
      "quantity": 2,
      "price": 2.0
    }
  ]
}
```

Napomene:

Narudžbe nisu unaprijed određene pa je potrebno odraditi POST metodu za stvaranje narudžbe prije dohvaćanja ili sortiranja.
Izračun ukupnog iznosa (totalPrice) se odvija automatski u backendu.

Get all orders

GET /orders

Update order status

PATCH /orders/{id}/status
```json
{
  "status": "PREPARING"
}
```

Mogući statusi:
 - WAITING_FOR_CONFIRMATION
 - PREPARING
 - DONE

Moguće opcije plaćanja:
 - CASH
 - CARD_UPFRONT
 - CARD_ON_DELIVERY

Sort orders by total price:

GET /orders/sorted?descending=true

GET /orders/sorted?descending=false

Arhitektura:

DatabaseInitializer – inicijalizacija baze

Manager - poslovna logika

Controller – HTTP logika

DTO – prijenos podataka između slojeva aplikacije (baze podataka i korisničkog sučelja)

Mapper – pretvaranje entiteta baze podataka u dto

Repository – pristup bazi podataka

Primijenjeni principi:

OOP

DRY

KISS

YAGNI

Autor:

Rješenje je implementirano kao dio tehničkog zadatka za Abysalto.
