using Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;
using SEP3_T3_ASP_Core_WebAPI;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

namespace EfcRepositories.Repositories;

public class EfcUserRepository: IUserRepository
{
    private readonly AppDbContext _ctx;
    public EfcUserRepository(AppDbContext ctx)
    {
        this._ctx = ctx;
    }
    
    // Add a user to the database
    public Task<User> GetUserById(int id)
    {
        throw new NotImplementedException();
    }

    public async Task<User> AddUserAsync(User user)
    {
        EntityEntry<User> entityEntry = await _ctx.Users.AddAsync(user);
        await _ctx.SaveChangesAsync();
        return entityEntry.Entity;
    }
    
    // Update a user in the database
    public async Task<User> UpdateUserAsync(User user)
    {
        if (!_ctx.Users.Any(u => u.UserId == user.UserId))
        {
            throw new InvalidOperationException("User does not exist");
        }
        _ctx.Users.Update(user);
        await _ctx.SaveChangesAsync();
        return user;
    }
    
    // Delete a user from the database
    public async Task<User> DeleteUserAsync(int id)
    {
        User? existingUser = await _ctx.Users.SingleOrDefaultAsync(u => u.UserId == id);
        if (existingUser == null)
        {
            throw new InvalidOperationException("User does not exist");
        }
        _ctx.Users.Remove(existingUser);
        await _ctx.SaveChangesAsync();
        return existingUser;
    }

    
    public IQueryable<User> GetAllUsers()
    {
        return _ctx.Users.AsQueryable();
    }

    public IQueryable<User> GetAllUsersByRole(string type)
    {
        return _ctx.Users.Where(u => u.UserRole == type);
    }

    public Task<User?> GetUserByUsernameAndPasswordAsync(string? username, string? password)
    {
        throw new NotImplementedException();
    }

    // Get a single user from the database
    public async Task<User> GetSingleAsync(int userId)
    {
        return await _ctx.Users.SingleOrDefaultAsync(u => u.UserId == userId) ?? throw new InvalidOperationException();
    }
    

    //get user by username
    public Task<User?> GetUserByUsernameAsync(string requestUserName)
    {
        return  Task.FromResult(_ctx.Users.FirstOrDefault(u => u.UserName == requestUserName));
    }
    
    
}