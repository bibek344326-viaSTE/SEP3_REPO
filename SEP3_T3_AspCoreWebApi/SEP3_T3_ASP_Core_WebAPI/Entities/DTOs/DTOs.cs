using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Entities.DTOs
{


        public class OrderDTO
        {
            public int OrderId { get; set; }
            public string OrderStatus { get; set; }
            public DateTime DeliveryDate { get; set; }
            public List<OrderItemDTO> OrderItems { get; set; }
            public UserDTO AssignedUser { get; set; }
            public UserDTO CreatedBy { get; set; }
        public DateTimeOffset CreatedAt { get; set; } 
    }
    public class OrderItemDTO
        {
            public int OrderItemId { get; set; }
            public string ProductName { get; set; }
            public int QuantityToPick { get; set; } // Quantity of the item in the order

        }
        public class UserDTO
        {
            public string UserName { get; set; }
        }
    }