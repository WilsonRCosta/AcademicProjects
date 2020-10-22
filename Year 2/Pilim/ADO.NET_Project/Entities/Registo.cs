using System;

namespace Entities
{
    public class Registo
    {
        public string Isin { get; set; }
        public DateTime Dia { get; set; }
        public decimal ValorAbertura { get; set; }
        public decimal ValorFecho { get; set; }
        public decimal ValorMaximo { get; set; }
        public decimal ValorMinimo { get; set; }
        public DateTime HoraFecho { get; set; }

        public override string ToString()
        {
            return "ISIN : " + Isin + ", Dia : " + Dia + ", Valor de Abertura : " + ValorAbertura +
                ", Valor Fecho : " + ValorFecho + ", Valor Maximo : " + ValorMaximo + ", Valor Minimo :" + ValorMinimo
                 + ", Hora de Fecho : " + HoraFecho;
        }
    }
}
