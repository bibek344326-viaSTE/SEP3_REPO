using Entities;
using Entities.DTOs;

namespace SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

public interface IOrderRepository
{
    Task<Order> GetOrderById(int id);
    Task<Order> AddOrderAsync(CreateOrderDTO order);
    Task<Order> UpdateOrderAsync(Order order);
    Task<Order> DeleteOrderAsync(int id);
    Task<List<GetOrderDTO>> GetAllOrders();
    Task<IQueryable<Order>> GetAllOrdersByType(string type);
}