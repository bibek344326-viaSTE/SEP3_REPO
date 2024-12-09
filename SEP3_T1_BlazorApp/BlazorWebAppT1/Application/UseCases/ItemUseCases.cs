using System.Collections.Generic;
using SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Models;
using SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Application.Interfaces;

namespace SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Application.UseCases
{
    public class ItemUseCases
    {
        private readonly IItemRepository _itemRepository;

        public ItemUseCases(IItemRepository itemRepository)
        {
            _itemRepository = itemRepository;
        }

        public void AddItem(Item item)
        {
            _itemRepository.AddItem(item);
        }

        public void DeleteItem(Item item)
        {
            _itemRepository.DeleteItem(item);
        }

        public IEnumerable<Item> GetAllItems()
        {
            return _itemRepository.GetAllItems();
        }
    }
}
