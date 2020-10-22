namespace DAL_Abstraction
{
    public interface IMapper<T, Tid>
    {
        void Create(T entity);
        T Read(Tid id);
        void Update(T entity);
        void Delete(T entity);
    }
}
