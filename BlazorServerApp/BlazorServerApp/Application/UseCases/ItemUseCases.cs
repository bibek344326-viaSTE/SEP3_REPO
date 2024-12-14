using BlazorServerApp.Application.Interfaces;

namespace BlazorServerApp.Application.UseCases
{
    public class ItemUseCases
    {
        private readonly IItemRepository _itemRepository;

        public ItemUseCases(IItemRepository itemRepository)
        {
            _itemRepository = itemRepository ?? throw new ArgumentNullException(nameof(itemRepository));
        }

        public async Task<Item> CreateItemAsync(ItemDTO itemDTO)
        {
            if (itemDTO == null) throw new ArgumentNullException(nameof(itemDTO));

            try
            {
                var createdItem = await _itemRepository.CreateItemAsync(itemDTO);
                return createdItem;
            }
            catch (Exception ex)
            {
                // Handle exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error creating item", ex);
            }
        }

        public async Task<Item> EditItemAsync(Item item)
        {
            if (item == null) throw new ArgumentNullException(nameof(item));

            try
            {
                var updatedItem = await _itemRepository.EditItemAsync(item);
                return updatedItem;
            }
            catch (Exception ex)
            {
                // Handle exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error editing item", ex);
            }
        }

        public async Task<ItemDeleteResponse> DeleteItemAsync(Item item)
        {
            if (item == null) throw new ArgumentNullException(nameof(item));

            try
            {
                var response = await _itemRepository.DeleteItemAsync(item);
                return response;
            }
            catch (Exception ex)
            {
                // Handle exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error deleting item", ex);
            }
        }

        public async Task<IEnumerable<Item>> GetAllItemsAsync()
        {
            try
            {
                var items = await _itemRepository.GetAllItemsAsync();
                return items;
            }
            catch (Exception ex)
            {
                // Handle exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error retrieving items", ex);
            }
        }
    }
}
