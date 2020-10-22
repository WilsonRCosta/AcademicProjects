'use strict'

const request = require('request-promise')

module.exports = function() {
    const client_id = 'JLBr5npPhV'
    let hostpath = `https://www.boardgameatlas.com/api/search?pretty=true&client_id=${client_id}&%s`
    return {
        getAllGames: getAllGames,
        getGameDetails:getGameDetails
    }

    // Obter a lista dos jogos mais populares
    function getAllGames() {
        const options = {
            method : 'GET',
            url : hostpath.replace("%s", "order_by=popularity"),
            json : true
        }
        console.log(options.url);
        return request(options)
            .then(body => printJsonData(body))
            .catch(error => {throw new Error("Error getting games from API\n"+error)})
    }

    // Mostrar os detalhes de apenas um jogo
    function getGameDetails(name) {
        const options = {
            method : 'GET',
            url : hostpath.replace("%s", "name=".concat(name)),
            json : true
        }
        console.log(options.url);
        return request(options)
            .then(body => printJsonData(body))
            .catch(error => {throw new Error("Error getting "+name+" game from API\n"+error)})
    }

    function printJsonData(body) {
        return body.games.map( e => {
            let average_user_rating = 0.00
            if(e.average_user_rating && e.average_user_rating % 1 !== 0)
                average_user_rating = e.average_user_rating.toFixed(2)
            return [{
                "id": e.id,
                "name":e.name,
                "year_published":e.year_published,
                "min_players":e.min_players,
                "max_players":e.max_players,
                "min_playtime":e.min_playtime,
                "max_playtime":e.max_playtime,
                "min_age":e.min_age,
                "description":e.description,
                "image_url":e.image_url,
                "price": e.price,
                "publishers":e.publishers,
                "average_user_rating": average_user_rating,
                "official_url":e.official_url,
                "rules_url":e.rules_url
            }
        ]})
    }
}

