using SEP3_Blazor_UI.Client.Application.UseCases;
using static SEP3_Blazor_UI.Client.models.Models;

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
    public Dictionary<string, int> CurrentPages { get; private set; }
    public int PageSize { get; private set; } = 5;

    public User? EditingUser { get; private set; }

    public List<string> Roles => _userUseCases.Roles;

    public IEnumerable<IGrouping<string, User>> GroupedUsers =>
        ApplyFilters().GroupBy(u => u.Role);

    private IEnumerable<User> ApplyFilters()
    {
        var filtered = _userUseCases.GetAllUsers();

        // Search
        if (!string.IsNullOrWhiteSpace(SearchQuery))
        {
            filtered = filtered.Where(u =>
                u.Username.Contains(SearchQuery, StringComparison.OrdinalIgnoreCase) ||
                u.WorkingNumber.ToString().Contains(SearchQuery));
        }

        // Sort
        return SortOrder == "Ascending"
            ? filtered.OrderBy(u => GetPropertyValue(u, SortField))
            : filtered.OrderByDescending(u => GetPropertyValue(u, SortField));
    }

    public IEnumerable<User> PaginatedUsers(IGrouping<string, User> roleGroup)
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

    public void PreviousPage(string role)
    {
        if (CurrentPages.ContainsKey(role) && CurrentPages[role] > 1)
        {
            CurrentPages[role]--;
        }
    }

    public void NextPage(string role)
    {
        if (CurrentPages.ContainsKey(role) && CurrentPages[role] < TotalPages(role))
        {
            CurrentPages[role]++;
        }
    }

    public bool IsFirstPage(string role) => CurrentPages.ContainsKey(role) && CurrentPages[role] == 1;

    public bool IsLastPage(string role) => CurrentPages.ContainsKey(role) && CurrentPages[role] == TotalPages(role);

    public int TotalPages(string role)
    {
        var totalUsers = _userUseCases.GetUsersByRole(role).Count();
        return (int)Math.Ceiling(totalUsers / (double)PageSize);
    }
}
