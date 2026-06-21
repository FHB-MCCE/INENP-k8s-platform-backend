# Backend Betrieb und Tenant-Verifikation

Dieses Dokument beschreibt den Gate-7-Betriebsstand des Backends. Es ergänzt
das zentrale E2E-Runbook im Platform-Repository und konzentriert sich auf
Backend, Datenbank, Health Checks und Secret-Vertrag.

## Aktueller Runtime-Stand

| Bereich | Wert |
|---|---|
| Image | `ghcr.io/fhb-mcce/inenp-k8s-platform-backend` |
| Aktiver Image-SHA | `f5feb13942519da31cfac8f87ebdd58fd0cc0784` |
| Runtime | Spring Boot mit Java 21 |
| Datenbank | PostgreSQL über CloudNativePG |
| Tenant Namespaces | `tenant-demo`, `tenant-staging` |
| Health Endpoint | `/actuator/health` |
| METAR Endpoint | `/api/{userId}/{locationId}/metar/` |

Demo und Staging verwenden denselben geprüften Backend Image-SHA.

## Secret-Vertrag

Das Backend erwartet sensible Werte ausschließlich über Kubernetes Secrets. Die
Secret-Werte selbst liegen in Google Secret Manager und werden über External
Secrets Operator beziehungsweise CloudNativePG in den Tenant-Namespace
synchronisiert.

| Environment Variable | Kubernetes Secret Key | Zweck |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `spring-datasource-url` | JDBC-Verbindung zur Tenant-Datenbank |
| `SPRING_DATASOURCE_USERNAME` | `spring-datasource-username` | Datenbankbenutzer |
| `SPRING_DATASOURCE_PASSWORD` | `spring-datasource-password` | Datenbankpasswort |
| `AVWX_API_KEY` | `avwx-api-key` | Serverseitiger AVWX-Zugriff |

Keine dieser Werte dürfen in Git, Issues, PRs, Screenshots oder Chat-Ausgaben
auftauchen. Bei Prüfungen nur Metadaten oder Kubernetes-Status ausgeben.

## Health Checks

Die Backend-Chart-Probes prüfen den Spring-Boot-Health-Endpunkt.

```powershell
kubectl -n tenant-demo get pods
kubectl -n tenant-staging get pods
kubectl -n tenant-demo describe pod -l app.kubernetes.io/name=weather-app-backend
kubectl -n tenant-staging describe pod -l app.kubernetes.io/name=weather-app-backend
```

Öffentliche Smoke Tests:

```powershell
curl.exe -sS -o NUL -w "demo_api %{http_code}`n" https://api.demo.inenp.naehrer.me/actuator/health
curl.exe -sS -o NUL -w "staging_api %{http_code}`n" https://api.staging.inenp.naehrer.me/actuator/health
curl.exe -sS -o NUL -w "demo_users %{http_code}`n" https://api.demo.inenp.naehrer.me/api/user/
curl.exe -sS -o NUL -w "staging_users %{http_code}`n" https://api.staging.inenp.naehrer.me/api/user/
```

Erwartung: Alle Aufrufe antworten mit HTTP `200`.

## Datenbankbetrieb

CloudNativePG erstellt pro Tenant eine eigene PostgreSQL-Datenbank. Das Backend
nutzt Flyway für das Schema. Die Migration wurde für PostgreSQL angepasst:

- `double precision` statt H2-spezifischer numerischer Typen
- PostgreSQL-Support im Runtime-Klassenpfad
- Secret-basierter Datenbankbenutzer `weather`

Prüfung:

```powershell
kubectl -n tenant-demo get clusters.postgresql.cnpg.io
kubectl -n tenant-staging get clusters.postgresql.cnpg.io
kubectl -n tenant-demo get secrets | Select-String weather
kubectl -n tenant-staging get secrets | Select-String weather
```

Nur Secret-Namen prüfen, nie Secret-Inhalte ausgeben.

## API- und METAR-Datenfluss

Der Browser ruft AVWX nicht direkt auf. Der Ablauf ist:

1. Frontend ruft das Backend unter der tenant-spezifischen Backend-URL auf.
2. Backend liest den AVWX-Key aus dem Secret.
3. Backend ruft AVWX serverseitig auf.
4. Backend gibt die METAR-Antwort an das Frontend zurück.

Dadurch bleibt der AVWX-Key außerhalb des Browsers und außerhalb der
ausgelieferten Frontend-Bundles.

## Fehlerbilder und Recovery

| Symptom | Prüfung | Erwartete Maßnahme |
|---|---|---|
| Backend Pod startet nicht | `kubectl describe pod` und Events prüfen | Probe-Timing, ImagePullSecret und Secret-Referenzen prüfen |
| Flyway schlägt fehl | Backend Logs ohne Secrets prüfen | Migration und Datenbanktyp prüfen |
| `/actuator/health` nicht `200` | Pod-Status und Datenbankstatus prüfen | CNPG-Cluster und Backend-Secret kontrollieren |
| METAR liefert Fehler | Backend Logs und AVWX Secret-Metadaten prüfen | AVWX-Key in Secret Manager erneuern, keinen Wert ausgeben |

## Gate-7 Nachweis

Für die Abgabe reichen folgende Nachweise:

- Backend Build Workflow auf `main` ist grün.
- Demo und Staging Backend Pods laufen `1/1`.
- Demo und Staging Health-Endpunkte antworten mit HTTP `200`.
- Benutzer-, Favoriten-, Forecast- und METAR-Flows laufen über das Backend.
- Keine Secret-Werte sind in Git oder Dokumentation enthalten.

