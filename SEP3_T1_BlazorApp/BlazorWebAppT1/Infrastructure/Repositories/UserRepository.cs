using SEP3_Blazor_UI.Client.Application.Interfaces;
using static SEP3_Blazor_UI.Client.models.Models;

public class UserRepository : IUserRepository
{
    private readonly List<User> _users;

    public UserRepository()
    {
        _users = new List<User>
        {
            new User { Username = "manager1", Password = "pass123", WorkingNumber = 101, Role = "Inventory Manager" },
            new User { Username = "worker1", Password = "pass456", WorkingNumber = 202, Role = "Warehouse Worker" },
            new User { Username = "manager2", Password = "pass789", WorkingNumber = 103, Role = "Inventory Manager" },
            new User { Username = "worker2", Password = "pass012", WorkingNumber = 204, Role = "Warehouse Worker" }
        };
    }

    public IEnumerable<User> GetAllUsers() => _users;

    public void AddUser(User user) => _users.Add(user);

    public void DeleteUser(User user)
    {
        _users.Remove(user);
    }
}
