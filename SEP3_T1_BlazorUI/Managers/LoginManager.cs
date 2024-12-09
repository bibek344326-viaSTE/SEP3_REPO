using Microsoft.JSInterop;
using SEP3_T1_BlazorUI.Models;
using SEP3_T1_BlazorUI.Application.UseCases;

namespace SEP3_T1_BlazorUI.Presentation.Managers
{
    public class LoginManager
    {
        private readonly AuthUseCases _authUseCases;
        private readonly IJSRuntime _jsRuntime;

        public LoginManager(AuthUseCases authUseCases, IJSRuntime jsRuntime)
        {
            _authUseCases = authUseCases;
            _jsRuntime = jsRuntime;
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
                    // Save the token in localStorage
                    await _jsRuntime.InvokeVoidAsync("localStorage.setItem", "authToken", token);
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

        public async Task LogoutAsync()
        {
            try
            {
                await _jsRuntime.InvokeVoidAsync("localStorage.removeItem", "authToken");
            }
            catch (Exception ex)
            {
                ErrorMessage = $"An error occurred while logging out: {ex.Message}";
            }
        }
    }
}
