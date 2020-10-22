using DAL_Specific;
using DALInterfaces;
using Entidades;
using Entities;
using System;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Transactions;

namespace Exercicio1
{
    public class ExerciciosADO
    {
        private static readonly string p_atualizaValor = "p_actualizaValorDiario";

        private static void P_atualizaValorFunc(SqlConnection con)
        {
            using (var ts = new TransactionScope())
            {
                using (SqlCommand cmd = new SqlCommand(p_atualizaValor, con) { CommandType = CommandType.StoredProcedure })
                {
                    try
                    {
                        con.Open();
                        cmd.ExecuteNonQuery();
                    }
                    catch (Exception e)
                    {
                        Console.WriteLine(e);
                    }
                    finally
                    {
                        con.Close();
                    }
                }
                ts.Complete();
            }
        }

        private static void Media6meses(SqlConnection con, string isin)
        {
            using (var ts = new TransactionScope())
            {
                using (SqlCommand cmd = new SqlCommand("Media6Meses", con) { CommandType = CommandType.StoredProcedure })
                {
                    try
                    {
                        cmd.Parameters.AddWithValue("@isin", isin);
                    
                        Console.WriteLine("\nChamada à função que atualiza a média de instrumento");

                        con.Open();
                        cmd.ExecuteNonQuery();
                    }
                    catch (Exception e)
                    {
                        Console.WriteLine(e);
                    }
                    finally
                    {
                        con.Close();
                    }
                }
                ts.Complete();
            }
        }

        public static void ExercicioF(string cs, string isin, decimal valor, DateTime date)
        {
            try
            {
                using (var ts = new TransactionScope())
                {
                    IMapperTriplo triplosMapper = new MapperTriplo();
                    IMapperRegisto registoMap = new MapperRegisto();

                    Triplo triplo = new Triplo()
                    {
                        Identificacao = isin,
                        Dia = date,
                        Valor = valor
                    };

                    RegistoKey key = new RegistoKey(isin, date);

                    triplosMapper.Create(triplo);

                    Console.WriteLine("\nChamada ao stored procedure:\n");

                    P_atualizaValorFunc(new SqlConnection(cs));

                    Console.WriteLine("Registo depois da chamada ao stored procedure:");
                    Console.WriteLine(registoMap.Read(key).ToString());

                    ts.Complete();
                }
            }
            catch(Exception ex)
            {
                Console.WriteLine("Excepção apanhada : " + ex.Message);
            }
        }

        public static void ExercicioG(string cs, string isin)
        {
            try
            {
                Console.WriteLine("\nInformação de instrumento :");

                IMapperInstrumento mapper = new MapperInstrumento();
                Console.WriteLine(mapper.Read(isin).ToString());

                using (var ts = new TransactionScope())
                {
                
                    //Processar novos triplos, caso existam
                    P_atualizaValorFunc(new SqlConnection(cs));

                    Media6meses(new SqlConnection(cs), isin);

                    ts.Complete();
                }

                Console.WriteLine("\nInformação de instrumento :");
                Console.WriteLine(mapper.Read(isin).ToString());
            }
            catch (Exception ex)
            {
                Console.WriteLine("Excepção apanhada : " + ex.Message);
            }
        }

        public static void ExercicioH(string cs, String isin, DateTime date, decimal val)
        {
            try
            {
                using (var ts = new TransactionScope())
                {
                    IMapperInstrumento mapper = new MapperInstrumento();
                    IMapperTriplo mapperTriplo = new MapperTriplo();

                    Console.WriteLine("Instrumento Antes do Procedimento:");
                    Console.WriteLine(mapper.Read(isin).ToString());

                    Triplo triplo = new Triplo()
                    {
                        Identificacao = isin,
                        Dia = date,
                        Valor = val
                    };

                    Console.WriteLine("Criação de triplo");
                    mapperTriplo.Create(triplo);

                    Console.WriteLine("\nChamada ao procedimento.\n");
                    P_atualizaValorFunc(new SqlConnection(cs));

                    Console.WriteLine("Instrumento Depois do Procedimento:");
                    Console.WriteLine(mapper.Read(isin).ToString());
                    ts.Complete();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Excepção apanhada : " + ex.Message);
            }
        }

        public static void ExercicioI(string nomeP, string cc, string nif, string nomeCLiente)
        {
            try
            {
                using (var ts = new TransactionScope())
                {
                    IMapperCliente mapper = new MapperCliente();

                    Cliente cliente = new Cliente
                    {
                        CC = cc,
                        NIF = nif,
                        NomeCliente = nomeCLiente,
                        NomePortfolio = nomeP,
                    };

                    Console.WriteLine("Criação de um novo Cliente");
                    mapper.Create(cliente);

                    Console.WriteLine("Informação de cliente:\n" + mapper.Read(cc).ToString());

                    ts.Complete();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Excepção apanhada : " + ex.Message);
            }
        }

        public static void ExercicioJ(string cc, string isin, int quantidade)
        {
            try
            {
                using (var ts = new TransactionScope())
                {
                    IMapperPosicao mapperPos = new MapperPosicao();
                    IMapperCliente mapperCliente = new MapperCliente();

                    Console.WriteLine("\nInformação de cliente antes da atualização da quantidade:\n" + mapperCliente.Read(cc).ToString());

                    var pos = mapperPos.Read(new PosicaoKey(isin, cc));
                    pos.Quantidade += quantidade;

                    mapperPos.Update(pos);
                    Console.WriteLine("\nInformação de cliente depois da atualização da quantidade:\n" + mapperCliente.Read(cc).ToString());

                    ts.Complete();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Excepção apanhada : " + ex.Message);
            }
        }

        public static void Exercicio1B_Create(string codigoMercado, string nomeMercado, string descMercado, string isin, string descrInst, DateTime dt, int val)
        {
            try
            {
                using (var ts = new TransactionScope())
                {
                    IMapperMercado mapperMercado = new MapperMercado();
                    IMapperValoresMercado mapperValoresMercado = new MapperValoresMercado();
                    IMapperInstrumento mapperInstrumento = new MapperInstrumento();
                    IMapperTriplo mapperTriplo = new MapperTriplo();
                    IMapperRegisto mapperRegisto = new MapperRegisto();


                    // Mercado
                    Mercado mercado = new Mercado()
                    {
                        Codigo = codigoMercado,
                        Nome = nomeMercado,
                        Descricao = descMercado
                    };

                    Console.WriteLine("\nCriação de mercado\n");
                    mapperMercado.Create(mercado);

                    // Instrumento
                    Instrumento instrumento = new Instrumento()
                    {
                        Isin = isin,
                        CodigoMercado = codigoMercado,
                        Descricao = descrInst
                    };


                    Console.WriteLine("\nCriação de instrumento\n");
                    mapperInstrumento.Create(instrumento);

                    // Triplo
                    Triplo triplo = new Triplo()
                    {
                        Identificacao = isin,
                        Dia = dt.Date,
                        Valor = val
                    };

                    Console.WriteLine("\nCriação de Triplo\n");
                    mapperTriplo.Create(triplo);

                    // Registo
                    Registo registo = new Registo()
                    {
                        Isin = isin,
                        Dia = dt,
                        ValorAbertura = val,
                        ValorMaximo = val,
                        ValorMinimo = val,
                        ValorFecho = val,
                        HoraFecho = dt
                    };

                    Console.WriteLine("\nCriação de registo\n");
                    mapperRegisto.Create(registo);

                    Console.WriteLine("Informação de valores de mercado:");
                    Console.WriteLine(mapperValoresMercado.Read(new ValoresMercadoKey(codigoMercado, dt)).ToString());
                    Console.WriteLine();

                    ts.Complete();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Excepção apanhada : " + ex.Message);
            }
        }

        public static void Exercicio1B_Remove(string codigoMercado)
        {
            try
            {
                using (var ts = new TransactionScope())
                {
                    IMapperMercado mapperMercado = new MapperMercado();

                    Mercado mercado = mapperMercado.Read(codigoMercado);
                    mapperMercado.Delete(mercado);

                    Console.WriteLine("Mercado " + codigoMercado + " deleted with sucess!\n");

                    ts.Complete();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Excepção apanhada : " + ex.Message);
            }
        }

        public static void Exercicio1B_Update(string isin, DateTime date, int val)
        {
            try
            {
                using (var ts = new TransactionScope())
                {
                    RegistoKey key = new RegistoKey(isin, date.Date);
                    IMapperRegisto mapperRegisto = new MapperRegisto();
                    IMapperValoresMercado mapperValoresMercado = new MapperValoresMercado();
                    IMapperInstrumento mapperInstrumento = new MapperInstrumento();

                    Instrumento instrumento = mapperInstrumento.Read(isin);

                    Console.WriteLine("Informação de valores de mercado antes do update:");
                    Console.WriteLine(mapperValoresMercado.Read(new ValoresMercadoKey(instrumento.CodigoMercado, date)).ToString());

                    //Update
                    Registo registo = mapperRegisto.Read(key);
                    registo.ValorAbertura = val;
                    mapperRegisto.Update(registo);
                    Console.WriteLine("\nUpdate\n");

                    Console.WriteLine("Informação de valores de mercado depois do update:");
                    Console.WriteLine(mapperValoresMercado.Read(new ValoresMercadoKey(instrumento.CodigoMercado, date)).ToString() + "\n");

                    ts.Complete();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Excepção apanhada : " + ex.Message);
            }

        }
        public static void Exercicio1C(string cc)
        {
            try
            {

                using (var ts = new TransactionScope())
                {
                    IMapperCliente mapperCliente = new MapperCliente();

                    mapperCliente.Delete(mapperCliente.Read(cc));
                    Console.WriteLine("Cliente removido com sucesso!\n");

                    ts.Complete();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Excepção apanhada : " + ex.Message);
            }

        }

        public static void ExercicioK(string cs, string nomePort)
        {
            try
            {
                using (var ts = new TransactionScope())
                {
                    using (var con = new SqlConnection(cs))
                    {
                        using (var command = con.CreateCommand())
                        {
                            command.CommandType = CommandType.Text;
                            command.CommandText = "SELECT * FROM listar_portfolio(@nomeP)";

                            command.Parameters.AddWithValue("@nomeP", nomePort);

                            con.Open();

                            SqlDataReader reader = command.ExecuteReader();
                            
                            Console.WriteLine("\nListagem do portfólio {0}", nomePort);

                            while (reader.Read())
                            {
                                Console.WriteLine("\nISDN: {0}", reader.GetString(0));
                                Console.WriteLine("Quantidade: {0}", (float)reader.GetDouble(1));
                                Console.WriteLine("Valor Atual do instrumento: {0}", reader.GetSqlMoney(2));
                                Console.WriteLine("Valor Total da Posição: {0}", reader.GetSqlMoney(3));
                                Console.WriteLine("Percentagem de Variação do instrumento em relação ao dia anterior: {0}", reader.GetSqlMoney(4));
                            }
                            reader.Close();
                        }
                    }
                    ts.Complete();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Excepção apanhada : " + ex.Message);
            }
        }
    }
}
