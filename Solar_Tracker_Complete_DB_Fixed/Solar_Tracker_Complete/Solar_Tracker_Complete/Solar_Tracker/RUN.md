# Run the Solar Tracker

## 1. Start MySQL
Make sure the MySQL server is running.

The application uses:
- Database: `solar_tracker`
- User: `root`
- Default password: `Test12345`

If your MySQL password is different, set `DB_PASSWORD` or edit `src/main/resources/application.properties`.

The JDBC URL contains `createDatabaseIfNotExist=true`, so the database is created automatically when the MySQL user has permission to create databases. JPA then creates/updates the required tables.

## 2. Run on Windows
Open Command Prompt in the project folder and run:

```bat
mvnw.cmd clean package
mvnw.cmd spring-boot:run
```

Or after packaging:

```bat
java -jar target\Solar_Tracker-0.0.1-SNAPSHOT.jar
```

Then open:

`http://localhost:8081/`

## 3. Recommended data-entry order

1. Add Installation: location + capacity in kW.
2. Add Household: select installation + household name + allocation percentage.
3. Add Daily Generation: select installation + date + generated units (kWh).
4. Add Daily Consumption: select household + same date + consumed units (kWh).
5. Open Monthly Summary for the household.

The backend prevents duplicate daily records and calculates generation share/exported units before storing consumption.
