using DALInterfaces;
using Entidades;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Transactions;

namespace DAL_Specific
{
    public class MapperValoresMercado : IMapperValoresMercado
    {
        private string cs;

        public MapperValoresMercado()
        {
            cs = ConfigurationManager.ConnectionStrings["TL51N_11"].ConnectionString;
        }
        public void Create(ValoresMercado valoresMercado)
        {
            throw new NotImplementedException();
        }

        public void Delete(ValoresMercado valoresMercado)
        {
            throw new NotImplementedException();
        }

        public ValoresMercado Read(ValoresMercadoKey key)
        {
            ValoresMercado valores = null;

            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand
                {
                    CommandText = "SELECT * FROM Valores_Mercado AS VM WHERE VM.Dia = @dia AND VM.Codigo = @codigo;"
                };
                SqlParameter codigo = new SqlParameter("@codigo", key.Codigo);
                SqlParameter dia = new SqlParameter("@dia", key.Dia.Date);

                command.Parameters.Add(codigo);
                command.Parameters.Add(dia);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;

                    con.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    while (reader.Read())
                    {
                        valores = new ValoresMercado()
                        {
                            Dia = reader.GetDateTime(0),
                            CodigoMercado = reader.GetString(1),
                            ValorIndice = (decimal)reader.GetSqlMoney(2),
                            ValorAbertura = (decimal)reader.GetSqlMoney(3),
                            VariacaoDiaria = (decimal)reader.GetSqlMoney(4)
                        };
                    }
                    reader.Close();
                }
                ts.Complete();
            }
            return valores;
        }

        public void Update(ValoresMercado valoresMercado)
        {
            throw new NotImplementedException();
        }
    }
}
