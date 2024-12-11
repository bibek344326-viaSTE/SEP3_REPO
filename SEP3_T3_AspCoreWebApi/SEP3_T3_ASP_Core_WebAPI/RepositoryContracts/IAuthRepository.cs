

using Entities;

namespace SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

public interface IAuthRepository
{
    Task<User> LoginAsync(string username, string password);
    Task<User> RegisterAsync(string username, string password, string role);

}