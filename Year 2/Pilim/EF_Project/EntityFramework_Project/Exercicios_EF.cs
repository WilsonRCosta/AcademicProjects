using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;

namespace Si2_Fase2_EF
{
    public class CustomSqlException : Exception
    {
        public CustomSqlException() { }
        public CustomSqlException(string message, SqlException innerException) : base(message, innerException) { }
    }

    public class Exercicios_EF {
        public static void ExercicioF(String isin, decimal valor, DateTime data) 
        {
            Registo registo;
            Triplos triplo = new Triplos() {
                Identificacao = isin,
                Dia = data,
                Valor = valor,
                Observado = false
            };
            
            using (PilimEntities ctx = new PilimEntities())
            {
                CheckISINExists(ctx, isin);
                try {
                    ctx.Triplos.Add(triplo);
                    ctx.SaveChanges();
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro! Triplo criado ja existe. ", ex);
                }
                try {
                    registo = ctx.Registo.Find(triplo.Identificacao, data);
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro! Registo procurado nao existe. ", ex);
                }
                Console.WriteLine(registo.ToString());
                try {
                    ctx.p_actualizaValorDiario();
                    ctx.SaveChanges();
                    Console.WriteLine("Valor diario atualizado.");
                } catch (SqlException ex) {
                    throw new CustomSqlException("Erro ao atualizar valor diario ", ex);
                }
                
            }

            using (PilimEntities ctx = new PilimEntities())
            {
                try {
                    registo = ctx.Registo.Find(triplo.Identificacao, data);
                }
                catch (SqlException ex) {
                    throw new CustomSqlException("Erro! Registo criado ja existe. ", ex);
                }
                Console.WriteLine(registo.ToString());
            }
        }

        public static void ExercicioG(string isin)
        {
            using (PilimEntities ctx = new PilimEntities())
            {
                CheckISINExists(ctx, isin);
                /*
                using (var ctx = new PilimEntities()) {
                    var i = new Instrumento_Financeiro();
                    i.ISIN = "1";
                    var query = ctx.calcAvg6Meses(i.ISIN, new DateTime());
                    var df = ctx.DADOS_INSTRUMENTO
                        .Where(s => s.ISIN == 1)
                        .FirstOrDefault<DADOS_INSTRUMENTO>();
                    Console.WriteLine(df.AVG_6MESES);
                }*/
                //....TODO
            }
           
        }
        public static void ExercicioH(string isin, DateTime data, decimal valor)
        {
            Instrumento_Financeiro inst = null;
           
            using (PilimEntities ctx = new PilimEntities())
            {
                CheckISINExists(ctx, isin);
                Console.WriteLine("Valor Actual do Instrumento<{0}> : {1}", isin, inst.ValorAtual);
                
                Triplos novoTriplo = new Triplos()
                {
                    Identificacao = isin,
                    Dia = data,
                    Valor = valor,
                    Observado = false
                };
                try {
                    ctx.Triplos.Add(novoTriplo);
                    ctx.SaveChanges();
                    Console.WriteLine("Novo triplo criado.");
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro! Triplo criado ja existe. ", ex);
                }
                try {
                    ctx.p_actualizaValorDiario();
                    ctx.SaveChanges();
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro ao atualizar o valor diario.", ex);
                } 
            }
            
            using (PilimEntities ctx = new PilimEntities()) 
            {
                Console.WriteLine("Instrumento Depois do Procedimento:");
                inst = CheckISINExists(ctx, isin); 
                Console.WriteLine("Valor Actual do Instrumento<{0}> : {1}", isin, inst.ValorAtual);
            }
        }
        public static void ExercicioI(string nomePort, string cc, string nif, string nomeC, int valTot) {
            Cliente cliente = new Cliente() {
                CC = cc,                        //"123824538674",
                NIF = nif,                      //"156368578",
                NomeCliente = nomeC,            //"Eusebio Ferreira",
                NomePortfolio = nomePort,
                ValorTotalPortfolio = valTot    //5000
            };
           
            using (PilimEntities ctx = new PilimEntities())
            {
                ShowPortfolios(ctx);
                
                try {
                    ctx.Cliente.Add(cliente);
                    ctx.SaveChanges();
                    Console.WriteLine("Novo portefolio adicionado");
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro! Portefolio inserido ja existe. ", ex);
                }
            }

            using (PilimEntities ctx = new PilimEntities())
            {
                ShowPortfolios(ctx);
            }
        }
        public static void ExercicioJ(string cc, string isin, double valor)
        {
            using (PilimEntities ctx = new PilimEntities())
            {
                Posicao posicao = null;
                try {
                    posicao = ctx.Posicao.Find(isin, cc);
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro! Posicao procurada inexistente. ", ex);
                }
                Console.WriteLine("Quantidade sobre a posicao com: ");
                Console.WriteLine("\n\tInstrumento: {0}\n\tCC: {1}\n\tQuantidade: {2}", isin, cc, posicao.Quantidade);
                try {
                    posicao.Quantidade += valor;
                    ctx.SaveChanges();
                    Console.WriteLine("Nova quantidade sobre a posicao com: ");
                    Console.WriteLine("\n\tInstrumento: {0}\n\tCC: {1}\n\tQuantidade: {2}", isin, cc, posicao.Quantidade);
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro ao adicionar quantidade a posicao. ", ex);
                }
            }   
        }

        public static void ExercicioK(string nomePort)
        {
            using (PilimEntities ctx = new PilimEntities()) 
            {
                try {
                    var portfolio = ctx.listar_portfolio(nomePort)
                        .Select(port => new {
                            port.isdn,
                            port.quantidade,
                            port.ValorAtual,
                            port.PercentagemVariacao
                        });
                    Console.WriteLine("Detalhes do portfolio " + nomePort);
                    foreach (var p in portfolio)
                    {
                        Console.WriteLine("ISDN: {0}\nQuantidade: {1}\nValor Atual: {2}\nPercentagem de Variacao: {3}",
                            p.isdn, p.quantidade, p.ValorAtual, p.PercentagemVariacao);
                    }
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro! Portfolio nao encontrado. ", ex);
                }
            }
        }

        public static void Exercicio1B_Create(string codigo, string nome, string desc, string isin, DateTime date, int val_abert)
        {
            Mercado_Financeiro mercado = new Mercado_Financeiro()
            {
                Codigo = codigo,    //"E9BT",
                Nome = nome,        //"Pilim4Everyone",
                Descricao = desc,   //"Contabilidade e Finanças"
            };
          
            using (var ctx = new PilimEntities())
            {
                try {
                    ctx.Mercado_Financeiro.Add(mercado);
                    ctx.SaveChanges();
                    Console.WriteLine("Mercado criado sem registos.");
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro! Mercado criado ja existe. ", ex);
                }
                ShowValoresMercado(ctx);
            }

            Registo registo = new Registo()
            {
                ISIN = isin,
                Dia = date,
                ValorAbertura = val_abert
            };

            using (var ctx = new PilimEntities())
            {
                CheckISINExists(ctx, isin);
                try {
                    ctx.Registo.Add(registo);
                    ctx.SaveChanges();
                    Console.WriteLine("Valores do mercado indicado criados.");
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro! Registo inserido ja existe. ", ex);
                }
                ShowValoresMercado(ctx);
            }
        }
        public static void Exercicio1B_Update(string isin, DateTime date, int val)
        {
            using (var ctx = new PilimEntities())
            {
                Registo registo;
                try {
                    registo = ctx.Registo.Find(isin, date);
                    ShowValoresMercado(ctx);
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro! Registo especificado nao existe.", ex);
                }
                try {
                    registo.ValorAbertura = val;
                    ctx.SaveChanges();
                    Console.WriteLine("Valor de mercado atualizado.");
                    ShowValoresMercado(ctx);
                } catch (SqlException ex) {
                    throw new CustomSqlException("Erro! Nao foi possivel atualizar o valor do registo indicado. ", ex);
                }
            }
        }
        public static void Exercicio1B_Remove(string isin, DateTime date)
        {
            using (var ctx = new PilimEntities())
            {
                Registo registo;
                try {
                    registo = ctx.Registo.Find(isin, date);
                } catch (SqlException ex) {
                    throw new CustomSqlException("Erro! Registo indicado nao existe. ", ex);
                }
                try {
                    ctx.Registo.Attach(registo);
                    ctx.Registo.Remove(registo);
                    ctx.SaveChanges();
                    Console.WriteLine("Registo do valor de mercado removido. ");
                } catch (SqlException ex) {
                    throw new CustomSqlException("Erro! Nao foi possivel remover o registo indicado. ", ex);
                }
            }
        }

        public static void Exercicio1C(string nomePort) //Remover portfolio sem stored procedures
        {
            using (var ctx = new PilimEntities())
            {
                Posicao posicao = null;
                try {
                    posicao = ctx.Posicao.Find(nomePort);
                } catch(SqlException ex) {
                    throw new CustomSqlException("Erro! Portfolio indicado nao existe." , ex);
                }
                try {
                    ctx.Posicao.Attach(posicao);
                    ctx.Posicao.Remove(posicao);
                    ctx.SaveChanges();
                    ShowPortfolios(ctx);
                } catch(SqlException ex)
                {
                    throw new CustomSqlException("Erro! Nao foi possivel remover o portfolio.", ex);
                }
            }
        }

        private static void ShowPortfolios(PilimEntities ctx)
        {
            List<Cliente> clientes;
            try {
                clientes = ctx.Cliente.ToList();
            } catch (SqlException ex) {
                throw new CustomSqlException("Erro! A procura da lista de clientes falhou. ", ex);
            }
            Console.WriteLine("Portefólios existentes:");
            foreach (Cliente c in clientes) {
                Console.WriteLine(c.NomePortfolio);
            }
        }

        private static void ShowValoresMercado(PilimEntities ctx)
        {
            try {
                List<Valores_Mercado> mercados = ctx.Valores_Mercado.ToList();
                foreach (Valores_Mercado m in mercados)
                {
                    Console.WriteLine("Mercado<{0}> : {1}\nDescricao: {2}\nDia: {3}\n" +
                        "Valor Abertura: {4}\nValor Indice: {5}\nVariação Diaria: {6}",
                        m.Codigo, m.Mercado_Financeiro.Nome, m.Mercado_Financeiro.Descricao, 
                        m.Dia, m.ValorAbertura, m.ValorIndice, m.VariacaoDiaria);
                }
            } catch (SqlException ex) {
                throw new CustomSqlException("Erro ao listar os valores de mercado.", ex);
            }
        }

        private static Instrumento_Financeiro CheckISINExists(PilimEntities ctx, string isin)
        {
            try {
                return ctx.Instrumento_Financeiro.Find(isin);
            } catch(SqlException ex) {
                throw new CustomSqlException("Erro! O instrumento indicado nao existe.", ex);
            }
        }
    }
}