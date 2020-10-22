using System;

namespace Entities
{
    public class TriploKey
    {
        public string id;
        public DateTime dia;
        public TriploKey(string id, DateTime dia)
        {
            this.id = id;
            this.dia = dia;
        }
        public string Identificacao { get { return id; } }
        public DateTime Dia { get { return dia; } }
    }
}
