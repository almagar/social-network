const { server: expressServer } = require('../');
const { Server } = require("socket.io");
const io = new Server(expressServer, {
    cors: {
        origin: "*",
    }
});

const { DrawPoint } = require('../models/drawPoint');

io.on('connection', (socket) => {
            console.log('Receiver connected..')

            socket.on('join', (whiteboardId) => {
                socket.join(whiteboardId);
            });

            socket.on('drawPoint', async (drawPoint) => {
                console.log('got drawPoint');
                io.to(drawPoint.whiteboardId).emit('drawPoint', drawPoint);
                console.log('emited drawPoint to room ' + drawPoint.whiteboardId);

                try {
                    await DrawPoint.create({
                        whiteboardId: drawPoint.whiteboardId,
                        x: drawPoint.x,
                        y: drawPoint.y,
                        color: drawPoint.color,
                        thickness: drawPoint.thickness,
                        tool: drawPoint.tool
                    });
                } catch (error) {
                    console.log(error);
                }
            });
});