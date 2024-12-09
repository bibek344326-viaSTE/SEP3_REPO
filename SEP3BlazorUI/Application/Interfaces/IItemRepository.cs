using SEP3BlazorUI.Models;

namespace SEP3BlazorUI.Application.Interfaces
{
    public interface IItemRepository
    {
        IEnumerable<Item> GetAllItems();
        void AddItem(Item item);
        void DeleteItem(Item item);
    }
}
