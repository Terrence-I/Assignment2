# Rate CS Teaching Staff

A Spring Boot, Thymeleaf, and PostgreSQL application for creating and managing
teaching staff ratings.

## Features

- List ratings with staff name, role title, and overall score.
- View, create, edit, and delete an individual rating.
- Validate names, email addresses, roles, comments, and 1–10 scores on the
  server.
- Keep email addresses unique: one email address may submit one rating.
- Display role-specific titles using `StaffMemberProfile` implementations.
- Persist data to PostgreSQL.

## How to run locally

Requirements: Java 17 or newer, PostgreSQL, and the included Maven wrapper.

1. Create a PostgreSQL database (the default expected database name is
   `staffratings`).
2. Set the database environment variables in the terminal used to start the
   application. For PowerShell:

   ```powershell
   $env:DB_URL = "jdbc:postgresql://localhost:5432/staffratings"
   $env:DB_USERNAME = "your-local-postgres-user"
   $env:DB_PASSWORD = "your-local-postgres-password"
   ```

3. Start the app:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

4. Open `http://localhost:8080/`.

The app uses `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`; defaults are provided
for a local PostgreSQL database. Do not commit real credentials.

## How to deploy on Render

1. Create a Render PostgreSQL database and a Render web service connected to
   this repository.
2. Configure the web service build command as `./mvnw clean package` and the
   start command as
   `java -jar target/rate_cs_teaching_staff-0.0.1-SNAPSHOT.jar`.
3. Set these environment variables in the Render web service:
   - `DB_URL`: JDBC-form URL for the database. Use the Render host and database
     name, with port `5432` and `?sslmode=require` (add
     `&channelBinding=disable` only if the driver reports a channel-binding
     error).
   - `DB_USERNAME`: Render database username.
   - `DB_PASSWORD`: Render database password.
   - `DDL_AUTO`: `update` for this assignment deployment.
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
