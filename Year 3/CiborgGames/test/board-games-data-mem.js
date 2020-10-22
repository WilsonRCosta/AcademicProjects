'use strict'

module.exports = function(){

    let games = [[[{
        "Game Name": "Spirit Island",
        "Year Published": 2016,
        "Min-Players": 1,
        "Max-Players": 4,
        "Min Playtime": 90,
        "Max Playtime": 120,
        "Min Age": 13,
        "Description": "Powerful Spirits have existed on this isolated island for time immemorial. They are both part of the natural world and - at the same time - something beyond nature. Native Islanders, known as the Dahan, have learned how to co-exist with the spirits, but with a healthy dose of fear and reverence. However, now, the island has been &quot;discovered&quot; by invaders from a far-off land. These would-be colonists are taking over the land and upsetting the natural balance, destroying the presence of Spirits as they go. As Spirits, you must grow in power and work together to drive the invaders from your island... before it's too late!",
        "Image": "https://s3-us-west-1.amazonaws.com/5cc.images/games/uploaded/1559254941010-61PJxjjnbfL.jpg",
        "Publishers": [
            "Greater Than Games"
        ],
        "Average Rating": 3.9416666666666678,
        "Official URL": "https://store.greaterthangames.com/spirit-island.html?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads",
        "Official Rules": "https://drive.google.com/file/d/0B9kp130SgLtdcGxTcTFodlhaWDg/view"
    }]],
    [[{
        "Game Name": "Gloomhaven",
        "Year Published": 2017,
        "Min-Players": 1,
        "Max-Players": 4,
        "Min Playtime": 60,
        "Max Playtime": 150,
        "Min Age": 12,
        "Description": "Gloomhaven is a game of Euro-inspired tactical combat in a persistent world of shifting motives. Players will take on the role of a wandering adventurer with their own special set of skills and their own reasons for traveling to this dark corner of the world.<br />\r\n<br />\r\nPlayers must work together out of necessity to clear out menacing dungeons and forgotten ruins. In the process they will enhance their abilities with experience and loot, discover new locations to explore and plunder, and expand an ever-branching story fueled by the decisions they make.<br />\r\n<br />\r\nThis is a legacy game with a persistent and changing world that is ideally played over many game sessions. After a scenario, players will make decisions on what to do, which will determine how the story continues, kind of like a &quot;Choose Your Own Adventure&quot; book. Playing through a scenario is a cooperative affair where players will fight against automated monsters using an innovative card system to determine the order of play and what a player does on their turn.",
        "Image": "https://s3-us-west-1.amazonaws.com/5cc.images/games/uploaded/1559254920151-51ulRXlJ7LL.jpg",
        "Publishers": [
            "Cephalofair Games"
        ],
        "Average Rating": 4.158730158730161,
        "Official URL": "http://www.cephalofair.com/gloomhaven?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads",
        "Official Rules": "https://online.flippingbook.com/view/598058/"
    }]]
    ]

    return {
        getAllGames: getAllGames,
        getGameByName: getGameByName
    }

    function getAllGames(){
        return new Promise((resolve, reject)=>{
            resolve(games)
        })
    }

    function getGameByName(name){
        return new Promise((resolve, reject)=>{
            for(let i = 0; i < games.length; i++){
                let game = games[i][0]
                if(game[0]["Game Name"] === name)
                    return resolve(games[i])
            }
            return reject({"message": "Group '"+name+"' not found!"})
        })
    }
}