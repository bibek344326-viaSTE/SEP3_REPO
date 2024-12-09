using SEP3BlazorUI.Models;
using SEP3BlazorUI.Application.Interfaces;

namespace SEP3BlazorUI.Application.UseCases
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
