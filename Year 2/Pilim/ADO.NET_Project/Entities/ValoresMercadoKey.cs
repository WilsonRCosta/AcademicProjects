using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Entidades
{
    public class ValoresMercadoKey
    {
        public ValoresMercadoKey(string codigo, DateTime dia)
        {
            Codigo = codigo;
            Dia = dia;
        }
        public string Codigo { get; }
        public DateTime Dia { get; }
    }
}
