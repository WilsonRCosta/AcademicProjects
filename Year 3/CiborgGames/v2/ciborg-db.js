'use strict'

const request = require('request-promise')

module.exports = function(port) {

    return {
        createGroup: createGroup,
        editGroup: editGroup, 
        getAllGroups: getAllGroups,
        getGroupDetails: getGroupDetails,
        insertGameInGroup: insertGameInGroup,
        removeGameFromGroup: removeGameFromGroup,
        getGroupGamesByDuration: getGroupGamesByDuration
    }

    // Criar grupo atribuindo-lhe um nome e descrição
    function createGroup(name, desc) {
        const options = {
            'method': 'POST',
            'uri': `http://${port}/groups/_doc`,
            'json': true,
            'body': {
                'groupName': name,
                'games': [],
                'description': desc
            }
        };
        return request(options)
            .then(body => {
                return (body._shards.failed === 0) ? {"message": `Group '${name}' was created with the description: '${desc}'!`} : Promise.reject({"message": `Group '${name}' was not created!`})
            })
    }

    // Editar grupo, alterando o seu nome e descrição
    function editGroup(oldName, newName, desc) {
        const options = {
            'method': 'POST',
            'uri': `http://${port}/groups/_doc/_update_by_query`,
            'json': true,
            'body': {
                'query': {
                    'match': {
                        'groupName': oldName
                    }
                },
                    'script': {
                        'source': `ctx._source.groupName = '${newName}'; ctx._source.description = '${desc}'`
                }
            }
        };
        return request(options)
            .then(body => {
                return (body.updated === 1) ? {"message" : `Group '${oldName}' is now '${newName}' with the description '${desc}'`} : {"message" : `Could not find group with '${oldName}' name`}
            })
    }

    // Listar todos os grupos
    function getAllGroups() {
        const options = {
            'method': 'GET',
            'uri': `http://${port}/groups/_search`,
            'json': true,
            'body': {
                'query' : {
                    'match_all' : {}
                }
            }
        };
        return request(options)
            .then(body => {
                let groups = []
                let bodyHits = body.hits.hits       //hits that match the query
                bodyHits.forEach(h => { groups.push(h._source) });
                return groups
            })
            .catch(error => {
                throw new Error(error.error.error.reason)
            })
    }

    // Obter os detalhes de um grupo, com o seu nome, descrição e nomes dos jogos que o constituem.
    function getGroupDetails(name){
        const options = {
            'method': 'GET',
            'uri': `http://${port}/groups/_search`,
            'json': true,
            'body': {
                'size': 1,
                'query' : {
                    'match': {
                        'groupName': name
                    }
                }
            }
        };
        return request(options)
            .then(body => {
                let group = []
                let bodyHits = body.hits.hits[0]._source      //hits that match the query
                group.push(bodyHits)
                return group
            })
            .catch(error => {
                return {"message": "Group does not exist!"}
            })
    }

    // Adicionar um jogo a um grupo
    function insertGameInGroup(name, game){
        const options = {
            'method': 'POST',
            'uri': `http://${port}/groups/_doc/_update_by_query`,
            'json': true,
            'body': {
                "query": {
                    "match": {
                        "groupName": name
                    }
                },
                "script": {
                    "source": "ctx._source.games.add(params.newGame)",
                    "params": {
                        "newGame": game
                    }
                }
            }
        }
        return request(options)
            .then(body => {
                return (body.updated === 0) ? {"message": `Game '${game["Game Name"]}' was not added to ${name}`} : {"message": `Game '${game["Game Name"]}' was added to ${name}`}
            })
    }

    // Remover um jogo de um grupo
    function removeGameFromGroup(groupName, gameName, gameIndex){
        const options = {
            'method': 'POST',
            'uri': `http://${port}/groups/_doc/_update_by_query`,
            'json': true,
            'body': {
                'query': {
                    'match': {
                        'groupName': groupName
                    }
                },
                'script': {
                    'source': "ctx._source.games.remove(params.GameIndex)",
                    'params': {
                        "GameIndex": gameIndex
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
    function getGroupGamesByDuration(name, minPlaytime, maxPlaytime){
        const options = {
            'method': 'GET',
            'uri': `http://${port}/groups/_search`,
            'json': true,
            'body': {
                'size': 1,
                'query' : {
                    'match': {
                        'groupName': name
                    }
                }
            }
        };
        return request(options)
            .then(body => {
                let games = body.hits.hits[0]._source.games
                if(games.length === 0) throw new Error("No games on this group")
                games = games.filter(game => parseInt(game["Min Playtime"]) > parseInt(minPlaytime) &&
                    parseInt(game["Max Playtime"]) < parseInt(maxPlaytime))
                games.sort(function(a,b){
                    return a["Max Playtime"]-b["Max Playtime"];
                })
                return games
            })
            .catch(error => {
                throw new Error(JSON.stringify({"message": error}))
            })
    }
}