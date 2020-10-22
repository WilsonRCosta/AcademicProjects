
namespace Entities
{
    public class Instrumento
    {
        public string Isin { get; set; }
        public string CodigoMercado { get; set; }
        public string Descricao { get; set; }
        public decimal ValorAtual { get; set; }
        public decimal ValorVariacaoDiaria { get; set; }
        public decimal PercentagemVariacaoDiaria { get; set; }
        public decimal ValorVariacao6Meses { get; set; }
        public decimal PercentagemVariacao6Meses { get; set; }
        public decimal Media6Meses { get; set; }

        public override string ToString()
        {
            return "ISIN : " + Isin + ", Codigo de Mercado : " + CodigoMercado + ", Descrição : " + Descricao +
                ", Valor Atual : " + ValorAtual + ", Valor Variação Diária : " + ValorVariacaoDiaria + ", Percentagem de Variação Diária : "
                + PercentagemVariacaoDiaria + ", Valor de Variação a 6 meses : " + ValorVariacao6Meses + ", " +
                "Percentagem de Variação a 6 meses : " + PercentagemVariacao6Meses + ", Media a 6 meses : " + Media6Meses;
        }
    }
}
