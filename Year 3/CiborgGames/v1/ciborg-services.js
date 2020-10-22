'use strict'

module.exports = function (gamesData, groupsData) {
    
    return {
        /* FROM BOARD-GAMES */
        getAllGames: getAllGames, //done tested
        getGameByName: getGameByName, //done tested

        /* FROM DATABASE */
        createGroup: createGroup, //done
        editGroup: editGroup, //done
        getAllGroups: getAllGroups, //done
        getGroupDetails: getGroupDetails, //done
        insertGameInGroup: insertGameInGroup,
        removeGameFromGroup: removeGameFromGroup,
        getGroupGamesByDuration: getGroupGamesByDuration
    }

    function getAllGames(processPopularGames) {
        gamesData.getAllGames(processPopularGames)
    }
    
    function getGameByName(name, processGamesByName) {
        gamesData.getGameByName(name, processGamesByName)
    }

    function createGroup(name, desc, process){
        groupsData.getGroupDetails(name, processGroup)
        function processGroup(err, group){
            if(group.message)
                groupsData.createGroup(name, desc, response)
            else
                process(err,{"message":"Group already exists!"})
        }

        function response(err, group) {
            process(err, group)
        }
    }

    function editGroup(oldName, newName, desc, process){
        groupsData.editGroup(oldName, newName, desc, response)

        function response(err, group) {
            process(err, group)
        }
    }

    function getAllGroups(processAllGroups) {
        groupsData.getAllGroups(processAllGroups)
    }

    function getGroupDetails(name, processGroup){
        groupsData.getGroupDetails(name, callback)
        
        function callback(err, group) {
            processGroup(err, group)
        }
    }

    function insertGameInGroup(groupName, gameName, processGameGroup){
        groupsData.getGroupDetails(groupName, processGroup)

        function processGroup(err, group){
            let gameExists = false;

            group[0].games.forEach(function(g) {
                String.prototype.replaceAll = function(search, replacement) {
                    var target = this;
                    return target.split(search).join(replacement);
                };
                let gameNameWithoutSpaces = g["Game Name"].replaceAll(" ", "%20")
                if(gameNameWithoutSpaces === gameName){
                    gameExists = true;
                    return processGameGroup(err,{"message" : "Game already exists on group!"})
                }
            });
            if(!gameExists){
                gamesData.getGameByName(gameName, processGame)
                function processGame(err, game) {
                    if(!game) return processGameGroup(err,{"message" : "Game does not exist!"})
                    let gameObj = game[0]
                    groupsData.insertGameInGroup(groupName, gameObj[0], callback)
                }
            }
        }

        function callback(err, message){
            return processGameGroup(err,message)
        }
    }

    function removeGameFromGroup(groupName, gameName, processGameGroup){
        groupsData.getGroupDetails(groupName, processGroup)

        function processGroup(err, group){
            let gameExists = false;
            let gameIndex = 0;

            for(;gameIndex < group[0].games.length; gameIndex++){
                String.prototype.replaceAll = function(search, replacement) {
                    var target = this;
                    return target.split(search).join(replacement);
                };
                let gameNameWithoutSpaces = group[0].games[gameIndex]["Game Name"].replaceAll(" ", "%20")
                if(gameNameWithoutSpaces === gameName){
                    gameExists = true;
                    break;
                }
            }

            if(!gameExists) return processGameGroup(err,{"message" : "Game does not exists on group!"});
            groupsData.removeGameFromGroup(groupName,gameName,gameIndex,callback)
        }

        function callback(err, message){
            return processGameGroup(err,message)
        }
    }

    function getGroupGamesByDuration(name, minVal, maxVal, processGroupGamesByDuration){
        groupsData.getGroupDetails(name, processGroup)

        function processGroup(err, group){
            groupsData.getGroupGamesByDuration(name,minVal,maxVal,processDuration)
            function processDuration(err, games) {
                return processGroupGamesByDuration(err, games)
            }
        }
    }
}