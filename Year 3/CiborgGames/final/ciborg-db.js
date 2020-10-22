'use strict'

const request = require('request-promise')

module.exports = function(port) {

    return {
        createUser: createUser,
        getUserByUsername: getUserByUsername,
        getUserById: getUserById,
        createGroup: createGroup,
        editGroup: editGroup, 
        getAllGroups: getAllGroups,
        getGroupDetails: getGroupDetails,
        insertGameInGroup: insertGameInGroup,
        removeGameFromGroup: removeGameFromGroup,
        getGroupGamesByDuration: getGroupGamesByDuration
    }

    //Criar username
    function createUser(user) {
        const options = {
            'method': 'POST',
            'uri': `http://${port}/users/_doc?refresh=true`,
            'json': true,
            'body': {
                'id': user.id,
                'username': user.username,
                'password' : user.password,
                'groups': []
            }
        };
        return request(options)
            .then(body => {
                return (body._shards.failed === 0) ? {"message": `Username '${user}' was created!`} : Promise.reject({"message": `Username '${user}' was not created!`})
            })
    }

    //Obter user pelo seu username
    function getUserByUsername(username){
        const options = {
            'method': 'GET',
            'uri': `http://${port}/users/_search`,
            'json': true,
            'body': {
                'size': 1,
                'query' : {
                    'match': {
                        'username': username
                    }
                }
            }
        };
        return request(options)
            .then(body => {
                return body.hits.hits[0]._source      //hits that match the query
            })
            .catch(error => {
                return {"message": "User does not exist!"}
            })
    }

    //Obter user pelo seu Id
    function getUserById(userId){
        const options = {
            'method': 'GET',
            'uri': `http://${port}/users/_search`,
            'json': true,
            'body': {
                'size': 1,
                'query' : {
                    'match': {
                        'id': userId
                    }
                }
            }
        };
        return request(options)
            .then(body => {
                return body.hits.hits[0]._source      //hits that match the query
            })
            .catch(error => {
                return {"message": "User does not exist!"}
            })
    }

    // Criar grupo atribuindo-lhe um nome e descrição
    function createGroup(userId, name, desc) {
        const options = {
            'method': 'POST',
            'uri': `http://${port}/users/_doc/_update_by_query?refresh=true`,
            'json': true,
            'body': {
                "query": {
                    "match": {
                        "id": userId
                    }
                },
                "script":{
                    "lang": "painless",
                    "inline": "ctx._source.groups.add(params.newGroup)",
                    "params": {
                        "newGroup": {
                            "groupName": name,
                            "games": [],
                            "description": desc
                        }
                    }
                }
            }
        };
        return request(options)
            .then(body => {
                return (body.updated === 1) ? {"message": `Group '${name}' was created with the description: '${desc}'!`} : Promise.reject({"message": `Group '${name}' was not created!`})
            })
    }

    // Editar grupo, alterando o seu nome e descrição
    function editGroup(userId, oldName, newName, desc) {
        const options = {
            'method': 'POST',
            'uri': `http://${port}/users/_doc/_update_by_query?refresh=true`,
            'json': true,
            'body': {

                "query":{
                    "match":{
                        "id": userId
                    }
                },
                "script":{
                    "lang": "painless",
                    "inline": "for(int i = 0; i < ctx._source.groups.size(); i++){ " +
                        "if(ctx._source.groups[i].groupName == params.oldGroupName) { " +
                            "ctx._source.groups[i].groupName = params.newGroupName; " +
                            "ctx._source.groups[i].description = params.newDesc; }" +
                        "}",
                    "params":{
                        "oldGroupName": oldName,
                        "newGroupName": newName,
                        "newDesc": desc
                    }
                }
            }
        };
        return request(options)
            .then(body => {
                return (body.updated === 1) ? {"message" : `Group '${oldName}' is now '${newName}' with the description '${desc}'`} : {"message" : `Could not find group with '${oldName}' name`}
            })
    }

    // Listar todos os grupos
    function getAllGroups(userId) {
        const options = {
            'method': 'GET',
            'uri': `http://${port}/users/_search`,
            'json': true,
            'body': {
                'query' : {
                    'match': {
                        'id': userId
                    }
                }
            }
        };
        return request(options)
            .then(body => {
                return body.hits.hits[0]._source.groups
            })
            .catch(error => {
                throw new Error(error.error.error.reason)
            })
    }

    // Obter os detalhes de um grupo, com o seu nome, descrição e nomes dos jogos que o constituem.
    function getGroupDetails(userId,name){
        const options = {
            'method': 'GET',
            'uri': `http://${port}/users/_search`,
            'json': true,
            'body': {
                'query' : {
                    'match': {
                        'id': userId
                    }
                }
            }
        };
        return request(options)
            .then(body => {
                let groups = body.hits.hits[0]._source.groups
                if(groups.length === 0)
                    return {"message": `Group '${name}' does not have games!`}
                for(let i = 0; i < groups.length; i++){
                    let group = groups[i]
                    if(group.groupName === name)
                        return group
                }
                return Promise.reject()
            })
            .catch(error => {
                return {"message": "Group does not exist!"}
            })
    }

    // Adicionar um jogo a um grupo
    function insertGameInGroup(userId, name, game){
        const options = {
            'method': 'POST',
            'uri': `http://${port}/users/_doc/_update_by_query?refresh=true`,
            'json': true,
            'body': {
                "query": {
                    "match": {
                        "id": userId
                    }
                },
                "script": {
                    "lang": "painless",
                    "inline": "for (int i = 0; i < ctx._source.groups.size(); i++){if (ctx._source.groups[i].groupName == params.groupName) ctx._source.groups[i].games.add(params.newGame);}",
                    "params": {
                        "groupName": name,
                        "newGame": game
                    }
                }
            }
        }
        return request(options)
            .then(body => {
                return (body.updated === 0) ? {"message": `Game '${game.name}' was not added to ${name}`} : {"message": `Game '${game.name}' was added to ${name}`}
            })
    }

    // Remover um jogo de um grupo
    function removeGameFromGroup(userId, groupName, gameName, gameIndex){
        const options = {
            'method': 'POST',
            'uri': `http://${port}/users/_doc/_update_by_query?refresh=true`,
            'json': true,
            'body': {
                "query": {
                    "match": {
                        "id": userId
                    }
                },
                "script": {
                    "lang": "painless",
                    "inline": "for (int i = 0; i < ctx._source.groups.size(); i++){if (ctx._source.groups[i].groupName == params.groupName) ctx._source.groups[i].games.remove(params.gameIndex);}",
                    "params": {
                        "groupName": groupName,
                        "gameIndex": gameIndex
                    }
                }
            }
        };
        return request(options)
            .then(body => {
                return (body.updated === 0) ? {"message": `Game '${gameName}' was not deleted from ${groupName}`} : {"message": `Game '${gameName}' was deleted from ${groupName}`}
            })
    }

    /*Obter os jogos de um grupo que têm um tempo de duração máxima entre dois valores temporais (mínimo e máximo), 
    sendo estes valores parametrizáveis no pedido. Os jogos vêm ordenados por ordem crescente do tempo de duração máxima dos jogos.*/
    function getGroupGamesByDuration(userId, name, minPlaytime, maxPlaytime){
        const options = {
            'method': 'GET',
            'uri': `http://${port}/users/_search`,
            'json': true,
            'body': {
                'query' : {
                    'match': {
                        'id': userId
                    }
                }
            }
        };
        return request(options)
            .then(body => {
                let groups = body.hits.hits[0]._source.groups
                let group
                for(let i = 0; i < groups.length; i++){
                    if(groups[i].groupName === name)
                        group = groups[i]
                }
                if(group === null)
                    return Promise.reject()
                let games = group.games
                if(games.length === 0) throw new Error("No games on this group")
                games = games.filter(game => parseInt(game.min_playtime) > parseInt(minPlaytime) &&
                    parseInt(game.max_playtime) < parseInt(maxPlaytime))
                games.sort(function(a,b){
                    return a.max_playtime-b.max_playtime;
                })
                return games
            })
            .catch(error => {
                throw new Error(JSON.stringify({"message": error}))
            })
    }
}