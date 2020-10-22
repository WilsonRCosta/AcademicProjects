const controller = require('./controller/ciborg-controller')
const views = require('./view/views')

module.exports = {
    home : {
        controller: controller.home,
        view: views.home
    },

    getAllGames : {
        controller: controller.getAllGames,
        view: views.getAllGames
    },

    gameDetails: {
        controller: controller.gameDetails,
        view: views.gameDetails
    },

    gamesByName: {
        controller: controller.gamesByName,
        view: views.gamesByName
    },

    getAllGroups: {
        controller: controller.getAllGroups,
        view: views.getAllGroups
    },

    createGroup : {
        controller: controller.createGroup,
        view: views.groupAlteration
    },

    editGroup : {
        controller: controller.editGroup,
        view: views.groupAlteration
    },

    groupDetails: {
        controller: controller.groupDetails,
        view: views.groupDetails
    },

    deleteGameFromGroup: {
        controller: controller.deleteGameFromGroup,
        view: views.deleteGameFromGroup
    },

    addGameToGroup: {
        controller: controller.addGameToGroup,
        view: views.addGameToGroup
    },

    getGamesByDuration: {
        controller: controller.getGamesByDuration,
        view: views.getGamesByDuration
    },

    signUp: {
        controller: controller.signUp,
        view: views.authentication
    },

    login: {
        controller: controller.login,
        view: views.authentication
    },

    logout: {
        controller: controller.logout,
        view: views.logout
    }
}