module.exports = function(groupsData) {
    return {
        authenticate: authenticate,
        createUser: createUser,
        getUser: getUser
    }
  
    async function authenticate(username, pass) {
        return groupsData.getUserByUsername(username)
            .then(user => {
                if(user.message)
                    return Promise.reject("Username '"+username+"' does not exist!")
                if(user.password !== pass)
                    return Promise.reject("Password is incorrect!")
                return user.id
            })
    }

    async function createUser(username,pass){
        return groupsData.getUserByUsername(username)
            .then(user => {
                if(user.message){
                    const rand = function() {
                        return Math.random().toString(36).substr(2);
                    };

                    const token = function() {
                        return rand() + rand();
                    };
                    let newUser = {
                        "id":token(),
                        "username":username,
                        "password":pass
                    }
                    groupsData.createUser(newUser)
                    return newUser.id
                }
                return null
            })
    }
  
    async function getUser(userId) {
        return groupsData.getUserById(userId)
            .then(user => {
                if(user.message)
                    return Promise.reject("Username does not exist!")
                return user.id
            })
    }
}