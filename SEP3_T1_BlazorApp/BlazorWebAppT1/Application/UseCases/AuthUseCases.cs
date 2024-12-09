using SEP3_Blazor_UI.Client.Application.Interfaces;
using static SEP3_Blazor_UI.Client.models.Models;

namespace SEP3_Blazor_UI.Client.Application.UseCases
{
    public class AuthUseCases
    {
        private readonly IAuthRepository _authRepository;

        public AuthUseCases(IAuthRepository authRepository)
        {
            _authRepository = authRepository;
        }

        public Task<string> Login(UserDTO userDTO)
        {
            return _authRepository.Login(userDTO);
        }

    }
}
