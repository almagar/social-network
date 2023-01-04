require('dotenv').config();

// Db
const sequelize = require('./sequelize');
const { syncWhiteboard } = require('./models/whiteboard');
const syncDrawPoint = require('./models/drawPoint');
const { DataTypes } = require('sequelize');

// Express server
const express = require('express');
const app = express(); // Creating Express Server
const host = process.env.EXPRESS_HOST;
const port = process.env.EXPRESS_PORT;// Specifying Port number
const http = require('http');
const server = http.createServer(app);
module.exports = {
    express,
    server
};
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
