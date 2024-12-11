using Microsoft.AspNetCore.Mvc;
using Microsoft.IdentityModel.Tokens;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using Entities;
using SEP3_T3_ASP_Core_WebAPI.ApiContracts.AuthDtos;

namespace SEP3_T3_ASP_Core_WebAPI.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class AuthController : ControllerBase
    {
        private readonly IAuthRepository authRepository;
        private readonly IUserRepository userRepository;

        private readonly IConfiguration configuration;

        public AuthController(IAuthRepository authRepository, IUserRepository userRepository, IConfiguration configuration)
        {
            this.authRepository = authRepository;
            this.userRepository = userRepository;

            this.configuration = configuration;

        }

        [HttpPost("login")]
        public async Task<ActionResult> Login([FromBody] LoginRequest loginRequest)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest("Invalid login request");
            }

            var user = await authRepository.LoginAsync(loginRequest.UserName, loginRequest.Password);

            if (user == null)
            {
                return Unauthorized("Invalid username or password");
            }

            var token = GenerateJwtToken(user);

            return Ok(new { Token = token });
        }

        [HttpPost("register")]
        public async Task<ActionResult> Register([FromBody] RegisterRequest registerRequest)
        {
            if (!ModelState.IsValid)
            {
                // Create a dictionary to hold the error messages
                var errorMessages = ModelState
                    .Where(x => x.Value.Errors.Count > 0)
                    .ToDictionary(
                        kvp => kvp.Key,
                        kvp => kvp.Value.Errors.Select(e => e.ErrorMessage).ToArray()
                    );

                // Return a bad request with the detailed error messages
                return BadRequest(new { Message = "Invalid registration data", Errors = errorMessages });
            }

            var existingUser = await userRepository.GetUserByUsernameAsync(registerRequest.UserName);
            if (existingUser != null)
            {
                return BadRequest("Username already exists");
            }

            var user = await authRepository.RegisterAsync(registerRequest.UserName, registerRequest.Password, registerRequest.UserRole);
            if (user == null)
            {
                return BadRequest("Unable to create user");
            }

            return Ok("User registered successfully");
        }

        private string GenerateJwtToken(User user)
        {
            var tokenHandler = new JwtSecurityTokenHandler();
            var key = Encoding.UTF8.GetBytes(configuration["Jwt:Key"]);

            var claims = new List<Claim>
            {
                new Claim(JwtRegisteredClaimNames.Sub, user.UserId.ToString()),
                new Claim(JwtRegisteredClaimNames.UniqueName, user.UserName),
                new Claim(ClaimTypes.Role, user.UserRole),
                new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
            };

            var tokenDescriptor = new SecurityTokenDescriptor
            {
                Subject = new ClaimsIdentity(claims),
                Expires = DateTime.UtcNow.AddHours(1), // Token expires in 1 hour
                SigningCredentials = new SigningCredentials(new SymmetricSecurityKey(key), SecurityAlgorithms.HmacSha256)
            };

            var token = tokenHandler.CreateToken(tokenDescriptor);
            return tokenHandler.WriteToken(token);
        }
    }
}