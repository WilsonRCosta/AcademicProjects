'use strict'

const DEFAULT_PORT = '8080'
const BOARDGAMES_DATA_HOST = "localhost:9200"

const http = require('http')
const router = require('./ciborg-router') 

const groupsData = require('./ciborg-db')(BOARDGAMES_DATA_HOST)
const gamesData = require('./board-games-data')()
const gamesService = require('./ciborg-services')(gamesData, groupsData)
const gamesApi = require('./ciborg-web-api')(gamesService)

//router.get('favicon.ico', gamesApi.GetFavicon)

router.get ('/games', gamesApi.getAllGames)
router.get ('/games/:gameName', gamesApi.getGameByName)
router.post('/groups/:groupName&:desc', gamesApi.createGroup)
router.put ('/groups/:oldName&:newName&:desc', gamesApi.editGroup)
router.get ('/groups', gamesApi.getAllGroups)
router.get ('/groups/:groupName', gamesApi.getGroupDetails)
router.put ('/groups/:groupName&:gameName', gamesApi.insertGameInGroup)
router.delete ('/groups/:groupName&:gameName', gamesApi.removeGameFromGroup)
router.get ('/groups/:groupName&:minVal&:maxVal', gamesApi.getGroupGamesByDuration)

const server = http.createServer(router)

server.listen(process.argv[2] || DEFAULT_PORT, () => console.log('Listening'))