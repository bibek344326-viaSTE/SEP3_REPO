using BlazorServerApp.Application.Interfaces;

namespace BlazorServerApp.Application.UseCases
{
    public class UserUseCases
    {
        private readonly IUserRepository _userRepository;

        public UserUseCases(IUserRepository userRepository)
        {
            _userRepository = userRepository ?? throw new ArgumentNullException(nameof(userRepository));
        }

        public async Task<UserResponse> AddUserAsync(UserDTO userDTO)
        {
            if (userDTO == null) throw new ArgumentNullException(nameof(userDTO));

            try
            {
                var response = await _userRepository.AddUserAsync(userDTO);
                return response;
            }
            catch (Exception ex)
            {
                // Handle exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error adding user", ex);
            }
        }

        public async Task DeleteUserAsync(User user)
        {
            if (user == null) throw new ArgumentNullException(nameof(user));

            try
            {
                await _userRepository.DeleteUserAsync(user);
            }
            catch (Exception ex)
            {
                // Handle exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error deleting user", ex);
            }
        }

        public async Task EditUserAsync(User user)
        {
            if (user == null) throw new ArgumentNullException(nameof(user));

            try
            {
                await _userRepository.EditUserAsync(user);
            }
            catch (Exception ex)
            {
                // Handle exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error editing user", ex);
            }
        }

        public async Task<IEnumerable<User>> GetAllUsersAsync()
        {
            try
            {
                var users = await _userRepository.GetAllUsersAsync();
                return users;
            }
            catch (Exception ex)
            {
                // Handle exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error retrieving users", ex);
            }
        }
    }
}
