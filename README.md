# Football Leagues

**Football Leagues** to aplikacja webowa typu RESTful stworzona w języku Java z wykorzystaniem frameworka Spring Boot.  
Celem projektu jest zarządzanie danymi lig piłkarskich, w tym drużynami, meczami oraz wynikami.

## Opis

Aplikacja umożliwia:

- Dodawanie, edytowanie i usuwanie drużyn piłkarskich
- Zarządzanie terminarzem meczów
- Rejestrowanie wyników spotkań
- Generowanie tabeli ligowej na podstawie wyników

Projekt wykorzystuje architekturę warstwową: kontrolery, serwisy i repozytoria.

## Funkcjonalności

- **REST API**: Aplikacja udostępnia API do zarządzania drużynami i meczami.
- **Baza danych**: Wykorzystanie wbudowanej bazy danych H2.
- **Testy jednostkowe**: Implementacja testów jednostkowych przy pomocy JUnit.

## Technologie

- Java 17
- Spring Boot
- Maven
- H2 Database (do testów)

## Struktura projektu

Główne pakiety znajdują się w `src/main/java` i obejmują:

- `controller` – obsługa żądań HTTP (REST)
- `service` – logika biznesowa
- `repository` – komunikacja z bazą danych (Spring Data JPA)
- `model` – klasy encji, np. `Team`, `Match`
