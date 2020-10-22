using DALInterfaces;
using Entities;
using System;
using System.Configuration;
using System.Data.SqlClient;
using System.Transactions;

namespace DAL_Specific
{
    public class MapperMercado : IMapperMercado
    {
        private string cs;

        public MapperMercado()
        {
            cs = ConfigurationManager.ConnectionStrings["TL51N_11"].ConnectionString;
        }
        public void Create(Mercado mercado)
        {
            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand();
                command.CommandText = "INSERT INTO Mercado_Financeiro (Codigo, Nome, Descricao) VALUES (@codigo, @nome, @desc)";
                SqlParameter codigo = new SqlParameter("@codigo", mercado.Codigo);
                SqlParameter nome = new SqlParameter("@nome", mercado.Nome);
                SqlParameter desc = new SqlParameter("@desc", mercado.Descricao);
                command.Parameters.Add(codigo);
                command.Parameters.Add(nome);
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

        public void Delete(Mercado mercado)
        {
            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand
                {
                    CommandText = "DELETE FROM Mercado_Financeiro WHERE Codigo = @codigo;"
                };

                SqlParameter cod = new SqlParameter("@codigo", mercado.Codigo);
                command.Parameters.Add(cod);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;

                    con.Open();

                    command.ExecuteNonQuery();
                }

                ts.Complete();
            }

        }

        public Mercado Read(string codigo)
        {
            Mercado mercado = null;

            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand
                {
                    CommandText = "SELECT * FROM Mercado_Financeiro AS M WHERE M.Codigo = @codigo;"
                };
                SqlParameter cod = new SqlParameter("@codigo", codigo);

                command.Parameters.Add(cod);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;

                    con.Open();
                    SqlDataReader reader = command.ExecuteReader();
                    while (reader.Read())
                    {
                        mercado = new Mercado()
                        {
                            Codigo = reader.GetString(0),
                            Nome = reader.GetString(1),
                            Descricao = reader.GetString(2)
                        };
                    }
                    reader.Close();
                }
                ts.Complete();
            }
            return mercado;
        }

        public void Update(Mercado entity)
        {
            throw new NotImplementedException();
        }
    }
}
