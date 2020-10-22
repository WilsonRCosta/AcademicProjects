CREATE TABLE Mercado_Financeiro (
	Codigo VARCHAR(12),
	Nome VARCHAR(50),
	Descricao VARCHAR(255),
	PRIMARY KEY (Codigo)
);

CREATE TABLE Valores_Mercado (
	Dia DATE,
	Codigo VARCHAR(12),
	ValorIndice MONEY default 0, 
	ValorAbertura MONEY default 0, 
	VariacaoDiaria MONEY default 0, 
	FOREIGN KEY (Codigo) REFERENCES Mercado_Financeiro(Codigo) ON DELETE CASCADE,  
	PRIMARY KEY(Dia, Codigo)
);

CREATE TABLE Instrumento_Financeiro(
	ISIN VARCHAR(12),
	CodigoMercado VARCHAR(12),
	Descricao VARCHAR(255),
	ValorAtual MONEY default 0,
	ValorVariacaoDiaria MONEY default 0, 
	PercentagemVariacaoDiaria MONEY default 0,
	ValorVariacao6Meses MONEY default 0,
	PercentagemVariacao6Meses MONEY default 0,
	Media6Meses MONEY default 0,
	FOREIGN KEY (CodigoMercado) REFERENCES Mercado_Financeiro(Codigo) ON DELETE CASCADE,  
	PRIMARY KEY (ISIN)
);

CREATE TABLE Registo(
	ISIN VARCHAR(12),
	Dia DATE,
	ValorAbertura MONEY,
	ValorFecho MONEY,
	ValorMaximo MONEY,
	ValorMinimo MONEY,
	HoraFecho DATETIME,
	FOREIGN KEY (ISIN) REFERENCES Instrumento_Financeiro(ISIN) ON DELETE CASCADE,  
	PRIMARY KEY (ISIN, Dia)
);

CREATE TABLE Cliente(
	CC VARCHAR(12),
	NIF VARCHAR(9) UNIQUE NOT NULL,
	NomeCliente VARCHAR(255),
	NomePortfolio VARCHAR(255) UNIQUE NOT NULL,
	ValorTotalPortfolio MONEY default 0,
	PRIMARY KEY (CC)
);

CREATE TABLE Contacto(
	Codigo VARCHAR(12),
	CC VARCHAR(12),
	Descricao VARCHAR(255),
	FOREIGN KEY (CC) REFERENCES Cliente(CC) ON DELETE CASCADE, 
	PRIMARY KEY (Codigo)
);

CREATE TABLE Contacto_Telefonico(
	Numero NUMERIC(9),
	Indicativo NUMERIC(3),
	Codigo_Contacto VARCHAR(12),
	FOREIGN KEY (Codigo_Contacto) REFERENCES Contacto(Codigo) ON DELETE CASCADE,  
	PRIMARY KEY (Numero)
)

CREATE TABLE Contacto_Email(
	Endereco VARCHAR(40),
	Codigo_Contacto VARCHAR(12),
	FOREIGN KEY (Codigo_Contacto) REFERENCES Contacto(Codigo) ON DELETE CASCADE,  
	PRIMARY KEY (Endereco)
)

CREATE TABLE Posicao(
	ISIN VARCHAR(12),
	CC VARCHAR(12), 
	Quantidade FLOAT,
	FOREIGN KEY (ISIN) REFERENCES Instrumento_Financeiro(ISIN) ON DELETE CASCADE, 
	FOREIGN KEY (CC) REFERENCES Cliente(CC) ON DELETE CASCADE,
	PRIMARY KEY(ISIN,CC)
);

CREATE TABLE Triplos(
	Identificacao VARCHAR(12), 
	Dia DATETIME,
	Valor MONEY,
	Observado BIT default 0,
	PRIMARY KEY(Identificacao, Dia)
);