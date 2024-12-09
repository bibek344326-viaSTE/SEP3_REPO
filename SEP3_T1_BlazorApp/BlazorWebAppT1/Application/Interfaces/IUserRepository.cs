using System.Collections.Generic;
using SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Models;

namespace SEP3_REPO.SEP3_T1_BlazorApp.BlazorWebAppT1.Application.Interfaces
{
    public interface IUserRepository
    {
        IEnumerable<User> GetAllUsers();
        void AddUser(User user);
        void DeleteUser(User user);
    }
}
