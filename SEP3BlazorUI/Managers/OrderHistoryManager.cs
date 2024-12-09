using System;
using System.Collections.Generic;
using System.Linq;
using SEP3BlazorUI.Models;
using SEP3BlazorUI.Application.UseCases;

namespace SEP3BlazorUI.Presentation.Managers
{
    public class OrderHistoryManager
    {
        private readonly OrderUseCases _orderUseCases;

        public OrderHistoryManager(OrderUseCases orderUseCases)
        {
            _orderUseCases = orderUseCases;
        }

        public string SelectedStatus { get; set; } = string.Empty;
        public string SearchQuery { get; set; } = string.Empty;

        public int CurrentPage { get; private set; } = 1;
        public int PageSize { get; set; } = 10;

        public IEnumerable<Order> FilteredOrders
        {
            get
            {
                var orders = _orderUseCases.GetAllOrders().AsQueryable();

                // Filter by Status
                if (!string.IsNullOrWhiteSpace(SelectedStatus))
                {
                    orders = orders.Where(o => o.Status.Equals(SelectedStatus, StringComparison.OrdinalIgnoreCase));
                }

                // Filter by Order ID
                if (!string.IsNullOrWhiteSpace(SearchQuery))
                {
                    if (int.TryParse(SearchQuery, out var orderId))
                    {
                        orders = orders.Where(o => o.OrderId == orderId);
                    }
                    else
                    {
                        // Reset to no results if searchQuery is invalid
                        orders = Enumerable.Empty<Order>().AsQueryable();
                    }
                }

                return orders.ToList();
            }
        }

        public IEnumerable<Order> PaginatedOrders => FilteredOrders
            .Skip((CurrentPage - 1) * PageSize)
            .Take(PageSize);

        public int TotalPages => (int)Math.Ceiling(FilteredOrders.Count() / (double)PageSize);
        public bool IsFirstPage => CurrentPage == 1;
        public bool IsLastPage => CurrentPage == TotalPages;

        public void PreviousPage()
        {
            if (!IsFirstPage)
            {
                CurrentPage--;
            }
        }

        public void NextPage()
        {
            if (!IsLastPage)
            {
                CurrentPage++;
            }
        }

        public string GetStatusClass(string status)
        {
            return status switch
            {
                "Completed" => "bg-success text-white",
                "Pending" => "bg-warning text-dark",
                "Rejected" => "bg-danger text-white",
                _ => "bg-secondary text-white"
            };
        }

        public void HandleSearchInput(string input)
        {
            SearchQuery = input;
            CurrentPage = 1; // Reset to the first page on new input
        }

        public void ClearSearch()
        {
            SearchQuery = string.Empty;
            CurrentPage = 1; // Reset to the first page when clearing the search
        }
    }
}
