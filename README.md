# URL Service

## Overview

This service is used to handle URL information, so far gonna contains only some functionality like shortening.

## Features

- **Shorten URL**: Converts a long URL into a shortened version.
- **Retrieve Original URL**: Retrieves the original URL from the shortened version.

## Technologies Used

- **Java 21**
- **Spring Boot 3.3.4**
- **Lombok**
- **JUnit 5**

## Architecture

This project follows a layered architecture, contains the following packages

- **Controller**: Handles HTTP requests and responses.
- **Mapper**: Maps all incoming and outgoing entities.
- **DTO**: Contains all the exposed entities which will be received or returned from Controller.
- **Validator**: Handles all the logic related to requests and constraint validation.
- **Service**: Contains the business logic for shortening and retrieving URLs.
- **Repository**: Contains all the repositories which are used to retrieve information from database or in-memory
  storage.
- **Model**: Represents core models of our service.

## Endpoints

### 1. Shorten URL

- **Endpoint**: `/url`
- **Method**: `POST`
- **Description**: This endpoint receives and a object with a longUrl and returns an object which contains shortUrl which should be used to retrieve the original URL
- **Request Body**:

```json
{
  "longUrl": "https://www.google.es"
}
 ```

- **Response**:

```json
{
  "shortUrl": "http://localhost:8080/url/d3d3Lmdv"
}
```

### 2. Retrieve original URL

- **Endpoint**: `/url/{encodedUrl}`
- **Method**: `GET`
- **Description**: This endpoint receives the encodedUrl as a path variable and returns an object containing longUrl
- **Response**:

```json
{
  "longUrl": "https://www.google.es"
}
```

**Example**:

- Generating shortUrl

```bash
curl -X POST -H "Content-Type: application/json" -d '{"longUrl": "https://www.google.es"}' http://localhost:8080/url
```

Response: `{"shortUrl":"aHR0cHM6"}`

- Retrieving original URL

```bash
curl -X GET http://localhost:8080/url/aHR0cHM6
```

Response: `{"longUrl":"https://www.google.es"}`

## Setup Instructions

### Prerequisites

- JDK 21+
- Maven 3.6+
- IDE (IntelliJ, Eclipse, etc.)

### Running the Application

1. Clone the repository:

```bash
git clone git@github.com:awaisiqbal/flip-url-service.git
cd flip-url-service
```

2. Build and run the application:

```bash
gradle build
gradle bootRun
```

## Trade-offs and Improvements

### Trade-offs

- The URL shortening algorithm used is a simple Base64 encoding with length of 8, which can lead to hash collision. In
  production, we would likely use a more robust, collision-resistant algorithm (e.g., hashing + ID generation).
- The service uses memory storage using a ConcurrentHashMap for simplicity. In a production environment, an external
  persistent database would be required (e.g., Redis, MongoDB, PostgreSQL).
- For simplicity this solution uses a Map<String, String> where we should use a entity which contains an ID, an
  depending on the database we should add a Index to shortUrl to make it more efficient.
- Due to the simplicity of this project I went to service layer architecture, the other architecture to use could be
  Hexagonal, where we gonna have a adapter to store and retrieve URL from database, and we gonna have 2 use cases, the
  application configuration like spring will be contained just inside `application` . Then the organization will be
  like:

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── awais/
│   │           └── urlservice/
│   │               ├── application/
│   │               │   ├── controller/
│   │               │   └── config/
│   │               ├── domain/
│   │               │   ├── model/
│   │               │   ├── port/
│   │               │   └── service/
│   │               └── infrastructure/
│   │                   └── repository/
├── resources/
│   ├── application.properties
│   └── logback.xml
└── test/
    └── java/
```

### Future Improvements

- **Scalability**: Implement a more efficient and scalable repository to store and retrieve stored urls (such as a
  distributed system using a key-value store like Redis).
- **Error handling**: Currently we are not mapping the error response to any HTTP response, just throwing exception
  which is
  converted to 500.
- **Error handling**: We need to have a standard error response, containing the error code, error message.
- **Custom Short URLs**: Allow users to specify their own short url length
- **Analytics**: Add click tracking and analytics to monitor how often shortened URLs are accessed, currently the code
  is
  ready to add more information into URL entity, the only thing to change will be the repository to handle
- **Security**: Implement rate limiting and user authentication.
- **Caching**: Use a caching layer to reduce database load for frequent lookups of popular URLs.
- **Validation**: Need to improve and validate if the user provided url is valid or not.
