# INENP Kubernetes Platform - Backend

Backend REST API für die INENP Kubernetes Platform.

## Überblick

Dieses Repository enthält:

- Spring Boot REST API auf Basis des Referenzprojekts
- Dockerfile für das Backend-Image
- Helm Chart für Kubernetes Deployments
- GitHub Actions für Test, Build und GHCR Publish

## Voraussetzungen

- Java 21
- Gradle Wrapper
- Docker für lokale Image-Builds

## Lokaler Start

```powershell
.\gradlew.bat bootRun
```

Standardmäßig verwendet die Anwendung eine lokale H2-In-Memory-Datenbank. Im Cluster werden Datenbankverbindung und AVWX API-Key über Kubernetes Secrets bereitgestellt.

## Container Image

Das Backend-Image wird als öffentliches GHCR-Image veröffentlicht:

```text
ghcr.io/fhb-mcce/inenp-k8s-platform-backend:latest
```

Der Workflow veröffentlicht `latest` und den Commit-SHA-Tag nach Merges auf `main`.

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

Diese Werte werden nicht plaintext im Repository gespeichert. Sie werden später durch External Secrets Operator, CloudNativePG und Crossplane bereitgestellt.

## Betrieb und Gate-7-Nachweise

Der aktuelle Backend-/Datenbank-Betrieb ist in [docs/backend-operations.md](docs/backend-operations.md) dokumentiert. Dort stehen Secret-Vertrag, Health Checks, CloudNativePG-Prüfungen, METAR-Fluss und Recovery-Hinweise für Demo und Staging.

## CI/CD

Pull Requests werden automatisch validiert:

- Gradle Tests
- Spring Boot Jar Build
- Docker Build Test

Merges auf `main` veröffentlichen das Image in GHCR.

## Lizenz

Internes Hochschulprojekt - FH Burgenland INENP 2026.
