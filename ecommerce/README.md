# E-Commerce - Test Pratico Euris IT

Implementazione del test pratico **Java Software Engineer** per la
realizzazione di un sistema E-Commerce.

L'applicativo gestisce clienti, prodotti e ordini, espone le
funzionalità tramite API REST e include gestione dello stock,
paginazione, stato degli ordini, cancellazione condizionata e gestione
della concorrenza tramite optimistic locking.

## Tecnologie

-   Java 21
-   Spring Boot 4.1.1
-   Spring MVC
-   Spring Data JPA
-   Spring Data REST
-   Spring Validation
-   Spring Retry/Resilience nativo di Spring Framework 7
-   Hibernate
-   H2 Database
-   Thymeleaf
-   Lombok
-   JUnit / Spring Boot Test / Mockito

## Architettura

L'applicazione segue una struttura a livelli:

``` text
REST Controller
      |
      v
   Service
      |
      v
  Repository
      |
      v
   H2 / JPA
```

Sono inoltre presenti:

-   DTO per separare il modello API dalle entità JPA;
-   Mapper per la conversione tra DTO ed entità;
-   `OrdineFactory` per centralizzare la costruzione degli ordini;
-   gestione centralizzata delle eccezioni tramite
    `@RestControllerAdvice`;
-   Bean Validation per la validazione delle richieste.

Le API REST personalizzate sono esposte sotto `/api/...`. I repository
Spring Data REST non vengono esposti direttamente, in modo da mantenere
il controllo sul contratto delle API.

## Struttura principale

``` text
src/
├── main/
│   ├── java/it/pierlorenzo/ecommerce/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── factory/
│   │   ├── mapper/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       ├── templates/
│       └── application.yaml
└── test/
    └── java/it/pierlorenzo/ecommerce/
```

## Modello di dominio

### Cliente

Un cliente è caratterizzato da:

-   nome
-   cognome
-   data di nascita
-   codice fiscale
-   email

### Prodotto

Un prodotto è caratterizzato da:

-   codice
-   nome
-   stock

L'entità `Prodotto` utilizza `@Version` per la gestione dell'optimistic
locking.

### Ordine

Un ordine appartiene a un cliente e contiene uno o più prodotti,
ciascuno associato alla relativa quantità tramite `ProdottoPerOrdine`.

Gli stati disponibili sono:

``` text
ORDINATO
CONSEGNATO
```

## API REST

### Clienti

#### Aggiunta cliente

``` http
POST /api/clienti
Content-Type: application/json
```

Esempio:

``` json
{
  "nome": "Mario",
  "cognome": "Rossi",
  "dataNascita": "1980-01-01",
  "codFiscale": "RSSMRA80A01H501Z",
  "email": "mario.rossi@example.com"
}
```

Risposta: `201 Created`.

#### Lista clienti

``` http
GET /api/clienti
```

Supporta la paginazione tramite `page` e `size`.

------------------------------------------------------------------------

### Prodotti

#### Aggiunta prodotto

``` http
POST /api/prodotti
Content-Type: application/json
```

Esempio:

``` json
{
  "codice": "P001",
  "nome": "Prodotto 1",
  "stock": 10
}
```

Risposta: `201 Created`.

#### Lista prodotti

``` http
GET /api/prodotti
```

Supporta la paginazione tramite `page` e `size`.

------------------------------------------------------------------------

### Ordini

#### Creazione ordine

``` http
POST /api/ordini
Content-Type: application/json
```

Esempio:

``` json
{
  "clienteId": 1,
  "prodotti": [
    {
      "prodottoId": 1,
      "quantita": 3
    },
    {
      "prodottoId": 2,
      "quantita": 2
    }
  ]
}
```

Risposta: `201 Created`.

Durante la creazione viene verificata la disponibilità di ogni prodotto.
Se lo stock non è sufficiente, l'ordine non viene creato.

Quando l'ordine viene creato correttamente, lo stock viene diminuito
della quantità ordinata.

#### Lista ordini

``` http
GET /api/ordini
```

Supporta la paginazione tramite `page` e `size`.

#### Aggiornamento dello stato

``` http
PUT /api/ordini/{id}/stato
Content-Type: application/json
```

Esempio:

``` json
{
  "status": "CONSEGNATO"
}
```

Un ordine `CONSEGNATO` non può essere riportato allo stato `ORDINATO`.

#### Cancellazione ordini

``` http
DELETE /api/ordini
Content-Type: application/json
```

Esempio:

``` json
{
  "ordineIds": [1, 2, 3]
}
```

Gli ordini `CONSEGNATO` non possono essere cancellati.

Quando un ordine viene cancellato, le quantità precedentemente riservate
vengono ripristinate nello stock dei prodotti.

La cancellazione multipla viene gestita in modo transazionale: se uno
degli ordini selezionati non può essere cancellato, l'operazione viene
rifiutata nel suo complesso.

## Paginazione

Le liste di clienti, prodotti e ordini utilizzano `Pageable` di Spring
Data.

La risposta viene esposta tramite un DTO comune:

``` json
{
  "content": [],
  "page": 0,
  "size": 10,
  "totalElements": 0,
  "totalPages": 0
}
```

## Gestione della concorrenza

La gestione degli ordini concorrenti è stata implementata tramite
**optimistic locking**.

L'entità `Prodotto` contiene:

``` java
@Version
private Long version;
```

Questo permette a JPA/Hibernate di rilevare modifiche concorrenti allo
stesso prodotto.

La creazione dell'ordine è inoltre transazionale:

``` java
@Transactional
```

In caso di conflitto di optimistic locking viene utilizzato il
meccanismo di retry di Spring:

``` java
@Retryable(
        includes = ObjectOptimisticLockingFailureException.class,
        maxRetries = 2,
        delay = 100
)
```

Il retry permette di rieseguire l'operazione e verificare nuovamente la
disponibilità dello stock.

Il comportamento è verificato da test dedicati:

-   test di ordini concorrenti, per verificare che lo stock disponibile
    non venga superato;
-   test di optimistic locking, per verificare il rilevamento effettivo
    del conflitto di versione.

## Validazione ed error handling

Le richieste REST utilizzano Jakarta Bean Validation.

Sono presenti, tra le altre:

-   `@NotBlank`
-   `@NotNull`
-   `@NotEmpty`
-   `@Positive`
-   `@PositiveOrZero`
-   `@Email`

Gli errori vengono gestiti centralmente tramite
`GlobalExceptionHandler`.

Esempi di risposte HTTP:

  Situazione                                          HTTP
  ------------------------------------ -------------------
  Creazione riuscita                         `201 Created`
  Risorsa non trovata                      `404 Not Found`
  Dati della richiesta non validi        `400 Bad Request`
  Stock insufficiente                       `409 Conflict`
  Ordine consegnato non cancellabile        `409 Conflict`
  Transizione di stato non valida           `409 Conflict`
  Cancellazione riuscita                  `204 No Content`

## Database

L'applicazione utilizza H2 in-memory.

Configurazione principale:

``` text
jdbc:h2:mem:ecommerce
```

Lo schema viene creato all'avvio e rimosso allo shutdown tramite
`create-drop`.

Di conseguenza, i dati inseriti non vengono mantenuti tra un'esecuzione
e l'altra.

### H2 Console

La console H2 è disponibile all'indirizzo:

``` text
http://localhost:8080/h2-console
```

Parametri:

``` text
JDBC URL: jdbc:h2:mem:ecommerce
User Name: sa
Password: [vuota]
```

## Frontend

È presente una semplice interfaccia web realizzata con Thymeleaf e
JavaScript per:

-   gestione clienti;
-   gestione prodotti;
-   creazione ordini;
-   visualizzazione e paginazione degli ordini;
-   selezione multipla e cancellazione degli ordini;
-   visualizzazione del dettaglio ordine;
-   aggiornamento dello stato dell'ordine.

Il frontend utilizza le API REST dell'applicazione.

## Test

I test comprendono:

### `OrdineServiceTest`

Verifica la logica applicativa relativa a:

-   creazione ordine;
-   cliente non trovato;
-   prodotto non trovato;
-   stock insufficiente;
-   aggiornamento dello stato;
-   transizione di stato non valida;
-   cancellazione ordine;
-   ripristino dello stock;
-   impossibilità di cancellare un ordine consegnato;
-   cancellazione multipla atomica.

### `OrdineRestControllerTest`

Verifica le API REST relative a:

-   creazione ordine;
-   validazione delle richieste;
-   errori `404`;
-   errore `409` per stock insufficiente;
-   aggiornamento stato;
-   transizioni di stato non valide;
-   cancellazione;
-   paginazione.

### `OrdineConcurrencyTest`

Verifica la gestione della concorrenza:

-   due ordini contemporanei sullo stesso prodotto non possono superare
    lo stock disponibile;
-   il conflitto di optimistic locking viene effettivamente rilevato
    tramite `@Version`.

### `EcommerceApplicationTests`

Verifica il caricamento del contesto Spring.

## Avvio dell'applicazione

### Prerequisiti

-   JDK 21
-   Maven oppure Maven Wrapper

### Linux / macOS

``` bash
./mvnw spring-boot:run
```

### Windows

``` cmd
mvnw.cmd spring-boot:run
```

Una volta avviata, l'applicazione è disponibile su:

``` text
http://localhost:8080
```

## Esecuzione dei test

### Linux / macOS

``` bash
./mvnw test
```

### Windows

``` cmd
mvnw.cmd test
```

## Interfaccia web

Dalla home dell'applicazione è possibile accedere alle sezioni:

``` text
/clienti
/prodotti
/ordini
```

Le operazioni eseguite dall'interfaccia utilizzano le API REST
dell'applicazione.

## Corrispondenza con il test pratico

  Requisito                         Implementazione
  --------------------------------- -------------------------------
  Aggiunta cliente                  `POST /api/clienti`
  Lista clienti                     `GET /api/clienti`
  Aggiunta prodotto                 `POST /api/prodotti`
  Lista prodotti                    `GET /api/prodotti`
  Creazione ordine                  `POST /api/ordini`
  Lista ordini                      `GET /api/ordini`
  Controllo disponibilità stock     `OrdineService`
  Aggiornamento stock               `OrdineService` + transazione
  Paginazione                       `Pageable` + `PageResponse`
  Concorrenza                       `@Version` + retry
  Database                          H2
  Stato ordine                      `StatoOrdineEnum`
  Aggiornamento stato               `PUT /api/ordini/{id}/stato`
  Cancellazione ordine              `DELETE /api/ordini`
  Blocco cancellazione consegnati   `OrdineConsegnatoException`

## Note progettuali

L'implementazione mantiene separati il modello di persistenza e il
contratto delle API tramite DTO e Mapper.

La logica relativa alla creazione dell'ordine è mantenuta nel service
layer ed è resa transazionale perché modifica contemporaneamente più
risorse correlate: ordine e stock dei prodotti.

La gestione della concorrenza è stata implementata per soddisfare il
requisito relativo agli ordini contemporanei, utilizzando optimistic
locking invece di un lock pessimista sul database.
