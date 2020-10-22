'use strict'

const assert = require('assert');

const groupsData = require('./ciborg-db-mem')()
const gamesData = require('./board-games-data-mem')()
const ciborgService = require('../Fase-2/ciborg-services')(gamesData,groupsData)
let gloomhaven

describe('Test service', function () {
	describe('getAllGames', function () {
        it('should match 2 games', done => {
            ciborgService.getAllGames()
                .then(games => {
                    assert.strictEqual(games.length, 2)
                    done()
                })
        })
    }),
    describe('getGameByName', function () {
        it('should match 1 game (Gloomhaven)', done => {
            ciborgService.getGameByName("Gloomhaven")
                .then(games => {
                    assert.strictEqual(games.length,1)
                    gloomhaven = games[0]
                    done()
                })
                .catch(error => done(new Error(error.message)))
        })
    }),
    describe('createGroup', function () {
        it("should create group 'Grupo_David_Wilson'", done => {
            let name = 'Grupo_David_Wilson'
            let desc = 'O_grupo_dos_mais_sexy'
            ciborgService.createGroup(name, desc)
                .then(groups => {
                    if(groups.message)
                        return Promise.reject(groups)
                    console.log(groups)
                    done()
                })
                .catch(error =>
                    done(new Error(error.message))
                )
        })
    }),
    describe('changeGroup', function () {
        it("should edit group name 'Grupo_David_Wilson' to 'Grupo_David_Wilson_Alterado'", done => {
            let oldName = 'Grupo_David_Wilson'
            let newName = 'Grupo_David_Wilson_Alterado'
            let desc = 'O_grupo_dos_mais_sexy'
            ciborgService.editGroup(oldName,newName,desc)
                .then(groups => {
                    if(groups.message)
                        return Promise.reject(groups)
                    done()
                })
                .catch(error =>
                    done(new Error(error.message))
                )
        })
    }),
    describe('getAllGroups', function () {
        it("should show all groups'", done => {
            ciborgService.getAllGroups()
                .then(groups => {
                    console.log(groups)
                    done()
                })
                .catch(error =>
                    done(new Error(error.message))
                )
        })
    }),
    describe('getGroupDetail', function () {
        it("should show group 'Grupo_David_Wilson_Alterado'", done => {
            let groupName = 'Grupo_David_Wilson_Alterado'
            ciborgService.getGroupDetails(groupName)
                .then(groups => {
                    console.log(groups)
                    done()
                })
                .catch(error =>
                    done(new Error("Group "+groupName+" not found!"))
                )
        })
    }),
    describe('insertGameInGroup', function () {
        it("should insert Gloomhaven game into 'Grupo_David_Wilson_Alterado' group", done => {
            let groupName = 'Grupo_David_Wilson_Alterado'
            let gameName = 'Gloomhaven'
            ciborgService.insertGameInGroup(groupName,gameName)
                .then(groups => {
                    console.log(groups)
                    done()
                })
                .catch(error =>
                    done(new Error(error))
                )
        })
    }),
    describe('removeGameFromGroup', function () {
        it("should remove Gloomhaven game into 'Grupo_David_Wilson_Alterado' group", done => {
            let groupName = 'Grupo_David_Wilson_Alterado'
            let gameName = 'Gloomhaven'
            ciborgService.removeGameFromGroup(groupName,gameName)
                .then(groups => {
                    console.log(groups)
                    done()
                })
                .catch(error =>
                    done(new Error(error))
                )
        })
    }),
    describe('getGroupGamesByDuration', function () {
        it("should show games on 'Grupo_David_Wilson_Alterado' group sorted by duration", done => {
            let groupName = 'Grupo_David_Wilson_Alterado'
            ciborgService.getGroupGamesByDuration(groupName, 50, 160)
                .then(groups => {
                    console.log(groups)
                    done()
                })
                .catch(error =>
                    done(new Error(error))
                )
        })
    })
})