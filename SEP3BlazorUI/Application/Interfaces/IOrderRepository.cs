using SEP3BlazorUI.Models;

namespace SEP3BlazorUI.Application.Interfaces
{
    public interface IOrderRepository
    {
        IEnumerable<Order> GetAllOrders();
        void AddOrder(Order order);

    }
}
