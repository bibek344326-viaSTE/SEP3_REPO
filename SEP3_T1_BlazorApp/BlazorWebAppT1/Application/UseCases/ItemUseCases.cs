using System.Collections.Generic;
using SEP3_Blazor_UI.Client.Application.Interfaces;
using SEP3_Blazor_UI.Client.models;
using static SEP3_Blazor_UI.Client.models.Models;

namespace SEP3_Blazor_UI.Client.Application.UseCases
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
