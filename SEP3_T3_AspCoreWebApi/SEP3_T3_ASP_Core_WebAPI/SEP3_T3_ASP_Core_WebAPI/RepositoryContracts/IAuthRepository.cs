using SEP3_T3_ASP_Core_WebAPI.Models;

namespace SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

public interface IAuthRepository
{
    Task<User> LoginAsync(string username, string password);
    Task<User> GetUserByIdAsync(int id);
    Task<User> GetUserByUsernameAsync(string username);
    Task<User> UpdateUserAsync(User user);
    Task<User> DeleteUserAsync(int id);
    IQueryable<User> GetAllUsers();
}