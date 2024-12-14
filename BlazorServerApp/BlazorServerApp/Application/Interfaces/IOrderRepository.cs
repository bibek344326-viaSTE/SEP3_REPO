namespace BlazorServerApp.Application.Interfaces
{
    public interface IOrderRepository
    {
        Task<IEnumerable<Order>> GetAllOrdersAsync();
        Task<OrderResponse> AddOrderAsync(OrderRequest order);
    }


}
