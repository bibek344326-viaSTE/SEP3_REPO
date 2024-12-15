using BlazorServerApp.Application.UseCases;
using Orders;

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

        public async Task<IEnumerable<Order>> GetFilteredOrdersAsync()
        {
            var orders = (await _orderUseCases.GetAllOrdersAsync()).AsQueryable();

            if (!string.IsNullOrWhiteSpace(SelectedStatus))
                orders = orders.Where(o => o.OrderStatus.ToString().Equals(SelectedStatus, StringComparison.OrdinalIgnoreCase));

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
                "Status" => Ascending ? orders.OrderBy(o => o.OrderStatus.ToString()) : orders.OrderByDescending(o => o.OrderStatus.ToString()),
                "UserId" => Ascending ? orders.OrderBy(o => o.CreatedByUser.Username) : orders.OrderByDescending(o => o.CreatedByUser.Username),
                _ => orders
            };

            return orders.ToList();
        }

        public async Task<IEnumerable<Order>> GetPaginatedOrdersAsync()
        {
            var filteredOrders = await GetFilteredOrdersAsync();
            return filteredOrders.Skip((CurrentPage - 1) * PageSize).Take(PageSize);
        }

        public async Task<int> GetTotalPagesAsync()
        {
            var filteredOrders = await GetFilteredOrdersAsync();
            return (int)Math.Ceiling(filteredOrders.Count() / (double)PageSize);
        }

        public Task<bool> IsFirstPageAsync() => Task.FromResult(CurrentPage == 1);

        public async Task<bool> IsLastPageAsync()
        {
            var totalPages = await GetTotalPagesAsync();
            return CurrentPage >= totalPages;
        }

        public void PreviousPage()
        {
            if (CurrentPage > 1)
                CurrentPage--;
        }

        public async Task NextPageAsync()
        {
            var totalPages = await GetTotalPagesAsync();
            if (CurrentPage < totalPages)
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

        public string GetSortIcon(string columnName)
        {
            return SortColumn == columnName
                ? (Ascending ? "fas fa-sort-up" : "fas fa-sort-down")
                : "fas fa-sort";
        }

        public string GetStatusClass(string status)
        {
            return status switch
            {
                "Completed" => "text-success fw-bold",
                "Rejected" => "text-danger fw-bold",
                _ => string.Empty
            };
        }

        public void ClearSearch()
        {
            SearchQuery = string.Empty;
            CurrentPage = 1;
        }
    }
}
