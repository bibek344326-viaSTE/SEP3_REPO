using SEP3_T1_BlazorUI.Application.UseCases;
using SEP3_T1_BlazorUI.Models;
using System.Collections.Generic;
using System.Linq;

namespace SEP3_T1_BlazorUI.Presentation.Managers
{
    public class UserManager
    {
        private readonly UserUseCases _userUseCases;

        public UserManager(UserUseCases userUseCases)
        {
            _userUseCases = userUseCases;
        }

        public string SearchQuery { get; set; } = string.Empty;

        public User? EditingUser { get; private set; }

        public IEnumerable<IGrouping<Role, User>> GroupedUsers =>
    _userUseCases.GetAllUsers()
        .Where(u => string.IsNullOrEmpty(SearchQuery) ||
                    u.Username.Contains(SearchQuery, StringComparison.OrdinalIgnoreCase) ||
                    u.WorkingNumber.ToString().Contains(SearchQuery))
        .GroupBy(u => u.Role);


        public void ToggleEditUser(User user)
        {
            EditingUser = EditingUser?.Username == user.Username ? null : new User
            {
                Username = user.Username,
                Password = user.Password,
                WorkingNumber = user.WorkingNumber,
                Role = user.Role
            };
        }

        public void SaveUser()
        {
            if (EditingUser == null) return;

            _userUseCases.AddUser(EditingUser);
            EditingUser = null;
        }

        public void CancelEdit()
        {
            EditingUser = null;
        }

        public void DeleteUser(User user)
        {
            _userUseCases.DeleteUser(user);
        }

        public void ClearSearch()
        {
            SearchQuery = string.Empty;
        }
    }
}
