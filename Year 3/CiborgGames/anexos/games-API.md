## Obtain games by popularity

- Request:
  - Method: GET
  - Path: /games
  - Body: This request does not have a body
  
- Response:
  - Success:
    - Status code: 200 OK 
    - Content-Type: application/json
    - Body:
```json
[
    [
        {
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
            "Average Rating": 3.956896551724139,
            "Official URL": "https://store.greaterthangames.com/spirit-island.html?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads",
            "Official Rules": "https://drive.google.com/file/d/0B9kp130SgLtdcGxTcTFodlhaWDg/view"
        }
    ],
	[
        {
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
            "Average Rating": 4.16393442622951,
            "Official URL": "http://www.cephalofair.com/gloomhaven?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads",
            "Official Rules": "https://online.flippingbook.com/view/598058/"
        }
    ]
]
```
   
## Obtain game by name

- Request:
  - Method: GET
  - Path: /games/gameName
    - Path parameters:
      - gameName - name of the game 
  - Body: none
- Response:
  - Success: 
    - Status code: 200 OK 
    - Content-Type: application/json
    - Body:
```json
[
    [
        {
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
            "Average Rating": 3.956896551724139,
            "Official URL": "https://store.greaterthangames.com/spirit-island.html?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads",
            "Official Rules": "https://drive.google.com/file/d/0B9kp130SgLtdcGxTcTFodlhaWDg/view"
        }
    ],
    [
        {
            "Game Name": "Spirit Island: Jagged Earth",
            "Year Published": 2020,
            "Min-Players": 1,
            "Max-Players": 6,
            "Min Playtime": null,
            "Max Playtime": null,
            "Min Age": null,
            "Description": "<p>Peril racks <a href=\"https://store.greaterthangames.com/spirit-island.html\" target=\"_blank\">Spirit Island</a>. The invaders are more numerous and more capable than ever before. As hope begin to fade, defense of the island falls to those spirits more in tune with the danger and chaos of the natural world. Will you be able to harness their power to protect the island or will it fall to the persistence of the invaders? Whatever the outcome, Spirit Island will never be the same after the time of <em><strong>Jagged Earth</strong></em>! </p>",
            "Image": "https://s3-us-west-1.amazonaws.com/5cc.images/games/uploaded/1539752922956",
            "Publishers": [
                "GreaterThanGames"
            ],
            "Average Rating": 5,
            "Official URL": "https://www.kickstarter.com/projects/gtgames/spirit-island-jagged-earth?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads",
            "Official Rules": "https://www.dropbox.com/s/evp70pilk7sgkkd/Jagged%20Earth%20PnP%20Rules%20-%20Play%20Options.pdf?dl=0"
        }
    ]
]
```
## Create Group

- Request:
  - Method: POST
  - Path: /groups/groupName
    - Path parameters:
      - name - name of the group
  - Body:
  
```json
	{
	        "groupName" : "Grupo_David_Wilson",
		"games": [],
		"description" : "O_grupo_dos_mais_sexy"
	}
```
  
- Response:
  - Success: 
    - Status code: 201 Created
    - Content-Type: application/json
    - Body:
 
```json
	{
		"message": "Group 'Grupo_David_Wilson' was created with the description: 'O_grupo_dos_mais_sexy'!"
	}
```	
	
 - Errors:
     - Body:
```json      
	{
		"message": "Group already exists!"
	}
```

### Edit Group

- Request:
  - Method: POST
  - Path: /groups/groupName&newGroupName&newDesc
    - Path parameters:
      - name - name of the group
      - newGroupName - new name of the group
      - newDesc - new description of the group
  - Body:
  
```json
	{
		"query":{
			"match":{
				"groupName": "Grupo_David_Wilson"
			}
		},
		"script":{
			"source": "ctx._source.groupName = 'Grupo_David_Wilson_Alterado'; ctx._source.desc = 'Nova_Descricao_Alterada'"
		}
	}
```

- Response:
  - Success: 
    - Status code: 201 Created
    - Content-Type: application/json
    - Body:
```json
	{
		"message": "Group 'Grupo_David_Wilson' is now 'Grupo_David_Wilson_Alterado' with the description 'Nova_Descricao_Alterada'"
	}
```	
	
 - Errors:
     - Body:
```json     
        {
		"message": "Could not find group with 'Grupo_David_Wen' name"
	}
```

### Get All Groups

- Request:
  - Method: GET
  - Path: /groups
    - Path parameters:
      - none
  - Body:
```json
	  { "query" : { "match_all" : {} }
```
  
- Response:
  - Success: 
    - Status code: 200 OK
    - Content-Type: application/json
    - Body:
```json
[
    {
        "groupName": "Grupo_David_Wilson_Alterado",
        "games": [
            {
                "Year Published": 2016,
                "Description": "Powerful Spirits have existed on this isolated island for time immemorial. They are both part of the natural world and - at the same time - something beyond nature. Native Islanders, known as the Dahan, have learned how to co-exist with the spirits, but with a healthy dose of fear and reverence. However, now, the island has been &quot;discovered&quot; by invaders from a far-off land. These would-be colonists are taking over the land and upsetting the natural balance, destroying the presence of Spirits as they go. As Spirits, you must grow in power and work together to drive the invaders from your island... before it's too late!",
                "Min-Players": 1,
                "Publishers": [
                    "Greater Than Games"
                ],
                "Average Rating": 3.965217391304349,
                "Official Rules": "https://drive.google.com/file/d/0B9kp130SgLtdcGxTcTFodlhaWDg/view",
                "GameName": "Spirit Island",
                "Max-Players": 4,
                "Official URL": "https://store.greaterthangames.com/spirit-island.html?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads",
                "Min Age": 13,
                "Image": "https://s3-us-west-1.amazonaws.com/5cc.images/games/uploaded/1559254941010-61PJxjjnbfL.jpg"
            },
            {
                "Year Published": 2016,
                "Description": "Powerful Spirits have existed on this isolated island for time immemorial. They are both part of the natural world and - at the same time - something beyond nature. Native Islanders, known as the Dahan, have learned how to co-exist with the spirits, but with a healthy dose of fear and reverence. However, now, the island has been &quot;discovered&quot; by invaders from a far-off land. These would-be colonists are taking over the land and upsetting the natural balance, destroying the presence of Spirits as they go. As Spirits, you must grow in power and work together to drive the invaders from your island... before it's too late!",
                "Min-Players": 1,
                "Publishers": [
                    "Greater Than Games"
                ],
                "Average Rating": 3.965217391304349,
                "Official Rules": "https://drive.google.com/file/d/0B9kp130SgLtdcGxTcTFodlhaWDg/view",
                "GameName": "Spirit Island",
                "Max-Players": 4,
                "Official URL": "https://store.greaterthangames.com/spirit-island.html?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads",
                "Min Age": 13,
                "Image": "https://s3-us-west-1.amazonaws.com/5cc.images/games/uploaded/1559254941010-61PJxjjnbfL.jpg"
                 }
              ]
        }
]
```
	
### Get Details of a Group 


- Request:
  - Method: GET
  - Path: /groups/groupName
    - Path parameters:
      - groupName: name of the group
  - Body:
```json
	{
		"size": 1,
		"query" : { "match": { "groupName": "Grupo_David_Wilson_Alterado" }}
	}
```
- Response:
  - Success: 
    - Status code: 200 OK
    - Content-Type: application/json
    - Body:
```json
[
    {
        "groupName": "Grupo_David_Wilson_Alterado",
        "games": [
            {
                "Description": "Powerful Spirits have existed on this isolated island for time immemorial. They are both part of the natural world and - at the same time - something beyond nature. Native Islanders, known as the Dahan, have learned how to co-exist with the spirits, but with a healthy dose of fear and reverence. However, now, the island has been &quot;discovered&quot; by invaders from a far-off land. These would-be colonists are taking over the land and upsetting the natural balance, destroying the presence of Spirits as they go. As Spirits, you must grow in power and work together to drive the invaders from your island... before it's too late!",
                "Min-Players": 1,
                "Max Playtime": 120,
                "Official URL": "https://store.greaterthangames.com/spirit-island.html?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads",
                "Game Name": "Spirit Island",
                "Image": "https://s3-us-west-1.amazonaws.com/5cc.images/games/uploaded/1559254941010-61PJxjjnbfL.jpg",
                "Year Published": 2016,
                "Min Playtime": 90,
                "Publishers": [
                    "Greater Than Games"
                ],
                "Average Rating": 3.956896551724139,
                "Official Rules": "https://drive.google.com/file/d/0B9kp130SgLtdcGxTcTFodlhaWDg/view",
                "Max-Players": 4,
                "Min Age": 13
            }
        ],
        "description": "Nova_Descricao_Alterada"
    }
]
```
- Errors:
	- Body:
```json 
	{
		"message": "Group does not exist!"
	}
```
	
### Insert Game into Group

- Request:
  - Method: POST
  - Path: /groups/groupName&gameName
    - Path parameters:
      - groupName: name of the group
	  - gameName: name of the game
  - Body:
```json
{
	"query": { "match": { "groupName": "Grupo_David_Wilson_Alterado" }},
	"script": {
		"source": "ctx._source.games.add(params.newGame)",
		"params": {
		"newGame": "Gloomhaven"
		}
	}
}
```
- Response:
  - Success: 
    - Status code: 201 Created
    - Content-Type: application/json
    - Body:
```json
	{
		"message": "Game Gloomhaven was added to Grupo_David_Wilson_Alterado"
	}
```
- Errors:
	- Body:
```json
	{
		"message": "Game already exists on group!"
	}
```

### Remove Game From Group

- Request:
  - Method: DELETE
  - Path: /groups/groupName&gameName
    - Path parameters:
      - groupName: name of the group
	  - gameName: name of the game
```json
{
	"query": {
		"match": { "groupName": "Grupo_David_Wilson_Alterado" } },
	"script": {
		"source": "ctx._source.games.remove(params.GameIndex)",
		"params": { "GameIndex": Gloomhaven }
	}
}
```

- Response:
  - Success: 
    - Status code: 201 Created
    - Content-Type: application/json
    - Body:
```json
	{
		"message": "Game Gloomhaven was deleted from Grupo_David_Wilson_Alterado"
	}
```

- Errors:
	- Body:
```json
	{
		"message": "Game does not exists on group!"
	}
```	
	
### Get Group Games By Duration
- Request:
  - Method: GET
  - Path: /groups/groupName
    - Path parameters:
      - groupName: name of the group
  - Body:
```json
	{
		"size": 1,
		"query" : { "match": { "groupName": "Grupo_David_Wilson_Alterado" }}
	}
```

- Response:
  - Success: 
    - Status code: 200 OK
    - Content-Type: application/json
    - Body:
```json
	[
    {
        "Description": "Powerful Spirits have existed on this isolated island for time immemorial. They are both part of the natural world and - at the same time - something beyond nature. Native Islanders, known as the Dahan, have learned how to co-exist with the spirits, but with a healthy dose of fear and reverence. However, now, the island has been &quot;discovered&quot; by invaders from a far-off land. These would-be colonists are taking over the land and upsetting the natural balance, destroying the presence of Spirits as they go. As Spirits, you must grow in power and work together to drive the invaders from your island... before it's too late!",
        "Min-Players": 1,
        "Max Playtime": 120,
        "Official URL": "https://store.greaterthangames.com/spirit-island.html?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads",
        "Game Name": "Spirit Island",
        "Image": "https://s3-us-west-1.amazonaws.com/5cc.images/games/uploaded/1559254941010-61PJxjjnbfL.jpg",
        "Year Published": 2016,
        "Min Playtime": 90,
        "Publishers": [
            "Greater Than Games"
        ],
        "Average Rating": 3.956896551724139,
        "Official Rules": "https://drive.google.com/file/d/0B9kp130SgLtdcGxTcTFodlhaWDg/view",
        "Max-Players": 4,
        "Min Age": 13
    },
    {
        "Description": "Gloomhaven is a game of Euro-inspired tactical combat in a persistent world of shifting motives. Players will take on the role of a wandering adventurer with their own special set of skills and their own reasons for traveling to this dark corner of the world.<br />\r\n<br />\r\nPlayers must work together out of necessity to clear out menacing dungeons and forgotten ruins. In the process they will enhance their abilities with experience and loot, discover new locations to explore and plunder, and expand an ever-branching story fueled by the decisions they make.<br />\r\n<br />\r\nThis is a legacy game with a persistent and changing world that is ideally played over many game sessions. After a scenario, players will make decisions on what to do, which will determine how the story continues, kind of like a &quot;Choose Your Own Adventure&quot; book. Playing through a scenario is a cooperative affair where players will fight against automated monsters using an innovative card system to determine the order of play and what a player does on their turn.",
        "Min-Players": 1,
        "Max Playtime": 150,
        "Official URL": "http://www.cephalofair.com/gloomhaven?utm_source=boardgameatlas.com&utm_medium=search&utm_campaign=bga_ads",
        "Game Name": "Gloomhaven",
        "Image": "https://s3-us-west-1.amazonaws.com/5cc.images/games/uploaded/1559254920151-51ulRXlJ7LL.jpg",
        "Year Published": 2017,
        "Min Playtime": 60,
        "Publishers": [
            "Cephalofair Games"
        ],
        "Average Rating": 4.16393442622951,
        "Official Rules": "https://online.flippingbook.com/view/598058/",
        "Max-Players": 4,
        "Min Age": 12
    }
]
```
