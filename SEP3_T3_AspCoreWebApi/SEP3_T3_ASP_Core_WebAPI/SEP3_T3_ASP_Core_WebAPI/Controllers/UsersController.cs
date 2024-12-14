// Purpose: Contains the UsersController class which is responsible for handling requests related to users.

using Entities;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SEP3_T3_ASP_Core_WebAPI.ApiContracts.AuthDtos;
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
    public async Task<ActionResult<UserDto>> AddUser([FromBody] RegisterRequest request)
    {
        await VerifyUserNameIsAvailableAsync(request.UserName);

        User user = Entities.User.Create(request.UserName, request.Password, request.UserRole);
        User created = await userRepo.AddUserAsync(user);
        UserDto dto = new()
        {
            UserId = created.UserId,
            UserName = created.UserName
        };
        return Created($"/Users/{dto.UserId}", created);
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
    public async Task<ActionResult> UpdateUser([FromRoute] int id, [FromBody] UpdateUserDto request)
    {
        try
        {
            User userToUpdate = await userRepo.GetUserById(id);
            userToUpdate.UserName = request.UserName;
            userToUpdate.Password = request.Password;
            userToUpdate.UserRole = request.UserRole;

            await userRepo.UpdateUserAsync(userToUpdate);
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
    public async Task<ActionResult<UserDto>> GetSingleUser([FromRoute] int id)
    {
        try
        {
            User user = await userRepo.GetUserById(id);
            UserDto dto = new()
            {
                UserId = user.UserId,
                UserName = user.UserName
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
    public async Task<ActionResult<IEnumerable<UserDto>>> GetAllUsers()
    {
        try
        {
            List<UserDto> dtos = await userRepo.GetAllUsers()
                .Select(u => new UserDto()
                {
                    UserId = u.UserId,
                    UserName = u.UserName
                })
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
    public async Task<ActionResult<IEnumerable<UserDto>>> GetAllUsersByType([FromRoute] string type)
    {
        try
        {
            List<UserDto> dtos = await userRepo.GetAllUsersByType(type)
                .Select(u => new UserDto()
                {
                    UserId = u.UserId,
                    UserName = u.UserName
                })
                .ToListAsync(); // Ensure asynchronous operation
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
    public async Task<ActionResult<UserDto>> GetUserByUsername([FromRoute] string username)
    {
        try
        {
            User? user = await userRepo.GetUserByUsernameAsync(username);
            if (user == null)
            {
                return NotFound($"User with username {username} not found.");
            }
            UserDto dto = new()
            {
                UserId = user.UserId,
                UserName = user.UserName
            };
            return Ok(dto);
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }

}