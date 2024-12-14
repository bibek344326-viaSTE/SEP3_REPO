using BlazorServerApp.Application.Interfaces;
using Grpc.Core;

namespace BlazorServerApp.Infrastructure.Repositories
{
    public class UserRepository : IUserRepository
    {
        private readonly UserService.UserServiceClient _client;

        public UserRepository(UserService.UserServiceClient client)
        {
            _client = client ?? throw new ArgumentNullException(nameof(client));
        }

        public async Task<UserResponse> AddUserAsync(UserDTO user)
        {
            try
            {
                var response = await _client.addUserAsync(user);
                return response;
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error adding user", ex);
            }
        }

        public async Task<UserDeleteResponse> DeleteUserAsync(User user)
        {
            try
            {
                var response = await _client.deleteUserAsync(user);
                return response;
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error deleting user", ex);
            }
        }

        public async Task<UserResponse> EditUserAsync(User user)
        {
            try
            {
                var response = await _client.editUserAsync(user);
                return response;
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error editing user", ex);
            }
        }

        public async Task<IEnumerable<User>> GetAllUsersAsync()
        {
            try
            {
                var response = await _client.getAllUsersAsync(new Google.Protobuf.WellKnownTypes.Empty());
                return response.Users;
            }
            catch (RpcException ex)
            {
                // Handle gRPC exception appropriately (logging, rethrow, etc.)
                throw new ApplicationException("Error retrieving all users", ex);
            }
        }
    }
}
