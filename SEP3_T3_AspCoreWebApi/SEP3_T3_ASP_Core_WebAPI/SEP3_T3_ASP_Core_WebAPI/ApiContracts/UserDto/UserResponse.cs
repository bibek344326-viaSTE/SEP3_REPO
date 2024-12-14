using Entities;

namespace SEP3_T3_ASP_Core_WebAPI.ApiContracts.UserDto
{
    public class UserResponse
    {
        public int UserId { get; set; }
        public string? UserName { get; set; }
        public UserRole? UserRole { get; set; }
    }
}
