using System;
using System.Globalization;

namespace Si2_Fase2_EF
{
    class App
    {
        public static void Main(string[] args)
        {
            bool quit = false;
            while (!quit)
            {
                switch (Menu())
                {
                    case ConsoleKey.F:
                        Console.Write("\nInserir Instrumento existente: "); string isin1 = Console.ReadLine();
                        Console.Write("Inserir Valor: "); decimal valor = Int32.Parse(Console.ReadLine());
                        Console.Write("Inserir Data (yyyy-MM-dd HH:mm): "); string dtS = Console.ReadLine();
                        Exercicios_EF.ExercicioF(isin1,valor, DateTimeInfo(dtS)); // "FR0004548873"
                        break;
                    case ConsoleKey.G:
                        Console.Write("\nInserir Instrumento existente: ");
                        string isin2 = Console.ReadLine();
                        Exercicios_EF.ExercicioG(isin2); // "FR0004548873"
                        break;
                    case ConsoleKey.H:
                        Console.Write("\nInserir Instrumento existente: "); string isin3 = Console.ReadLine();
                        Console.Write("Inserir Valor: "); decimal valor2 = Int32.Parse(Console.ReadLine());
                        Console.Write("Inserir Data (yyyy-MM-dd HH:mm): "); string dtS2 = Console.ReadLine();
                        Exercicios_EF.ExercicioH(isin3, DateTimeInfo(dtS2), valor2); // "FR0004548873"
                        break;
                    case ConsoleKey.I:
                        Console.WriteLine("\nInsira os seguintes requisitos:");
                        Console.Write("Nome do Portfolio: "); string port1 = Console.ReadLine();
                        Console.Write("Instrumento: "); string isin4 = Console.ReadLine();
                        Console.Write("Cartão Cidadão: "); string cc = Console.ReadLine();
                        Console.Write("NIF: "); string nif = Console.ReadLine();
                        Console.Write("Nome Cliente: "); string nCliente = Console.ReadLine();
                        Console.Write("Valor Total Portfolio: "); int valTot = Int32.Parse(Console.ReadLine());
                        Console.Write("Quantidade: "); int quantidade = Int32.Parse(Console.ReadLine());
                        Exercicios_EF.ExercicioI(port1, isin4, cc, nif, nCliente, valTot, quantidade); //"Description7"
                        break;
                    case ConsoleKey.J:
                        Console.WriteLine("\nInsira os seguintes requisitos:");
                        Console.Write("ISIN: "); string isin5 = Console.ReadLine(); //"FR0004548873"
                        Console.Write("CC: "); string cc2 = Console.ReadLine(); //"125236521474"
                        Console.Write("Valor: "); int val = Int32.Parse(Console.ReadLine()); //200
                        Exercicios_EF.ExercicioJ(cc2, isin5 , val);
                        break;
                    case ConsoleKey.K:
                        Console.Write("\nNome do Portfolio: ");
                        string port2 = Console.ReadLine(); //"Description7"
                        Exercicios_EF.ExercicioK(port2);
                        break;
                    case ConsoleKey.B:
                        ChooseMethod();
                        break;
                    case ConsoleKey.C:
                        Console.Write("\nNome do Portfolio: "); 
                        string port3 = Console.ReadLine();
                        Exercicios_EF.Exercicio1C(port3);
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
                "C: Remover um portfolio\n\t" +
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
                    Console.WriteLine("Insira a informacao do mercado:");
                    Console.Write("Codigo: "); string codigo = Console.ReadLine();
                    Console.Write("Nome: "); string nome = Console.ReadLine();
                    Console.Write("Descricao: "); string desc = Console.ReadLine();
                    Console.WriteLine("Insira a informacao de um registo para o mercado:");
                    Console.Write("ISIN existente: "); string isin = Console.ReadLine();
                    Console.Write("Data (yyyy-MM-dd HH:mm): "); DateTime dtS = DateTimeInfo(Console.ReadLine());
                    Console.Write("Valor de Abertura: "); int val = Int32.Parse(Console.ReadLine());
                    Exercicios_EF.Exercicio1B_Create(codigo, nome, desc, isin, dtS, val);
                    break;
                case ConsoleKey.B:
                    Console.Write("ISIN: "); string isin2 = Console.ReadLine();
                    Console.Write("Data (yyyy-MM-dd HH:mm): "); DateTime dtS2 = DateTimeInfo(Console.ReadLine());
                    Console.Write("Valor de Abertura: "); int val2 = Int32.Parse(Console.ReadLine());
                    Exercicios_EF.Exercicio1B_Update(isin2, dtS2, val2);
                    break;
                case ConsoleKey.C:
                    Console.Write("ISIN: "); string isin3 = Console.ReadLine();
                    Console.Write("Data (yyyy-MM-dd HH:mm): "); DateTime dtS3 = DateTimeInfo(Console.ReadLine());
                    Exercicios_EF.Exercicio1B_Remove(isin3, dtS3);
                    break;
                default: break;
            }
        }

        private static DateTime DateTimeInfo(string stData)
        {
            DateTime data = new DateTime();
            try {
                data = DateTime.ParseExact(stData, "yyyy-MM-dd HH:mm", CultureInfo.InvariantCulture);
            } catch {
                throw new FormatException("Erro! Tipo de data invalido");
            }
            return data;
        }
    }
}
