using Microsoft.AspNetCore.Mvc;
using BlazorServerApp.Data;
using System.Threading.Tasks;

namespace BlazorServerApp.Controllers
{
    [Route("api/auth")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly AuthRepository _authRepository;

        public AuthController(AuthRepository authRepository)
        {
            _authRepository = authRepository;
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login([FromBody] LoginRequest request)
        {
            if (request == null || string.IsNullOrEmpty(request.Username) || string.IsNullOrEmpty(request.Password))
            {
                return BadRequest("Invalid login request");
            }

            var result = await _authRepository.LoginAsync(request.Username, request.Password);

            if (string.IsNullOrEmpty(result) || result.Contains("failed"))
            {
                return Unauthorized(new { message = "Login failed" });
            }

            return Ok(new { token = result });
        }

        [HttpPost("register")]
        public async Task<IActionResult> Register([FromBody] RegisterRequest request)
        {
            if (request == null || string.IsNullOrEmpty(request.Username) || string.IsNullOrEmpty(request.Password) || string.IsNullOrEmpty(request.Role))
            {
                return BadRequest("Invalid registration request");
            }

            var result = await _authRepository.RegisterAsync(request.Username, request.Password, request.Role);

            if (string.IsNullOrEmpty(result) || result.Contains("failed"))
            {
                return BadRequest(new { message = "Registration failed" });
            }

            return Ok(new { message = result });
        }
    }
}
