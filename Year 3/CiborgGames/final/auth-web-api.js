const passport = require('passport')

module.exports = function (global, router, authService) {
    
    global.use(passport.initialize())
    global.use(passport.session())

    passport.serializeUser(serializeUser)
    passport.deserializeUser(deserializeUser)

    router.get('/session', getSession)
    router.post('/login', login)
    router.post('/logout', logout)
    router.post('/signup', signup)

    return router

    function getSession(req, resp) {
        const username = req.isAuthenticated() ? req.user.username : undefined
        resp.json({
            'auth': req.isAuthenticated(),
            'username': username
        })
    }
    function login(req, resp) {
        authService
            .authenticate(req.body.username, req.body.password)
            .then(user => {
                console.log("Service Login ",user)
                req.login(user, (err) => {
                    if (err)
                        sendUnauthorized(resp,err)
                    else
                        sendAuthorized(resp)
                })
            })
            .catch(()=>sendUnauthorized(resp, "Couldnt Authenticate"))
    }

    function logout(req, resp) {
        console.log("Service Signout")
        req.logout()
        getSession(req, resp)
    }

    function signup(req, resp) {
        authService
            .createUser(req.body.username, req.body.password)
            .then(user => {
                console.log("Service Signup",user)
                req.login(user, (err) => {
                    if (err) sendUnauthorized(resp,err)
                    else sendAuthorized(resp)
                })
            })
    }

    function serializeUser(userId, done) {
        console.log('serializeUser')
        done(null, userId)
    }

    function deserializeUser(userId, done) {
        console.log('deserializeUser')
        authService
            .getUser(userId)
            .then(user => done(null, user))
            .catch(err => done(err))
    }

    function sendUnauthorized(resp,err){
        resp.status(403).end(JSON.stringify({status:"Unauthorized", message : err}))
    }

    function sendAuthorized(resp){
        resp.status(200).end(JSON.stringify({status:"Authorized"}))
    }
}

