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

        public User NewUser { get; private set; }
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
            NewUser = new User();
            EditContext = new EditContext(NewUser);
        }

        /// <summary>
        /// Handles adding a new user. Validates the form, checks for username duplicates, and adds the user.
        /// </summary>
        public async Task HandleAddUser()
        {
            if (!EditContext.Validate())
            {
                _toastService.ShowError("Please fix validation errors before submitting.");
                Console.WriteLine("Validation failed. Please check your inputs.");
                return;
            }

            IsLoading = true;

            try
            {
                // Log the user details being sent
                Console.WriteLine("Attempting to add new user with the following details (as JSON):");
                Console.WriteLine(JsonConvert.SerializeObject(NewUser, Formatting.Indented));

                var isUsernameTaken = _userUseCases.IsUsernameTaken(NewUser.Username);
                if (isUsernameTaken)
                {
                    _toastService.ShowError("This username is already taken. Please choose another one.");
                    Console.WriteLine("This username is already taken. Please choose another one.");
                    return;
                }

                await Task.Run(() => _userUseCases.AddUser(NewUser));

                _toastService.ShowSuccess($"User '{NewUser.Username}' added successfully!");
                Console.WriteLine($"User '{NewUser.Username}' was successfully added.");
                NewUser = new User();
                EditContext = new EditContext(NewUser); // Reset form
            }
            catch (Exception ex)
            {
                _toastService.ShowError("An error occurred while adding the user.");
                Console.WriteLine($"An error occurred while adding the user: {ex.Message}");
            }
            finally
            {
                IsLoading = false;
            }
        }

        /// <summary>
        /// Validates if a field is valid and applies the "is-invalid" class if it is invalid.
        /// </summary>
        /// <param name="fieldName">The name of the field to check.</param>
        /// <returns>A string representing the validation class to apply to the input.</returns>
        public string ValidationClass(string fieldName)
        {
            var fieldIdentifier = new FieldIdentifier(NewUser, fieldName);
            var isValid = !EditContext.GetValidationMessages(fieldIdentifier).Any();
            return isValid ? "" : "is-invalid";
        }

        /// <summary>
        /// Converts a Role enum value into a human-readable string.
        /// </summary>
        /// <param name="role">The role to humanize.</param>
        /// <returns>A human-readable version of the role.</returns>
        public string HumanizeRole(Role role)
        {
            if (role == Role.InventoryManager)
                return "Inventory Manager";
            else if (role == Role.WarehouseWorker)
                return "Warehouse Worker";
            else
                return role.ToString(); // Fallback for any unknown roles
        }

        /// <summary>
        /// Toggles the editing state for a specific user.
        /// </summary>
        /// <param name="user">The user to edit.</param>
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

        /// <summary>
        /// Saves changes to an edited user.
        /// </summary>
        public void SaveUser()
        {
            if (EditingUser == null) return;

            _userUseCases.AddUser(EditingUser);
            _toastService.ShowSuccess("User details updated successfully.");
            EditingUser = null;
        }

        /// <summary>
        /// Cancels the edit process for a user.
        /// </summary>
        public void CancelEdit()
        {
            EditingUser = null;
        }

        /// <summary>
        /// Deletes a user from the system.
        /// </summary>
        /// <param name="user">The user to delete.</param>
        public void DeleteUser(User user)
        {
            _userUseCases.DeleteUser(user);
            _toastService.ShowInfo($"User '{user.Username}' was deleted successfully.");
        }

        /// <summary>
        /// Clears the search query for user filtering.
        /// </summary>
        public void ClearSearch()
        {
            SearchQuery = string.Empty;
        }
    }
}
