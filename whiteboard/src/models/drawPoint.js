const { DataTypes } = require('sequelize');
const sequelize = require("../sequelize");

const DrawPoint = sequelize.define('DrawPoint', {
    id: {
        type: DataTypes.UUID,
        defaultValue: DataTypes.UUIDV4,
        primaryKey: true
    },
    whiteboardId: {
        type: DataTypes.UUID,
        allowNull: false,
        references: {
            model: 'Whiteboard',
            key: 'id'
        }
    },
    x: {
        type: DataTypes.INTEGER,
        allowNull: false
    },
    y: {
        type: DataTypes.INTEGER,
        allowNull: false
    },
    color: {
        type: DataTypes.STRING,
    },
    thickness: {
        type: DataTypes.INTEGER,
        allowNull: false
    },
    tool: {
        type: DataTypes.STRING
    }
}, {
    freezeTableName: true
});

async function syncDrawPoint() {
    try {
        await DrawPoint.sync();
    } catch (error) {
        console.log(error);
    }
}

module.exports = { 
    syncDrawPoint,
    DrawPoint
 };