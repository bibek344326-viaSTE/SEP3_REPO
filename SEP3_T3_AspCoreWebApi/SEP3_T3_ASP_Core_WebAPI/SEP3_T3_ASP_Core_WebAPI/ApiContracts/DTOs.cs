using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace Entities.DTOs
{


        public class GetOrderDTO
    {
            public int OrderId { get; set; }
            public string OrderStatus { get; set; }
            public DateTime DeliveryDate { get; set; }
            public List<OrderItemDTO> OrderItems { get; set; }
        public  string AssignedUser { get; set; }
        public string CreatedBy { get; set; }

        public DateTimeOffset CreatedAt { get; set; } 
    }
    public class CreateOrderDTO
    {
        public DateTime DeliveryDate { get; set; }
        public List<OrderItemDTO> OrderItems { get; set; }
        public int CreatedBy { get; set; } 
        public DateTimeOffset CreatedAt { get; set; }
    }

    public class OrderItemDTO
        {
            public ItemDTO item { get; set; }
            public int QuantityToPick { get; set; } // Quantity of the item in the order

        }
    public class ItemDTO
    {
        public int ItemId { get; set; }
        public required string ItemName { get; set; }
        public string? Description { get; set; }
        public int QuantityInStore { get; set; }
    }
}