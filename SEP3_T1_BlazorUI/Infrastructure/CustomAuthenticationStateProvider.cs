using SEP3_T1_BlazorUI.Application.Interfaces;
using Blazored.LocalStorage; // Added for ILocalStorageService
using Microsoft.AspNetCore.Components.Authorization; // Added for AuthenticationStateProvider, AuthenticationState
using System.Security.Claims; // Added for Claim, ClaimsIdentity, ClaimsPrincipal

namespace SEP3_T1_BlazorUI.Infrastructure
{
    public class CustomAuthenticationStateProvider : AuthenticationStateProvider
    {
        private readonly IAuthRepository _authRepository;
        private readonly ILocalStorageService _localStorage;

        public CustomAuthenticationStateProvider(IAuthRepository authRepository, ILocalStorageService localStorage)
        {
            _authRepository = authRepository;
            _localStorage = localStorage;
        }

        public override async Task<AuthenticationState> GetAuthenticationStateAsync()
        {
            var token = await _localStorage.GetItemAsync<string>("authToken");

            if (string.IsNullOrEmpty(token))
            {
                return new AuthenticationState(new ClaimsPrincipal(new ClaimsIdentity()));
            }

            // Parse the token to extract user information (simulating JWT token parsing)
            var identity = new ClaimsIdentity(ParseClaimsFromToken(token), "jwt");
            var user = new ClaimsPrincipal(identity);

            return new AuthenticationState(user);
        }

        public async Task MarkUserAsAuthenticated(string token)
        {
            await _localStorage.SetItemAsync("authToken", token);

            var identity = new ClaimsIdentity(ParseClaimsFromToken(token), "jwt");
            var user = new ClaimsPrincipal(identity);
            var authenticationState = new AuthenticationState(user);

            NotifyAuthenticationStateChanged(Task.FromResult(authenticationState));
        }

        public async Task MarkUserAsLoggedOut()
        {
            await _localStorage.RemoveItemAsync("authToken");

            var anonymous = new ClaimsPrincipal(new ClaimsIdentity());
            var authenticationState = new AuthenticationState(anonymous);

            NotifyAuthenticationStateChanged(Task.FromResult(authenticationState));
        }

        private IEnumerable<Claim> ParseClaimsFromToken(string token)
        {
            // In a real-world scenario, you would decode a JWT and extract claims from it.
            // Here we simulate the parsing:
            var parts = token.Split('|');
            if (parts.Length == 3)
            {
                // Token format: Username|WorkingNumber|Role
                return new List<Claim>
                {
                    new Claim(ClaimTypes.Name, parts[0]), // Username
                    new Claim("WorkingNumber", parts[1]), // Working Number
                    new Claim(ClaimTypes.Role, parts[2])   // Role
                };
            }
            return new List<Claim>();
        }
    }
}
