using SEP3BlazorUI.Application.UseCases;
using SEP3BlazorUI.Models;

namespace SEP3BlazorUI.Presentation.Managers
{

    public class UserManager
    {
        private readonly UserUseCases _userUseCases;

        public UserManager(UserUseCases userUseCases)
        {
            _userUseCases = userUseCases;
            CurrentPages = _userUseCases.GetAllUsers()
                .Select(u => u.Role)
                .Distinct()
                .ToDictionary(role => role, role => 1);
        }

        public string SearchQuery { get; set; } = string.Empty;
        public string SortField { get; private set; } = "Username";
        public string SortOrder { get; private set; } = "Ascending";
        public Dictionary<Role, int> CurrentPages { get; private set; }
        public int PageSize { get; private set; } = 5;

        public User? EditingUser { get; private set; }

        public List<Role> Roles => Enum.GetValues<Role>().ToList();

        public IEnumerable<IGrouping<Role, User>> GroupedUsers =>
            ApplyFilters().GroupBy(u => u.Role);

        private IEnumerable<User> ApplyFilters()
        {
            var filtered = _userUseCases.GetAllUsers();

            if (!string.IsNullOrWhiteSpace(SearchQuery))
            {
                filtered = filtered.Where(u =>
                    u.Username.Contains(SearchQuery, StringComparison.OrdinalIgnoreCase) ||
                    u.WorkingNumber.ToString().Contains(SearchQuery));
            }

            return SortOrder == "Ascending"
                ? filtered.OrderBy(u => GetPropertyValue(u, SortField))
                : filtered.OrderByDescending(u => GetPropertyValue(u, SortField));
        }

        public IEnumerable<User> PaginatedUsers(IGrouping<Role, User> roleGroup)
        {
            int currentPage = CurrentPages[roleGroup.Key];
            return roleGroup.Skip((currentPage - 1) * PageSize).Take(PageSize);
        }

        public void SetSort(string field, string order)
        {
            SortField = field;
            SortOrder = order;
        }

        public void ToggleEditUser(User user)
        {
            EditingUser = EditingUser?.Username == user.Username ? null : new User
            {
                Username = user.Username,
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

        private object GetPropertyValue(User user, string propertyName) =>
            propertyName switch
            {
                "Username" => user.Username,
                "WorkingNumber" => user.WorkingNumber,
                "Role" => user.Role,
                _ => user.Username
            };

        public void PreviousPage(Role role)
        {
            if (CurrentPages.ContainsKey(role) && CurrentPages[role] > 1)
            {
                CurrentPages[role]--;
            }
        }

        public void NextPage(Role role)
        {
            if (CurrentPages.ContainsKey(role) && CurrentPages[role] < TotalPages(role))
            {
                CurrentPages[role]++;
            }
        }

        public bool IsFirstPage(Role role) => CurrentPages.ContainsKey(role) && CurrentPages[role] == 1;

        public bool IsLastPage(Role role) => CurrentPages.ContainsKey(role) && CurrentPages[role] == TotalPages(role);

        public int TotalPages(Role role)
        {
            var totalUsers = _userUseCases.GetUsersByRole(role).Count();
            return (int)Math.Ceiling(totalUsers / (double)PageSize);
        }
    }

}