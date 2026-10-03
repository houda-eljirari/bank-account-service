# Bank Account Service

Micro-service de gestion de comptes bancaires développé avec **Spring Boot**.

Le projet expose la même ressource, le compte bancaire, de trois façons différentes :

- une **API REST** classique, construite avec un contrôleur, une couche service, des DTOs et des mappers ;
- une **API Spring Data REST**, générée automatiquement à partir du repository, avec des projections ;
- une **API GraphQL**.

Il est documenté avec **Swagger (OpenAPI)** et utilise une base **H2** en mémoire.

---

## Technologies utilisées

| Technologie | Rôle |
|---|---|
| Java 17+ | Langage |
| Spring Boot | Framework principal |
| Spring Web | Contrôleurs REST |
| Spring Data JPA | Accès aux données (DAO) |
| Spring Data REST | API REST générée automatiquement |
| H2 Database | Base de données en mémoire |
| Lombok | Réduction du code répétitif (getters, setters, builder) |
| SpringDoc OpenAPI | Documentation Swagger |
| Spring for GraphQL | API GraphQL |
| Maven | Gestion du projet et des dépendances |

> Spring Boot 4 nécessite SpringDoc en version **3.x**. La version 2.x, prévue pour Spring Boot 3, provoque une erreur au démarrage.

---

## Architecture

Le projet suit une architecture en couches. Chaque couche ne dialogue qu'avec celle qui est juste en dessous.

```
Client (Postman, navigateur, GraphiQL)
        │
        ▼
 ┌─────────────────────────────┐
 │  Web : REST / GraphQL        │  Reçoit les requêtes, renvoie des DTOs
 └─────────────────────────────┘
        │
        ▼
 ┌─────────────────────────────┐
 │  Service (métier)            │  Logique métier, utilise les mappers
 └─────────────────────────────┘
        │
        ▼
 ┌─────────────────────────────┐
 │  Repository (DAO)            │  Accès à la base via Spring Data JPA
 └─────────────────────────────┘
        │
        ▼
     Base H2
```

---

## Modèle de données

L'entité `BankAccount` représente un compte bancaire.

| Attribut | Type | Description |
|---|---|---|
| `id` | `String` (UUID) | Identifiant généré automatiquement |
| `createdAt` | `LocalDate` | Date de création |
| `balance` | `Double` | Solde |
| `currency` | `String` | Devise (MAD, EUR, USD...) |
| `type` | `AccountType` | `CURRENT_ACCOUNT` (courant) ou `SAVING_ACCOUNT` (épargne) |

---

## Explication des étapes

### 1. Création du projet

Le projet est généré avec Spring Initializr (Maven, Java 17+). Les dépendances de départ sont Spring Web, Spring Data JPA, H2 et Lombok. Spring Data REST, SpringDoc OpenAPI et Spring GraphQL sont ajoutées au fur et à mesure.

### 2. Entité JPA

`BankAccount` est annotée avec `@Entity`. Lombok génère les getters, les setters, les constructeurs et le builder (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@Builder`). L'identifiant est un UUID généré automatiquement.

### 3. Repository

`BankAccountRepository` étend `JpaRepository<BankAccount, String>`. Spring Data fournit sans code les opérations de base (`save`, `findAll`, `findById`, `deleteById`). La méthode `findByType` est dérivée du nom de la méthode.

### 4. Test de la couche DAO

Deux approches sont utilisées :

- un `CommandLineRunner` déclaré en `@Bean` qui insère quatre comptes de test au démarrage et les affiche dans la console ;
- des tests unitaires avec `@DataJpaTest` qui vérifient l'enregistrement, la recherche par type et la suppression.

La base peut aussi être consultée dans la console H2.

### 5. Web service RESTful

Un `@RestController` exposé sous `/api` fournit les opérations CRUD :

| Méthode | URL | Description |
|---|---|---|
| GET | `/api/bankAccounts` | Liste des comptes |
| GET | `/api/bankAccounts/{id}` | Consulter un compte |
| POST | `/api/bankAccounts` | Créer un compte |
| PUT | `/api/bankAccounts/{id}` | Modifier un compte |
| DELETE | `/api/bankAccounts/{id}` | Supprimer un compte |

### 6. Test avec Postman

Chaque opération est testée avec Postman. Exemple de corps de requête pour la création :

```json
{
  "balance": 7500,
  "currency": "MAD",
  "type": "SAVING_ACCOUNT"
}
```

### 7. Documentation Swagger

SpringDoc génère automatiquement la documentation OpenAPI à partir des contrôleurs. L'interface Swagger UI permet de lire la description de chaque endpoint et de l'exécuter directement depuis le navigateur.

### 8. Spring Data REST et projections

L'annotation `@RepositoryRestResource` sur le repository suffit à exposer une API REST complète, sans écrire de contrôleur. Elle est configurée sous le préfixe `/rest` pour ne pas entrer en conflit avec `/api`.

Les **projections** permettent de choisir les champs renvoyés :

| Projection | Champs | URL |
|---|---|---|
| `mobile` | id, balance | `/rest/bankAccounts?projection=mobile` |
| `web` | id, balance, currency, type | `/rest/bankAccounts?projection=web` |

La recherche par type est également disponible : `/rest/bankAccounts/search/byType?t=SAVING_ACCOUNT`.

### 9. DTOs et Mappers

L'entité JPA n'est plus exposée directement par l'API :

- `BankAccountRequestDTO` contient uniquement les champs que le client peut envoyer ;
- `BankAccountResponseDTO` contient les champs renvoyés au client ;
- `AccountMapper` convertit entre l'entité et les DTOs.

Ce découpage protège le modèle interne et permet de faire évoluer l'API indépendamment de la base de données.

### 10. Couche Service

`AccountService` définit les opérations métier et `AccountServiceImpl` les réalise : ajout (avec la date de création), modification partielle, consultation, liste et suppression. Le contrôleur REST ne dépend plus que du service.

### 11. Web service GraphQL

Le schéma est défini dans `src/main/resources/graphql/schema.graphqls`.

| Type | Nom | Description |
|---|---|---|
| Query | `accountsList` | Liste des comptes |
| Query | `bankAccountById` | Compte par identifiant |
| Mutation | `addAccount` | Création |
| Mutation | `updateAccount` | Modification |
| Mutation | `deleteAccount` | Suppression |

Exemple de requête :

```graphql
query {
  accountsList {
    id
    balance
    currency
    type
  }
}
```

Exemple de mutation :

```graphql
mutation {
  addAccount(bankAccount: { balance: 4000, currency: "MAD", type: "SAVING_ACCOUNT" }) {
    id
    createdAt
    balance
  }
}
```

---

## Lancer le projet

### Prérequis

- JDK 17 ou supérieur
- Maven (ou le wrapper `mvnw` fourni)

### Démarrage

```bash
./mvnw spring-boot:run
```

Sous Windows : `mvnw spring-boot:run`. L'application démarre sur http://localhost:8082.

Au démarrage, quatre comptes de test sont créés et affichés dans la console.

### Lancer les tests

```bash
./mvnw test
```

---

## Points d'accès

| Usage | URL |
|---|---|
| API REST | http://localhost:8082/api/bankAccounts |
| Swagger UI | http://localhost:8082/swagger-ui.html |
| Spécification OpenAPI (JSON) | http://localhost:8082/v3/api-docs |
| Spring Data REST | http://localhost:8082/rest/bankAccounts |
| GraphiQL | http://localhost:8082/graphiql |
| Endpoint GraphQL | http://localhost:8082/graphql |
| Console H2 | http://localhost:8082/h2-console |

Paramètres de la console H2 : JDBC URL `jdbc:h2:mem:account-db`, utilisateur `sa`, mot de passe vide.

La base étant en mémoire, les données sont recréées à chaque démarrage, avec de nouveaux identifiants.

---

