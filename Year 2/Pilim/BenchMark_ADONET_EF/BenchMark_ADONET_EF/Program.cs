using BenchmarkDotNet.Running;

namespace BenchMark_ADONET_EF
{
    class Program
    {
        static void Main(string[] args)
        {
            BenchmarkRunner.Run<Benchmark>();
        }
    }
}
