require('dotenv').config();

// Db
const sequelize = require('./sequelize');
const syncWhiteboard = require('./models/whiteboard');
const syncDrawPoint = require('./models/drawPoint');

// Express server
const express = require('express');
const app = express(); // Creating Express Server
const host = 'localhost' // Specifying Host
const port = 8000 // Specifying Port number
const http = require('http');
const server = http.createServer(app);
const cors = require('cors');
app.use(cors());
app.use(express.json());
server.listen(port, host, () => {
    console.log(`Listening on http://${host}:${port}/`);
    syncWhiteboard();
    syncDrawPoint(); 
});

// Web socket
const { Server } = require("socket.io");
const { v4: uuidv4 } = require('uuid');
const io = new Server(server, {
    cors: {
        origin: "*",
    }
});



try {
    sequelize.authenticate();
    console.log('Connection has been established successfully.');
  } catch (error) {
    console.error('Unable to connect to the database:', error);
  }


io.on('connection', (socket) => {
    console.log('Receiver connected..') // Logging when user is connected

    socket.on('drawPoint', (drawPoint) => {
        console.log('got drawPoint');
        io.emit('drawPoint', drawPoint);
    });

    socket.on('join-room', (room) => {
        console.log('joining room ' + room);
        socket.join(room)
    });
});

app.post('/whiteboard', (req, res) => {
    const data = req.body;
    const uuid = uuidv4();
    const whiteboard = {
        id: uuid,
        chat: data.name,
        chatId: data.chatId
    };

    console.log(whiteboard);

    res.json({ id: uuid });
    // spara i db
});

app.get('/whiteboard/chatId/:id', (req, res) => {
    res.json({})
})

