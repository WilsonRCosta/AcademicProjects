'use strict'

const Handlebars = require('handlebars/dist/handlebars')

module.exports = {
    home: Handlebars.compile(require('./templates/home.hbs').default),
    gameDetails: Handlebars.compile(require('./templates/gameDetails.hbs').default),
    allGames: Handlebars.compile(require('./templates/allGames.hbs').default),
    allGroups: Handlebars.compile(require('./templates/allGroups.hbs').default),
    groupDetails: Handlebars.compile(require('./templates/groupDetails.hbs').default),
    alert_success: Handlebars.compile(require('./templates/alert_success.hbs').default),
    alert_warning: Handlebars.compile(require('./templates/alert_warning.hbs').default)
}

Handlebars.registerHelper("inc", function(value, options) {
    return parseInt(value) + 1;
});