using BlazorServerApp.Application.UseCases;
using System;
using System.Threading.Tasks;

namespace BlazorServerApp.Presentation.Managers
{
    public class LoginManager
    {
        private readonly AuthUseCases _authUseCases;
        public LoginRequest LoginRequest { get; private set; } = new LoginRequest();
        public string ErrorMessage { get; private set; } = string.Empty;

        public LoginManager(AuthUseCases authUseCases)
        {
            _authUseCases = authUseCases ?? throw new ArgumentNullException(nameof(authUseCases));
        }

        public async Task<bool> AttemptLoginAsync()
        {
            try
            {
                ErrorMessage = string.Empty;

                if (string.IsNullOrEmpty(LoginRequest.Username) || string.IsNullOrEmpty(LoginRequest.Password))
                {
                    ErrorMessage = "Username and Password are required.";
                    return false;
                }

                var token = await _authUseCases.Login(LoginRequest);

                if (!string.IsNullOrEmpty(token))
                {
                    // Here you could save the token in local storage, cookies, or session state.
                    return true;
                }

                ErrorMessage = "Invalid username or password.";
                return false;
            }
            catch (Exception ex)
            {
                ErrorMessage = $"An error occurred while attempting to login: {ex.Message}";
                return false;
            }
        }

        public async Task<string> GetUserRoleAsync()
        {
            try
            {
                var role = await _authUseCases.GetUserRole(LoginRequest.Username);
                return role;
            }
            catch (Exception ex)
            {
                ErrorMessage = $"An error occurred while retrieving user role: {ex.Message}";
                return string.Empty;
            }
        }
    }
}
