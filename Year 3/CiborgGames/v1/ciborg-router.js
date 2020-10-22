'use strict'

const pathGames = {
    get : [],
    post : [],
	put: [],
	delete: []
}

const urlNotFound = -1;
const templateNotFoundYet = -2;

function router(req,res){
    console.log(`Received Request - ${req.method} ${req.url}`)
    const reqGames = pathGames[req.method.toLowerCase()]
    req["params"] = []
    
    let indice = splitURLAndGet(req,reqGames)

    if(indice === urlNotFound){
        res.statusCode = 404
        res.end('404 - Not Found')
    }
    else
        reqGames[indice].games(req,res)        
}

function splitURLAndGet(req,template){
    let urlArr = req.url.split('/')

    if(urlArr[1] === "" || urlArr[1] === "favicon.ico")
        return urlNotFound;

    for(let i = 0; i < template.length; i++) {
        let templateArr = template[i].path.split('/')

        if(templateArr.length !== urlArr.length)
            continue

        let params = function checkParams(idxMax, idx) {
            if (idx > idxMax)
                return i; 
                
            if (urlArr[idx] === templateArr[idx]){
                idx++
                return checkParams(idxMax, idx);
            }

            if (templateArr[idx].includes(':')){
                let urlParams = urlArr[idx].split('&')
                let templateParams = templateArr[idx].split('&')
                if(urlParams.length !== templateParams.length)
                    return templateNotFoundYet
                if(!templateArr[idx].includes('&')){
                    let attrs = templateArr[idx].replace(':','');
                    req.params[attrs] = urlArr[idx]
                    return i
                }
                let paramIndice = 0
                let setParams = (function separateAndSetParams(urlParams,templateParams,paramIndice){
                    if(paramIndice >= templateParams.length)
                        return;
                    let attrs = templateParams[paramIndice].replace(':','');
                    req.params[attrs] = urlParams[paramIndice]
                    paramIndice++
                    separateAndSetParams(urlParams,templateParams,paramIndice)
                })(urlParams,templateParams,paramIndice)
                setParams
            } else if (urlArr[idx] !== templateArr[idx])         //Caso em que se mete /groups e ele tem de passar por /games primeiro
            return templateNotFoundYet;

            idx++
            return checkParams(idxMax, idx);
        }(templateArr.length-1, 1);

        if(params !== templateNotFoundYet)
            return params
    }
}

router.get = function (p, h){
    pathGames.get.push({path:p, games:h})
}

router.post = function post(p, h){
    pathGames.post.push({path:p, games:h})
}

router.put = function put(p, h){
    pathGames.put.push({path:p, games:h})
}

router.delete = function del(p, h){
    pathGames.delete.push({path:p, games:h})
}

module.exports = router