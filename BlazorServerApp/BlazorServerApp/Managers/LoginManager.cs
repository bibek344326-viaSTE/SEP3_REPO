using BlazorServerApp.Application.UseCases;
using Microsoft.AspNetCore.Components.Authorization;
using System.Security.Claims;

public class LoginManager
{
    private readonly AuthUseCases _authUseCases;
    private readonly AuthenticationStateProvider _authenticationStateProvider;

    public LoginRequest LoginRequest { get; set; } = new();
    public string ErrorMessage { get; private set; }

    public LoginManager(AuthUseCases authUseCases, AuthenticationStateProvider authenticationStateProvider)
    {
        _authUseCases = authUseCases;
        _authenticationStateProvider = authenticationStateProvider;
    }

    public async Task<bool> AttemptLoginAsync()
    {
        try
        {
            var token = await _authUseCases.Login(LoginRequest);
            if (!string.IsNullOrEmpty(token))
            {
                // Store token in AuthenticationStateProvider (for Blazor authentication)
                ((CustomAuthenticationStateProvider)_authenticationStateProvider).MarkUserAsAuthenticated(token);
                return true;
            }
            else
            {
                ErrorMessage = "Invalid username or password.";
                return false;
            }
        }
        catch (Exception ex)
        {
            ErrorMessage = "An error occurred during login: " + ex.Message;
            return false;
        }
    }

    public async Task<string> GetUserRoleAsync()
    {
        var authState = await _authenticationStateProvider.GetAuthenticationStateAsync();
        var user = authState.User;

        if (user.Identity?.IsAuthenticated == true)
        {
            return user.FindFirst(ClaimTypes.Role)?.Value ?? string.Empty;
        }

        return string.Empty;
    }
}
