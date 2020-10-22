# PROJETO CIBORG GAMES

## Introdução

**Ciborg Games** é um projeto desenvolvido para a UC Programação na Internet. O objetivo deste projeto é construir uma aplicação web capaz de recolher dados do site [Board Games Atlas](https://www.boardgameatlas.com/) e apresentá-los ao utilizador, nomeadamente apresentar os detalhes de cada um dos jogos. É também capaz de, localmente, efetuar as seguintes funcionalidades:

1.  Dar a possibilidade ao utilizador de se autenticar;
2.  Criar o seu próprio grupo e editá-lo;
3.  Adicionar e remover os jogos aos vários grupos que possua;
4.  Ordenar e filtrar os jogos que possua na sua lista de jogos por ordem crescente da duração mínima e máxima que tenha escolhido.

Para efetuar as funcionalidades indicadas foi necessário dividir o projeto em duas camadas: 

- O servidor, que providencia todos os dados e funcionalidades pretendidas;
- O cliente, que apresenta todo o conteúdo produzido pelo servidor ao utilizador.



## Servidor

Para o servidor foram usadas `Promises` para ser possível processar a informação de forma assíncrona, bem como os módulos `Express` (framework usada para Node.js) e `Passport` (necessária para autenticação) .

O servidor é dividido em duas partes. O lado que irá recolher dados da API do site indicado é gerido pelo módulo `board-games-data`, o lado que irá armazenar os dados do cliente é gerido pelo módulo `ciborg-db`, este que armazena e recolhe dados do `ElasticSearch` . 

As funções destes módulos fazem pedidos do tipo `request` às respetivas fontes com a ajuda de um objeto `options` que indica o tipo de pedido que se pretende realizar. No caso do `ciborg-db` foi necessário recorrer ao `Postman` para testar cada um dos URI's que fazem pedidos ao ElasticSerch. Depois de efetuados os pedidos a `Promise` usada em cada uma das funções retorna para os módulos de serviço `ciborg-services` e `auth-service`.

O objetivo do módulo `ciborg-services` é o de escolher a qual das bases de dados irá recolher ou armazenar dados e tem o trabalho de reencaminhar a informação que lhe chega. O módulo `auth-service` tem o mesmo objetivo do anterior com a diferença de que este irá tratar da informação de autenticação de um utilizador, sendo esta de autenticar, criar utilizador ou recolher dados de um utilizador. 

Os módulos anteriores irão ser requisitados pelos módulos `ciborg-web-api` e `auth-web-api` respetivamente. Estes módulos têm a função de afixar as rotas necessárias para efetuar todas as funcionalidades usando para isso o módulo `router` do `Express`. Têm também o tipo de *status code* que se pretende indicar para a informação que chegue proveniente dos módulos antecessores. No caso do `auth-web-api` , é necessário utilizar o módulo `Passport` para criar um novo user e recolher os seus dados usando `serializeUser` e `deserializeUser`.

Estes dois módulos de API irão depois ser utilizados pelo módulo `ciborg-server`, o módulo principal do Servidor que tem as funções de requisitar todos os módulos necessários, bem como de iniciar um novo socket para a conexão entre cliente e servidor ser possível através da porta indicada `localhost:8080`.



## Cliente

Para o cliente foram usados os módulos `Webpack` (empacotador de módulos Javascript em browsers), `Handlebars` (gerador de HTML dinâmico), `CSS` (usado para customizar a aparência da aplicação) e `Boostrap` (framework front-end para apresentar estilo previamente desenvolvido).

A camada do cliente foi dividida maioritariamente em duas partes, a parte que lida com a lógica/controlo da aplicação e a parte que lida com a visualização. 

O módulo `ciborg-data` tem a função de efetuar pedidos `fetch` ao servidor que são retornados de forma assíncrona para os restantes módulos. Estes pedidos serão realizados para as rotas criadas previamente no servidor. 

O `ciborg-controller` tem o objetivo de controlar todas as chamadas assíncronas feitas pelos módulos posteriores e de realizar pedidos ao `ciborg-data`, este que irá retornar o seu *body* em formato `json`. 

Na parte da visualização da aplicação, implementaram-se os ficheiros do tipo `Handlebars` necessários em `HTML`, com os nomes dos objetos que serão inseridos nesses ficheiros dinamicamente a serem indicados como se mostra no exemplo seguinte: `{{nome_do_objeto}}`. De seguida, todos esses ficheiros serão compilados pelo módulo `templates` que tem a função de encaminhar para o módulo `views` todos os `Handlebars` criados.

O módulo `views` irá utilizar todos os ficheiros indicados para criar e apresentar as páginas web de toda a aplicação e suas funcionalidades.

Os módulos `views` e `ciborg-controller`  irão ser requisitados pelo módulo`routes` , que contém um objeto que alberga a informação lógica e visual e que irá encaminhar essa informação para o `router`, módulo principal da camada do cliente que faz todas as mudanças de rota necessárias com `location.hash` bem como colocar todos os ficheiros `HTML` num só, o `index.html`. Tem também a função de chamar o `controller` e o `view` de cada objeto recebido.

A informação é disposta no ficheiro `index.html` que possui um `head` com as referências utilizadas pela aplicação e um `body` que possui código da página inicial `home` e que será aumentado de forma dinâmica.

![Ciborg-Games-Diagram](..\anexos\Ciborg-Games-Diagram.png)









