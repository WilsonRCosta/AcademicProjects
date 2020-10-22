using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Entidades
{
    public class ValoresMercado
    {
        public string CodigoMercado { get; set; }
        public DateTime Dia { get; set; }
        public decimal ValorIndice { get; set; }
        public decimal ValorAbertura { get; set; }
        public decimal VariacaoDiaria { get; set; }

        public override string ToString()
        {
            return "CodigoMercado : " + CodigoMercado + ", Dia : " + Dia + ", Valor Indice : " + ValorIndice + ", Valor Abertura : " + ValorAbertura
                + ", Variacao Diaria : " + VariacaoDiaria;
        }
    }
}
