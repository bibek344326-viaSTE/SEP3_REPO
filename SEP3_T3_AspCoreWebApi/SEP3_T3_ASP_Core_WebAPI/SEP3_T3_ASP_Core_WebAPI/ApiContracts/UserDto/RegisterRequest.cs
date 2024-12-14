using Entities;
using System.ComponentModel.DataAnnotations;

public class RegisterRequest
{
    public string UserName { get; set; }

    public string Password { get; set; }

    public UserRole UserRole { get; set; }
}
