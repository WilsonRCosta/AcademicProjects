using BenchmarkDotNet.Attributes;
using BenchmarkDotNet.Configs;
using BenchmarkDotNet.Jobs;
using BenchmarkDotNet.Toolchains.InProcess.Emit;
using Si2_Fase2_EF.sln;
using SI2_Fase2.sln;
using System;
using System.Transactions;
using System.Data.SqlClient;
using System.Configuration;

namespace BenchMark_ADONET_EF
{

    public class BenchmarkConfig : ManualConfig
    {
        public BenchmarkConfig()
        {
            Add(Job.MediumRun
                    .WithLaunchCount(1)
                    .With(InProcessEmitToolchain.Instance)
                    .WithId("InProcess"));
        }
    }

    [RankColumn]
    [Config(typeof(BenchmarkConfig))]
    public class Benchmark
    {

        [Benchmark]
        public void ExercicioF_EF()
        {
            Exercicios_EF.ExercicioF("IE00BXC8D038", 420, new DateTime(2019, 12, 26, 16, 0, 0));
        }

        [Benchmark]
        public void RemoveTriplo_ExercicioF_EF()
        {
            using(var ctx = new PilimEntities())
            {
                Triplos triplo = ctx.Triplos.Find("IE00BXC8D038", new DateTime(2019, 12, 26, 16, 0, 0));
                ctx.Triplos.Attach(triplo);
                ctx.Triplos.Remove(triplo);
                ctx.SaveChanges();
            }
        }

        [Benchmark]
        public void ExercicioF_ADONET()
        {
            string cs = ConfigurationManager.ConnectionStrings["TL51N_11"].ConnectionString;
            ExerciciosADO.ExercicioF(cs, "IE00BXC8D038", 420, new DateTime(2019, 12, 26, 16, 0, 0));
               
        }

        [Benchmark]
        public void RemoveTriplo_ExercicioF_ADONET()
        {
            string cs = ConfigurationManager.ConnectionStrings["TL51N_11"].ConnectionString;
            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand();
                command.CommandText = "DELETE FROM Triplos WHERE Identificacao = @isin ";
                SqlParameter isin = new SqlParameter("@isin", "IE00BXC8D038");
                command.Parameters.Add(isin);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;
                    con.Open();
                    command.ExecuteNonQuery();
                }
                ts.Complete();
            }
        }

        [Benchmark]
        public void ExercicioI_EF()
        {
            Exercicios_EF.ExercicioI("PortfolioTeste", "18572048", "192847028", "Inacio Meireles", 5000);
        }

        [Benchmark]
        public void RemoveClient_ExI_EF()
        {
            using (var ctx = new PilimEntities())
            {
                Cliente cliente = ctx.Cliente.Find("18572048");
                ctx.Cliente.Attach(cliente);
                ctx.Cliente.Remove(cliente);
                ctx.SaveChanges();
            }
        }

        [Benchmark]
        public void RemoveClient_ExI_ADONET()
        {
            string cs = ConfigurationManager.ConnectionStrings["TL51N_11"].ConnectionString;
            using (var ts = new TransactionScope(TransactionScopeOption.Required))
            {
                SqlCommand command = new SqlCommand();
                command.CommandText = "DELETE FROM Cliente WHERE CC = @cc ";
                SqlParameter isin = new SqlParameter("@cc", "18572048");
                command.Parameters.Add(isin);

                using (var con = new SqlConnection(cs))
                {
                    command.Connection = con;
                    con.Open();
                    command.ExecuteNonQuery();
                }
                ts.Complete();
            }
        }

        [Benchmark]
        public void ExercicioI_ADONET()
        {
            string cs = ConfigurationManager.ConnectionStrings["TL51N_11"].ConnectionString;
            ExerciciosADO.ExercicioI(cs, "PortfolioTeste", "18572048", "192847028", "Inacio Meireles", 5000);
        }

        [Benchmark]
        public void ExercicioK_EF()
        {
            Exercicios_EF.ExercicioK("Description0");
        }

        [Benchmark]
        public void ExercicioK_ADONET()
        {
            string cs = ConfigurationManager.ConnectionStrings["TL51N_11"].ConnectionString;
            ExerciciosADO.ExercicioI(cs, "Description0");
        }
    }
}
