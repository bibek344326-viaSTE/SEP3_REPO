using SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Models;
using SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Application.Interfaces;
namespace SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Infrastructure.Repositories
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
            _users.Add(new User { Username = "john", Password = "john123", WorkingNumber = 2, Role = Role.WarehouseWorker });
            _users.Add(new User { Username = "jane", Password = "jane123", WorkingNumber = 3, Role = Role.WarehouseWorker });
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
