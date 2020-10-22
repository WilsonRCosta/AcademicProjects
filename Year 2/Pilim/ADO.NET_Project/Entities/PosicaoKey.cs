using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Entities
{
    public class PosicaoKey
    {
        private string isin;
        private string cc;
        public PosicaoKey(string isin, string cc)
        {
            this.isin = isin;
            this.cc = cc;
        }
        public string Isin { get { return isin; } }
        public string CC { get { return cc; } }
    }
}
