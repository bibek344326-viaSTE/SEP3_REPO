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
                Console.WriteLine("Sending CreateItem request with data: ");
                Console.WriteLine($"ItemName: {itemDTO.Name}, Description: {itemDTO.Description}, QuantityInStore: {itemDTO.QuantityInStore}");

                var response = await _client.createItemAsync(itemDTO);

                // Log the full response
                Console.WriteLine("Response from gRPC (CreateItem): " + response.ToString());

                // Log each field of the response
                Console.WriteLine($"Item Created - Id: {response.ItemId}, Name: {response.ItemName}, Description: {response.Description}, QuantityInStore: {response.QuantityInStore}");

                return response;
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                Console.WriteLine("Error during CreateItem request. RPC Exception: " + ex.ToString());
                throw new ApplicationException("Error creating item", ex);
            }
        }

        public async Task DeleteItemAsync(Item item)
        {
            try
            {
                Console.WriteLine("Sending DeleteItem request for ItemId: " + item.ItemId);

                await _client.deleteItemAsync(item);

                // Log success message
                Console.WriteLine($"Successfully deleted item with Id: {item.ItemId}");
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                Console.WriteLine("Error during DeleteItem request. RPC Exception: " + ex.ToString());
                throw new ApplicationException("Error deleting item", ex);
            }
        }

        public async Task EditItemAsync(Item item)
        {
            try
            {
                Console.WriteLine("Sending EditItem request for ItemId: " + item.ItemId);
                Console.WriteLine($"Updated Values - Name: {item.ItemName}, Description: {item.Description}, QuantityInStore: {item.QuantityInStore}");

                var response = await _client.editItemAsync(item);

                // Log success message
                Console.WriteLine($"Successfully edited item with Id: {item.ItemId}");
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                Console.WriteLine("Error during EditItem request. RPC Exception: " + ex.ToString());
                throw new ApplicationException("Error editing item", ex);
            }
        }

        public async Task<IEnumerable<Item>> GetAllItemsAsync()
        {
            try
            {
                Console.WriteLine("Sending GetAllItems request...");

                // Call the gRPC service
                var response = await _client.getAllItemsAsync(new Google.Protobuf.WellKnownTypes.Empty());

                // 🔥 Log the entire response (use Newtonsoft.Json to properly serialize it)
                var jsonResponse = System.Text.Json.JsonSerializer.Serialize(response);
                Console.WriteLine("Full gRPC Response (Serialized): " + jsonResponse);

                if (response == null)
                {
                    Console.WriteLine("Response is NULL!");
                }

                if (response.Items == null || !response.Items.Any())
                {
                    Console.WriteLine("Response.Items is NULL or EMPTY!");
                }

                // 🔥 Log each item separately
                Console.WriteLine("Items received from gRPC:");
                foreach (var item in response.Items)
                {
                    Console.WriteLine($"Item - Id: {item.ItemId}, Name: {item.ItemName}, Description: {item.Description}, QuantityInStore: {item.QuantityInStore}");
                }

                return response.Items;
            }
            catch (RpcException ex)
            {
                Console.WriteLine("Error during GetAllItems request. RPC Exception: " + ex.ToString());
                throw new ApplicationException("Error retrieving all items", ex);
            }
        }


    }
}
