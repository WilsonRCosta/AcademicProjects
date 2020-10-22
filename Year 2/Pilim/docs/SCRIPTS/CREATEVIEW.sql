CREATE or alter VIEW resumo_portfolios AS	
SELECT
	NomeCliente,
	NomePortfolio,
	COUNT(Cliente.NomePortfolio) as NumeroInstrumentos,
	ValorTotalPortfolio,
	SUM(Posicao.Quantidade) as NumeroAcções
FROM
	Cliente
INNER JOIN Posicao ON
	Posicao.CC = Cliente.CC
GROUP BY
	NomeCliente,
	NomePortfolio,
	ValorTotalPortfolio;