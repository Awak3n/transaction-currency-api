# Transaction Currency API

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

This project is an API built using **Java, Java Spring, PostgreSQL as the database.** 

The Unit tests was built using **JUnit and Mockito.**

## Table of Contents

- [Installation](#installation)
- [API Endpoints](#api-endpoints)
- [Database](#database)

## Installation

1. Clone the repository:

```bash
git clone https://github.com/Awak3n/transaction-currency-api.git
```

2. Start the application with Docker:

```bash
docker compose up
```

3. Open the Swagger in your browser:

```
http://localhost:8080/swagger-ui/index.html#/
```

## API Endpoints
The API provides the following endpoints:

**POST TRANSACTION**
```markdown
POST /transaction - Register a new transaction into the App
```
```json
{
    "description": "This is a description",
    "transactionDate": "2020-02-20",
    "purchaseAmount": "19.10"
}
```

**GET TRANSACTION**
```markdown
GET /transaction/{id}/currency/{currency} - Retrieve a transaction with a new amount coverted by the currency
```

## Database

The project utilizes [PostgreSQL](https://www.postgresql.org/about/) as the main database. 

The project utilizes [H2 Database](https://www.h2database.com/html/tutorial.html) for testing. 






