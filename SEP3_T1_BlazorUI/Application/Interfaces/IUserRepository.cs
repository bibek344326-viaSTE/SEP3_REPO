using SEP3_T1_BlazorUI.Models;

namespace SEP3_T1_BlazorUI.Application.Interfaces
{
    public interface IUserRepository
    {
        IEnumerable<User> GetAllUsers();
        void AddUser(User user);
        void DeleteUser(User user);

    }
}
