GO

CREATE or alter PROCEDURE Media6Meses(@isin VARCHAR(12))
AS
BEGIN
	
	DECLARE @media MONEY
	DECLARE @valor MONEY

	DECLARE @today DATE
	DECLARE @days INT


	SET @days = 0
	SET @media = 0
	SET @today = convert(DATE, GETDATE())

	DECLARE @cursor CURSOR
	
	SET @cursor = CURSOR FOR
		SELECT R.ValorFecho FROM Registo AS R
		WHERE R.ISIN = @isin AND R.Dia BETWEEN CONVERT(DATE, DATEADD(month, -6, @today)) AND @today

	OPEN @cursor
	FETCH NEXT FROM @cursor INTO @valor

	WHILE @@FETCH_STATUS=0
		BEGIN 
			SET @days = @days + 1
			SET @media = @media + @valor
			FETCH NEXT FROM @cursor INTO @valor
		END

	CLOSE @cursor
	DEALLOCATE @cursor

	SET @media = @media / @days
	
	UPDATE Instrumento_Financeiro SET Media6Meses = @media WHERE ISIN = @isin
END

GO

CREATE or alter PROCEDURE p_actualizaValorDiario
AS
BEGIN
	DECLARE @id VARCHAR(12) -- ISIN
	DECLARE @dia DATE
	DECLARE @valorAbertura MONEY
	DECLARE @valorFecho MONEY
	DECLARE @valorMaximo MONEY
	DECLARE @valorMinimo MONEY
	DECLARE @valorMaximoObservado MONEY
	DECLARE @valorMinimoObservado MONEY
	DECLARE @horaFecho DATETIME
	DECLARE @horaFechoObservado DATETIME

	DECLARE @cursor CURSOR

	SET @cursor = CURSOR FOR 
		SELECT DISTINCT Identificacao, convert(date, T.Dia) as Dia
		FROM Triplos as T
		INNER JOIN Instrumento_Financeiro AS I ON (T.Identificacao = I.ISIN)
		WHERE T.Observado = 0 -- obter os triplos que ainda não foram observados
		
		OPEN @cursor
		FETCH NEXT FROM @cursor INTO @id, @dia

	WHILE @@FETCH_STATUS=0
		BEGIN 

			SELECT *
			INTO #triplos_temp -- tabela temporária -> visto que iremos usar a mesma tabela várias vezes podemos guardá-la e melhorar a perfomance das próximas queries
			FROM Triplos as T
			WHERE T.Identificacao = @id	AND convert(DATE, T.Dia) = @dia

			SELECT TOP 1 @valorAbertura = T.Valor
			FROM #triplos_temp AS T
			ORDER BY T.dia ASC

			SELECT TOP 1 @valorFecho = T.Valor, @horaFecho = T.Dia
			FROM #triplos_temp AS T
			ORDER BY T.dia DESC

			SELECT TOP 1 @valorMinimo = T.Valor
			FROM #triplos_temp AS T
			ORDER BY T.Valor ASC

			SELECT TOP 1 @valorMaximo = T.Valor
			FROM #triplos_temp AS T
			ORDER BY T.Valor DESC

			-- inserir caso não existir registo
			IF NOT EXISTS(SELECT * FROM REGISTO AS R WHERE R.ISIN = @id AND R.Dia = @dia)
			BEGIN
				INSERT INTO Registo VALUES (@id, @dia, @valorAbertura, @valorFecho, @valorMaximo, @valorMinimo, @horaFecho)
			END

			-- atualizar caso já existir registo
			ELSE 
			BEGIN

				-- obter o valor máximo e mínimo existente no Registo
				SELECT @valorMaximoObservado = R.ValorMaximo, @valorMinimoObservado = R.ValorMinimo, @horaFechoObservado = R.HoraFecho
				FROM Registo AS R WHERE R.ISIN = @id AND R.Dia = @dia
				
				-- atualizar valorMáximo se necessário
				IF(@valorMaximo > @valorMaximoObservado)
					BEGIN
						UPDATE Registo SET ValorMaximo = @valorMaximo WHERE ISIN = @id AND Dia = @dia
					END

				-- atualizar valorMinimo se necessário
				IF(@valorMinimo < @valorMinimoObservado)
					BEGIN
						UPDATE Registo SET ValorMinimo = @valorMinimo WHERE ISIN = @id AND Dia = @dia
					END

				IF(@horaFecho > @horaFechoObservado) 
					BEGIN
						UPDATE Registo SET HoraFecho = @horaFecho, ValorFecho = @valorFecho WHERE ISIN = @id AND Dia = @dia
					END

			END

		DROP TABLE #triplos_temp -- É necessário dar reset à tabela
		FETCH NEXT FROM @cursor INTO @id, @dia
		END
		
	CLOSE @cursor
	DEALLOCATE @cursor

	UPDATE Triplos SET Observado = 1 WHERE Observado = 0

END
