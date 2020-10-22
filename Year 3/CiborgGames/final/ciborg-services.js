'use strict'

module.exports = function (gamesData, groupsData) {
    
    return {
        getAllGames: getAllGames,
        getGameDetails: getGameDetails,
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

    function getGameDetails(name) {
        return gamesData.getGameDetails(name)
            .catch(error => {throw new Error(error)})
    }
    
    function createGroup(userId, name, desc){
        if(userId != null)
            return groupsData.getGroupDetails(userId, name)
                    .then(group => {
                        if(group.message)
                            return groupsData.createGroup(userId, name, desc)
                        return Promise.reject("Group already exists!")
                    })
                    .catch(error => {
                        throw new Error(JSON.stringify({"message":error}))
                    })
        return Promise.reject({"message":"User not logged in"})
    }

    function editGroup(userId, oldName, newName, desc){
        if(userId != null)
            return groupsData.getGroupDetails(userId, oldName)
                .then(group => {
                    if(group.message)
                        return Promise.reject("Group '"+oldName+"' does not exist!")
                    return groupsData.editGroup(userId, group.groupName, newName, desc)
                })
                .catch(groupError => {
                    throw new Error(JSON.stringify({"message" : groupError}))
                })
        return Promise.reject({"message":"User not logged in"})
    }

    function getAllGroups(userId) {
        if(userId != null)
            return groupsData.getAllGroups(userId)
                .catch(error => {
                    throw new Error(JSON.stringify({"message": error.message}))
                })
        return Promise.reject({"message":"User not logged in"})
    }

    function getGroupDetails(userId, groupName){
        if(userId != null)
            return groupsData.getGroupDetails(userId, groupName)
                .then(group => {
                    if(group.message)
                        return Promise.reject("Group '"+groupName+"' does not exist!")
                    return group
                })
                .catch(error => {
                    throw new Error(JSON.stringify({"message" : error}))
                })
        return Promise.reject({"message":"User not logged in"})
    }

    function insertGameInGroup(userId, groupName, gameName){
        if(userId != null)
            return groupsData.getGroupDetails(userId, groupName)
                    .then(group => {
                        if(group.message)
                            return Promise.reject({"status" : 404, "message" : "Group '"+groupName+"' does not exist!"})
                        let gameExists = false;

                        group.games.forEach(function(g) {
                            if(g.name === gameName){
                                gameExists = true;
                                return
                            }
                        });
                        if(gameExists) return Promise.reject({"status" : 409, "message" : "Game '"+gameName+"' already exists on group!"})
                        return gamesData.getGameDetails(gameName)
                            .then(game => {
                                if(!game) return Promise.reject({"status" : 404, "message" : "Game does not exist!"})
                                let gameObj = game[0]
                                return groupsData.insertGameInGroup(userId, groupName, gameObj[0])
                            })
                            .catch(error => {
                                return Promise.reject({"status" : 404, "message" : error.message})
                            })
                    })
                .catch(error => {
                    throw new Error(JSON.stringify(error))
                })
        return Promise.reject({"status" : 401, "message" : '{"status" : 401,"message":"User not logged in"}'})
    }

    function removeGameFromGroup(userId, groupName, gameName){
        if(userId != null)
            return groupsData.getGroupDetails(userId, groupName)
                    .then(group => {
                        if(group.message)
                            return Promise.reject("Group '"+groupName+"' does not exist!")
                        let gameExists = false;
                        let gameIndex = 0;

                        for(;gameIndex < group.games.length; gameIndex++){
                            if(group.games[gameIndex].name === gameName){
                                gameExists = true;
                                break;
                            }
                        }

                        if(!gameExists) return Promise.reject("Game '"+gameName+"' does not exist!")
                        return groupsData.removeGameFromGroup(userId, groupName,gameName,gameIndex)
                    })
                .catch(error => {
                    throw new Error(JSON.stringify({"message" : error}))
                })
        return Promise.reject({"message":"User not logged in"})
    }

    function getGroupGamesByDuration(userId, name, minVal, maxVal){
        if(userId != null)
            return groupsData.getGroupDetails(userId, name)
                .then(group => {
                    if(group.message)
                        return Promise.reject("Group '"+name+"' does not exist!")
                    return groupsData.getGroupGamesByDuration(userId, group.groupName,minVal,maxVal)
                        .catch(error => {
                            throw new Error(`There's no games on the group '${name}'`)
                        })
                })
                .catch(error => {
                    throw new Error(JSON.stringify({"message" : error.message}))
                })
        return Promise.reject({"message":"User not logged in"})
    }
}