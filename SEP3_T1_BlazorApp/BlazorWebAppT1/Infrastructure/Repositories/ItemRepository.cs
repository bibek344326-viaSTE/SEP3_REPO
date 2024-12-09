using SEP3_Blazor_UI.Client.Application.Interfaces;
using static SEP3_Blazor_UI.Client.models.Models;

namespace SEP3_Blazor_UI.Client.Infrastructure.Repositories
{
    public class ItemRepository : IItemRepository
    {
        private readonly List<Item> _items;

        public ItemRepository()
        {
            _items = new List<Item>();
            InitializeMockData();
        }

        public IEnumerable<Item> GetAllItems() => _items;

        public void AddItem(Item item)
        {
            item.Id = _items.Any() ? _items.Max(i => i.Id) + 1 : 1;
            _items.Add(item);
        }

        public void DeleteItem(Item item) => _items.Remove(item);

        private void InitializeMockData()
        {
            var items = new List<Item>
        {
            new Item { Id = 1, Name = "Milk", Description = "1 Gallon of Whole Milk", QuantityInStore = 20 },
            new Item { Id = 2, Name = "Coffee", Description = "Ground Coffee Beans, 1 lb", QuantityInStore = 15 },
            new Item { Id = 3, Name = "Chocolate", Description = "Dark Chocolate Bar, 70% Cocoa", QuantityInStore = 25 },
            new Item { Id = 4, Name = "Bread", Description = "Whole Wheat Bread Loaf", QuantityInStore = 30 },
            new Item { Id = 5, Name = "Butter", Description = "Salted Butter, 1 lb", QuantityInStore = 10 },
            new Item { Id = 6, Name = "Cheese", Description = "Cheddar Cheese, 1 lb", QuantityInStore = 12 },
            new Item { Id = 7, Name = "Eggs", Description = "Dozen Large Eggs", QuantityInStore = 18 },
            new Item { Id = 8, Name = "Apples", Description = "Red Apples, 1 lb", QuantityInStore = 22 },
            new Item { Id = 9, Name = "Bananas", Description = "Bananas, 1 lb", QuantityInStore = 25 },
            new Item { Id = 10, Name = "Orange Juice", Description = "1 Gallon of Orange Juice", QuantityInStore = 14 },
            new Item { Id = 11, Name = "Cereal", Description = "Oat Cereal, 500g", QuantityInStore = 16 },
            new Item { Id = 12, Name = "Yogurt", Description = "Greek Yogurt, 1 lb", QuantityInStore = 20 },
            new Item { Id = 13, Name = "Honey", Description = "Organic Honey, 12 oz", QuantityInStore = 8 },
            new Item { Id = 14, Name = "Tea", Description = "Green Tea Bags, 20 count", QuantityInStore = 25 },
            new Item { Id = 15, Name = "Sugar", Description = "Granulated Sugar, 2 lb", QuantityInStore = 30 }
        };

            foreach (var item in items)
            {
                _items.Add(item);
            }

            Console.WriteLine("Mock data initialized in ItemRepository.");
        }
    }
}
