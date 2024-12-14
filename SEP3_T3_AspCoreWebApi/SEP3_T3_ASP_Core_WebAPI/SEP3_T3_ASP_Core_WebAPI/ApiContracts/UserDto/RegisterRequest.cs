using System.ComponentModel.DataAnnotations;

public class RegisterRequest
{
    public string UserName { get; set; }

    public string Password { get; set; }

    public string UserRole { get; set; }
}
