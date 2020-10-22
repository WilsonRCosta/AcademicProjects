'use strict'

const DEFAULT_PORT = '8080'
const BOARDGAMES_DATA_HOST = "localhost:9200"

const express = require('express')
const app = express()
const expressSession = require('express-session')

const groupsData = require('./ciborg-db')(BOARDGAMES_DATA_HOST)
const gamesData = require('./board-games-data')()

const gamesService = require('./ciborg-services')(gamesData, groupsData)
const authService = require('./auth-service')(groupsData)

app.use(expressSession({secret: 'keyboard cat'}))
app.use(express.json())
app.use('/', express.static('dist'))

const gamesApi = require('./ciborg-web-api')(express.Router(), gamesService)
const authApi = require('./auth-web-api')(app, express.Router(), authService)

app.use('/api/auth',authApi)
app.use('/api',gamesApi)

app.listen(process.argv[2] || DEFAULT_PORT, ()=>console.log('Listening'))

function verifyAuthenticated(req, rsp, next) {
    if(req.isAuthenticated())
        return next()
    rsp.status(403).json({message:"Not Authenticated"})
}