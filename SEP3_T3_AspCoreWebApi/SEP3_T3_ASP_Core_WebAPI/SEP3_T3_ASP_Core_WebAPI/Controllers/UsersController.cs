using Entities;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SEP3_T3_ASP_Core_WebAPI.ApiContracts.UserDto;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

namespace SEP3_T3_ASP_Core_WebAPI.Controllers;

[ApiController]
[Route("[controller]")]
public class UsersController: ControllerBase
{
    private readonly IUserRepository userRepo;

    public UsersController(IUserRepository userRepo)
    {
        this.userRepo = userRepo;
    }

    // ********** CREATE Endpoints **********
    // POST: /Users
    [HttpPost]
    public async Task<ActionResult<User>> AddUser([FromBody] UerDTO request)
    {
        // Verify that the username is available
        await VerifyUserNameIsAvailableAsync(request.UserName);

        // Create a new User instance
        User user = Entities.User.Create(request.UserName, request.Password, request.UserRole);

        // Save the user to the repository
        User created = await userRepo.AddUserAsync(user);

        // Return the created user with status 201 (Created)
        return CreatedAtAction(nameof(AddUser), new { id = created.UserId }, created);
    }


    private async Task VerifyUserNameIsAvailableAsync(string requestUserName)
    {
        User? existingUser = await userRepo.GetUserByUsernameAsync(requestUserName);
        if (existingUser != null)
        {
            throw new InvalidOperationException($"Username {requestUserName} is already taken.");
        }
    }


    // ********** UPDATE Endpoints **********
    // PUT: /Users/{id}
    [HttpPut("{id}")]
    public async Task<ActionResult> UpdateUser([FromRoute] int id, [FromBody] User request)
    {
        try
        {

            await userRepo.UpdateUserAsync(id, request);
            return NoContent();
        }
        catch (InvalidOperationException)
        {
            return NotFound($"User with ID {id} not found.");
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }

    // ********** Delete Endpoints **********
    // DELETE: /Users/{id}
    [HttpDelete("{id}")]
    public async Task<ActionResult> DeleteUser([FromRoute] int id)
    {
        try
        {
            User userToDelete = await userRepo.GetUserById(id);
            await userRepo.DeleteUserAsync(id);
            return NoContent();
        }
        catch (InvalidOperationException)
        {
            return NotFound($"User with ID {id} not found.");
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }

    // ********** GET Endpoints **********
    // GET: /Users/{id}
    [HttpGet("{id}")]
    public async Task<ActionResult<User>> GetSingleUser([FromRoute] int id)
    {
        try
        {
            User user = await userRepo.GetUserById(id);
            User dto = new User()
            {
                UserId = user.UserId,
                UserName = user.UserName,
                Password = user.Password,
                UserRole = user.UserRole,
                IsActive = user.IsActive,
            };
            return Ok(dto);
        }
        catch (InvalidOperationException)
        {
            return NotFound($"User with ID {id} not found.");
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }

    // GET: /Users
    [HttpGet]
    public async Task<ActionResult<IEnumerable<User>>> GetAllUsers()
    {
        try
        {
            List<User> dtos = await userRepo.GetAllUsers()
                .ToListAsync(); // Ensure asynchronous operation
            return Ok(dtos);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }


    // Line 145: Update GetAllUsersByType method
    [HttpGet("Type/{type}")]
    public async Task<ActionResult<IEnumerable<User>>> GetAllUsersByType([FromRoute] UserRole type)
    {
        try
        {
            List<User> dtos = await userRepo.GetAllUsersByRole(type)
                .ToListAsync();
            return Ok(dtos);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }


    // GET: /Users/Username/{username} 
    [HttpGet("Username/{username}")]
    public async Task<ActionResult<User>> GetUserByUsername([FromRoute] string username)
    {
        try
        {
            User? user = await userRepo.GetUserByUsernameAsync(username);
            if (user == null)
            {
                return NotFound($"User with username {username} not found.");
            }
            return Ok(user);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }

}