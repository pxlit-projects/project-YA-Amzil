# Architecture

![ContentMangementSystem](Project%20Blogpost.png)

# Microservices Architectuur en Communicatie

## 1. Config Service
De Config Service fungeert als een gecentraliseerd punt waar alle configuratiebestanden van de verschillende microservices beheerd worden. Dit zorgt ervoor dat de instellingen, zoals database-URL's, poortnummers, API-sleutels en andere configuratievariabelen, gecentraliseerd zijn en gemakkelijk kunnen worden aangepast. Hierdoor hoef je de configuratie-instellingen niet afzonderlijk per service bij te werken, wat helpt om consistentie te waarborgen en configuratiebeheer te vereenvoudigen.

### Toepassing:
- De **Post Service**, **Review Service** en **Comment Service** maken gebruik van de Config Service voor het laden van hun configuraties.

---

## 2. Discovery Service
De Discovery Service zorgt ervoor dat microservices zich kunnen registreren en elkaar kunnen vinden in het netwerk zonder vooraf de exacte locaties (zoals IP-adressen) van andere services te hoeven kennen. Dit vergemakkelijkt de communicatie tussen microservices en maakt dynamische schaling mogelijk. Netflix Eureka wordt gebruikt als implementatie van de Discovery Service.

### Functies:
- **Registratie**: Wanneer een microservice opstart, registreert deze zich bij de Discovery Service met zijn naam en locatie (meestal een URL).
- **Zoeken**: Microservices die een andere service nodig hebben, kunnen via de Discovery Service zoeken naar de geregistreerde diensten en verbinding maken met de juiste instantie.

---

## 3. Gateway
De Gateway dient als het centrale toegangspunt voor alle client-aanvragen. In plaats van dat clients rechtstreeks communiceren met individuele microservices, wordt al het verkeer via de Gateway geleid. De Gateway stuurt verzoeken door naar de juiste microservices op basis van de configuraties en routingregels.

### Functies:
- **Verzoekroutering**: De Gateway analyseert inkomende requests en stuurt ze door naar de juiste microservice, zoals de Post Service, Review Service of Comment Service.
- **Veiligheid**: Beveiligingstaken, zoals authenticatie en autorisatie, kunnen op de Gateway worden uitgevoerd.
- **API-aggregatie**: De Gateway kan meerdere microservices aanroepen om een enkele reactie samen te stellen voor de client.
- **Load balancing**: De Gateway kan requests verdelen over meerdere instanties van een microservice.

---

## 4. Messaging Service (RabbitMQ)  
De Messaging Service maakt gebruik van RabbitMQ om asynchrone berichten te verzenden tussen microservices. Dit betekent dat een service berichten kan versturen die door een andere service worden opgepakt, zonder dat beide services op hetzelfde moment actief hoeven te zijn. Dit is handig voor het afhandelen van gebeurtenissen of meldingen, zoals goedkeuringen, afwijzingen of gebruikersmeldingen.  

### Functies:  
- **Berichten verzenden (Producer)**:  
  Een service, zoals de **ReviewService**, fungeert als producer door berichten naar RabbitMQ te sturen. Bijvoorbeeld:  
  - Wanneer een artikel wordt goedgekeurd of afgewezen, stuurt de ReviewService een bericht met de status van het artikel naar een specifieke wachtrij in RabbitMQ.  
  - Dit bericht bevat gegevens zoals de ID van de post, de status (bijv. "goedgekeurd" of "afgewezen") en eventuele opmerkingen.  

- **Berichten ontvangen (Consumer)**:  
  Een andere service, zoals de **PostService**, fungeert als consumer door te luisteren naar berichten in de wachtrij. Bijvoorbeeld:  
  - De PostService ontvangt berichten over wijzigingen in de status van artikelen.  
  - Bij ontvangst van een bericht werkt de PostService de status van het artikel bij in de database.  
  - De consumer haalt het bericht uit de wachtrij, verwerkt het, en bevestigt vervolgens de succesvolle afhandeling aan RabbitMQ.  

### Voordelen van deze aanpak:  
- **Decoupling**: De producer (ReviewService) en de consumer (PostService) zijn niet direct afhankelijk van elkaar. Dit maakt het systeem robuuster en schaalbaarder.  
- **Betrouwbaarheid**: RabbitMQ zorgt ervoor dat berichten niet verloren gaan, zelfs als een service tijdelijk niet beschikbaar is.  
- **Asynchrone verwerking**: Services kunnen onafhankelijk en op verschillende tijdstippen werken, wat leidt tot betere prestaties en schaalbaarheid.  

---

## 5. OpenFeign
**OpenFeign** is een hulpmiddel dat helpt om eenvoudig te communiceren tussen verschillende microservices. Het maakt het gemakkelijker om HTTP-verzoeken te sturen zonder veel ingewikkelde code te schrijven.

### Functies:
- **Eenvoudige API-aanroepen**: Je hoeft geen gedetailleerde HTTP-code te schrijven, alleen de interface.
 - **Ondersteunt HTTP-methoden**: Werkt met GET, POST, PUT, DELETE, enz.

---

# Microservices en Communicatie met User Stories  

## PostService  
- **US1 - US3 (PostService)**:  
  Als redacteur wil ik posts kunnen aanmaken, opslaan als concept en bewerken, zodat ik nieuws en updates kan delen, later kan bijwerken of fouten kan corrigeren.  
- **US4 - US5 (PostService)**:  
  Als gebruiker wil ik een overzicht van gepubliceerde posts kunnen zien en deze kunnen filteren op inhoud, auteur en datum, zodat ik eenvoudig op de hoogte blijf van het laatste nieuws.  

## PostService, ReviewService en CommentService (Synchrone Communicatie via OpenFeign)  
- **(PostService - ReviewService - CommentService)**:  
  Als redacteur wil ik dat het verwijderen van een post automatisch alle gekoppelde reacties en reviews verwijdert.  

## ReviewService  
- **US6 - US8 (ReviewService - PostService - NotificationService)**:  
  Als redacteur wil ik ingediende posts kunnen bekijken, goedkeuren of afwijzen met opmerkingen, en meldingen ontvangen, zodat alleen goedgekeurde content wordt gepubliceerd en afwijzingen duidelijk zijn.  

## ReviewService en NotificationService (Synchrone Communicatie via OpenFeign)  
- **US7 (ReviewService - NotificationService)**:  
  Als redacteur wil ik een melding ontvangen wanneer een post goedgekeurd of afgewezen is, zodat ik weet of het gepubliceerd kan worden of moet worden herzien.  
  - Zodra een redacteur een post goedkeurt of afwijst, stuurt de **NotificationService** een melding via een gekoppelde mailservice. Zo ontvangt de redacteur een e-mailnotificatie over de status van de post.  

## ReviewService en PostService (Asynchrone Communicatie via RabbitMQ)  
- **US7 (ReviewService - PostService)**:  
  Als redacteur wil ik een melding ontvangen wanneer een post goedgekeurd of afgewezen is, zodat ik weet of het gepubliceerd kan worden of moet worden herzien.  
  - Wanneer een artikel wordt gepubliceerd of herzien, stuurt de **ReviewService** een asynchrone boodschap via RabbitMQ naar de **PostService**, die de status van de post bijwerkt met behulp van een producer en een consumer.  

## CommentService  
- **US9 - US11 (CommentService)**:  
  Als gebruiker wil ik reacties kunnen plaatsen, lezen, bewerken of verwijderen op posts, zodat ik mijn mening kan delen, vragen kan stellen, inzicht krijg in reacties van collega’s en mijn eigen bijdragen kan corrigeren of verwijderen.  

<!-- :heavy_check_mark:_(COMMENT) Add a description of the architecture of your application and create a diagram like the one below. Link to the diagram in this document._

![eShopOnContainers Architecture](https://docs.microsoft.com/en-us/dotnet/architecture/cloud-native/media/eshoponcontainers-development-architecture.png)

[Source](https://docs.microsoft.com/en-us/dotnet/architecture/cloud-native/introduce-eshoponcontainers-reference-app) -->