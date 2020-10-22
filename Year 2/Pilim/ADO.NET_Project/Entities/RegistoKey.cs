using System;

namespace Entities
{
    public class RegistoKey
    {
        public string isin;
        public DateTime dia;

        public RegistoKey(string isin, DateTime dia)
        {
            this.isin = isin;
            this.dia = dia;
        }
        public string Isin { get { return isin; } }
        public DateTime Dia { get { return dia; } }
    }
}
