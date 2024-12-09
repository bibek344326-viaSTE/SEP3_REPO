using SEP3_T1_BlazorUI.Models;
using SEP3_T1_BlazorUI.Application.UseCases;

namespace SEP3_T1_BlazorUI.Presentation.Managers
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

        public IEnumerable<Item> PagedItems => FilterAndSortItems()
            .Skip((CurrentPage - 1) * PageSize)
            .Take(PageSize);

        public bool HasSelectedItems => FilterAndSortItems().Any(i => i.IsSelected);

        public void LoadData()
        {
            // Load initial data if needed
        }

        public void ClearSearch()
        {
            SearchQuery = string.Empty;
        }

        public void SortByName() => SortByColumn("Name");

        public void SortByDescription() => SortByColumn("Description");

        public void SortByQuantityInStore() => SortByColumn("QuantityInStore");

        public void DeleteItem(Item item)
        {
            _itemUseCases.DeleteItem(item);
        }

        public void PlaceOrder()
        {
            var selectedItems = FilterAndSortItems().Where(i => i.IsSelected && i.OrderQuantity > 0).ToList();
            if (!selectedItems.Any()) return;

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

            _orderUseCases.AddOrder(newOrder);

            foreach (var item in selectedItems)
            {
                item.IsSelected = false;
                item.OrderQuantity = 0;
            }
        }

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

        public bool IsFirstPage => CurrentPage == 1;
        public bool IsLastPage => CurrentPage == TotalPages;
        public int TotalPages => (int)Math.Ceiling(FilterAndSortItems().Count() / (double)PageSize);

        public void ToggleSelection(Item item)
        {
            item.IsSelected = !item.IsSelected;
        }

        public string GetSortIcon(string columnName) => SortColumn == columnName
            ? (Ascending ? "fas fa-sort-up" : "fas fa-sort-down")
            : "fas fa-sort";

        public void SortByColumn(string columnName)
        {
            if (SortColumn == columnName)
            {
                Ascending = !Ascending;
            }
            else
            {
                SortColumn = columnName;
                Ascending = true;
            }
        }

        public IEnumerable<Item> FilterAndSortItems()
        {
            var items = _itemUseCases.GetAllItems().AsQueryable();

            if (!string.IsNullOrWhiteSpace(SearchQuery))
            {
                items = items.Where(i =>
                    i.Name.Contains(SearchQuery, StringComparison.OrdinalIgnoreCase) ||
                    i.Description.Contains(SearchQuery, StringComparison.OrdinalIgnoreCase));
            }

            items = SortColumn switch
            {
                "Name" => Ascending ? items.OrderBy(i => i.Name) : items.OrderByDescending(i => i.Name),
                "Description" => Ascending ? items.OrderBy(i => i.Description) : items.OrderByDescending(i => i.Description),
                "QuantityInStore" => Ascending ? items.OrderBy(i => i.QuantityInStore) : items.OrderByDescending(i => i.QuantityInStore),
                _ => items
            };

            return items;
        }
    }
}
