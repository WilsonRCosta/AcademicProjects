using DALInterfaces;
using Entities;
using System.Configuration;
using System.Data.SqlClient;
using System.Transactions;

namespace DAL_Specific
{
    public class MapperInstrumento : IMapperInstrumento
    {
        private string cs;

        public MapperInstrumento()
        {
            cs = ConfigurationManager.ConnectionStrings["TL51N_11"].ConnectionString;
        }
        public void Create(Instrumento instrumento)
        {
            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand();
                command.CommandText = "INSERT INTO Instrumento_Financeiro (ISIN, CodigoMercado, Descricao) VALUES (@isin, @codigo, @desc)";
                SqlParameter isin = new SqlParameter("@isin", instrumento.Isin);
                SqlParameter codigo = new SqlParameter("@codigo", instrumento.CodigoMercado);
                SqlParameter desc = new SqlParameter("@desc", instrumento.Descricao);
                command.Parameters.Add(isin);
                command.Parameters.Add(codigo);
                command.Parameters.Add(desc);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;

                    con.Open();

                    command.ExecuteNonQuery();

                }
                ts.Complete();
            }
        }
        public void Delete(Instrumento entity)
        {
            throw new System.NotImplementedException();
        }

        public Instrumento Read(string id)
        {
            Instrumento inst = null;

            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand
                {
                    CommandText = "SELECT * FROM Instrumento_Financeiro AS I WHERE I.ISIN = @isin;"
                };

                SqlParameter isin = new SqlParameter("@isin", id);

                command.Parameters.Add(isin);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;

                    con.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    while (reader.Read())
                    {
                        inst = new Instrumento()
                        {
                                Isin = reader.GetString(0),

                                CodigoMercado = reader.GetString(1),

                                Descricao = reader.GetString(2),

                                ValorAtual = (decimal) reader.GetSqlMoney(3),

                                ValorVariacaoDiaria = (decimal) reader.GetSqlMoney(4),
                                PercentagemVariacaoDiaria = (decimal) reader.GetSqlMoney(5),

                                ValorVariacao6Meses = (decimal) reader.GetSqlMoney(6),
                                PercentagemVariacao6Meses = (decimal) reader.GetSqlMoney(7),

                                Media6Meses = (decimal) reader.GetSqlMoney(8)
                        };
                    }
                    reader.Close();
                }
                ts.Complete();
            }
            return inst;
        }

        public void Update(Instrumento entity)
        {
            throw new System.NotImplementedException();
        }
    }
}
