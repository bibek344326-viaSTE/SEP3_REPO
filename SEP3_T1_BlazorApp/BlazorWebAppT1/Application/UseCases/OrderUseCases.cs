﻿using System.Collections.Generic;
using SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Models;
using SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Application.Interfaces;

namespace SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Application.UseCases
{
    public class OrderUseCases
    {
        private readonly IOrderRepository _orderRepository;

        public OrderUseCases(IOrderRepository orderRepository)
        {
            _orderRepository = orderRepository;
        }

        public IEnumerable<Order> GetAllOrders()
        {
            return _orderRepository.GetAllOrders();
        }

        public void AddOrder(Order order)
        {
            _orderRepository.AddOrder(order);
        }

        public IEnumerable<Order> GetOrdersByStatus(string status)
        {
            var orders = _orderRepository.GetAllOrders();
            return string.IsNullOrWhiteSpace(status)
                ? orders
                : orders.Where(o => o.Status.Equals(status, StringComparison.OrdinalIgnoreCase));
        }

        public IEnumerable<Order> GetOrdersByEmployeeId(string employeeId)
        {
            var orders = _orderRepository.GetAllOrders();
            return string.IsNullOrWhiteSpace(employeeId)
                ? orders
                : orders.Where(o => o.EmployeeId == employeeId);
        }
    }
}
