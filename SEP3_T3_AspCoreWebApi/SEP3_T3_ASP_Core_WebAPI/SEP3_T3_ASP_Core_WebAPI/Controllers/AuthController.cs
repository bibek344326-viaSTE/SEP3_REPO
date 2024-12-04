using Microsoft.AspNetCore.Mvc;
using SEP3_T3_ASP_Core_WebAPI.ApiContracts.LoginDto;
using SEP3_T3_ASP_Core_WebAPI.ApiContracts.UserDto;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

namespace SEP3_T3_ASP_Core_WebAPI.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly AppDbContext _context;
        
        private readonly IUserRepository userService;
        
        public AuthController(AppDbContext context, IUserRepository userService)
        {
            _context = context;
            this.userService = userService;
        }
        
        [HttpPost]
        public async Task<ActionResult<LoginRequest>> Login([FromBody] LoginRequest loginRequest)
        {
            // Find user by username
            var user = await userService.GetUserByUsernameAndPasswordAsync(loginRequest.UserName, loginRequest.Password);

            // Check if User exists
            if (user == null)
            {
                return Unauthorized("Invalid Username or Password");
            }

            // Check if password is correct
            if (user.Password != loginRequest.Password)
            {
                return Unauthorized("Incorrect Password");
            }

            var userDto = new UserDto()
            {
                UserId = user.UserId,
                UserName = user.UserName
            };
            return Ok(userDto);
        }
    }
}