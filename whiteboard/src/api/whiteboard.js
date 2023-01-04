const { express } = require('../');
const { Whiteboard } = require('../models/whiteboard');
const { DrawPoint } = require('../models/drawPoint');

const whiteboard = express.Router();

whiteboard.post('/', async (req, res) => {
    const body = req.body;
    try {
        const data = await Whiteboard.create({
            chatId: body.chatId,
            chatName: body.chatName
        });

        res.json({ data });
    } catch (error) {
        console.log(error);
    }
});

whiteboard.get('/chatId/:id', async (req, res) => {
    const chatId = req.params.id;
    try {
        const data = await Whiteboard.findOne({
            where: {
                chatId: chatId
            }
        });

        res.json({ data });
    } catch (error) {
        console.log(error);
    }
});

whiteboard.get('/:id', async (req, res) => {
    const id = req.params.id;
    try {
        const data = await DrawPoint.findAll({
            where: {
                whiteboardId: id
            },
            order: [
                ['createdAt', 'DESC']
            ],
            attributes: [
                'whiteboardId',
                'x',
                'y',
                'color',
                'thickness',
                'tool',
                'createdAt'
            ]
        });

        console.log(data);

        res.json({ data });
    } catch (error) {
        console.log(error);
    }
});

module.exports = whiteboard;