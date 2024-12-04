namespace SEP3_T3_ASP_Core_WebAPI.Models
{
    public class Item
    {
        public int ItemId { get; set; }
        public required string ItemName { get; set; }
        public string ?Description { get; set; }
        public int QuantityInStore {  get; set; }
        
        // Navigation property for OrderItem
        public List<OrderItem> OrderItems { get; set; } // One-to-many relationship with OrderItem
    }
}
