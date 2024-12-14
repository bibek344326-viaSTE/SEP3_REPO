namespace BlazorServerApp.Application.Interfaces
{
    public interface IUserRepository
    {
        Task<IEnumerable<User>> GetAllUsersAsync();
        Task <UserResponse>AddUserAsync(UserDTO user);
        Task<UserDeleteResponse> DeleteUserAsync(User user);
        Task<UserResponse> EditUserAsync(User user);
    }

}
