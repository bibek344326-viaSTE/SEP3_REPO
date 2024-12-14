namespace BlazorServerApp.Application.Interfaces
{
    public interface IItemRepository
    {
        Task<Item> CreateItemAsync(ItemDTO itemDTO);
        Task EditItemAsync(Item item);
        Task DeleteItemAsync(Item item);
        Task<IEnumerable<Item>> GetAllItemsAsync();
    }
}
