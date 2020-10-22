'use strict'

require('bootstrap/dist/css/bootstrap.min.css')
require ('bootstrap')
const templates = require('./templates')

module.exports = {
    home: home,
    getAllGames: getAllGames,
    gameDetails: gameDetails,
    gamesByName: gamesByName,
    getAllGroups: getAllGroups,
    groupAlteration: groupAlteration,
    groupDetails: groupDetails,
    deleteGameFromGroup: deleteGameFromGroup,
    addGameToGroup: addGameToGroup,
    getGamesByDuration: getGamesByDuration,
    authentication: authentication,
    logout: logout
}

function home(obj, routeManager){
    routeManager.setMainContent(templates.home())
    const signUpModal = document.querySelector('#signUpForm')
    signUpModal.addEventListener('submit', (e) => {
        e.preventDefault()
        const username = document.querySelector("#signupUsername")
        const password = document.querySelector("#signupPassword")
        routeManager.changeRoute('signUp', {username: username.value, password: password.value})
        document.getElementById("closeSignup").click();
    })
    const loginModal = document.querySelector('#loginForm')
    loginModal.addEventListener('submit', (e) => {
        e.preventDefault()
        const username = document.querySelector("#loginUsername")
        const password = document.querySelector("#loginPassword")
        routeManager.changeRoute('login', {username: username.value, password: password.value})
        document.getElementById("closeLogin").click();

    })
    const logoutButton = document.querySelector('#logoutRef')
    logoutButton.addEventListener('click', (e) => {
        e.preventDefault()
        routeManager.changeRoute('logout');
        document.getElementById('signUpBtn').style.display="block";
        document.getElementById('loginBtn').style.display="block";
        document.getElementById('logoutRef').style.display="none";
    })
}

function getAllGames(games, routeManager){
    routeManager.setMainContent(templates.allGames({title:"Popular",games}))
    const formSearchGame = document.querySelector("#gamesByName")
    formSearchGame.addEventListener('submit', (e) =>{
        e.preventDefault()
        const inputGameName = document.querySelector("#inputGameName")
        routeManager.changeRoute('gamesByName', {name : inputGameName.value})
    })
}

function gamesByName(games, routeManager){
    routeManager.setMainContent(templates.allGames({title: "'"+games[0][0].name+"'", games}))
    document.getElementById("gamesByName").style.display="none";
}

function gameDetails(object, routeManager) {
    let addImg = object.addImage
    let game = object.games[0][0]
    let groups = object.groups
    if(groups === undefined){
        groups = []
        routeManager.setMainContent(templates.gameDetails({addImg, game, groups}))
        document.getElementById('gameAddition').style.display="none";
    } else {
        routeManager.setMainContent(templates.gameDetails({addImg, game, groups}))

        const addGameModal = document.querySelector('#addGame')
        addGameModal.addEventListener('click', (e) => {
            e.preventDefault()
            for (let idx = 0; idx < groups.length; idx++) {
                let groupName = groups[idx].groupName
                const groupDetailsRef = document.querySelector(`#groupBtn${idx}`)
                groupDetailsRef.addEventListener('click', (e) => {
                    e.preventDefault()
                    routeManager.changeRoute(`addGameToGroup/${groupName}/${game.name}`, {
                        groupName: groupName,
                        gameName: game.name
                    })
                })
            }
        })
    }
}

function getAllGroups(object, routeManager){
    let title = "List of groups"
    let editImg = object.editImage
    let disableEditImg = object.disableEditImage
    let deleteImg = object.deleteImage
    let groups = object.groups

    if(groups == null){
        routeManager.changeRoute('home')
        return
    }

    if(groups.message) groups = []
        routeManager.setMainContent(templates.allGroups({title:title,groups,editImg,disableEditImg,deleteImg}))

    const formSearchGroup = document.querySelector("#groupsByName")
    formSearchGroup.addEventListener('submit', (e) => {
        e.preventDefault()
        const inputGroupName = document.querySelector("#inputGroupName")
        routeManager.changeRoute('groupDetails/'+inputGroupName.value, inputGroupName.value)
    })

    const formCreateGroup = document.querySelector("#createGroup")
    formCreateGroup.addEventListener('submit', (e) => {
        e.preventDefault()
        const createInGName = document.querySelector("#createInGName")
        const createInGDesc = document.querySelector("#createInGDesc")
        routeManager.changeRoute('createGroup', {
            groupName : createInGName.value,
            description : createInGDesc.value
        })
    })

    for (let idx = 0; idx < groups.length; idx++) {
        const enableEdition = document.querySelector(`#button-edition-${idx}`)
        enableEdition.addEventListener('click', (e) => {
            e.preventDefault()
            document.getElementById(`divEditGroup${idx}`).style.display = "inline-block";
        })

        const disableEdition = document.querySelector(`#no-button-edition-${idx}`)
        disableEdition.addEventListener('click', (e) => {
            e.preventDefault()
            document.getElementById(`divEditGroup${idx}`).style.display = "none";
        })

        const formEditGroup = document.querySelector(`#editGroup${idx}`)
        formEditGroup.addEventListener('submit', (e) => {
            e.preventDefault()
            const inputGroupName = groups[idx].groupName
            const inputNewGroupName = document.querySelector(`#editInGName${idx}`)
            const inputNewGroupDescription = document.querySelector(`#editInGDesc${idx}`)
            let newGroupName = inputNewGroupName.value
            let newGroupDesc = inputNewGroupDescription.value
            if(newGroupName !== "" && newGroupDesc !== "")
                routeManager.changeRoute('editGroup', {
                    groupName : inputGroupName,
                    newGroupName: newGroupName,
                    newDescription : newGroupDesc
                })
            else {
                routeManager.setAlertContent(templates.alert_warning({message:  'Name or description of group cannot be empty.'}))
            }
        })
    }
}

function groupDetails(group, routeManager) {
    let delImg = group.delImage;
    let filterImg = group.filterImage;
    if(group.groupDetails == null){
        routeManager.changeRoute('getAllGroups')
        return
    }
    let groupDetails = group.groupDetails
    routeManager.setMainContent(templates.groupDetails({groupDetails,delImg, filterImg}))

    const formFilterGamesByDuration = document.querySelector("#formFilterGames")
    formFilterGamesByDuration.addEventListener('submit', (e) => {
        e.preventDefault()
        let minTime = document.querySelector("#minPlaytime").value
        let maxTime = document.querySelector("#maxPlaytime").value
        if(minTime !== "" && maxTime !== "")
            routeManager.changeRoute(`getGamesByDuration/${groupDetails.groupName}/${minTime}/${maxTime}`, {
                groupName: groupDetails.groupName, minTime: minTime, maxTime: maxTime
            })
        else {
            routeManager.setAlertContent(templates.alert_warning({message: 'Min or max time of game cannot be empty.'}))
        }
    })

    const resetFilter = document.querySelector(`#reset-filter`)
    resetFilter.addEventListener('click', (e) => {
        e.preventDefault()
        routeManager.changeRoute(`groupDetails/`+groupDetails.groupName)
    })
}

function getGamesByDuration(games, routeManager) {
    let rows = document.getElementById("gamesTable").rows;
    let sizeOfRows = rows.length - 1
    if(sizeOfRows > games.length){
        for(let i = sizeOfRows; i > games.length; i--){
            document.getElementById("gamesTable").deleteRow(i);
        }
    } else if (sizeOfRows < games.length){ return }
    for (let i = 1; i < rows.length; i++) {
        let game = games[i-1]
        changeNameColumn(rows[i],game)
        changeMinPlaytimeColumn(rows[i],game)
        changeMaxPlaytimeColumn(rows[i],game)
        changeDeleteColumn(i-1,rows[i],game)
    }
}

function changeNameColumn(row,game){
    let oldTd = row.cells[1]
    let newTd = document.createElement('td')
    let a = document.createElement('a');
    a.style.color = "black"
    a.href = "#gameDetails/"+game.name
    a.title = game.name
    let strong = document.createElement('strong');
    strong.appendChild(document.createTextNode(game.name))
    a.appendChild(strong)
    newTd.appendChild(a)
    oldTd.parentNode.replaceChild(newTd,oldTd)
}

function changeMinPlaytimeColumn(row,game) {
    let oldTd = row.cells[2]
    let newTd = document.createElement('td')
    newTd.appendChild(document.createTextNode(game.min_playtime))
    oldTd.parentNode.replaceChild(newTd,oldTd)
}

function changeMaxPlaytimeColumn(row,game) {
    let oldTd = row.cells[3]
    let newTd = document.createElement('td')
    newTd.appendChild(document.createTextNode(game.max_playtime))
    oldTd.parentNode.replaceChild(newTd,oldTd)
}

function changeDeleteColumn(index,row,game){
    let oldTd = row.cells[4]
    let img = document.getElementById("deleteGameImg-"+index)
    let new_td = document.createElement('td')
    let new_a = document.createElement('a')
    new_a.href = "#deleteGameFromGroup/"+window.location.hash.split('/')[1]+"/"+game.name
    new_a.title = "Delete "+game.name
    new_a.appendChild(img)
    new_td.appendChild(new_a)
    oldTd.parentNode.replaceChild(new_td,oldTd)
}

function authentication(obj, routeManager) {
    if(obj) {
        document.getElementById('logoutRef').style.display = "block";
        document.getElementById('signUpBtn').style.display = "none";
        document.getElementById('loginBtn').style.display = "none";
        routeManager.setAlertContent(templates.alert_success({message: 'User authenticated.'}))
    }
    routeManager.changeRoute('home')
}
function logout(obj, routeManager) {
    routeManager.setAlertContent(templates.alert_success({message: 'User logged out.'}))
    routeManager.changeRoute('home')
}

function groupAlteration(object, routeManager){
    routeManager.changeRoute('getAllGroups')
    let message = object.message
    routeManager.setAlertContent(templates.alert_success({message}))
}

function deleteGameFromGroup(group, routeManager){
    let url = window.location.hash.split('/')
    routeManager.changeRoute('groupDetails/'+url[1])
    let message = group.message
    routeManager.setAlertContent(templates.alert_success({message}))
}

function addGameToGroup(game, routeManager){
    let url = window.location.hash.split('/')
    routeManager.changeRoute(`gameDetails/${url[2]}`)
    let message = game.message
    routeManager.setAlertContent(templates.alert_success({message}))
}
