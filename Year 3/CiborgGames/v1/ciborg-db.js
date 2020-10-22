'use strict'

const request = require('request');

module.exports = function(port) {

    return {
        createGroup: createGroup, //done
        editGroup: editGroup, 
        getAllGroups: getAllGroups, //done
        getGroupDetails: getGroupDetails,
        insertGameInGroup: insertGameInGroup,
        removeGameFromGroup: removeGameFromGroup,
        getGroupGamesByDuration: getGroupGamesByDuration
    }

    // Criar grupo atribuindo-lhe um nome e descrição
    function createGroup(name, desc, process) {
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

        request.post(options,(err,res,body) => {
            if(err == null) {
                let message = (body._shards.failed === 0) ? {"message": `Group '${name}' was created with the description: '${desc}'!`} : {"message": `Group '${name}' was not created!`}
                return process(err, message)
            }
        });
    }

    // Editar grupo, alterando o seu nome e descrição
    function editGroup(oldName, newName, desc, process) {
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
        request.post(options,(err,res,body) => {
            if(err == null) {
                let message = (body.updated === 1) ? {"message" : `Group '${oldName}' is now '${newName}' with the description '${desc}'`} : {"message" : `Could not find group with '${oldName}' name`}
                return process(null, message)
            }
        });
    }

    // Listar todos os grupos
    function getAllGroups(process) {
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

        request.get(options, (err, res, body) => {
            if(err == null) {
                if(body.status === 404)
                    return process(null,{"message": body.error.reason})
                let groups = []
                let bodyHits = body.hits.hits       //hits that match the query
                bodyHits.forEach(h => { groups.push(h._source) });
                return process(null, groups)
            }
        });
    }

    // Obter os detalhes de um grupo, com o seu nome, descrição e nomes dos jogos que o constituem.
    function getGroupDetails(name, process){
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

        request.get(options, (err, res, body) => {
            if(err == null) {
                if(body.status === 404 || body.hits.total.value === 0)
                    return process(null,{"message": "Group does not exist!"})
                let group = []
                let bodyHits = body.hits.hits[0]._source      //hits that match the query
                group.push(bodyHits)
                return process(null, group)
            }
        });
    }

    // Adicionar um jogo a um grupo
    function insertGameInGroup(name, game, process){
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

        request.post(options, (err, res, body) => {
            if(err == null) {
                let message = (body.updated === 0) ? {"message": `Game '${game["Game Name"]}' was not added to ${name}`} : {"message": `Game '${game["Game Name"]}' was added to ${name}`}
                return process(null, message)
            }
        });
    }

    // Remover um jogo de um grupo
    function removeGameFromGroup(groupName, gameName, gameIndex, process){
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
        request.post(options,(err,res,body) => {
            if(err == null) {
                let message = (body.updated === 0) ? {"message": `Game '${gameName}' was not deleted from ${groupName}`} : {"message": `Game '${gameName}' was deleted from ${groupName}`}
                return process(null, message)
            }
        });
    }

    //TODO
    /*Obter os jogos de um grupo que têm um tempo de duração máxima entre dois valores temporais (mínimo e máximo), 
    sendo estes valores parametrizáveis no pedido. Os jogos vêm ordenados por ordem crescente do tempo de duração máxima dos jogos.*/
    function getGroupGamesByDuration(name, minPlaytime, maxPlaytime, process){
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

        request.get(options, (err, res, body) => {
            if(err == null) {
                let games = body.hits.hits[0]._source.games
                games = games.filter(game => parseInt(game["Min Playtime"]) > parseInt(minPlaytime) &&
                    parseInt(game["Max Playtime"]) < parseInt(maxPlaytime))
                games.sort(function(a,b){
                    return a["Max Playtime"]-b["Max Playtime"];
                })
                return process(null, games)
            }
        });
    }
}