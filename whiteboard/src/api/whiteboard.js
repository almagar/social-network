const { express } = require('../');
const { Whiteboard } = require('../models/whiteboard');

const whiteboard = express.Router();

whiteboard.post('/', async (req, res) => {
    const body = req.body;
    try {
        const data = await Whiteboard.create({
            chatId: body.chatId,
            chatName: body.chatName
        })

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

module.exports = whiteboard;