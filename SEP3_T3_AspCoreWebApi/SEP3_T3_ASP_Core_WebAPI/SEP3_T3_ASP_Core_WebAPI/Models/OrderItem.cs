﻿namespace SEP3_T3_ASP_Core_WebAPI.Models
{
    public class OrderItem
    {
        public int OrderItemId { get; set; } // Primary key
        public int OrderId { get; set; } // Foreign key to Order
        public Order Order { get; set; } // Navigation property for Order

        public int ItemId { get; set; } // Foreign key to Item
        public Item Item { get; set; } // Navigation property for Item

        public int QuantityToPick { get; set; } // Quantity of the item in the order

    }
}