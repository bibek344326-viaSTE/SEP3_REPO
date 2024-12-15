using Microsoft.AspNetCore.Components.Forms;
using Blazored.Toast.Services;
using BlazorServerApp.Application.UseCases;

namespace BlazorServerApp.Managers
{
    public class UserManager
    {
        private readonly UserUseCases _userUseCases;
        private readonly IToastService _toastService;

        public UserDTO NewUser { get; private set; }
        public EditContext EditContext { get; private set; }
        public bool IsLoading { get; private set; }
        public string SearchQuery { get; set; } = string.Empty;
        public User? EditingUser { get; private set; }

        public List<Role> Roles => Enum.GetValues(typeof(Role)).Cast<Role>().ToList();

        public IEnumerable<IGrouping<Role, User>> GroupedUsers =>
            _userUseCases.GetAllUsersAsync().Result // Await for async operation
                .Where(u => string.IsNullOrEmpty(SearchQuery) ||
                            u.Username.Contains(SearchQuery, StringComparison.OrdinalIgnoreCase))
                .GroupBy(u => u.Role);

        public UserManager(UserUseCases userUseCases, IToastService toastService)
        {
            _userUseCases = userUseCases;
            _toastService = toastService;
            NewUser = new UserDTO
            {
                Role = Role.WarehouseWorker
            };
            EditContext = new EditContext(NewUser);
        }

        // Add new user
        public async Task HandleAddUser()
        {
            if (!EditContext.Validate())
                return;

            IsLoading = true;

            try
            {
                await _userUseCases.AddUserAsync(NewUser);
                _toastService.ShowSuccess($"User '{NewUser.Username}' added successfully!");
                ResetForm();
            }
            catch (Exception ex)
            {
                _toastService.ShowError("An error occurred while adding the user: " + ex.Message);
            }
            finally
            {
                IsLoading = false;
            }
        }

        private void ResetForm()
        {
            NewUser = new UserDTO
            {
                Role = Role.WarehouseWorker
            };
            EditContext = new EditContext(NewUser);
        }

        // Update existing user
        public void ClearSearch()
        {
            SearchQuery = string.Empty;
        }

        public void ToggleEditUser(User user)
        {
            if (EditingUser?.Userid == user.Userid)
            {
                EditingUser = null;
                return;
            }

            EditingUser = new User
            {
                Username = user.Username,
                Password = string.Empty,
                Userid = user.Userid,
                Role = user.Role
            };
        }

        public async Task DeleteUserAsync(User user)
        {
            try
            {
                await _userUseCases.DeleteUserAsync(user);
                _toastService.ShowInfo($"User '{user.Username}' was deleted successfully.");
            }
            catch (Exception ex)
            {
                _toastService.ShowError("An error occurred while deleting the user: " + ex.Message);
            }
        }

        public async Task SaveUserAsync()
        {
            if (EditingUser == null) return;

            try
            {
                await _userUseCases.EditUserAsync(EditingUser);
                _toastService.ShowSuccess("User details updated successfully.");
                EditingUser = null;
            }
            catch (Exception ex)
            {
                _toastService.ShowError("An error occurred while updating the user: " + ex.Message);
            }
        }

        public void CancelEdit()
        {
            EditingUser = null;
        }

        public string HumanizeRole(Role role)
        {
            return role switch
            {
                Role.InventoryManager => "Inventory Manager",
                Role.WarehouseWorker => "Warehouse Worker",
                _ => role.ToString()
            };
        }

        public string ValidationClass(object model, string fieldName)
        {
            if (model == null) return string.Empty;

            var fieldIdentifier = new FieldIdentifier(model, fieldName);
            var isValid = !EditContext.GetValidationMessages(fieldIdentifier).Any();
            return isValid ? "" : "is-invalid";
        }
    }
}
