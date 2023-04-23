# Student Society

## Opis
Aplikacja webowa pozwalająca użytkownika na wymianę treści oraz ich wyszukiwanie na podstawie predefiniowanych przez moderatorów kategorii oraz tworzonych przez użytkowników  tagów. Publikowane treści mogą zawierać multimedia tj. zdjęcia, a treść tekstową można edytować w "bogaty" sposób (docelowo markdown). 

## Użytkownicy
- gość
- moderator
- Student/Użytkownik zalogowany

## Wymagania funkcjonalne
- [x] Użytkownik może utworzyć konto
- [x] Użytkownik może zweryfikować swoje konto w celu dostępu do większej ilości akcji w portalu
- [ ] Użytkownik może edytować dane swojego konta
- [x] Zalogowany użytkownik może dodawać posty składające się z maksymalnie jednego obrazu i tekstu. 
- [x] Użytkownik tworzący post może do niego dodać odpowiednie tagi pomagające w jego wyszukiwaniu
- [x] Strona posiada predefiniowane kategorie np. („W4”, „Humor”). Które będą widoczne z poziomu strony głównej. 
- [x] Użytkownik może przeglądać posty. Filtrować je na podstawie  targów, kategorii, i czasu dodania. Sortować na podstawie polubieni i czasu dodania
- [x] Każdy post może być „plusowany” bądź „minusowany” przez zalogowanego użytkownika co będzie wpływało na jego pozycjonowanie w połączeniu z czasem
- [x] Zalogowany użytkownik może dodawać komentarze do każdego postu
- [ ] Użytkownik może wyszukiwać posty za pomocą frazy (opcjonalne)
- [ ] Moderator może blokować określone posty
- [ ] Moderator może nakładać blokady czasowe na określonych użytkowników.
- [x] Możliwość wyboru motywów kolorystycznych dark i light

## Diagram aktywności

![driagram aktywności](/doc/img/diagram_aktywnosci.png)


## Pomocniczy model danych

![DB schema](/doc/img/db_schema.png)

## Architektura

![DB schema](/doc/img/arch.png)

## Technologie

* React
* Next.js
* TailwindCSS
* DaisyUI
* Java
* Spring, Sbring Boot, Spring Security
* JWT
* Hibernate
* MySQL, SQL
* Azure Blob Storage
* Hosting Vercel, Azure
* Docker

## Uruchomienie loklane

1. Instalacja Docker (https://docs.docker.com/desktop/install/linux-install/)
2. W pliku docker-compose.yaml znajdującym się w folderze głównym aplikacji wprowadzamy odpwiednie wartości dla: 
    * AZURE_BLOB_ACCOUNT_KEY - klucz do konta Azure Blob
    * AZURE_BLOB_ACCOUNT_NAME - nazwa konta Azure blob
    * AZURE_BLOB_CONTAINER - nazwa konterera na multimedia
    * AZURE_BLOB_ENDPOINT - endpint serwisu Azure Blob
    * MYSQL_HOST - adres hosta bazy MySQL
    * DB_NAME - nazwa bazy danych 
    * DB_PASS - hasło bazy danych
    * DB_USER - urzytkownik bazy danych
    * GMAIL_USER GMAIL_PASS - konto pocztowe używane do mailingu
    * JWT_KEY - klucz prywatny używany do tworzenia tokenów JWT
    * AUTH_SERVICE_URL; DATA_PROVIDER_URL - adres hosta aplikacji backendowej (lokanie nic nie trzeba zmieniać)
3. Zbudowanie dockerowych obrazów oraz uruchomienie 
```bash
# budowanie obrazu
docker compose build
# uruchomienie
docker compose up -d
```
5. Domyśle parametry uruchamiają aplikację frontendowom na adresie `localhost:3000`

## Widoki

### Desktop

* Widok główny (różne motywy)
![main view](/doc/img/main.png)
![main view](/doc/img/main_bk.png)
![main view](/doc/img/main_cp.png)

* Widok przeszukiwania kategorii postów
![article search](/doc/img/article_search.png)

* Widok postu
![article](/doc/img/article.png)


* Widok komentarzy postu
![article](/doc/img/article_comments.png)

* Logowanie
![login](/doc/img/login.png)

* Register
![login](/doc/img/register.png)

* Tworzenie postow
![article creation](/doc/img/creation.png)
![article creation](/doc/img/creation_2.png)

* Profil urzytkownika 
![profile](/doc/img/profil.png)

### Przykład responsywnosci mobile
![profile](/doc/img/mobile_main_2.png)
![profile](/doc/img/mobile_main_1.png)