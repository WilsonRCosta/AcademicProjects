'use strict'

const DEFAULT_PORT = '8080'
const BOARDGAMES_DATA_HOST = "localhost:9200"

const express = require('express')

const groupsData = require('./ciborg-db')(BOARDGAMES_DATA_HOST)
const gamesData = require('./board-games-data')()
const gamesService = require('./ciborg-services')(gamesData, groupsData)
const gamesApi = require('./ciborg-web-api')(express.Router(), gamesService)

const app = express()

app.use(express.json())
app.use('',gamesApi)
app.listen(process.argv[2] || DEFAULT_PORT, ()=>console.log('Listening'))