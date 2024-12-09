using System.Collections.Generic;
using SEP3_Blazor_UI.Client.models;
using static SEP3_Blazor_UI.Client.models.Models;

namespace SEP3_Blazor_UI.Client.Application.Interfaces
{
    public interface IUserRepository
    {
        IEnumerable<User> GetAllUsers();
        void AddUser(User user);
        void DeleteUser(User user);
    }
}
