using DAL_Abstraction;
using Entidades;
using Entities;

namespace DALInterfaces
{
    public interface IMapperTriplo : IMapper<Triplo, TriploKey> { }
    public interface IMapperInstrumento: IMapper<Instrumento, string> { }
    public interface IMapperCliente: IMapper<Cliente, string> { }
    public interface IMapperMercado: IMapper<Mercado, string> { }
    public interface IMapperRegisto: IMapper<Registo, RegistoKey> { }
    public interface IMapperPosicao : IMapper<Posicao, PosicaoKey> { }
    public interface IMapperValoresMercado : IMapper<ValoresMercado, ValoresMercadoKey> { }
 }
