using SEP3_T1_BlazorUI.Application.UseCases;
using SEP3_T1_BlazorUI.Models;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Newtonsoft.Json;
using System;
using Microsoft.AspNetCore.Components.Forms;
using Blazored.Toast.Services;

namespace SEP3_T1_BlazorUI.Presentation.Managers
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

        public List<Role> Roles => _userUseCases.Roles;

        public IEnumerable<IGrouping<Role, User>> GroupedUsers =>
            _userUseCases.GetAllUsers()
                .Where(u => string.IsNullOrEmpty(SearchQuery) ||
                            u.Username.Contains(SearchQuery, StringComparison.OrdinalIgnoreCase) ||
                            u.WorkingNumber.ToString().Contains(SearchQuery))
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

        public async Task HandleAddUser()
        {
            if (!EditContext.Validate())
            {
                _toastService.ShowError("Please fix validation errors before submitting.");
                return;
            }

            if (string.IsNullOrWhiteSpace(NewUser.Username))
            {
                _toastService.ShowError("Username cannot be empty.");
                return;
            }

            if (string.IsNullOrWhiteSpace(NewUser.Password))
            {
                _toastService.ShowError("Password cannot be empty.");
                return;
            }

            IsLoading = true;

            try
            {
                Console.WriteLine(JsonConvert.SerializeObject(NewUser, Formatting.Indented));

                var isUsernameTaken = _userUseCases.IsUsernameTaken(NewUser.Username);
                if (isUsernameTaken)
                {
                    _toastService.ShowError("This username is already taken. Please choose another one.");
                    return;
                }

                await Task.Run(() => _userUseCases.AddUser(NewUser));

                _toastService.ShowSuccess($"User '{NewUser.Username}' added successfully!");

                ResetForm();
            }
            catch (Exception ex)
            {
                _toastService.ShowError("An error occurred while adding the user.");
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
                Role = Roles.FirstOrDefault()
            };
            EditContext = new EditContext(NewUser);
        }

        public string ValidationClass(string fieldName)
        {
            var fieldIdentifier = new FieldIdentifier(NewUser, fieldName);
            var isValid = !EditContext.GetValidationMessages(fieldIdentifier).Any();
            return isValid ? "" : "is-invalid";
        }

        public string HumanizeRole(Role role)
        {
            if (role == Role.InventoryManager)
                return "Inventory Manager";
            else if (role == Role.WarehouseWorker)
                return "Warehouse Worker";
            else
                return role.ToString();
        }

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

        public async Task SaveUser()
        {
            if (EditingUser == null) return;

            try
            {
                var userDTO = new UserDTO
                {
                    Username = EditingUser.Username,
                    Password = EditingUser.Password,
                    Role = EditingUser.Role
                };

                await Task.Run(() => _userUseCases.AddUser(userDTO));

                _toastService.ShowSuccess("User details updated successfully.");
                EditingUser = null;
            }
            catch (Exception ex)
            {
                _toastService.ShowError("An error occurred while updating the user.");
            }
        }

        public void CancelEdit()
        {
            EditingUser = null;
        }

        public void DeleteUser(User user)
        {
            _userUseCases.DeleteUser(user);
            _toastService.ShowInfo($"User '{user.Username}' was deleted successfully.");
        }

        public void ClearSearch()
        {
            SearchQuery = string.Empty;
        }
    }
}
