FROM node:alpine as build-deps
WORKDIR /usr/src/app
COPY . ./
RUN npm install
RUN npm run build


FROM tomcat:9.0.68-jre17-temurin-jammy
COPY --from=build-deps /usr/src/app/build/ /usr/local/tomcat/webapps/app/
