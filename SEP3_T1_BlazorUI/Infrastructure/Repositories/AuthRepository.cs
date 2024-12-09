using SEP3_T1_BlazorUI.Application.Interfaces;
using SEP3_T1_BlazorUI.Models;

namespace SEP3_T1_BlazorUI.Infrastructure.Repositories
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
            _users.Add(new User { Username = "admin", Password = "admin", WorkingNumber = 1, Role = Role.InventoryManager });
            _users.Add(new User { Username = "worker", Password = "worker", WorkingNumber = 2, Role = Role.WarehouseWorker });
        }

        public async Task<string> Login(UserDTO userDTO)
        {
            var user = _users.FirstOrDefault(u => u.Username == userDTO.Username && u.Password == userDTO.Password);

            if (user != null)
            {
                // Token format: Username|WorkingNumber|Role|ExpirationTimestamp
                var expirationTime = DateTimeOffset.UtcNow.AddHours(1).ToUnixTimeSeconds(); // Token expires in 1 hour
                string token = $"{user.Username}|{user.WorkingNumber}|{user.Role}|{expirationTime}";
                return await Task.FromResult(token);
            }

            return string.Empty;
        }
    }
}
