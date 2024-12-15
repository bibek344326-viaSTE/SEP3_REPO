using BlazorServerApp.Application.Interfaces;
using Users;

namespace BlazorServerApp.Application.UseCases
{
    public class UserUseCases
    {
        private readonly IUserRepository _userRepository;

        public UserUseCases(IUserRepository userRepository)
        {
            _userRepository = userRepository;
        }

        public async Task<IEnumerable<User>> GetAllUsersAsync()
        {
            try
            {
                return await _userRepository.GetAllUsersAsync();
            }
            catch (Exception ex)
            {
                throw new ApplicationException("Error retrieving users", ex);
            }
        }

        internal async Task DeleteUserAsync(User user)
        {
            throw new NotImplementedException();
        }

        internal async Task EditUserAsync(User editingUser)
        {
            throw new NotImplementedException();
        }
    }
}
