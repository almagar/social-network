require('dotenv').config();

const { Sequelize } = require('sequelize');
const dbHost = process.env.POSTGRES_HOST;
const dbPort = process.env.POSTGRES_PORT;
const dbName = process.env.POSTGRES_DB;
const dbUser = process.env.POSTGRES_USER;
const dbPwd = process.env.POSTGRES_PWD;
const sequelize = new Sequelize(`postgres://${dbUser}:${dbPwd}@${dbHost}:${dbPort}/${dbName}`);

module.exports = sequelize;