# Community Rooftop Solar Usage Tracker

The application is a Spring Boot + MySQL web app. Open `http://localhost:8081/` after starting it.

## Database

The app connects to MySQL database `solar_tracker`. With the default configuration, MySQL creates the database automatically if it does not exist.

Default connection:
- username: `root`
- password: `Test12345`

For another password, set `DB_PASSWORD` before starting Spring Boot.

## Data flow

1. Create an installation with location and solar capacity in kW.
2. Add households to that installation and optionally give each household an allocation percentage.
3. Record one generation log per installation per date. Generated units are energy in kWh (1 unit = 1 kWh).
4. Record household consumption for a date after the generation log for that installation/date exists.
5. The backend calculates the household's generation share and exported energy and stores them with the consumption record.
6. Monthly Summary totals the stored consumption, generation share and exported units.

## Main endpoints

- `GET /installations`
- `POST /installations`
- `GET /households`
- `POST /households?installationId=1`
- `GET /generation`
- `POST /generation?installationId=1`
- `GET /consumption`
- `POST /consumption?householdId=1`
- `GET /consumption/summary/monthly?householdId=1&year=2026&month=9`

The browser UI uses these endpoints directly, so records are stored in MySQL rather than browser/local storage.
