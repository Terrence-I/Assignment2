# Rate CS Teaching Staff

A Spring Boot, Thymeleaf, and PostgreSQL application for creating and managing
teaching staff ratings.

AI usage Declaration.
Name: Terrence Ishak
I used VScode's built in ai agent in order to trouble shoot through the the Controller, the Test setup, as well a general bug fixing.
Also used AI to help with getting the website to launch on Render, since I was having trouble connecting the database to the project.
## How to run locally

Requirements: Java 17 or newer, PostgreSQL, and the included Maven wrapper.

1. In rate_cs_teaching_staff\src\main\resources\application.properties
2. Set the database environment variables in the terminal used to start the
   application. The app uses `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` to connect to the render PostgreSQL database.
   ```
   These are the environment variables used in application.properties
   DB_URL = jdbc:postgresql://dpg-dbavi90m7kps73afk920-a.oregon-postgres.render.com:5432/staffratingterrenceishak
   DB_USERNAME = te_is_staffrate_user
   DB_PASSWORD = 1FwqzQP8v03KoUXTdeNmQDD5kgUjgvhT
   ```   
3. I have been running the code on VS code's terminal, once it has finished compiling and is running, open `(http://localhost:8080/ratings/main)` in a browser.



## How to deploy on Render

1. Create a Render PostgreSQL database and a Render web service connected to
   this repository.

3. Set these environment variables in the Render web service:
   ```
    DB_PASSWORD=1FwqzQP8v03KoUXTdeNmQDD5kgUjgvhT
    DB_URL=jdbc:postgresql://dpg-dbavi90m7kps73afk920-a.oregon-postgres.render.com:5432/staffratingterrenceishak
    DB_USERNAME=te_is_staffrate_user
  
4. Deploy and open the Render service URL.

Use Render’s internal database host when the web service and database are in
the same Render region. Keep credentials in Render environment settings, not
in source files.

## Tests

Run the automated test suite with `.\mvnw.cmd test` on Windows or `./mvnw test`
on macOS/Linux. See [TESTING.md](./TESTING.md) for the test strategy and
coverage.

## Known issues / future work

- Email uniqueness currently limits each email address to one rating total,
  rather than one rating per staff member.
- Overall scores are per-entry averages; aggregate averages per staff member
  are outside this iteration.
- Authentication and authorization are not implemented, so CRUD routes are
  available to anyone who can reach the app.
