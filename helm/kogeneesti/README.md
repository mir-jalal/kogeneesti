# Kogeneesti Helm Chart

This chart deploys the Kogeneesti backend service.

## Install

```bash
helm upgrade --install kogeneesti ./helm/kogeneesti \
  --namespace kogeneesti --create-namespace
```

## Common customizations

- Set image tag:

```bash
helm upgrade --install kogeneesti ./helm/kogeneesti \
  --namespace kogeneesti --create-namespace \
  --set image.tag=latest
```

- Use an existing secret for database credentials:

```bash
helm upgrade --install kogeneesti ./helm/kogeneesti \
  --namespace kogeneesti --create-namespace \
  --set secret.create=false \
  --set secret.existingSecret=kogeneesti-db-secret
```

Expected keys in the existing secret:

- `KOGENEESTI_JDBC_URL`
- `KOGENEESTI_DB_PASSWORD`

- Enable persistence for image storage:

```bash
helm upgrade --install kogeneesti ./helm/kogeneesti \
  --namespace kogeneesti --create-namespace \
  --set persistence.enabled=true \
  --set persistence.size=10Gi
```
