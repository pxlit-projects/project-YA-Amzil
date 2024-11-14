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
- **Berichten verzenden**: Een service (bijvoorbeeld de Review Service) stuurt een bericht naar een wachtrij wanneer een artikel is goedgekeurd of afgewezen.
- **Berichten ontvangen**: Een andere service (bijvoorbeeld de Post Service of Comment Service) luistert naar berichten en handelt ze af wanneer ze binnenkomen.

---

## Microservices en Communicatie met User Stories

### PostService en ReviewService (Synchrone Communicatie via OpenFeign)
- **US1 - US3 (PostService)**: Een redacteur kan artikelen aanmaken, opslaan als concept en bewerken. Wanneer een artikel klaar is, kan het via een directe aanroep naar de **ReviewService** ter goedkeuring worden aangeboden.
- **US7 (ReviewService)**: De hoofdredacteur kan ingediende artikelen goedkeuren of afwijzen en stuurt via een synchrone call een statusupdate naar de **PostService**. Dit zorgt ervoor dat de status van het artikel meteen wordt bijgewerkt.

### CommentService (Synchrone Communicatie via OpenFeign)
- **US10 - US12**: Een medewerker kan reacties plaatsen, lezen, bewerken of verwijderen bij artikelen. Dit vereist een directe call naar de **PostService** om te controleren of het artikel bestaat en om de reacties te beheren.

### Messaging Service (Asynchrone Communicatie via RabbitMQ)
- **US8 - US9 (ReviewService)**: Wanneer een artikel wordt goedgekeurd of afgewezen, stuurt de **ReviewService** een asynchrone melding via RabbitMQ naar redacteuren. Eventuele opmerkingen bij afwijzingen worden ook asynchroon verwerkt, zodat redacteuren op de hoogte worden gesteld zonder vertragingen.
- **US10 - US12 (CommentService)**: Naast het plaatsen van reacties kan de **CommentService** meldingen asynchroon verzenden naar andere gebruikers wanneer er nieuwe reacties zijn of als reacties worden bewerkt/verwijderd. Dit zorgt ervoor dat gebruikers meldingen ontvangen zonder directe interactie.

<!-- :heavy_check_mark:_(COMMENT) Add a description of the architecture of your application and create a diagram like the one below. Link to the diagram in this document._

![eShopOnContainers Architecture](https://docs.microsoft.com/en-us/dotnet/architecture/cloud-native/media/eshoponcontainers-development-architecture.png)

[Source](https://docs.microsoft.com/en-us/dotnet/architecture/cloud-native/introduce-eshoponcontainers-reference-app) -->