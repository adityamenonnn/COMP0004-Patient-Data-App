# Patient Data Web Application

## Section 1: Features


**Core Data Model (Requirements 1-4):**
- `Column`, `DataFrame`, `DataLoader`, and `Model` form the data layer, loading CSV data into an ordered in-memory structure managed by a singleton

**Web Interface (Requirement 5):**
- Pages for viewing the patient list, individual details, and filtered results, with navigation on every page

**Search (Requirement 6):**
- Case-insensitive keyword search across all columns, with results linking directly to each patient's detail page

**Statistics (Requirement 7):**
- Oldest and youngest living patient, age distribution in ten-year bands, and gender breakdown by count

**Add, Edit, Delete (Requirement 8):**
- Patients can be added, edited, and deleted; every mutation rewrites the CSV via `RecordWriter`

**JSON Export (Requirement 9):**
- `JSONWriter` streams the full dataset to the browser as a downloadable JSON file

**Charts (Requirement 10):**
- `DataVisualiser` generates a horizontal bar chart (age distribution) and pie chart (gender breakdown) as server-side SVG


## Section 2: Design Evaluation

### Class Architecture

The application follows a clean MVC architecture:

- **Model layer**: `Column`, `DataFrame`, `DataLoader`, `Model`, `PatientSearch`, `PatientStatistics`, `RecordWriter`, `JSONWriter`, `DataExporter`, `DataVisualiser`, and `PatientSnapshot` handle all data and business logic
- **Controller layer**: Nine dedicated servlets each handle a single endpoint, coordinating the model and forwarding to the appropriate JSP
- **View layer**: JSP pages contain only the Java code needed to display data, with no business logic

The `Model` class was split into three focused classes once it exceeded ten methods. `Model` handles data loading and CRUD, `PatientSearch` handles keyword and field-based search, and `PatientStatistics` handles all statistical computations. This gives each class a single reason to change.

### Object Oriented Design

**Encapsulation:** All instance variables are private. The `Model` singleton is accessed only through `AppContext`, preventing direct instantiation elsewhere.

**Cohesion:** Each class has a clear, focused responsibility. `DataLoader` only parses CSV input. `RecordWriter` only writes CSV output. `DataVisualiser` only produces SVG markup.

**Use of Records:** `PatientSnapshot` is a Java record pairing a row index with its field data. This makes patient results immutable and removes boilerplate, while carrying the row index alongside the data so servlets and JSPs never need to track them separately.

**Inheritance:** `DataExporter` is an abstract class defining a common `export(DataFrame, Writer)` contract. `RecordWriter` and `JSONWriter` both extend it, each providing their own implementation for CSV and JSON output respectively. So, either exporter can be used interchangeably wherever a `DataExporter` is expected.

**Design Patterns:** The Singleton pattern is used in `Model` to ensure a single shared data source across all servlet requests. `AppContext` acts as the single access point to that instance.

