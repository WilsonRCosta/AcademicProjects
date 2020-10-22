'use strict'

module.exports = function(gamesService) {

    return {
        getAllGames: getAllGames, //done tested
        getGameByName: getGameByName, //done tested
        createGroup: createGroup, //done
        editGroup: editGroup, //done
        getAllGroups: getAllGroups, //done
        getGroupDetails: getGroupDetails, //done
        insertGameInGroup: insertGameInGroup, //done
        removeGameFromGroup: removeGameFromGroup, //done
        getGroupGamesByDuration: getGroupGamesByDuration //done
    }

    function getAllGames(req, rsp) {
        gamesService.getAllGames(sendResponse(rsp, 200))
    }

    function getGameByName(req, rsp) {
        gamesService.getGameByName(req.params.gameName, sendResponse(rsp, 200))
    }

    function createGroup(req, rsp) {
        gamesService.createGroup(req.params.groupName, req.params.desc, sendResponse(rsp, 201))
    }

    function editGroup(req, rsp) {
        gamesService.editGroup(req.params.oldName, req.params.newName, req.params.desc, sendResponse(rsp, 201))
    }

    function getAllGroups(req, rsp) {
        gamesService.getAllGroups(sendResponse(rsp, 200))
    }

    function getGroupDetails(req, rsp){
        gamesService.getGroupDetails(req.params.groupName, sendResponse(rsp, 200))
    }

    function insertGameInGroup(req, rsp) {
        gamesService.insertGameInGroup(req.params.groupName, req.params.gameName, sendResponse(rsp, 201))
    }

    function removeGameFromGroup(req, rsp) {
        gamesService.removeGameFromGroup(req.params.groupName, req.params.gameName, sendResponse(rsp, 201))
    }

    function getGroupGamesByDuration(req, rsp) {
        gamesService.getGroupGamesByDuration(
            req.params.groupName,
            req.params.minVal,
            req.params.maxVal,
            sendResponse(rsp, 200))
    }

    function sendResponse(rsp, status) {
        return function (err, data) {
            rsp.writeHead(status, {'Content-type' : 'application/json'})
            rsp.end(JSON.stringify(data))
        }
    }
}

