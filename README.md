# SmallerWebHexagon

A Java application demonstrating **Hexagonal Architecture** (Ports & Adapters pattern) with a web interface for rating calculations.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENT LAYER                             │
│                    (HTTP Browser Requests)                       │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                      ADAPTER LAYER                               │
│                                                                   │
│  ┌──────────────────┐              ┌──────────────────┐         │
│  │   WebAdapter     │              │  File System     │         │
│  │  (HTTP Server)   │              │   Adapter        │         │
│  │  Port: 4567      │              │  (FileRater)     │         │
│  └────────┬─────────┘              └────────┬─────────┘         │
│           │                                  │                   │
│           └──────────────┬───────────────────┘                   │
│                          │                                        │
└──────────────────────────┼────────────────────────────────────────┘
                           │
            ┌──────────────▼──────────────┐
            │      PORT INTERFACE         │
            │       <<Rater>>             │
            │                             │
            │  + rate(value: int)         │
            └──────────────▲──────────────┘
                           │
            ┌──────────────┴──────────────┐
            │                             │
       ┌────▼─────────┐         ┌────────▼────┐
       │  FileRater   │         │ InCodeRater │
       │  (Port impl) │         │  (Port impl)│
       └────┬─────────┘         └────────┬────┘
            │                            │
            └────────────┬───────────────┘
                         │
        ┌────────────────▼────────────────┐
        │    CORE BUSINESS LOGIC          │
        │   SmallerWebHexagon             │
        │                                 │
        │  Calculation:                   │
        │  result = value × (1 - rate%)   │
        └────────────────┬────────────────┘
                         │
        ┌────────────────▼────────────────┐
        │       OUTPUT ADAPTERS           │
        │                                 │
        │  ┌──────────────────────────┐   │
        │  │   HTML Template          │   │
        │  │  (result_view.html)      │   │
        │  └──────────────────────────┘   │
        └────────────────┬────────────────┘
                         │
        ┌────────────────▼────────────────┐
        │      HTTP Response (HTML)       │
        └─────────────────────────────────┘
```

## Component Descriptions

### Core (Hexagon)
- **SmallerWebHexagon**: Contains the pure business logic
  - Receives a `Rater` through dependency injection
  - Calculates: `result = value × (1 - rate/100)`
  - Returns `RateResult` with value, rate, and result

### Ports (Interfaces)
- **Rater**: Port interface defining the contract for rating services
  - Method: `rate(int value): double`

### Adapters (Implementations)
- **FileRater**: Reads rating data from a file
- **InCodeRater**: Implements rating logic in code
- **WebAdapter**: HTTP server adapter
  - Uses `com.sun.net.httpserver.HttpServer`
  - Listens on `http://localhost:4567`
  - Route: `GET /{value}`
  - Renders response using HTML template

### Entry Point
- **AppMain**: Bootstrap class
  - Instantiates `SmallerWebHexagon` with chosen `Rater` implementation
  - Starts `WebAdapter`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/smallerwebhexagon/
│   │       ├── AppMain.java              (Entry point)
│   │       ├── SmallerWebHexagon.java    (Core business logic)
│   │       ├── Rater.java                (Port interface)
│   │       ├── FileRater.java            (File adapter)
│   │       ├── InCodeRater.java          (Code adapter)
│   │       └── WebAdapter.java           (Web adapter)
│   └── resources/
│       ├── templates/
│       │   └── result_view.html          (HTML template)
│       └── test_file_rater.txt           (Rating data)
└── test/
    └── java/
        └── com/example/smallerwebhexagon/
            └── SmallerWebHexagonTest.java
```

## How It Works

1. **Request**: User navigates to `http://localhost:4567/100`
2. **WebAdapter** captures the request and extracts the value (100)
3. **Core Logic** calls `rateAndResult(100)` on `SmallerWebHexagon`
4. **Port Invocation**: Core calls `rater.rate(100)` (via Rater interface)
5. **Adapter Processing**: FileRater or InCodeRater processes the rating
6. **Calculation**: Core computes `result = 100 × (1 - rate/100)`
7. **Rendering**: WebAdapter renders the result using HTML template
8. **Response**: Browser displays the formatted result

## Key Architectural Principles

✓ **Dependency Inversion**: Core depends on abstraction (Rater), not concrete implementations
✓ **Loose Coupling**: Can swap between FileRater and InCodeRater without changing core logic
✓ **Separation of Concerns**: Business logic isolated from adapters
✓ **Testability**: Core logic can be tested independently of I/O adapters

## Usage

To switch between rating adapters, modify `AppMain.java`:

```java
// Using file-based rater (default)
SmallerWebHexagon hex = new SmallerWebHexagon(new FileRater("src/main/resources/test_file_rater.txt"));

// OR using code-based rater
// SmallerWebHexagon hex = new SmallerWebHexagon(new InCodeRater());
```
