using SEP3_Blazor_UI.Client.Application.Interfaces;
using static SEP3_Blazor_UI.Client.models.Models;

namespace SEP3_Blazor_UI.Client.Infrastructure.Repositories
{
    public class AuthRepository : IAuthRepository
    {
        private readonly List<User> _users;

        public AuthRepository()
        {
            _users = new List<User>();
            InitializeMockUsers();
        }

        private void InitializeMockUsers()
        {
            _users.Add(new User { Username = "admin", Password = "admin", WorkingNumber = 1, Role = UserRole.InventoryManager });
            _users.Add(new User { Username = "john", Password = "john123", WorkingNumber = 2, Role = UserRole.WarehouseWorker });
            _users.Add(new User { Username = "jane", Password = "jane123", WorkingNumber = 3, Role = UserRole.WarehouseWorker });
        }

        public async Task<string> Login(UserDTO userDTO)
        {
            var user = _users.FirstOrDefault(u => u.Username == userDTO.Username && u.Password == userDTO.Password);

            if (user != null)
            {
                string token = $"{user.Username}|{user.WorkingNumber}|{user.Role}";
                return await Task.FromResult(token); 
            }

            return null;
        }
    }
}
