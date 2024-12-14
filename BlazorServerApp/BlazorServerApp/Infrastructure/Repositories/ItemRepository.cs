using BlazorServerApp.Application.Interfaces;
using Grpc.Core;

namespace BlazorServerApp.Infrastructure.Repositories
{
    public class ItemRepository : IItemRepository
    {
        private readonly ItemService.ItemServiceClient _client;

        public ItemRepository(ItemService.ItemServiceClient client)
        {
            _client = client;
        }

        public async Task<Item> CreateItemAsync(ItemDTO itemDTO)
        {
            try
            {
                var response = await _client.createItemAsync(itemDTO);
                return response;
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error creating item", ex);
            }
        }

        public async Task DeleteItemAsync(Item item)
        {
            try
            {
                await _client.deleteItemAsync(item);
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error deleting item", ex);
            }
        }

        public async Task EditItemAsync(Item item)
        {
            try
            {
                var response = await _client.editItemAsync(item);
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error editing item", ex);
            }
        }

        public async Task<IEnumerable<Item>> GetAllItemsAsync()
        {
            try
            {
                var response = await _client.getAllItemsAsync(new Google.Protobuf.WellKnownTypes.Empty());
                return response.Items;
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error retrieving all items", ex);
            }
        }
    }
}
