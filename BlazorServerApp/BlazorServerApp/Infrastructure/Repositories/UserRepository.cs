using BlazorServerApp.Application.Interfaces;
using Grpc.Core;
using Users;

namespace BlazorServerApp.Infrastructure.Repositories
{
    public class UserRepository : IUserRepository
    {
        private readonly UserService.UserServiceClient _client;

        public UserRepository(UserService.UserServiceClient client)
        {
            _client = client;
        }

        public Task<User> AddUserAsync(UserDTO user)
        {
            throw new NotImplementedException();
        }

        public Task DeleteUserAsync(User user)
        {
            throw new NotImplementedException();
        }

        public Task EditUserAsync(User user)
        {
            throw new NotImplementedException();
        }

        public async Task<IEnumerable<User>> GetAllUsersAsync()
        {
            Console.WriteLine("[UserRepository] GetAllUsersAsync started...");

            try
            {
                var response = await _client.getAllUsersAsync(new Google.Protobuf.WellKnownTypes.Empty());
                return response.Users;
            }
            catch (RpcException ex)
            {
                throw new ApplicationException("Error retrieving all users", ex);
            }
        }
    }
}
