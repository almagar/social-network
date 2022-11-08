# social-network
Alma Garpenfeldt och Linus Ekman

## Run
Requires docker and docker compose

### Run everything
1. Create a `.env` file in the project root with the following environment variables
```
POSTGRES_USER=socialnetworkuser
POSTGRES_PASSWORD=123456
POSTGRES_DATABASE=socialnetwork
```
2. Run `docker compose up --build -d`

### Development

#### Backend

1. Create a `.env` file in the project root with the following environment variables
```
POSTGRES_USER=socialnetworkuser
POSTGRES_PASSWORD=123456
POSTGRES_DATABASE=socialnetwork
```

2. Start the database:
```sh
docker run --rm -d \
        --name social-network-db-1 \
        -p 8181:5432 \
        -e POSTGRES_USER=socialnetworkuser \
        -e POSTGRES_PASSWORD=123456 \
        -e POSTGRES_DATABASE=socialnetwork \
        -v $(pwd)/db/:/docker-entrypoint-initdb.d/ \
        postgres:15.0-bullseye
```

3. Start keycloak:
```sh
docker run --rm -d \
    --name social-network-keycloak-1 \
    -p 8282:8080 \
    -e KEYCLOAK_USER=admin \
    -e KEYCLOAK_PASSWORD=password \
    -e KEYCLOAK_IMPORT=/opt/jboss/keycloak/data/import/socialnetwork.json \
    -v $(pwd)/keycloak/socialnetwork.json:/opt/jboss/keycloak/data/import/socialnetwork.json \
    quay.io/keycloak/keycloak:legacy
```

4. Start the backend:
```sh
./mvnw spring-boot:run
```


#### Frontend
Requires NodeJS and npm

1. Install dependencies:
```sh
npm install
```

2. Start the frontend:
```sh
npm run start
```
