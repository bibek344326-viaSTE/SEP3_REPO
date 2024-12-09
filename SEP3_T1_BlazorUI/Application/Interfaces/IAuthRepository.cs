using SEP3_T1_BlazorUI.Models;

namespace SEP3_T1_BlazorUI.Application.Interfaces
{
    public interface IAuthRepository
    {
        Task<string> Login(UserDTO userDTO);
    }
}
