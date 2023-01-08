FROM node:alpine
WORKDIR /usr/src/app
COPY . ./
RUN npm install
ENV WHITEBOARD_PRODUCTION=true
CMD ["node", "src/index.js"]