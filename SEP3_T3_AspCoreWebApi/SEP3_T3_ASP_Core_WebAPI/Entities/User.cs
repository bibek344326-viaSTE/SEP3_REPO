using System.Text.Json.Serialization;

namespace Entities
{
    public enum UserRole
    {
        INVENTORY_MANAGER,
        WAREHOUSE_WORKER
    }
    public class User
    {
        public int UserId { get; set; }
        public required string UserName { get; set; }
        public required string Password { get; set; }
        [JsonConverter(typeof(JsonStringEnumConverter))]
        public required UserRole UserRole { get; set; }
        public required bool IsActive { get; set; }


        public static User Create(string requestUserName, string requestPassword, UserRole requestUserRole)
        {
            return new User
            {
                UserName = requestUserName,
                Password = requestPassword,
                UserRole = requestUserRole,
                IsActive = true
            };
        }

    }
}
