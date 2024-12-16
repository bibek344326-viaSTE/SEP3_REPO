using BlazorServerApp.Application.UseCases;
using Orders;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace BlazorServerApp.Managers
{
    public class OrderHistoryManager
    {
        private readonly OrderUseCases _orderUseCases;

        public OrderHistoryManager(OrderUseCases orderUseCases)
        {
            _orderUseCases = orderUseCases;
        }

        // Filters
        public OrderStatus? SelectedStatus { get; set; } = null;

        private string _searchQuery = string.Empty;
        public string SearchQuery
        {
            get => _searchQuery;
            set => _searchQuery = value;
        }

        public DateTime? StartDate { get; set; }
        public DateTime? EndDate { get; set; }

        // Pagination
        public int CurrentPage { get; private set; } = 1;
        public int PageSize { get; set; } = 17;

        // Sorting
        // If sorting is needed, we can use these. For now, defaults are fine.
        public string SortColumn { get; private set; } = "OrderDate";
        public bool Ascending { get; private set; } = true;

        // Cached Orders
        private List<Order> AllOrders = new();

        public async Task LoadAllOrdersAsync()
        {
            AllOrders = (await _orderUseCases.GetAllOrdersAsync()).ToList();
        }

        /// <summary>
        /// ResetPagination sets the current page back to 1. 
        /// This is intended to be called after filters change.
        /// </summary>
        public void ResetPagination()
        {
            CurrentPage = 1;
        }

        /// <summary>
        /// Applies filtering and sorting to the entire order list.
        /// </summary>
        private IEnumerable<Order> FilterAndSortOrders()
        {
            var orders = AllOrders.AsQueryable();

            if (SelectedStatus.HasValue)
                orders = orders.Where(o => o.OrderStatus == SelectedStatus.Value);

            if (!string.IsNullOrWhiteSpace(SearchQuery))
                orders = orders.Where(o => o.OrderId.ToString().Contains(SearchQuery, StringComparison.OrdinalIgnoreCase));

            if (StartDate.HasValue)
                orders = orders.Where(o => o.CreatedAt.ToDateTime() >= StartDate.Value);

            if (EndDate.HasValue)
                orders = orders.Where(o => o.CreatedAt.ToDateTime() <= EndDate.Value);

            // Sorting - if needed, add logic for different columns
            // Default is by OrderDate ascending/descending if desired
            return SortColumn switch
            {
                "OrderId" => Ascending ? orders.OrderBy(o => o.OrderId) : orders.OrderByDescending(o => o.OrderId),
                "CreatedAt" => Ascending ? orders.OrderBy(o => o.CreatedAt) : orders.OrderByDescending(o => o.CreatedAt),
                "Status" => Ascending ? orders.OrderBy(o => o.OrderStatus) : orders.OrderByDescending(o => o.OrderStatus),
                _ => orders // Default (no specific sorting)
            };
        }

        public IEnumerable<Order> PagedItems => FilterAndSortOrders()
            .Skip((CurrentPage - 1) * PageSize)
            .Take(PageSize);

        public bool IsFirstPage => CurrentPage == 1;
        public bool IsLastPage => CurrentPage >= TotalPages;

        public int TotalPages => Math.Max(1, (int)Math.Ceiling(FilterAndSortOrders().Count() / (double)PageSize));

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

        public string GetStatusClass(OrderStatus status)
        {
            return status switch
            {
                OrderStatus.Completed => "text-success fw-bold",
                OrderStatus.InProgress => "text-warning fw-bold",
                _ => string.Empty
            };
        }

        public void ClearFilters()
        {
            SelectedStatus = null;
            SearchQuery = string.Empty;
            StartDate = null;
            EndDate = null;
            CurrentPage = 1;
        }
    }
}
