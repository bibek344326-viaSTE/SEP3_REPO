using static SEP3_Blazor_UI.Client.models.Models;

namespace SEP3_Blazor_UI.Client.Application.Interfaces
{
    public interface IAuthRepository
    {
        // Expecting backend to respond with token upon successful login
        Task<string> Login(UserDTO userDTO);
    }
}
