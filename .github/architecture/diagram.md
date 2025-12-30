# SmallerWebHexagon — Architecture Diagram

This file contains a simple Mermaid diagram visualizing the hexagonal architecture of the app.

```mermaid
flowchart LR

  subgraph Domain
    SW["SmallerWebHexagon"]
    R["Rater\n(port)"]
  end

  subgraph Adapters
    Web["WebAdapter\n(Spark HTTP)"]
    IC["InCodeRater\n(outbound adapter)"]
    FR["FileRater\n(outbound adapter)"]
    AM["AppMain\n(bootstrap)"]
    T["templates/result_view.html\n(resource)"]
  end

  %% Relationships
  Web -->|HTTP GET /:value -> calls| SW
  SW -->|depends on (calls)| R
  IC -->|implements| R
  FR -->|implements| R
  AM -->|wires| Web
  AM -->|wires| SW
  Web -->|renders template| T

  %% Request flow numbers
  subgraph Flow
    F1([1. Client GET /:value])
    F2([2. WebAdapter parses :value])
    F3([3. SmallerWebHexagon.rateAndResult(value)])
    F4([4. Rater implementation computes rate/result])
    F5([5. WebAdapter renders HTML template and returns response])
  end
  F1 --> Web
  Web --> F2 --> SW --> F3 --> R --> F4
  F4 --> SW --> F5 --> Web

```

Quick notes
- Core domain: `SmallerWebHexagon` (pure business logic) depends on the `Rater` port.
- Outbound adapters: `InCodeRater`, `FileRater` implement `Rater` and provide rating strategies.
- Inbound adapter: `WebAdapter` exposes an HTTP API using Spark and renders `templates/result_view.html`.
- `AppMain` composes the application (chooses a `Rater` impl and starts the web adapter).

Open file: [.github/architecture/diagram.md](.github/architecture/diagram.md)
