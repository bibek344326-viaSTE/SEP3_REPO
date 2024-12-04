﻿using System.Runtime.InteropServices.JavaScript;

namespace SEP3_T3_ASP_Core_WebAPI.Models
{
    public class Order
    {
        public int OrderId { get; set; }
        public int OrderStatus { get; set; }
        public DateTime DeliveryDate { get; set; }

        // Navigation property to OrderItem for many-to-many relationship
        public ICollection<OrderItem> OrderItems { get; set; } // OrderItems in the order

        // Navigation property to User for one-to-many relationship
        public User? AssignedUser { get; set; } // User assigned to the order
        public int UserId { get; set; } // Foreign key to User
    }
}