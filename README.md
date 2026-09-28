# Hospital Patient Data Web Application

A Java web application for managing hospital patient records, built with servlets and JSPs on embedded Tomcat. Supports CRUD operations, search, filtering, statistics, data visualisation, and JSON export across datasets of up to 100,000 patients.

## Features

- **Dataset selection**: Switch between 100, 10,000, and 100,000 patient CSV files at runtime
- **Patient list**: Browse all patients with combined filtering by city, gender, and state (AND logic)
- **Patient detail**: View individual records in list or table layout (toggle between views)
- **Search**: Case-insensitive keyword search across all columns
- **Add, edit, delete**: Full CRUD with automatic CSV persistence via `RecordWriter`
- **Statistics**: Oldest/youngest patient, age distribution in 10-year bands, gender breakdown
- **Charts**: Server-side SVG bar chart (age distribution) and pie chart (gender breakdown) via `DataVisualiser`
- **JSON export**: Download the full dataset as a JSON file

## Architecture

The application follows MVC with clean separation of concerns:

```
Model Layer          Controller Layer        View Layer
-----------          ----------------        ----------
Model (singleton)    10 Servlets             JSP pages
PatientSearch        (one per endpoint)      (display only)
PatientStatistics
DataLoader / RecordWriter / JSONWriter
DataVisualiser
```

### OOP Design

- **Singleton**: `Model` ensures a single shared data source across all requests, accessed via `AppContext`
- **Encapsulation**: All instance variables are private
- **Inheritance**: `DataExporter` is an abstract class; `RecordWriter` (CSV) and `JSONWriter` (JSON) extend it with their own `export()` implementations
- **Records**: `PatientSnapshot` is a Java record pairing a row index with its field data (immutable, no boilerplate)
- **Cohesion**: Each class has one responsibility (e.g. `DataLoader` only parses CSV, `DataVisualiser` only produces SVG)

## Project Structure

```
src/main/java/uk/ac/ucl/
  main/Main.java                  -- Embedded Tomcat entry point
  model/
    Model.java                    -- Singleton data manager (CRUD + persistence)
    DataFrame.java / Column.java  -- In-memory columnar data store
    DataLoader.java               -- CSV parser
    PatientSearch.java            -- Keyword and field-based search
    PatientStatistics.java        -- Age/gender statistics
    DataExporter.java             -- Abstract export contract
    RecordWriter.java             -- CSV writer
    JSONWriter.java               -- JSON writer
    DataVisualiser.java           -- SVG chart generator
    PatientSnapshot.java          -- Immutable record (row index + fields)
  servlets/                       -- One servlet per endpoint
src/main/webapp/                  -- JSP views + CSS
data/                             -- Patient CSV files (100, 10K, 100K)
```

## Running

```bash
# Build and run (requires Maven and Java 25)
mvn clean package
java -jar target/WebApp-1.3.jar

# Open in browser
open http://localhost:8080
```

## Tech Stack

- **Java 25** with Jakarta Servlets
- **Embedded Apache Tomcat 11**
- **JSP** for server-side rendering
- **Maven** for build management
- No external dependencies beyond Tomcat and the Servlet/JSP APIs
