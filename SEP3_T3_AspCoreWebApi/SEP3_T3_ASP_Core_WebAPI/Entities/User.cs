namespace Entities
{
    public class User
    {
        public int UserId { get; set; }
        public required string UserName { get; set; }
        public required string Password { get; set; }
        public required string UserRole { get; set; }

        public static User Create(string requestUserName, string requestPassword, string requestUserRole)
        {
            return new User
            {
                UserName = requestUserName,
                Password = requestPassword,
                UserRole = requestUserRole
            };
        }
    }
}
