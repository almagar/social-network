const { DataTypes } = require('sequelize');
const sequelize = require("../sequelize");

const Whiteboard = sequelize.define('Whiteboard', {
    id: {
        type: DataTypes.UUID,
        defaultValue: DataTypes.UUIDV4,
        primaryKey: true
    },
    chatId: {
        type: DataTypes.UUID,
        allowNull: false,
        unique: true
    },
    chatName: {
        type: DataTypes.STRING
    }
}, {
    freezeTableName: true
});

async function syncWhiteboard() {
    try {
        await Whiteboard.sync();
    } catch (error) {
        console.log(error)
    }
};

module.exports = {
    syncWhiteboard,
    Whiteboard
};

