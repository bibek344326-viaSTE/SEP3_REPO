using SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Models;
using SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Application.Interfaces;

namespace SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Application.UseCases
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
