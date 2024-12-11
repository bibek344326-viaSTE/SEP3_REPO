using Grpc.Net.Client;
using SEP3T1BlazorUI.Infrastructure;

namespace SEP3_T1_BlazorUI.Infrastructure.GrpcClients
{
    public class AuthServiceClient
    {
        private readonly AuthService.AuthServiceClient _client; // Fixed naming conflict

        public AuthServiceClient(string grpcServiceEndpoint)
        {
            var httpClientHandler = new HttpClientHandler();
            if (System.Diagnostics.Debugger.IsAttached) // Use environment checks instead
            {
                httpClientHandler.ServerCertificateCustomValidationCallback =
                    HttpClientHandler.DangerousAcceptAnyServerCertificateValidator;
            }
            var httpClient = new HttpClient(httpClientHandler);

            var channel = GrpcChannel.ForAddress(grpcServiceEndpoint, new GrpcChannelOptions { HttpClient = httpClient });
            _client = new AuthService.AuthServiceClient(channel);
        }

        public async Task<string> LoginAsync(string username, string password)
        {
            var request = new LoginRequest { Username = username, Password = password };
            var response = await _client.loginAsync(request);
            return string.Empty;
        }
    }
}
