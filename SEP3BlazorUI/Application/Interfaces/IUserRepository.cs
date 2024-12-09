using SEP3BlazorUI.Models;

namespace SEP3BlazorUI.Application.Interfaces
{
    public interface IUserRepository
    {
        IEnumerable<User> GetAllUsers();
        void AddUser(User user);
        void DeleteUser(User user);

    }
}
