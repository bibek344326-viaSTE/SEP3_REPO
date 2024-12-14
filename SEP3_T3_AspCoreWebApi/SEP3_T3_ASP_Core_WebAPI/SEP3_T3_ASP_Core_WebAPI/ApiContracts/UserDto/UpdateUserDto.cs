namespace SEP3_T3_ASP_Core_WebAPI.ApiContracts.UserDto
{
    public class UpdateUserDto
    {
        public required int UserId { get; set; }
        public required string UserName { get; set; }
        public required string Password { get; set; }
        public required string UserRole { get; set; }
    }
}
