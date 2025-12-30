# SmallerWebHexagon — Architecture Diagram

This file contains a simple Mermaid diagram visualizing the hexagonal architecture of the app.

```mermaid
flowchart LR
  %% Simple GitHub-compatible diagram (no multiline labels or HTML)
  SW[SmallerWebHexagon]
  R[Rater]
  Web[WebAdapter]
  IC[InCodeRater]
  FR[FileRater]
  AM[AppMain]
  T[Template]

  %% Relationships
  Web --> SW
  SW --> R
  IC --> R
  FR --> R
  AM --> Web
  AM --> SW
  Web --> T

  %% Request flow (simple)
  F1([Client GET /:value]) --> Web
  Web --> SW --> R
  R --> SW --> Web

```

Quick notes
- Core domain: `SmallerWebHexagon` (pure business logic) depends on the `Rater` port.
- Outbound adapters: `InCodeRater`, `FileRater` implement `Rater` and provide rating strategies.
- Inbound adapter: `WebAdapter` exposes an HTTP API using Spark and renders `templates/result_view.html`.
- `AppMain` composes the application (chooses a `Rater` impl and starts the web adapter).

Open file: [.github/architecture/diagram.md](.github/architecture/diagram.md)
