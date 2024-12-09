using SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Models;

namespace SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Application.Interfaces
{
    public interface IAuthRepository
    {
        // Expecting backend to respond with token upon successful login
        Task<string> Login(UserDTO userDTO);
    }
}
