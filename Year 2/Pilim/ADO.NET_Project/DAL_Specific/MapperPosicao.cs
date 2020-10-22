using DALInterfaces;
using Entities;
using System;
using System.Configuration;
using System.Data.SqlClient;
using System.Transactions;

namespace DAL_Specific
{
    public class MapperPosicao : IMapperPosicao
    {
        private string cs;
        public MapperPosicao()
        {
            cs = ConfigurationManager.ConnectionStrings["TL51N_11"].ConnectionString;
        }
        public void Create(Posicao posicao)
        {
            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand
                {
                    CommandText = "INSERT INTO Posicao (ISIN, CC, Quantidade) VALUES (@isin, @cc, @quantidade)"
                };
                SqlParameter isin = new SqlParameter("@isin", posicao.ISIN);
                SqlParameter cc = new SqlParameter("@cc", posicao.CC);
                SqlParameter quantidade = new SqlParameter("@quantidade", posicao.Quantidade);
                command.Parameters.Add(isin);
                command.Parameters.Add(cc);
                command.Parameters.Add(quantidade);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;

                    con.Open();

                    command.ExecuteNonQuery();

                }
                ts.Complete();
            }
        }

        public void Delete(Posicao posicao)
        {
            throw new NotImplementedException();
        }

        public Posicao Read(PosicaoKey key)
        {
            Posicao pos = null;

            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand
                {
                    CommandText = "SELECT * FROM Posicao AS P WHERE P.ISIN = @isin AND P.CC = @cc ;"
                };
                SqlParameter isin = new SqlParameter("@isin", key.Isin);
                SqlParameter cc = new SqlParameter("@cc", key.CC);

                command.Parameters.Add(isin);
                command.Parameters.Add(cc);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;

                    con.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    while (reader.Read())
                    {
                        pos = new Posicao()
                        {
                            ISIN = reader.GetString(0),
                            CC = reader.GetString(1),
                            Quantidade = (float)reader.GetDouble(2)
                        };
                    }
                    reader.Close();
                }
                ts.Complete();
            }
            return pos;
        }

        public void Update(Posicao posicao)
        {
            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand
                {
                    CommandText = "UPDATE Posicao SET Quantidade = @quantidade WHERE ISIN = @isin AND CC = @cc"
                };
                SqlParameter isin = new SqlParameter("@isin", posicao.ISIN);
                SqlParameter cc = new SqlParameter("@cc", posicao.CC);
                SqlParameter quantidade = new SqlParameter("@quantidade", posicao.Quantidade);
                command.Parameters.Add(isin);
                command.Parameters.Add(cc);
                command.Parameters.Add(quantidade);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;

                    con.Open();

                    command.ExecuteNonQuery();
                }
                ts.Complete();
            }
        }
    }
}
