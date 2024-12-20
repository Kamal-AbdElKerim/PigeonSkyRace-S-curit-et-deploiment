# Documentation CI/CD et HTTPS

## CI/CD avec Jenkins
- Étapes automatisées : Checkout, Build, Tests, Déploiement Docker.
- Notifications configurées via Jenkins Email Plugin.
- Rapport de couverture généré avec JaCoCo.

## HTTPS dans Spring Boot
- Fichier keystore généré avec `keytool`.
- Configuration dans `application.properties`.

## Commandes Docker
- Build : `docker build -t image-name .`
- Run : `docker run -p 8443:8443 image-name`
