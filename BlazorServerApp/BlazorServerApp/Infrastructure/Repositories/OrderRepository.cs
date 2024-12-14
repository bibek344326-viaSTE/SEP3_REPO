using BlazorServerApp.Application.Interfaces;
using Grpc.Core;

namespace BlazorServerApp.Infrastructure.Repositories
{
    public class OrderRepository : IOrderRepository
    {
        private readonly OrderService.OrderServiceClient _client;

        public OrderRepository(OrderService.OrderServiceClient client)
        {
            _client = client ?? throw new ArgumentNullException(nameof(client));
        }

        public async Task<Order> AddOrderAsync(OrderRequest order)
        {
            try
            {
                var response = await _client.CreateOrderAsync(order);
                return response;
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error adding order", ex);
            }
        }

        public async Task<IEnumerable<Order>> GetAllOrdersAsync()
        {
            try
            {
                var response = await _client.GetAllOrdersAsync(new Google.Protobuf.WellKnownTypes.Empty());
                return response.Orders;
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error retrieving all orders", ex);
            }
        }
    }
}
