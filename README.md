# INENP Kubernetes Platform - Backend

Backend REST API fÃ¼r die INENP Kubernetes Platform.

## Ãœberblick

Dieses Repository enthÃ¤lt:

- Spring Boot REST API auf Basis des Referenzprojekts
- Dockerfile fÃ¼r das Backend-Image
- Helm Chart fÃ¼r Kubernetes Deployments
- GitHub Actions fÃ¼r Test, Build und GHCR Publish

## Voraussetzungen

- Java 21
- Gradle Wrapper
- Docker fÃ¼r lokale Image-Builds

## Lokaler Start

```powershell
.\gradlew.bat bootRun
```

StandardmÃ¤ÃŸig verwendet die Anwendung eine lokale H2-In-Memory-Datenbank. Im Cluster werden Datenbankverbindung und AVWX API-Key Ã¼ber Kubernetes Secrets bereitgestellt.

## Container Image

Das Backend-Image wird als Ã¶ffentliches GHCR-Image verÃ¶ffentlicht:

```text
ghcr.io/fhb-mcce/inenp-k8s-platform-backend:latest
```

Der Workflow verÃ¶ffentlicht `latest` und den Commit-SHA-Tag nach Merges auf `main`.

## Helm Chart

Das Chart liegt unter `charts/weather-app-backend`.

```powershell
helm template weather-backend .\charts\weather-app-backend
```

Erwartete Secret-Keys im Cluster:

- `spring-datasource-url`
- `spring-datasource-username`
- `spring-datasource-password`
- `avwx-api-key`

Diese Werte werden nicht plaintext im Repository gespeichert. Sie werden spÃ¤ter durch External Secrets Operator, CloudNativePG und Crossplane bereitgestellt.

## Betrieb und Gate-7-Nachweise

Der aktuelle Backend-/Datenbank-Betrieb ist in [docs/backend-operations.md](docs/backend-operations.md) dokumentiert. Dort stehen Secret-Vertrag, Health Checks, CloudNativePG-Prüfungen, METAR-Fluss und Recovery-Hinweise für Demo und Staging.
## Betrieb und Gate-7-Nachweise

Der aktuelle Backend-/Datenbank-Betrieb ist in [docs/backend-operations.md](docs/backend-operations.md) dokumentiert. Dort stehen Secret-Vertrag, Health Checks, CloudNativePG-Prüfungen, METAR-Fluss und Recovery-Hinweise für Demo und Staging.
## CI/CD

Pull Requests werden automatisch validiert:

- Gradle Tests
- Spring Boot Jar Build
- Docker Build Test

Merges auf `main` verÃ¶ffentlichen das Image in GHCR.

## Lizenz

Internes Hochschulprojekt - FH Burgenland INENP 2026.
