-- TRIGGERS



GO

CREATE TRIGGER updatePortfolioOnInsertPosicao ON Posicao 
FOR INSERT
AS
BEGIN 
	DECLARE @id VARCHAR(12) 
	DECLARE @cc VARCHAR(12)
	DECLARE @quantidadeInserida BIGINT
	DECLARE @precoInstrumento MONEY
	DECLARE @valorTotal MONEY
	
	SELECT @quantidadeInserida = inserted.Quantidade, @cc = inserted.CC, @id = inserted.ISIN FROM inserted
	SELECT @precoInstrumento = ValorAtual FROM Instrumento_Financeiro WHERE ISIN = @id

	SELECT @valorTotal = ValorTotalPortfolio FROM Cliente AS C WHERE C.CC = @cc
	
	SET @valorTotal = @valorTotal + ( @quantidadeInserida * @precoInstrumento )

	UPDATE Cliente SET ValorTotalPortfolio = @valorTotal WHERE CC = @cc
END

GO

CREATE TRIGGER updatePortfolioOnUpdatePosicao ON Posicao 
FOR UPDATE
AS
BEGIN 
	DECLARE @id VARCHAR(12) 
	DECLARE @cc VARCHAR(12)
	DECLARE @quantidadeAnterior BIGINT
	DECLARE @precoInstrumento MONEY
	DECLARE @valorTotal MONEY
	DECLARE @quantidadeNova BIGINT
	
	SELECT @quantidadeAnterior = deleted.Quantidade, @cc = deleted.CC, @id = deleted.ISIN FROM deleted

	SELECT @precoInstrumento = ValorAtual FROM Instrumento_Financeiro WHERE ISIN = @id
	SELECT @valorTotal = ValorTotalPortfolio FROM Cliente AS C WHERE C.CC = @cc
	
	SELECT @quantidadeNova = inserted.Quantidade FROM inserted

	SET @valorTotal = @valorTotal + ( (@quantidadeNova - @quantidadeAnterior) * @precoInstrumento)

	UPDATE Cliente SET ValorTotalPortfolio = @valorTotal WHERE CC = @cc
END

GO

CREATE TRIGGER updatePortfolioOnDeletePosicao ON Posicao 
FOR DELETE
AS
BEGIN 
	DECLARE @id VARCHAR(12) 
	DECLARE @cc VARCHAR(12)
	DECLARE @quantidadeAnterior BIGINT
	DECLARE @precoInstrumento MONEY
	DECLARE @valorTotal MONEY
	
	SELECT @quantidadeAnterior = deleted.Quantidade, @cc = deleted.CC, @id = deleted.ISIN FROM deleted

	SELECT @precoInstrumento = ValorAtual FROM Instrumento_Financeiro WHERE ISIN = @id
	SELECT @valorTotal = ValorTotalPortfolio FROM Cliente AS C WHERE C.CC = @cc

	SET @valorTotal = @valorTotal - ( @quantidadeAnterior * @precoInstrumento)

	UPDATE Cliente SET ValorTotalPortfolio = @valorTotal WHERE CC = @cc
END

GO

CREATE TRIGGER updatePortfolioOnUpdateInstrumento ON Instrumento_Financeiro 
FOR UPDATE
AS
BEGIN 
	DECLARE @id VARCHAR(12)
	DECLARE @precoAnterior MONEY
	DECLARE @precoNovo MONEY
	DECLARE @valorTotal MONEY
	DECLARE @quantidade BIGINT

	SELECT @id = deleted.ISIN, @precoAnterior = deleted.ValorAtual FROM deleted
	SELECT @precoNovo = inserted.ValorAtual FROM inserted

	DECLARE @cc VARCHAR(12)

	DECLARE @cursor CURSOR

	SET @cursor = CURSOR FOR 
		SELECT P.CC, P.Quantidade FROM Posicao AS P
		WHERE P.ISIN = @id

	OPEN @cursor
	FETCH NEXT FROM @cursor INTO @cc, @quantidade

	WHILE @@FETCH_STATUS = 0
		BEGIN 

			SELECT @valorTotal = C.ValorTotalPortfolio FROM CLIENTE AS C WHERE C.CC = @cc 

			SET @valorTotal = @valorTotal - (@precoAnterior * @quantidade) + (@precoNovo * @quantidade)

			UPDATE Cliente SET ValorTotalPortfolio = @valorTotal WHERE CC = @cc

		FETCH NEXT FROM @cursor INTO @cc, @quantidade
		END
		
	CLOSE @cursor
	DEALLOCATE @cursor

END

GO


CREATE TRIGGER updateMercadoOnRegistoChanges
ON Registo
FOR INSERT, UPDATE
AS
BEGIN	

	DECLARE @isin VARCHAR(12)
	DECLARE @dia DATE
	DECLARE @codigoMercado VARCHAR(12)
	DECLARE @valorAberturaMercado MONEY
	DECLARE @variacaoDiaria MONEY
	DECLARE @valorIndice MONEY

	SELECT @isin = inserted.ISIN, @dia = inserted.Dia, @valorIndice = inserted.ValorAbertura FROM inserted
	
	SELECT @codigoMercado = CodigoMercado FROM Instrumento_Financeiro AS I WHERE I.ISIN = @isin
	
	SELECT * INTO #valoresMercado FROM Valores_Mercado WHERE Dia = @dia AND Codigo = @codigoMercado 

	IF NOT EXISTS(SELECT * FROM #valoresMercado) -- não existe valores de mercado ainda
		BEGIN
		
			SET @valorAberturaMercado = 0 -- caso não exista dia anterior(tabela sem registos) o valor de Abertura é 0
			SELECT TOP 1 @valorAberturaMercado = ValorIndice FROM Valores_Mercado ORDER BY Dia DESC -- busca do último dia útil

			SET @variacaoDiaria = (@valorIndice - @valorAberturaMercado)

			INSERT INTO Valores_Mercado (Dia, Codigo, ValorIndice, ValorAbertura, VariacaoDiaria) 
			VALUES (@dia, @codigoMercado, @valorIndice, @valorAberturaMercado, @variacaoDiaria)

		END
	ELSE -- já existem valores de mercado 
		BEGIN
			UPDATE Valores_Mercado SET ValorIndice = ValorIndice + @valorIndice WHERE Dia = @dia AND Codigo = @codigoMercado
			UPDATE Valores_Mercado SET VariacaoDiaria = ValorIndice - ValorAbertura WHERE Dia = @dia AND Codigo = @codigoMercado
		END

	DROP TABLE #valoresMercado

END

GO

CREATE TRIGGER updateInstrumentoOnInsertRegisto on Registo
AFTER INSERT,UPDATE
AS
BEGIN
	DECLARE @ISIN VARCHAR(12)
	DECLARE @VALOR_PERCENTAGEM MONEY
	DECLARE @VALOR_VARIACAO MONEY
	DECLARE @VALOR_VARIACAO6MESES MONEY
	DECLARE @VALOR_6MESES_PERCENTAGEM MONEY
	DECLARE @TODAY_DATE DATE
	DECLARE @VALOR_MAXIMO MONEY
	DECLARE @VALOR_MINIMO MONEY
	DECLARE @VALOR_MAXIMO6MESES MONEY
	DECLARE @VALOR_MINIMO6MESES MONEY
	DECLARE @VALOR_ATUAL MONEY
	DECLARE @MEDIA6MESES MONEY

	SELECT @VALOR_MINIMO = inserted.ValorMinimo, @VALOR_MAXIMO = inserted.ValorMaximo, @ISIN = inserted.ISIN, @TODAY_DATE = inserted.Dia, @VALOR_ATUAL = ValorFecho 
	FROM inserted 

	SET @VALOR_VARIACAO = @VALOR_MAXIMO - @VALOR_MINIMO

	SET @VALOR_PERCENTAGEM =  ( (@VALOR_VARIACAO) / ( (@VALOR_MINIMO + @VALOR_MAXIMO) / 2) ) * 100

	SELECT TOP 1 @VALOR_MINIMO6MESES = R.ValorMinimo, @VALOR_MAXIMO6MESES = R.ValorMaximo 
	FROM Registo AS R 	
	WHERE R.ISIN = @ISIN AND R.Dia BETWEEN CONVERT(DATE, DATEADD(month, -6, @TODAY_DATE)) AND @TODAY_DATE
	ORDER BY R.Dia ASC

	SET @VALOR_VARIACAO6MESES = (@VALOR_VARIACAO) - (@VALOR_MAXIMO6MESES - @VALOR_MINIMO6MESES)
	
	SET @VALOR_6MESES_PERCENTAGEM = ( (@VALOR_VARIACAO) / ( (@VALOR_MAXIMO6MESES + @VALOR_MINIMO6MESES) / 2 ) ) * 100

	
	EXEC Media6Meses @ISIN

	UPDATE Instrumento_Financeiro 
	SET ValorAtual = @VALOR_ATUAL, ValorVariacaoDiaria = @VALOR_VARIACAO , PercentagemVariacaoDiaria = @VALOR_PERCENTAGEM, 
	ValorVariacao6Meses = @VALOR_VARIACAO6MESES, PercentagemVariacao6Meses = @VALOR_6MESES_PERCENTAGEM
	WHERE ISIN = @ISIN

END