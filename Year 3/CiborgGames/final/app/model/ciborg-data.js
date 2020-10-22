'use strict'

function GamesApiUris() {
    const baseUri = "http://localhost:8080/api/";
    this.getAllGamesUri = () => `${baseUri}games`
    this.getAllGroupsUri = () => `${baseUri}groups`
    this.getGameDetailsUri = (name) => `${baseUri}game/${name}`
    this.gamesByNameUri = (name) => `${baseUri}games/${name}`
    this.createGroupUri = (groupName, description) => `${baseUri}groups/${groupName}&${description}`
    this.editGroupUri = (groupName, newGroupName, newDescription) => `${baseUri}groups/${groupName}&${newGroupName}&${newDescription}`
    this.getGroupDetailsUri = (groupName) => `${baseUri}groups/${groupName}`
    this.deleteGameFromGroup = (groupName,gameName) => `${baseUri}groups/${groupName}&${gameName}`
    this.addGameToGroup = (groupName, gameName) => `${baseUri}groups/${groupName}&${gameName}`
    this.getGamesByDuration = (groupName, minPlayTime, maxPlayTime) => `${baseUri}groups/${groupName}/${minPlayTime}/${maxPlayTime}`
    this.signUpUri = () => `${baseUri}auth/signup`
    this.loginUri = () => `${baseUri}auth/login`
    this.logoutUri = () => `${baseUri}auth/logout`
    this.getSessionUri = () =>`${baseUri}auth/session`
}

const Uris = new GamesApiUris()

function getAllGames() {
    return fetch(Uris.getAllGamesUri())
        .then(res => res.json())
}

function gameDetails(name) {
    return fetch(Uris.getGameDetailsUri(name))
        .then(res => res.json())
}

function gamesByName(name, user) {
    return fetch(Uris.gamesByNameUri(name))
        .then(res => res.json())
}

function getAllGroups() {
    return fetch(Uris.getAllGroupsUri())
        .then(res => {
            if (res.status !== 200)
                alert('User not signed in!')
            else
                return res.json()
        })
}

function createGroup(groupName, description){
    const options = {
        method : "POST",
        headers : {
            "Content-Type" : "application/json",
            "Accept" : "application/json"
        }
    }

    return fetch(Uris.createGroupUri(groupName, description), options)
        .then(res => {
            if (res.status !== 201)
                alert(`Error creating group "${groupName}" with the description "${description}"`)
            else
                return res.json()
        })
}

function editGroup(groupName, newGroupName, newDescription){
    const options = {
        method : "PUT",
        headers : {
            "Content-Type" : "application/json",
            "Accept" : "application/json"
        }
    }

    return fetch(Uris.editGroupUri(groupName, newGroupName, newDescription), options)
        .then(res => {
            if (res.status !== 201)
                alert(`Error editing group "${groupName}" to new Name "${newGroupName}" with the description "${newDescription}"`)
            else
                return res.json()
        })
}

function groupDetails(name) {
    return fetch(Uris.getGroupDetailsUri(name))
        .then(res => {
            if (res.status !== 200)
                alert(`Group "${name}" does not exist`)
            else
                return res.json()
        })
}

function deleteGameFromGroup(groupName, gameName){
    const options = {
        method : "DELETE"
    }

    return fetch(Uris.deleteGameFromGroup(groupName, gameName), options)
        .then(res => {
            if (res.status !== 200)
                alert(`Error deleting game "${gameName}" from group "${groupName}"`)
            else
                return res.json()
        })
}
function addGameToGroup(groupName, gameName) {
    const options = {
        method : "PUT",
        headers : {
            "Content-Type" : "application/json",
            "Accept" : "application/json"
        }
    }
    return fetch(Uris.addGameToGroup(groupName, gameName), options)
        .then(res => {
            if (res.status !== 201)
                alert(`Game "${gameName}" already exists in group "${groupName}"`)
            else
                return res.json()
        })
}

function getGamesByDuration(groupName, minPlayTime, maxPlayTime) {
    return fetch(Uris.getGamesByDuration(groupName, minPlayTime, maxPlayTime))
        .then(res => {
            if (res.status !== 200)
                alert(`Group "${groupName}" does not exist`)
            else return res.json()
        })
}

function signUp(username,password){
    const options = {
        method: 'POST',
        headers : {
            "Content-Type" : "application/json",
            "Accept" : "application/json"
        },
        body : JSON.stringify({username:username,password:password})
    }
    return fetch(Uris.signUpUri(), options)
        .then(res =>{
            if (res.status !== 200) {
                alert(`${res.statusText}. User ${username} already is a member.`)
                return res.ok
            }
            else return res.json()
        })
}

function login(username,password){
    const options = {
        method: 'POST',
        headers : {
            "Content-Type" : "application/json",
            "Accept" : "application/json"
        },
        body : JSON.stringify({username:username,password:password})
    }
    return fetch(Uris.loginUri(), options)
        .then(res => {
            if (res.status !== 200){
                alert(`${res.statusText}. User ${username} is not a member yet. `)
                return res.ok
            } else {
                return res.json()
            }
        })
}

function getSession() {
    return fetch(Uris.getSessionUri())
        .then(res => res.json())
}

function logout(){
    const options = {
        method: 'POST',
        headers : {
            "Content-Type" : "application/json",
            "Accept" : "application/json"
        }
    }
    return fetch(Uris.logoutUri(), options)
        .then(res => {
            return res
        })
}

module.exports  = {
    getAllGames: getAllGames,
    gameDetails: gameDetails,
    gamesByName: gamesByName,
    getAllGroups: getAllGroups,
    createGroup: createGroup,
    editGroup: editGroup,
    groupDetails: groupDetails,
    deleteGameFromGroup: deleteGameFromGroup,
    addGameToGroup: addGameToGroup,
    getGamesByDuration: getGamesByDuration,
    signUp: signUp,
    login: login,
    logout: logout,
    getSession: getSession
}