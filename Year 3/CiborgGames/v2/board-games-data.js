'use strict'

const request = require('request-promise')

module.exports = function() {
    let hostpath = "https://www.boardgameatlas.com/api/search?%s&pretty=true&client_id=SB1VGnDv7M";

    return {
        getAllGames: getAllGames,
        getGameByName: getGameByName
    }

    // Obter a lista dos jogos mais populares
    function getAllGames() {
        const query = hostpath.replace("%s", "order_by=popularity");
        const options = {
            method : 'GET',
            url : `${query}`,
            json : true
        }
        console.log(options.url);
        return request(options)
            .then(body => printJsonData(body))
            .catch(error => {throw new Error("Error getting games from API\n"+error)})
    }

    // Pesquisar jogos pelo nome 
    function getGameByName(name) {
        const query = hostpath.replace("%s", "name=".concat(name));
        const options = {
            method : 'GET',
            url : `${query}`,
            json : true
        }
        console.log(options.url);
        return request(options)
            .then(body => printJsonData(body))
            .catch(error => {throw new Error("Error getting "+name+" game from API\n"+error)})
    }
    
    function printJsonData(body) {
        return body.games.map( e => [
            {
                "Game Name":e.name,
                "Year Published":e.year_published,
                "Min-Players":e.min_players,
                "Max-Players":e.max_players,
                "Min Playtime":e.min_playtime,
                "Max Playtime":e.max_playtime,
                "Min Age":e.min_age,
                "Description":e.description,
                "Image":e.image_url,
                "Publishers":e.publishers,
                "Average Rating":e.average_user_rating,
                "Official URL":e.official_url,
                "Official Rules":e.rules_url
            }
        ])
    }
}

