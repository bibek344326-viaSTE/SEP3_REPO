using SEP3_Blazor_UI.Client.Application.UseCases;
using static SEP3_Blazor_UI.Client.models.Models;

namespace SEP3_Blazor_UI.Client.Presentation.Managers
{
    public class InventoryManager
    {
        private readonly ItemUseCases _itemUseCases;
        private readonly OrderUseCases _orderUseCases;

        public InventoryManager(ItemUseCases itemUseCases, OrderUseCases orderUseCases)
        {
            _itemUseCases = itemUseCases;
            _orderUseCases = orderUseCases;
        }

        public string SearchQuery { get; set; } = string.Empty;
        public string SortColumn { get; private set; } = "Name";
        public bool Ascending { get; private set; } = true;
        public int CurrentPage { get; private set; } = 1;
        public int PageSize { get; private set; } = 12;

        public IEnumerable<Item> FilterAndSortItems()
        {
            var items = _itemUseCases.GetAllItems().AsQueryable();

            // Search
            if (!string.IsNullOrWhiteSpace(SearchQuery))
            {
                items = items.Where(i =>
                    i.Name.Contains(SearchQuery, StringComparison.OrdinalIgnoreCase) ||
                    i.Description.Contains(SearchQuery, StringComparison.OrdinalIgnoreCase));
            }

            // Sort
            items = SortColumn switch
            {
                "Name" => Ascending ? items.OrderBy(i => i.Name) : items.OrderByDescending(i => i.Name),
                "Description" => Ascending ? items.OrderBy(i => i.Description) : items.OrderByDescending(i => i.Description),
                "QuantityInStore" => Ascending ? items.OrderBy(i => i.QuantityInStore) : items.OrderByDescending(i => i.QuantityInStore),
                _ => items
            };

            return items;
        }

        public void SortByColumn(string columnName)
        {
            if (SortColumn == columnName)
            {
                Ascending = !Ascending; // Toggle sort order
            }
            else
            {
                SortColumn = columnName;
                Ascending = true; // Default to ascending order
            }
        }

        public void ToggleSelection(Item item)
        {
            item.IsSelected = !item.IsSelected;
        }

        public void PlaceOrder()
        {
            var selectedItems = _itemUseCases.GetAllItems().Where(i => i.IsSelected && i.OrderQuantity > 0).ToList();

            if (!selectedItems.Any())
                throw new InvalidOperationException("No items selected for order.");

            // Convert selected items into an order
            var newOrder = new Order
            {
                OrderDate = DateTime.Now,
                Status = "Pending",
                TotalQuantity = selectedItems.Sum(i => i.OrderQuantity),
                OrderItems = selectedItems.Select(i => new OrderItem
                {
                    ItemId = i.Id,
                    Name = i.Name,
                    QuantityOrdered = i.OrderQuantity
                }).ToList()
            };

            // Deduct quantities and reset item state
            foreach (var item in selectedItems)
            {
                item.QuantityInStore -= item.OrderQuantity;
                item.OrderQuantity = 0;
                item.IsSelected = false;
            }

            _orderUseCases.AddOrder(newOrder);
        }

        // Pagination
        public int TotalPages => (int)Math.Ceiling(FilterAndSortItems().Count() / (double)PageSize);
        public bool IsFirstPage => CurrentPage == 1;
        public bool IsLastPage => CurrentPage == TotalPages;

        public void PreviousPage()
        {
            if (!IsFirstPage)
                CurrentPage--;
        }

        public void NextPage()
        {
            if (!IsLastPage)
                CurrentPage++;
        }
    }
}
