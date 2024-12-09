using System.Collections.Generic;
using SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Models;

namespace SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Application.Interfaces
{
    public interface IItemRepository
    {
        IEnumerable<Item> GetAllItems();
        void AddItem(Item item);
        void DeleteItem(Item item);
    }
}
