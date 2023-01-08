require('dotenv').config({ path: process.env.WHITEBOARD_PRODUCTION ? '.env.production' : '.env' });

// Db
const sequelize = require('./sequelize');
const { syncWhiteboard } = require('./models/whiteboard');
const { syncDrawPoint } = require('./models/drawPoint');
const { DataTypes } = require('sequelize');

// Express server
const express = require('express');
const app = express(); // Creating Express Server
const host = process.env.EXPRESS_HOST;
const port = process.env.EXPRESS_PORT;// Specifying Port number
const http = require('http');
const server = http.createServer(app);

// Keycloak
const Keycloak = require('keycloak-connect');

const keycloak = new Keycloak({}, {
    realm: 'socialnetwork',
    'auth-server-url': 'http://localhost:8282/auth',
    'ssl-required': 'none',
    resource: 'whiteboard',
    credentials: {
        secret: process.env.KEYCLOAK_SECRET
    }
});
app.use(keycloak.middleware());

module.exports = {
    express,
    server,
    keycloak
};


//express forts
const cors = require('cors');
app.use(cors());
app.use(express.json());



// API
app.use('/whiteboard', require('./api/whiteboard'));

// Websocket
require('./websocket/websocket');

server.listen(port, host, () => {
    syncWhiteboard();
    syncDrawPoint();
});
