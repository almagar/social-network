const { response } = require('express');
const express = require('express')
const app = express() // Creating Express Server
const host = 'localhost' // Specifying Host
const port = 8000 // Specifying Port number
const http = require('http');
const server = http.createServer(app);
const { Server } = require("socket.io");

server.listen(port, host, () => console.log(`Listening on http://${host}:${port}/`));

// Initializing socket.io object
const io = new Server(server, {
    // Specifying CORS 
    cors: {
        origin: "*",
    }
});

// Socket event
io.on('connection', (socket) => {
    console.log('Receiver connected..') // Logging when user is connected

    socket.on('hej', () => {
        socket.emit('hej', "hej");
        console.log('got ping');
    });

    socket.on('drawPoint', (drawPoint) => {
        console.log('got drawPoint');
        io.emit('drawPoint', drawPoint);
    });
});



app.get('/', (req, res) => {
    console.log("hej") // Emitting event.
    res.send('tjena');
});

// Post request on home page
app.post('/', (req, res) => {
    liveData.emit("new-data", req.body.message) // Emitting event.
});

