using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SEP3_T3_ASP_Core_WebAPI.Models;

namespace SEP3_T3_ASP_Core_WebAPI.Controllers;

[ApiController]
[Route("[controller]")]
public class UsersController: ControllerBase
{
    private readonly AppDbContext _context;

    public UsersController(AppDbContext context)
    {
        _context = context;
    }
    
    //Endpoint to get all users
    [HttpGet]
    public async Task<ActionResult<IEnumerable<User>>> GetUsers()
    {
        return await _context.Users.ToListAsync();
    }
    
    //Endpoint to get a specific user
    [HttpGet("{id}")]
    public async Task<ActionResult<User>> GetUser(int id)
    {
        var user = await _context.Users.FindAsync(id);

        if (user == null)
        {
            return NotFound();
        }

        return user;
    }
    
    //Endpoint to create a new user
    [HttpPost]
    public async Task<ActionResult<User>> PostUser(User user)
    {
        _context.Users.Add(user);
        await _context.SaveChangesAsync();

        return CreatedAtAction("GetUser", new { id = user.UserId }, user);
    }
    
    //Endpoint to update a user
    [HttpPut("{id}")]
    public async Task<IActionResult> PutUser(int id, User user)
    {
        if (id != user.UserId)
        {
            return BadRequest();
        }

        _context.Entry(user).State = EntityState.Modified;
        await _context.SaveChangesAsync();

        return NoContent();
    }
    
    //Endpoint to delete a user
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteUser(int id)
    {
        var user = await _context.Users.FindAsync(id);
        if (user == null)
        {
            return NotFound();
        }

        _context.Users.Remove(user);
        await _context.SaveChangesAsync();

        return NoContent();
    }
    
    //Endpoint to get a user by username and password
    [HttpGet("login")]
    public async Task<ActionResult<User>> GetUserByUsernameAndPassword(string username, string password)
    {
        var user = await _context.Users.FirstOrDefaultAsync(u => u.UserName == username && u.Password == password);

        if (user == null)
        {
            return NotFound();
        }

        return user;
    }
    
    //Endpoint to get all users by role
    [HttpGet("role/{role}")]
    public async Task<ActionResult<IEnumerable<User>>> GetUsersByRole(string role)
    {
        return await _context.Users.Where(u => u.UserRole == role).ToListAsync();
    }
}