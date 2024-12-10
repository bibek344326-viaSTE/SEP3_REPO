namespace SEP3_T1_BlazorUI.Models
{
    public class Item
    {
        public int Id { get; set; }
        public string Name { get; set; } = "";
        public string Description { get; set; } = "";
        public int QuantityInStore { get; set; }
        public int OrderQuantity { get; set; } = 1;
        public bool IsSelected { get; set; } = false;
    }

    public class Order
    {
        public int OrderId { get; set; }
        public DateTime OrderDate { get; set; }
        public int TotalQuantity { get; set; }
        public List<OrderItem> OrderItems { get; set; } = new();
        public string Status { get; set; } = "Pending";
        public string? EmployeeId { get; set; }
    }

    public class OrderItem
    {
        public int ItemId { get; set; }
        public string Name { get; set; } = "";
        public int QuantityOrdered { get; set; }
    }

    public class User
    {
        public string Username { get; set; } = "";
        public string Password { get; set; } = "";
        public int WorkingNumber { get; set; }
        public Role Role { get; set; }
    }

    public class UserDTO
    {
        public string Username { get; set; } = "";
        public string Password { get; set; } = "";
        public Role? Role { get; set; }
    }

    public enum Role
    {
        InventoryManager,
        WarehouseWorker
    }
}
