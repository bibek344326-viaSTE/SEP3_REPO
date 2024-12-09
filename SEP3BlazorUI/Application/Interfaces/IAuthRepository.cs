using SEP3BlazorUI.Models;

namespace SEP3BlazorUI.Application.Interfaces
{
    public interface IAuthRepository
    {
        Task<string> Login(UserDTO userDTO);
    }
}
