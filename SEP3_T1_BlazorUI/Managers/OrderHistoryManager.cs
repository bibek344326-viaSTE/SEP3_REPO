using System;
using System.Collections.Generic;
using System.Linq;
using SEP3_T1_BlazorUI.Models;
using SEP3_T1_BlazorUI.Application.UseCases;

namespace SEP3_T1_BlazorUI.Presentation.Managers
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

                if (!string.IsNullOrWhiteSpace(SelectedStatus))
                {
                    orders = orders.Where(o => o.Status.Equals(SelectedStatus, StringComparison.OrdinalIgnoreCase));
                }

                if (!string.IsNullOrWhiteSpace(SearchQuery))
                {
                    if (int.TryParse(SearchQuery, out var orderId))
                    {
                        orders = orders.Where(o => o.OrderId == orderId);
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
