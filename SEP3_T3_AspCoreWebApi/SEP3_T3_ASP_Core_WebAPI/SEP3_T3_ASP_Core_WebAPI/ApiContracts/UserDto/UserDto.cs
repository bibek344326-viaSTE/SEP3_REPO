using Entities;

namespace SEP3_T3_ASP_Core_WebAPI.ApiContracts.UserDto
{
    public class UerDTO
    {
        public string UserName { get; set; }
        public string Password { get; set; }

        public UserRole UserRole { get; set; }
    }
}
