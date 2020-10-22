'use strict'

module.exports= function(){

    const db = {
        message : null,
        groups : []
    }
    return {
        createGroup: createGroup,
        editGroup: editGroup, 
        getAllGroups: getAllGroups,
        getGroupDetails: getGroupDetails,
        insertGameInGroup: insertGameInGroup,
        removeGameFromGroup: removeGameFromGroup,
        getGroupGamesByDuration: getGroupGamesByDuration
    }

    function createGroup(name, desc){
        return new Promise((resolve, reject)=>{
            db.groups.push(
                [{
                    "groupName": name,
                    "games": [],
                    "description": desc
                }])
            resolve(db.groups)
        })
    }

    function editGroup(oldName, newName, desc){
        return new Promise((resolve, reject)=>{
            for(let i = 0; i < db.groups.length; i++){
                let group = db.groups[i]
                if(group[0]["groupName"] === oldName){
                    group[0]["groupName"] = newName
                    group[0]["description"] = desc
                    return resolve(db.groups)
                }
            }
        })
    }

    function getAllGroups(){
        return new Promise((resolve, reject)=>{
            resolve(db.groups)
        })
    }

    function getGroupDetails(name){
        return new Promise((resolve, reject)=>{
            for(let i = 0; i < db.groups.length; i++){
                let group = db.groups[i]
                if(group[0]["groupName"] === name)
                    return resolve(db.groups[i])
            }
            db.message = []
            resolve(db)
        })
    }

    function insertGameInGroup(groupName, gameName){
        return new Promise((resolve, reject)=>{
            for(let i = 0; i < db.groups.length; i++){
                let group = db.groups[i]
                if(group[0]["groupName"] === groupName) {
                    db.groups[i][0].games.push(gameName)
                    return resolve(db.groups[i])
                }
            }
            reject(db)
        })
    }

    function removeGameFromGroup(groupName, gameName){
        return new Promise((resolve, reject)=>{
            for(let i = 0; i < db.groups.length; i++){
                let group = db.groups[i]
                if(group[0]["groupName"] === groupName) {
                    db.groups[i][0].games.forEach( (game, index, object) => {
                        if(game['Game Name'] === gameName)
                            object.splice(index, 1);
                    })
                    return resolve(db.groups[i])
                }
            }
            reject(db)
        })
    }

    function getGroupGamesByDuration(groupName, minPlaytime, maxPlaytime){
        return new Promise((resolve, reject)=>{
            for(let i = 0; i < db.groups.length; i++){
                let group = db.groups[i]
                if(group[0]["groupName"] === groupName) {
                    let games = db.groups[i][0].games
                    games = games.filter(game => parseInt(game["Min Playtime"]) > parseInt(minPlaytime) &&
                        parseInt(game["Max Playtime"]) < parseInt(maxPlaytime))
                    games.sort(function(a,b){
                        return a["Max Playtime"]-b["Max Playtime"];
                    })
                    return resolve(db.groups[i])
                }
            }
            reject(db)
        })
    }
}