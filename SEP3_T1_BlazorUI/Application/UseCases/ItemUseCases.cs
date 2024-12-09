﻿using SEP3_T1_BlazorUI.Models;
using SEP3_T1_BlazorUI.Application.Interfaces;

namespace SEP3_T1_BlazorUI.Application.UseCases
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
        public void UpdateItem(Item item) {
            _itemRepository.UpdateItem(item);

        }

        public IEnumerable<Item> GetAllItems()
        {
            return _itemRepository.GetAllItems();
        }
    }
}
