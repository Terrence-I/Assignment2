# Testing

The project uses full Spring Boot integration tests with MockMvc and an
in-memory H2 database. The tests exercise MVC routing, Thymeleaf rendering,
Bean Validation, repository persistence, and CRUD behavior without requiring a
running PostgreSQL server.

Run the suite:

```powershell
.\mvnw.cmd test
```

On macOS/Linux:

```sh
./mvnw test
```

The test configuration overrides the normal PostgreSQL connection with H2.
The application itself still uses PostgreSQL when run outside the test suite.

Coverage includes:

- Root/index and rating detail/edit/delete confirmation pages.
- Create and update success redirects and persistence.
- Invalid email, missing name, out-of-range scores, and duplicate email
  returning the form with field errors.
- Delete removing a persisted rating.
- Timestamp preservation during edits.
- Role-specific display-title behavior.
