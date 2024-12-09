using SEP3_Blazor_UI.Client.Application.Interfaces;
using static SEP3_Blazor_UI.Client.models.Models;

namespace SEP3_Blazor_UI.Client.Application.UseCases
{
    public class UserUseCases
    {
        private readonly IUserRepository _userRepository;

        // Add a static list of roles
        public List<string> Roles { get; } = new List<string> { "Admin", "User", "Manager" };

        public UserUseCases(IUserRepository userRepository)
        {
            _userRepository = userRepository;
        }

        public IEnumerable<User> GetAllUsers()
        {
            var users = _userRepository.GetAllUsers();
            Console.WriteLine($"Users count: {users.Count()}");  // Debug line
            return users;
        }


        public void AddUser(User user)
        {
            _userRepository.AddUser(user);
        }

        public IEnumerable<User> GetUsersByRole(string role)
        {
            var users = _userRepository.GetAllUsers();
            return string.IsNullOrWhiteSpace(role)
                ? users
                : users.Where(u => u.Role.Equals(role, StringComparison.OrdinalIgnoreCase));
        }

        public bool IsUsernameTaken(string username)
        {
            var users = _userRepository.GetAllUsers();
            return users.Any(u => u.Username.Equals(username, StringComparison.OrdinalIgnoreCase));
        }
        public void DeleteUser(User user)
        {
            _userRepository.DeleteUser(user); // Implement this in your repository
        }

    }

}