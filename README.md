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

2. Create a `.env.development` file in `frontend/` with the following environment variables
```
REACT_APP_BASE_URL=http://backend:8080
```

3. Run `docker compose up --build -d`

### Development

#### Backend

1. Create a `.env` file in the project root with the following environment variables
```
POSTGRES_USER=socialnetworkuser
POSTGRES_PASSWORD=123456
POSTGRES_DATABASE=socialnetwork
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
    -e KC_DB=postgres \
    -e DB_VENDOR=postgres \
    -e DB_ADDR=db \
    -e DB_DATABASE=socialnetwork \
    -e DB_USER=socialnetworkuser \
    -e DB_PASSWORD=123456 \
    -v $(pwd)/keycloak/socialnetwork.json:/opt/jboss/keycloak/data/import/socialnetwork.json \
    --network socialnetwork \
    quay.io/keycloak/keycloak:legacy
```

4. Start the backend:
```sh
./mvnw spring-boot:run
```


#### Frontend
Requires NodeJS and npm

1. Create a `.env.development` file in `frontend/` with the following environment variables
```
REACT_APP_BASE_URL=http://localhost:8080
```

2. Install dependencies:
```sh
npm install
```

3. Start the frontend:
```sh
npm run start
```
