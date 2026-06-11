FROM amazoncorretto:21-alpine

ARG CI_COMMIT_TIMESTAMP
ARG CI_COMMIT_SHA
ARG CI_COMMIT_TAG

LABEL org.opencontainers.image.authors="INENP Project Team"
LABEL org.opencontainers.image.vendor="FH Burgenland INENP 2026"
LABEL org.opencontainers.image.source="https://github.com/FHB-MCCE/INENP-k8s-platform-backend"
LABEL org.opencontainers.image.created="${CI_COMMIT_TIMESTAMP}"
LABEL org.opencontainers.image.title="inenp-k8s-platform-backend"
LABEL org.opencontainers.image.description="Weather app backend used for the INENP Kubernetes platform tenant demo."
LABEL org.opencontainers.image.revision="${CI_COMMIT_SHA}"
LABEL org.opencontainers.image.version="${CI_COMMIT_TAG}"

COPY build/libs/*.jar /app.jar

EXPOSE 8080/tcp

CMD ["java", "-jar", "/app.jar"]
