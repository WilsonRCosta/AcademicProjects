using DALInterfaces;
using Entities;
using System;
using System.Configuration;
using System.Data.SqlClient;
using System.Transactions;

namespace DAL_Specific
{
    public class MapperCliente : IMapperCliente
    {
        private string cs;

        public MapperCliente()
        {
            cs = ConfigurationManager.ConnectionStrings["TL51N_11"].ConnectionString;
        }
        public void Create(Cliente cliente)
        {
            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand();
                command.CommandText = "INSERT INTO Cliente (CC, NIF, NomeCliente, NomePortfolio) VALUES (@cc, @nif, @nomecliente, @nomeport)";
                SqlParameter cc = new SqlParameter("@cc", cliente.CC);
                SqlParameter nif = new SqlParameter("@nif", cliente.NIF);
                SqlParameter nomecliente = new SqlParameter("@nomecliente", cliente.NomeCliente);
                SqlParameter nomeport = new SqlParameter("@nomeport", cliente.NomePortfolio);
                command.Parameters.Add(cc);
                command.Parameters.Add(nif);
                command.Parameters.Add(nomecliente);
                command.Parameters.Add(nomeport);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;

                    con.Open();

                    command.ExecuteNonQuery();

                }
                ts.Complete();
            }
        }

        public void Delete(Cliente cliente)
        {
            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand
                {
                    CommandText = "DELETE FROM Cliente WHERE CC = @cc;"
                };

                SqlParameter cc = new SqlParameter("@cc", cliente.CC);
                command.Parameters.Add(cc);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;

                    con.Open();

                    command.ExecuteNonQuery();
                }

                ts.Complete();
            }
        }

        public Cliente Read(string id)
        {
            Cliente cliente = null;

            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand
                {
                    CommandText = "SELECT * FROM Cliente AS C WHERE C.CC = @cc;"
                };
                SqlParameter cc = new SqlParameter("@cc", id);

                command.Parameters.Add(cc);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;

                    con.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    while (reader.Read())
                    {
                        cliente = new Cliente()
                        {
                            CC = reader.GetString(0),
                            NIF = reader.GetString(1),
                            NomeCliente = reader.GetString(2),
                            NomePortfolio = reader.GetString(3),
                            ValorTotalPortfolio = (decimal) reader.GetSqlMoney(4)
                        };
                    }
                    reader.Close();
                }
                ts.Complete();
            }
            return cliente;
        }

        public void Update(Cliente entity)
        {
            throw new NotImplementedException();
        }
    }
}
