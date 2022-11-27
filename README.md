# social-network
Alma Garpenfeldt & Linus Ekman

## Run
Requires docker and docker compose

### Run everything
1. Create a `.env` file in the project root with the following environment variables
```
POSTGRES_USER=socialnetworkuser
POSTGRES_PASSWORD=123456
POSTGRES_DATABASE=socialnetwork
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=password
KC_DB=postgres
KC_DB_USERNAME=keycloak
KC_DB_PASSWORD=password123
KC_DB_URL=jdbc:postgresql://keycloakdb:5432/keycloak
KC_HOSTNAME=keycloakdb
KC_PORT=5432
KC_DB_SCHEMA=keycloak
```

2. Create a `.env.production` file in `frontend/` with the following environment variables
```
REACT_APP_BASE_URL=http://localhost:8080/api
REACT_APP_KEYCLOAK_URL=http://localhost:8080
REACT_APP_REDIRECT_URI=http://localhost:8080
```

3. Run `docker compose up --build -d`

The app should then be available at `localhost:8080`


### Development

#### Backend

1. Create a `.env` file in the project root with the following environment variables
```
POSTGRES_USER=socialnetworkuser
POSTGRES_PASSWORD=123456
POSTGRES_DATABASE=socialnetwork
KEYCLOAK_ADMIN=admin
KEYCLOAK_ADMIN_PASSWORD=password
KC_DB=postgres
KC_DB_USERNAME=keycloak
KC_DB_PASSWORD=password123
KC_DB_URL=jdbc:postgresql://keycloakdb:5432/keycloak
KC_HOSTNAME=keycloakdb
KC_PORT=5432
KC_DB_SCHEMA=keycloak
```

2. Create network:
```
docker network create socialnetwork
```

3. Start the database:
```sh
docker run --rm -d \
        --name db \
        -p 8181:5432 \
        -e POSTGRES_USER=socialnetworkuser \
        -e POSTGRES_PASSWORD=123456 \
        -e POSTGRES_DATABASE=socialnetwork \
        -v $(pwd)/db/:/docker-entrypoint-initdb.d/ \
        --network socialnetwork \
        postgres:15.0-bullseye
```

4. Start keycloak:
```sh
docker run --rm -d \
    --name keycloak \
    -p 8282:8080 \
    -e KEYCLOAK_USER=admin \
    -e KEYCLOAK_PASSWORD=password \
    -e KEYCLOAK_IMPORT=/opt/jboss/keycloak/data/import/socialnetwork.json \
    -v $(pwd)/keycloak/socialnetwork.json:/opt/jboss/keycloak/data/import/socialnetwork.json \
    --network socialnetwork \
    quay.io/keycloak/keycloak:legacy
```

5. Start the backend:
```sh
./mvnw spring-boot:run
```


#### Frontend
Requires NodeJS and npm

1. Create a `.env.development` file in `frontend/` with the following environment variables
```
REACT_APP_BASE_URL=http://localhost:8080
REACT_APP_KEYCLOAK_URL=http://localhost:8282
REACT_APP_REDIRECT_URI=http://localhost:3000
```

2. Install dependencies:
```sh
npm install
```

3. Start the frontend:
```sh
npm run start
```
