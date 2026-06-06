# INENP Kubernetes Platform – Backend

Backend REST API für die INENP Kubernetes Platform.

## Überblick

Dieses Repository enthält:

- **Backend-Applikation** – Spring Boot REST API (Fork des Referenzprojekts)
- **Dockerfile** – Multi-Stage Build für Production Image
- **Helm Chart** – Kubernetes Deployment-Konfiguration
- **CI/CD** – GitHub Actions für Build, Test und Publish

## Voraussetzungen

- Java >= 21
- Gradle (Wrapper enthalten)
- Docker (für lokale Image-Builds)

## Quickstart

```bash
./gradlew bootRun
```

## Projektstruktur

```
.
├── src/              # Applikations-Quellcode
├── charts/           # Helm Chart
├── Dockerfile        # Container Build
├── .github/          # CI/CD Workflows
└── README.md
```

## Container Image

Das Backend-Image wird als **öffentliches** Image in GitHub Container Registry (GHCR) publiziert:

```
ghcr.io/fhb-mcce/inenp-k8s-platform-backend:latest
```

## CI/CD

Pull Requests werden automatisch validiert:
- Checkstyle & PMD
- Unit Tests
- Docker Build Test

## Lizenz

Internes Hochschulprojekt – FH Burgenland INENP 2026.
