﻿using SEP3_T1_BlazorUI.Application.Interfaces;
using SEP3_T1_BlazorUI.Models;

namespace SEP3_T1_BlazorUI.Infrastructure.Repositories
{
    public class UserRepository: IUserRepository
    {
        private readonly List<User> _users;

        public UserRepository()
        {
            _users = new List<User>
        {
            new User { Username = "manager1", Password = "pass123", WorkingNumber = 101, Role = Role.InventoryManager },
            new User { Username = "worker1", Password = "pass456", WorkingNumber = 202, Role = Role.WarehouseWorker },
            new User { Username = "manager2", Password = "pass789", WorkingNumber = 103, Role = Role.InventoryManager },
            new User { Username = "worker2", Password = "pass012", WorkingNumber = 204, Role = Role.WarehouseWorker }
        };
        }

        public IEnumerable<User> GetAllUsers() => _users;

        public void AddUser(User user) => _users.Add(user);

        public void DeleteUser(User user)
        {
            _users.Remove(user);
        }
    }
}