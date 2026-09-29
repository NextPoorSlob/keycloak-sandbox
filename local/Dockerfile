FROM quay.io/keycloak/keycloak:22.0.0

COPY --chown=keycloak:keycloak keycloak.conf /opt/keycloak/conf/keycloak.conf
COPY --chown=keycloak:keycloak local-auth.p12 /opt/keycloak/conf/local-auth.p12
