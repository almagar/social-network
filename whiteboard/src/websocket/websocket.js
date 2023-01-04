const { server: expressServer } = require('../');
const { Server } = require("socket.io");
const io = new Server(expressServer, {
    cors: {
        origin: "*",
    }
});

io.on('connection', (socket) => {
    let room;
    console.log('Receiver connected..')

    socket.on('join', (whiteboardId) => {
        console.log(whiteboardId);
        room = whiteboardId;
        socket.join(room);
        console.log('joining whiteboard-room ' + room);
    });

    socket.on('drawPoint', (drawPoint) => {
        console.log('got drawPoint');
        io.to(room).emit('drawPoint', drawPoint);
        console.log('emited drawPoint to room ' + room);
    });
});