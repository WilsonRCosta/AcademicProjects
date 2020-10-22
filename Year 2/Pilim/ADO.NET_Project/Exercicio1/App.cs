using System;
using System.Collections.Generic;
using System.Configuration;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Exercicio1
{
    public class App
    {
        private static readonly string cs = ConfigurationManager.ConnectionStrings["TL51N_11"].ConnectionString;

        public static void Main(string[] args)
        {
            bool quit = false;
            while (!quit)
            {
                switch (Menu())
                {
                    case ConsoleKey.F:
                        Console.WriteLine("\nInserção de um Triplo!");
                        Console.Write("Inserir Instrumento existente: "); string isin1 = Console.ReadLine();
                        Console.Write("Inserir Valor: "); decimal valor = Int32.Parse(Console.ReadLine());
                        Console.Write("Inserir Data (yyyy-MM-dd HH:mm): "); string dtS = Console.ReadLine();
                        ExerciciosADO.ExercicioF(cs, isin1, valor, DateTimeInfo("yyyy-MM-dd HH:mm", dtS)); 
                        break;
                    case ConsoleKey.G:
                        Console.Write("\nInserir Instrumento existente: ");
                        string isin2 = Console.ReadLine();
                        ExerciciosADO.ExercicioG(cs, isin2); 
                        break;
                    case ConsoleKey.H:
                        Console.Write("\nInserir Instrumento existente: "); string isin3 = Console.ReadLine();
                        Console.Write("Inserir Valor: "); decimal valor2 = Int32.Parse(Console.ReadLine());
                        Console.Write("Inserir Data (yyyy-MM-dd HH:mm): "); string dtS2 = Console.ReadLine();
                        ExerciciosADO.ExercicioH(cs, isin3, DateTimeInfo("yyyy-MM-dd HH:mm", dtS2), valor2); 
                        break;
                    case ConsoleKey.I:
                        Console.WriteLine("\nInsira os seguintes requisitos:");
                        Console.Write("Nome do Portfolio: "); string port1 = Console.ReadLine();
                        Console.Write("Cartão Cidadão: "); string cc = Console.ReadLine();
                        Console.Write("NIF: "); string nif = Console.ReadLine();
                        Console.Write("Nome Cliente: "); string nCliente = Console.ReadLine();
                        ExerciciosADO.ExercicioI(port1, cc, nif, nCliente); 
                        break;
                    case ConsoleKey.J:
                        Console.WriteLine("\nInsira os seguintes requisitos:");
                        Console.Write("CC do portfolio a atualizar: "); string cc2 = Console.ReadLine();
                        Console.Write("ISIN(existente no portfolio do cliente): "); string isin5 = Console.ReadLine();
                        Console.Write("Quantidade de instrumentos a somar: "); int quantidade = Int32.Parse(Console.ReadLine()); 
                        ExerciciosADO.ExercicioJ(cc2, isin5, quantidade);
                        break;
                    case ConsoleKey.K:
                        Console.Write("\nNome do Portfolio: ");
                        string port2 = Console.ReadLine();
                        ExerciciosADO.ExercicioK(cs, port2);
                        break;
                    case ConsoleKey.B:
                        ChooseMethod();
                        break;
                    case ConsoleKey.C:
                        Console.Write("\nCC do Cliente: ");
                        string cc1 = Console.ReadLine();
                        ExerciciosADO.Exercicio1C(cc1);
                        break;
                    case ConsoleKey.Escape:
                        quit = true;
                        break;
                    default: break;
                }
            }
        }
        private static ConsoleKey Menu()
        {
            Console.WriteLine("Indicar Exercicio: ");
            Console.WriteLine(
                "\tB: Inserir, remover ou atualizar informação de um mercado\n\t" +
                "C: Remover um portfolio(Remoção de um cliente)\n\t" +
                "F: Aceder ao procedimento p_actualizaValorDiario\n\t" +
                "G: Calcular a media a 6 meses de um instrumento\n\t" +
                "H: Actualizar dados instrumento\n\t" +
                "I: Criar um portefolio\n\t" +
                "J: Actualizar o valor total do portfolio\n\t" +
                "K: Aceder a funcao para produzir a listagem de um portefolio\n" +
                "Esc: Sair"
            );
            return Console.ReadKey().Key;
        }

        private static void ChooseMethod()
        {
            Console.WriteLine("\nIndicar uma das ações a realizar sob a informação de um mercado:\n\t" +
                "A: Inserir\n\t" + "B: Atualizar\n\t" + "C: Remover");
            ConsoleKey key = Console.ReadKey().Key;

            switch (key)
            {
                case ConsoleKey.A:
                    Console.WriteLine("\nInsira a informacao do mercado que deseja criar:");
                    Console.Write("Codigo: "); string codigo = Console.ReadLine();
                    Console.Write("Nome: "); string nome = Console.ReadLine();
                    Console.Write("Descricao: "); string descrMercado = Console.ReadLine();
                    Console.WriteLine("\nInsira a informacao para a criação de um instrumento para o mercado criado:");
                    Console.Write("ISIN: "); string isin = Console.ReadLine();
                    Console.Write("Descrição de instrumento: "); string descrInstr = Console.ReadLine(); 
                    Console.WriteLine("\nInsira a restante informacao para a criação de um triplo e respectivo registo para o instrumento criado:");
                    Console.Write("Data (yyyy-MM-dd HH:mm): "); DateTime dtS = DateTimeInfo("yyyy-MM-dd HH:mm", Console.ReadLine());
                    Console.Write("Valor de Abertura: "); int val = Int32.Parse(Console.ReadLine());
                    ExerciciosADO.Exercicio1B_Create(codigo, nome, descrMercado, isin, descrInstr, dtS, val);
                    break;
                case ConsoleKey.B:
                    Console.WriteLine("\nAtualizar valor de abertura de um instrumento, para o seu mercado correspondente, num determinado dia. Atualizacao de Registo.");
                    Console.Write("ISIN: "); string isin2 = Console.ReadLine();
                    Console.Write("Dia (yyyy-MM-dd): "); DateTime dtS2 = DateTimeInfo("yyyy-MM-dd", Console.ReadLine());
                    Console.Write("Valor de Abertura: "); int val2 = Int32.Parse(Console.ReadLine());
                    ExerciciosADO.Exercicio1B_Update(isin2, dtS2, val2);
                    break;
                case ConsoleKey.C:
                    Console.WriteLine("\nRemoção de um mercado:");
                    Console.Write("\nCodigo de mercado a remover: "); string codigo2 = Console.ReadLine();
                    ExerciciosADO.Exercicio1B_Remove(codigo2);
                    break;
                default: break;
            }
        }

        private static DateTime DateTimeInfo(string format, string stData)
        {
            DateTime data = new DateTime();
            try
            {
                data = DateTime.ParseExact(stData, format, CultureInfo.InvariantCulture);
            }
            catch
            {
                throw new FormatException("Erro! Tipo de data invalido");
            }
            return data;
        }
    }
}
