﻿using BlazorServerApp.Application.UseCases;
using Microsoft.AspNetCore.Components.Authorization;

namespace BlazorServerApp.Managers
{
    public class InventoryManager
    {
        private readonly ItemUseCases _itemUseCases;
        private readonly OrderUseCases _orderUseCases;
        private readonly AuthenticationStateProvider _authenticationStateProvider;
        private string _searchQuery = string.Empty;

        public InventoryManager(ItemUseCases itemUseCases, OrderUseCases orderUseCases, AuthenticationStateProvider authenticationStateProvider)
        {
            _itemUseCases = itemUseCases;
            _orderUseCases = orderUseCases;
            _authenticationStateProvider = authenticationStateProvider;
        }

        // Search filter
        public string SearchQuery
        {
            get => _searchQuery;
            set
            {
                if (_searchQuery != value)
                {
                    _searchQuery = value;
                    CurrentPage = 1;
                }
            }
        }

        public void ClearSearch()
        {
            SearchQuery = string.Empty;
            CurrentPage = 1;
        }

        // Sorting
        public string SortColumn { get; private set; } = "Name";
        public bool Ascending { get; private set; } = true;

        public void SortByName() => SortByColumn("Name");

        public void SortByDescription() => SortByColumn("Description");

        public void SortByQuantityInStore() => SortByColumn("QuantityInStore");

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

        public string GetSortIcon(string columnName) => SortColumn == columnName
            ? (Ascending ? "fas fa-sort-up" : "fas fa-sort-down")
            : "fas fa-sort";

        public IEnumerable<ItemViewModel> FilterAndSortItems()
        {
            var items = _itemUseCases.GetAllItemsAsync().Result
                .Select(item => new ItemViewModel
                {
                    Id = item.ItemId,
                    Name = item.ItemName,
                    Description = item.Description,
                    QuantityInStore = item.QuantityInStore
                });

            if (!string.IsNullOrWhiteSpace(SearchQuery))
            {
                items = items.Where(i => i.Name.Contains(SearchQuery, StringComparison.OrdinalIgnoreCase) ||
                                         i.Description.Contains(SearchQuery, StringComparison.OrdinalIgnoreCase));
            }

            return SortColumn switch
            {
                "Name" => Ascending ? items.OrderBy(i => i.Name) : items.OrderByDescending(i => i.Name),
                "Description" => Ascending ? items.OrderBy(i => i.Description) : items.OrderByDescending(i => i.Description),
                "QuantityInStore" => Ascending ? items.OrderBy(i => i.QuantityInStore) : items.OrderByDescending(i => i.QuantityInStore),
                _ => items
            };
        }

        // Pagination properties and methods
        public int CurrentPage { get; private set; } = 1;
        public int PageSize { get; private set; } = 12;

        public IEnumerable<ItemViewModel> PagedItems => FilterAndSortItems()
            .Skip((CurrentPage - 1) * PageSize)
            .Take(PageSize);

        public bool IsFirstPage => CurrentPage == 1;
        public bool IsLastPage => CurrentPage >= TotalPages;

        public int TotalPages => Math.Max(1, (int)Math.Ceiling(FilterAndSortItems().Count() / (double)PageSize));

        public void PreviousPage()
        {
            if (!IsFirstPage)
                CurrentPage--;

            if (CurrentPage > TotalPages)
                CurrentPage = TotalPages;
        }

        public void NextPage()
        {
            if (!IsLastPage)
                CurrentPage++;

            if (CurrentPage > TotalPages)
                CurrentPage = TotalPages;
        }

        // Item operations
        public void UpdateItem(ItemViewModel item)
        {
            var updatedItem = new Item
            {
                ItemId = item.Id,
                ItemName = item.Name,
                Description = item.Description,
                QuantityInStore = item.QuantityInStore
            };

            _itemUseCases.EditItemAsync(updatedItem).Wait();
        }

        public void DeleteItem(ItemViewModel item)
        {
            var itemToDelete = new Item
            {
                ItemId = item.Id
            };

            _itemUseCases.DeleteItemAsync(itemToDelete).Wait();
        }

        public bool HasSelectedItems => FilterAndSortItems().Any(i => i.IsSelected);

        public async Task PlaceOrder()
        {
            var selectedItems = FilterAndSortItems().Where(i => i.IsSelected).ToList();
            var orderItems = selectedItems.Select(i => new OrderItem
            {
                ItemId = int.Parse(i.Id),
                QuantityToPick = i.OrderQuantity
            }).ToList();

            var orderRequest = new OrderRequest
            {
                OrderItems = { orderItems },
                UserId = 1,
                CreatedBy = 1
            };

            await _orderUseCases.AddOrderAsync(orderRequest);
        }
    }
}


public class ItemViewModel
{
    public string Id { get; set; }
    public string Name { get; set; }
    public string Description { get; set; }
    public int QuantityInStore { get; set; }

    // Client-side properties for UI interactions
    public bool IsSelected { get; set; } = false; 
    public int OrderQuantity { get; set; } = 0; 
}