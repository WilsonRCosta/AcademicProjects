CREATE FUNCTION listar_portfolio (@nomeP varchar(255))
RETURNS @portfolio TABLE (
        isdn VARCHAR(12),
        quantidade FLOAT,
        ValorAtualInstrumento MONEY,
		ValorTotal MONEY,
        PercentagemVariacao MONEY
    )
AS
BEGIN
	DECLARE @cc VARCHAR(12)
	DECLARE @quantidade FLOAT
	DECLARE @isin VARCHAR(12)
	DECLARE @valorFecho MONEY
	DECLARE @valorAtual MONEY
	DECLARE @valorTotal MONEY
	DECLARE @percentagemVar MONEY
	DECLARE @ultimoDia DATE

	SELECT @cc = C.CC FROM Cliente AS C WHERE C.NomePortfolio = @nomeP

	DECLARE @cursor CURSOR
	SET @cursor = CURSOR FOR
		SELECT P.ISIN, P.Quantidade FROM Posicao AS P WHERE P.CC = @cc

	OPEN @cursor
	FETCH NEXT FROM @cursor INTO @isin, @quantidade

	WHILE @@FETCH_STATUS=0
		BEGIN 
			-- último dia útil
			SELECT TOP 1 @ultimoDia = R.Dia FROM Registo AS R 
			WHERE R.ISIN = @isin
			ORDER BY R.Dia DESC

			SELECT TOP 1 @valorFecho = R.ValorFecho FROM Registo AS R 
			WHERE R.ISIN = @isin AND R.Dia != @ultimoDia
			ORDER BY R.Dia DESC

			SELECT @valorAtual = I.ValorAtual FROM Instrumento_Financeiro AS I WHERE I.ISIN = @isin
			
			SET @valorTotal = @valorAtual * @quantidade
			
			SET @percentagemVar = ( ( (@valorAtual - @valorFecho) / @valorFecho ) * 100 )

			INSERT INTO @portfolio VALUES( @isin, @quantidade, @valorAtual, @valorTotal ,@percentagemVar) 

			FETCH NEXT FROM @cursor INTO @isin, @quantidade
		END

		
	CLOSE @cursor
	DEALLOCATE @cursor


	RETURN
END