using SEP3_T1_BlazorUI.Models;
using SEP3_T1_BlazorUI.Application.Interfaces;

namespace SEP3_T1_BlazorUI.Application.UseCases
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
