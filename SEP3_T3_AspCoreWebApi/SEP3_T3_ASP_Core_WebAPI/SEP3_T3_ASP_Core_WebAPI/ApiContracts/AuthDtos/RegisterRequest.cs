using System.ComponentModel.DataAnnotations;

public class RegisterRequest
{
    [Required(ErrorMessage = "Username is required")]
    [MinLength(1, ErrorMessage = "Username cannot be empty")]
    public string UserName { get; set; }

    [Required(ErrorMessage = "Password is required")]
    [MinLength(1, ErrorMessage = "Password cannot be empty")]
    public string Password { get; set; }

    [Required(ErrorMessage = "User role is required")]
    [MinLength(1, ErrorMessage = "User role cannot be empty")]
    [RegularExpression("^(InventoryManager|WarehouseWorker)$", ErrorMessage = "User role must be either 'InventoryManager' or 'WarehouseWorker'")]
    public string UserRole { get; set; }
}
