'use strict'

module.exports = function (gamesData, groupsData) {
    
    return {
        getAllGames: getAllGames,
        getGameByName: getGameByName,
        createGroup: createGroup,
        editGroup: editGroup,
        getAllGroups: getAllGroups,
        getGroupDetails: getGroupDetails,
        insertGameInGroup: insertGameInGroup,
        removeGameFromGroup: removeGameFromGroup,
        getGroupGamesByDuration: getGroupGamesByDuration
    }

    function getAllGames() {
        return gamesData.getAllGames()
            .catch(error => {throw new Error(error)})
    }
    
    function getGameByName(name) {
        return gamesData.getGameByName(name)
            .catch(error => {throw new Error(error)})
    }

    function createGroup(name, desc){
        return groupsData.getGroupDetails(name)
                .then(group => {
                    if(group.message)
                        return groupsData.createGroup(name, desc)
                    return Promise.reject("Group already exists!")
                })
                .catch(error => {
                    throw new Error(JSON.stringify({"message":error}))
                })
    }

    function editGroup(oldName, newName, desc){
        return groupsData.getGroupDetails(oldName)
            .then(group => {
                if(group.message)
                    return Promise.reject("Group '"+oldName+"' does not exist!")
                return groupsData.editGroup(group[0].groupName, newName, desc)
            })
            .catch(groupError => {
                throw new Error(JSON.stringify({"message" : groupError}))
            })
    }

    function getAllGroups() {
        return groupsData.getAllGroups()
            .catch(error => {
                throw new Error(JSON.stringify({"message" : error.message}))
            })
    }

    function getGroupDetails(groupName){
        return groupsData.getGroupDetails(groupName)
            .then(group => {
                if(group.message)
                    return Promise.reject("Group '"+groupName+"' does not exist!")
                return group
            })
            .catch(error => {
                throw new Error(JSON.stringify({"message" : error}))
            })
    }

    function insertGameInGroup(groupName, gameName){
        return groupsData.getGroupDetails(groupName)
                .then(group => {
                    if(group.message)
                        return Promise.reject({"status" : 404, "message" : "Group '"+groupName+"' does not exist!"})
                    let gameExists = false;

                    group[0].games.forEach(function(g) {
                        if(g["Game Name"] === gameName){
                            gameExists = true;
                            return
                        }
                    });
                    if(gameExists) return Promise.reject({"status" : 409, "message" : "Game '"+gameName+"' already exists on group!"})
                    return gamesData.getGameByName(gameName)
                        .then(game => {
                            if(!game) return Promise.reject({"status" : 404, "message" : "Game does not exist!"})
                            let gameObj = game[0]
                            return groupsData.insertGameInGroup(groupName, gameObj[0])
                        })
                        .catch(error => {
                            return Promise.reject({"status" : 404, "message" : error.message})
                        })
                })
            .catch(error => {
                throw new Error(JSON.stringify(error))
            })
    }

    function removeGameFromGroup(groupName, gameName){
        return groupsData.getGroupDetails(groupName)
                .then(group => {
                    if(group.message)
                        return Promise.reject("Group '"+groupName+"' does not exist!")
                    let gameExists = false;
                    let gameIndex = 0;

                    for(;gameIndex < group[0].games.length; gameIndex++){
                        if(group[0].games[gameIndex]["Game Name"] === gameName){
                            gameExists = true;
                            break;
                        }
                    }

                    if(!gameExists) return Promise.reject("Game '"+gameName+"' does not exist!")
                    return groupsData.removeGameFromGroup(groupName,gameName,gameIndex)
                })
            .catch(error => {
                throw new Error(JSON.stringify({"message" : error}))
            })
    }

    function getGroupGamesByDuration(name, minVal, maxVal){
        return groupsData.getGroupDetails(name)
            .then(group => {
                if(group.message)
                    return Promise.reject("Group '"+name+"' does not exist!")
                return groupsData.getGroupGamesByDuration(group[0].groupName,minVal,maxVal)
                    .catch(error => {
                        throw new Error(`There's no games on the group '${name}'`)
                    })
            })
            .catch(error => {
                throw new Error(JSON.stringify({"message" : error.message}))
            })
    }
}