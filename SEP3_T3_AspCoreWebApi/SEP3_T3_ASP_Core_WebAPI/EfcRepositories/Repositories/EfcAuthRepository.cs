using Microsoft.EntityFrameworkCore;
using Entities;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;
using SEP3_T3_ASP_Core_WebAPI;
using Microsoft.AspNetCore.Identity;

namespace EfcRepositories.Repositories
{
    public class EfcAuthRepository : IAuthRepository
    {
        private readonly AppDbContext _ctx;

        public EfcAuthRepository(AppDbContext ctx)
        {
            this._ctx = ctx;
        }

        public async Task<User> LoginAsync(string username, string password)
        {
            User user;
            // Query the database for a user with the given username
            try
            {
                user = await _ctx.Users.FirstOrDefaultAsync(u => u.UserName == username);
            }
            catch (Exception ex)
            {
                // Log exception and handle it
                throw new Exception("An error occurred while querying the database.", ex);
            }

            if (user == null)
            {
                return null; // User not found
            }

            // Verify the password using the PasswordHasher
            var hasher = new PasswordHasher<User>();
            var result = hasher.VerifyHashedPassword(user, user.Password, password);
            if (result != PasswordVerificationResult.Success)
            {
                return null; // Password does not match
            }

            // Return the user if the username and password are correct
            return user;
        }

        public Task<User> RegisterAsync(string username, string password, string role)
        {
            throw new NotImplementedException();
        }
    }
}
