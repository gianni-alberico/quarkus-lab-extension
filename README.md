# Quarkus Lab

Ce repository contient deux modules :
- [quarkus-lab-extension](./quarkus-lab-extension) : l'extension Quarkus.
- [quarkus-lab-app](./quarkus-lab-app) : l'application de validation de l'extension.

La partie application couvre l'enregistrement au build, la configuration de la réflexion pour le mode natif, la sélection conditionnelle de beans et les beans synthétiques pilotés par configuration build time.

---

## Prérequis

- Java 25
- Maven 3.9+
- Quarkus 3.32.x (ce projet utilise la version 3.32.3)
- Docker (pour le build natif en container)
- Extension installée localement : `io.github.giannialberico:quarkus-lab-extension:1.0.0-SNAPSHOT`

Si l'extension n'est pas encore installée, commencer par la builder depuis le projet extension :
```bash
mvn install
```

---

## Ce que ce projet démontre

- **Recorder build time** — logue la date de build et les dépendances runtime packagées, au démarrage de l'application.
- **Réflexion automatique** — déclare automatiquement la réflexion pour toutes les classes implémentant `Cloneable`.
- **Sélection conditionnelle de bean** — choisit l'implémentation de `Colored` selon la présence de `@RunOnVirtualThread` dans l'application.
- **Beans synthétiques** — créés à partir d'une configuration build time (`quarkus.lab-extension.beans`).

---

## Endpoints

| Endpoint | Description |
|---|---|
| `GET /lab/reflect` | Invoque `ReflectiveService.hello()` par réflexion |
| `GET /lab/color` | Retourne la couleur de l'implémentation `Colored` active (méthode annotée `@RunOnVirtualThread`) |
| `GET /lab/extensionbean/{name}` | Résout un bean synthétique par `@Identifier` et appelle `greet()` |

---

## Configuration

Dans `src/main/resources/application.properties` :
```properties
quarkus.lab-extension.beans=toto,tutu
```

Cette propriété build time active les endpoints `/lab/extensionbean/toto` et `/lab/extensionbean/tutu`.

---

## Lancement en dev mode
```bash
mvn quarkus:dev
```

Vérifications manuelles des endpoints :
```bash
curl localhost:8080/lab/reflect
curl localhost:8080/lab/color
curl localhost:8080/lab/extensionbean/toto
curl localhost:8080/lab/extensionbean/tutu
```

---

## Packaging et lancement
```bash
mvn package -DskipTests
java -jar target/quarkus-app/quarkus-run.jar
```

---

## Build natif (en container)

Compiler le binaire natif :
```bash
mvn package -Dnative -Dquarkus.native.container-build=true
```

Construire l'image Docker :
```bash
docker build -f src/main/docker/Dockerfile.native-micro -t quarkus-lab-app-native .
```

Démarrer le container :
```bash
docker run --rm -p 8080:8080 quarkus-lab-app-native
```
---

## Tests

| Commande                                                    | Description |
|-------------------------------------------------------------|---|
| `mvn test`                                                  | Tests JVM |
| `mvn verify -DskipTests`                                    | Tests d'intégration sur l'app packagée |
| `mvn verify -Dnative -Dquarkus.native.container-build=true` | Tests d'intégration natifs |
