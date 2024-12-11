using Grpc.Net.Client;
using SEP3_T1_BlazorUI.Application.Interfaces;
using SEP3T1BlazorUI.Infrastructure;

namespace SEP3_T1_BlazorUI.Infrastructure.Repositories
{
    public class AuthRepository : IAuthRepository
    {
        private readonly AuthService.AuthServiceClient _client;

        public AuthRepository(HttpClient httpClient, string grpcServiceEndpoint)
        {
            var channel = GrpcChannel.ForAddress(grpcServiceEndpoint, new GrpcChannelOptions { HttpClient = httpClient });
            _client = new AuthService.AuthServiceClient(channel);
        }

        public async Task<string> LoginAsync(LoginRequest loginRequest)
        {
            var response = await _client.loginAsync(loginRequest);
            return response.Token;
        }
    }

}


