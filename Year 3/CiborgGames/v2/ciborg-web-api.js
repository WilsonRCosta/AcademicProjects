'use strict'

module.exports = function(router, gamesService) {

    router.get ('/games', getAllGames)
    router.get ('/games/:gameName', getGameByName)
    router.post('/groups/:groupName&:desc', createGroup)
    router.put ('/groups/:oldName&:newName&:desc', editGroup)
    router.get ('/groups', getAllGroups)
    router.get ('/groups/:groupName', getGroupDetails)
    router.put ('/groups/:groupName&:gameName', insertGameInGroup)
    router.delete ('/groups/:groupName&:gameName', removeGameFromGroup)
    router.get ('/groups/:groupName/:minVal/:maxVal', getGroupGamesByDuration)

    return router

    function getAllGames(req, rsp) {
        gamesService.getAllGames()
            .then(data => rsp.status(200).end(JSON.stringify(data)))
            .catch(data => rsp.status(409).end(data.message))
    }

    function getGameByName(req, rsp) {
        gamesService.getGameByName(req.params.gameName)
            .then(data => rsp.status(200).end(JSON.stringify(data)))
            .catch(data => rsp.status(409).end(data.message))
    }

    function createGroup(req, rsp) {
        gamesService.createGroup(req.params.groupName, req.params.desc)
            .then(data => rsp.status(201).end(JSON.stringify(data)))
            .catch(data => rsp.status(409).end(data.message))
    }

    function editGroup(req, rsp) {
        gamesService.editGroup(req.params.oldName, req.params.newName, req.params.desc)
            .then(data => rsp.status(201).end(JSON.stringify(data)))
            .catch(data => rsp.status(404).end(data.message))
    }

    function getAllGroups(req, rsp) {
        gamesService.getAllGroups()
            .then(data => rsp.status(200).end(JSON.stringify(data)))
            .catch(data => rsp.status(404).end(data.message))
    }

    function getGroupDetails(req, rsp){
        gamesService.getGroupDetails(req.params.groupName)
            .then(data => rsp.status(200).end(JSON.stringify(data)))
            .catch(data => rsp.status(404).end(data.message))
    }

    function insertGameInGroup(req, rsp) {
        gamesService.insertGameInGroup(req.params.groupName, req.params.gameName)
            .then(data => rsp.status(201).end(JSON.stringify(data)))
            .catch(data => {
                let dataObj = JSON.parse(data.message)
                rsp.status(dataObj.status).end(dataObj.message)
            })
    }

    function removeGameFromGroup(req, rsp) {
        gamesService.removeGameFromGroup(req.params.groupName, req.params.gameName)
            .then(data => rsp.status(200).end(JSON.stringify(data)))
            .catch(data => rsp.status(404).end(data.message))
    }

    function getGroupGamesByDuration(req, rsp) {
        gamesService.getGroupGamesByDuration(
            req.params.groupName,
            req.params.minVal,
            req.params.maxVal)
            .then(data => rsp.status(200).end(JSON.stringify(data)))
            .catch(data => rsp.status(404).end(data.message))
    }
}

