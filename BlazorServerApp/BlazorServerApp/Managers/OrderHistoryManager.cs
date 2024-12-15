using BlazorServerApp.Application.UseCases;

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
        public string SelectedStatus { get; set; } = string.Empty;
        public string SearchQuery { get; set; } = string.Empty;
        public DateTime? StartDate { get; set; }
        public DateTime? EndDate { get; set; }

        // Pagination
        public int CurrentPage { get; private set; } = 1;
        public int PageSize { get; set; } = 17;

        // Sorting
        public string SortColumn { get; private set; } = "OrderDate";
        public bool Ascending { get; private set; } = true;

        // Sorting methods
        public void SortByOrderId() => SortByColumn("OrderId");
        public void SortByOrderDate() => SortByColumn("CreatedAt");
        public void SortByStatus() => SortByColumn("Status");
        public void SortByEmployeeId() => SortByColumn("UserId");

        /// <summary>
        /// Asynchronous method to filter and sort orders.
        /// </summary>
        public async Task<IEnumerable<Order>> GetFilteredOrdersAsync()
        {
            var orders = (await _orderUseCases.GetAllOrdersAsync()).AsQueryable();

            if (!string.IsNullOrWhiteSpace(SelectedStatus))
                orders = orders.Where(o => o.Status.Equals(SelectedStatus, StringComparison.OrdinalIgnoreCase));

            if (!string.IsNullOrWhiteSpace(SearchQuery) && int.TryParse(SearchQuery, out var orderId))
                orders = orders.Where(o => o.OrderId == orderId);

            if (StartDate.HasValue)
                orders = orders.Where(o => o.CreatedAt.ToDateTime() >= StartDate.Value);

            if (EndDate.HasValue)
                orders = orders.Where(o => o.CreatedAt.ToDateTime() <= EndDate.Value);

            orders = SortColumn switch
            {
                "OrderId" => Ascending ? orders.OrderBy(o => o.OrderId) : orders.OrderByDescending(o => o.OrderId),
                "CreatedAt" => Ascending ? orders.OrderBy(o => o.CreatedAt) : orders.OrderByDescending(o => o.CreatedAt),
                "Status" => Ascending ? orders.OrderBy(o => o.Status) : orders.OrderByDescending(o => o.Status),
                "UserId" => Ascending ? orders.OrderBy(o => o.UserId) : orders.OrderByDescending(o => o.UserId),
                _ => orders
            };

            return orders.ToList();
        }

        /// <summary>
        /// Asynchronous method to get paginated orders.
        /// </summary>
        public async Task<IEnumerable<Order>> GetPaginatedOrdersAsync()
        {
            var filteredOrders = await GetFilteredOrdersAsync();
            return filteredOrders.Skip((CurrentPage - 1) * PageSize).Take(PageSize);
        }

        /// <summary>
        /// Get the total number of pages based on the filtered results.
        /// </summary>
        public async Task<int> GetTotalPagesAsync()
        {
            var filteredOrders = await GetFilteredOrdersAsync();
            return (int)Math.Ceiling(filteredOrders.Count() / (double)PageSize);
        }

        /// <summary>
        /// Check if the current page is the first page.
        /// </summary>
        public Task<bool> IsFirstPageAsync() => Task.FromResult(CurrentPage == 1);

        /// <summary>
        /// Check if the current page is the last page.
        /// </summary>
        public async Task<bool> IsLastPageAsync()
        {
            var totalPages = await GetTotalPagesAsync();
            return CurrentPage >= totalPages;
        }

        /// <summary>
        /// Move to the previous page, but don't go below page 1.
        /// </summary>
        public void PreviousPage()
        {
            if (CurrentPage > 1)
                CurrentPage--;
        }

        /// <summary>
        /// Move to the next page, but don't go beyond the total number of pages.
        /// </summary>
        public async Task NextPageAsync()
        {
            var totalPages = await GetTotalPagesAsync();
            if (CurrentPage < totalPages)
                CurrentPage++;
        }

        /// <summary>
        /// Toggle sorting for a given column. If the column is already the current sort column, flip the direction.
        /// </summary>
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

        /// <summary>
        /// Get the sort icon to display in the UI.
        /// </summary>
        public string GetSortIcon(string columnName)
        {
            return SortColumn == columnName
                ? (Ascending ? "fas fa-sort-up" : "fas fa-sort-down")
                : "fas fa-sort";
        }

        /// <summary>
        /// Get the CSS class for the status based on the status text.
        /// </summary>
        public string GetStatusClass(string status)
        {
            return status switch
            {
                "Completed" => "text-success fw-bold",
                "Rejected" => "text-danger fw-bold",
                _ => string.Empty
            };
        }

        /// <summary>
        /// Clear the search and reset pagination.
        /// </summary>
        public void ClearSearch()
        {
            SearchQuery = string.Empty;
            CurrentPage = 1;
        }
    }
}
