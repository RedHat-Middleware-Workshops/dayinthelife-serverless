FROM registry.redhat.io/rhel8/postgresql-12:1-83

ENV POSTGRESQL_USER=patient_portal
ENV POSTGRESQL_PASSWORD=secret
ENV POSTGRESQL_DATABASE=patient_portal
ENV POSTGRESQL_HOST_AUTH_METHOD=scram-sha-256
ENV POSTGRESQL_INITDB_ARGS=--auth-host=scram-sha-256

COPY config/ /opt/app-root/src