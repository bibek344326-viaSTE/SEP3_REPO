using SEP3_T1_BlazorUI.Application.Interfaces;
using SEP3_T1_BlazorUI.Models;

namespace SEP3_T1_BlazorUI.Infrastructure.Repositories
{
    public class UserRepository: IUserRepository
    {
        private readonly List<User> _users;

        public UserRepository()
        {
            _users = new List<User>
        {
new User { Username = "admin", Password = "admin", WorkingNumber = 1, Role = Role.InventoryManager },
new User { Username = "worker", Password = "worker", WorkingNumber = 2, Role = Role.WarehouseWorker }
            };
        }

        public IEnumerable<User> GetAllUsers() => _users;

        public void AddUser(User user) => _users.Add(user);

        public void DeleteUser(User user)
        {
            _users.Remove(user);
        }
    }
}
