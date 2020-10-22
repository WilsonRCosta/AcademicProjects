const data = require('../model/ciborg-data')

module.exports = {
  home: async function() {
    return {}
  },

  getAllGames: async function() {
    return data.getAllGames()
  },

  gameDetails: async function(name) {
    let addImage = require('../img/add.png')
    let games = await data.gameDetails(name)
    let session = await data.getSession()
    if(!session.auth) {
      return {addImage, games}
    } else {
      let groups = await data.getAllGroups()
      return {addImage, games, groups}
    }

  },

  gamesByName: async function(game){
    return data.gamesByName(game.name)
  },

  getAllGroups: async function() {
    let deleteImage = require('../img/delete.png')
    let editImage = require('../img/edit.png')
    let disableEditImage = require('../img/cancel.png')
    let groups = await data.getAllGroups()
    return { deleteImage, editImage, disableEditImage, groups }
  },

  createGroup: async function(group) {
    return data.createGroup(group.groupName, group.description)
  },

  editGroup: async function(group) {
    return data.editGroup(group.groupName, group.newGroupName, group.newDescription)
  },

  groupDetails: async function(groupName) {
    let delImage = require('../img/delete.png')
    let filterImage = require('../img/filter.png')
    let groupDetails = await data.groupDetails(groupName)
    return {delImage, filterImage, groupDetails}
  },

  deleteGameFromGroup: async function(group, game){
    return data.deleteGameFromGroup(group, game)
  },

  addGameToGroup: async function(game, group) {
    return data.addGameToGroup(game, group)
  },

  getGamesByDuration: async function(groupName, minTime, maxTime) {
    return data.getGamesByDuration(groupName, minTime, maxTime)
  },

  signUp: async function(user){
    return data.signUp(user.username,user.password)
  },

  login: async function(user){
    return data.login(user.username,user.password)
  },

  logout: async function(){
    return data.logout()
  },

  getSession: async function() {
    return data.getSession()
  }
}