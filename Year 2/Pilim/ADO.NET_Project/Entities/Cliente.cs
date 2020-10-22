
namespace Entities
{
    public class Cliente
    {
        public string CC { get; set; }
        public string NIF { get; set; }
        public string NomeCliente { get; set; }
        public string NomePortfolio { get; set; }
        public decimal ValorTotalPortfolio { get; set; }

        public override string ToString()
        {
            return "CC : " + CC + ", NIF : " + NIF + ", Nome de Cliente : " + NomeCliente + ", Nome de Portfolio : " + NomePortfolio
                + ", Valor Total de Portfolio : " + ValorTotalPortfolio;
        }

    }
}
