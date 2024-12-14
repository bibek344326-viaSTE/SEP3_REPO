namespace BlazorServerApp.Application.Interfaces
{
    public interface IItemRepository
    {
        Task<Item> CreateItemAsync(ItemDTO itemDTO);
        Task<Item> EditItemAsync(Item item);
        Task<ItemDeleteResponse> DeleteItemAsync(Item item);
        Task<IEnumerable<Item>> GetAllItemsAsync();
    }
}
