using Microsoft.JSInterop;
using SEP3_T1_BlazorUI.Models;
using SEP3_T1_BlazorUI.Application.UseCases;
using Microsoft.AspNetCore.Components.Authorization;
using SEP3_T1_BlazorUI.Infrastructure;
using System.Security.Claims;

namespace SEP3_T1_BlazorUI.Presentation.Managers
{
    public class LoginManager
    {
        private readonly AuthUseCases _authUseCases;
        private readonly AuthenticationStateProvider _authenticationStateProvider;

        public LoginManager(AuthUseCases authUseCases, AuthenticationStateProvider authenticationStateProvider)
        {
            _authUseCases = authUseCases;
            _authenticationStateProvider = authenticationStateProvider;
        }

        public UserDTO UserDTO { get; set; } = new UserDTO();
        public string ErrorMessage { get; set; } = string.Empty;

        public async Task<bool> AttemptLoginAsync()
        {
            ErrorMessage = string.Empty;

            try
            {
                string token = await _authUseCases.Login(UserDTO);

                if (!string.IsNullOrWhiteSpace(token))
                {
                    // Use CustomAuthenticationStateProvider to set user as authenticated
                    if (_authenticationStateProvider is CustomAuthenticationStateProvider customAuthProvider)
                    {
                        await customAuthProvider.MarkUserAsAuthenticated(token);
                    }
                    return true;
                }
                else
                {
                    ErrorMessage = "Login failed. Invalid token received.";
                    return false;
                }
            }
            catch (Exception ex)
            {
                ErrorMessage = $"An error occurred: {ex.Message}";
                return false;
            }
        }

        public async Task<string> GetUserRole()
        {
            var authState = await _authenticationStateProvider.GetAuthenticationStateAsync();
            var roleClaim = authState.User.Claims.FirstOrDefault(c => c.Type == ClaimTypes.Role);
            return roleClaim?.Value ?? string.Empty;
        }

        public async Task LogoutAsync()
        {
            try
            {
                if (_authenticationStateProvider is CustomAuthenticationStateProvider customAuthProvider)
                {
                    await customAuthProvider.MarkUserAsLoggedOut();
                }
            }
            catch (Exception ex)
            {
                ErrorMessage = $"An error occurred while logging out: {ex.Message}";
            }
        }
    }
}
