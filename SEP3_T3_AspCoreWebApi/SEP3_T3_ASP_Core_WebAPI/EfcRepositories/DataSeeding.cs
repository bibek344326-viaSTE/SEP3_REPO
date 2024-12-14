using Entities;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;

namespace SEP3_T3_ASP_Core_WebAPI.Data
{
    public static class DataSeeder
    {
        public static void Seed(AppDbContext context, IPasswordHasher<User> passwordHasher, bool clearDatabase, bool seedData)
        {
            // Begin a transaction to ensure data integrity
            using var transaction = context.Database.BeginTransaction();

            try
            {
                // Clear existing data if ClearDatabase is true
                if (clearDatabase)
                {
                    // Remove data in the correct order to respect foreign key constraints
                    context.OrderItems.RemoveRange(context.OrderItems);
                    context.Orders.RemoveRange(context.Orders);
                    context.Items.RemoveRange(context.Items);
                    context.Users.RemoveRange(context.Users);
                    context.SaveChanges();
                }

                // Insert mock data if SeedData is true
                if (seedData)
                {
                    // ----- Step 1: Create Users -----
                    var user1 = new User
                    {
                        UserName = "admin",
                        UserRole = UserRole.INVENTORY_MANAGER,
                        Password = passwordHasher.HashPassword(null, "admin")
                    };

                    var user2 = new User
                    {
                        UserName = "worker",
                        UserRole = UserRole.INVENTORY_MANAGER,
                        Password = passwordHasher.HashPassword(null, "worker")
                    };

                    context.Users.AddRange(user1, user2);
                    context.SaveChanges();

                    // ----- Step 2: Insert Items -----
                    var items = new List<Item>
                    {
                        new Item { ItemName = "Milk", Description = "1 Gallon of Whole Milk", QuantityInStore = 20 },
                        new Item { ItemName = "Coffee", Description = "Ground Coffee Beans, 1 lb", QuantityInStore = 15 },
                        new Item { ItemName = "Chocolate", Description = "Dark Chocolate Bar, 70% Cocoa", QuantityInStore = 25 },
                        new Item { ItemName = "Bread", Description = "Whole Wheat Bread Loaf", QuantityInStore = 30 },
                        new Item { ItemName = "Butter", Description = "Salted Butter, 1 lb", QuantityInStore = 10 },
                        new Item { ItemName = "Cheese", Description = "Cheddar Cheese, 1 lb", QuantityInStore = 12 },
                        new Item { ItemName = "Eggs", Description = "Dozen Large Eggs", QuantityInStore = 18 },
                        new Item { ItemName = "Apples", Description = "Red Apples, 1 lb", QuantityInStore = 22 },
                        new Item { ItemName = "Bananas", Description = "Bananas, 1 lb", QuantityInStore = 25 },
                        new Item { ItemName = "Orange Juice", Description = "1 Gallon of Orange Juice", QuantityInStore = 14 },
                        new Item { ItemName = "Cereal", Description = "Oat Cereal, 500g", QuantityInStore = 16 },
                        new Item { ItemName = "Yogurt", Description = "Greek Yogurt, 1 lb", QuantityInStore = 20 },
                        new Item { ItemName = "Honey", Description = "Organic Honey, 12 oz", QuantityInStore = 8 },
                        new Item { ItemName = "Tea", Description = "Green Tea Bags, 20 count", QuantityInStore = 25 },
                        new Item { ItemName = "Sugar", Description = "Granulated Sugar, 2 lb", QuantityInStore = 30 },
                        new Item { ItemName = "Rice", Description = "Basmati Rice, 1 kg", QuantityInStore = 50 },
                        new Item { ItemName = "Pasta", Description = "Italian Spaghetti, 500g", QuantityInStore = 40 },
                        new Item { ItemName = "Tomato Sauce", Description = "Tomato Sauce, 16 oz", QuantityInStore = 25 },
                        new Item { ItemName = "Salt", Description = "Table Salt, 1 lb", QuantityInStore = 100 },
                        new Item { ItemName = "Pepper", Description = "Ground Black Pepper, 4 oz", QuantityInStore = 35 },
                        new Item { ItemName = "Olive Oil", Description = "Extra Virgin Olive Oil, 1L", QuantityInStore = 12 },
                        new Item { ItemName = "Vinegar", Description = "Apple Cider Vinegar, 16 oz", QuantityInStore = 20 },
                        new Item { ItemName = "Onions", Description = "Yellow Onions, 1 lb", QuantityInStore = 60 },
                        new Item { ItemName = "Potatoes", Description = "Russet Potatoes, 1 lb", QuantityInStore = 70 },
                        new Item { ItemName = "Carrots", Description = "Fresh Carrots, 1 lb", QuantityInStore = 50 },
                        new Item { ItemName = "Chicken Breast", Description = "Boneless Chicken Breast, 1 lb", QuantityInStore = 25 },
                        new Item { ItemName = "Beef", Description = "Ground Beef, 1 lb", QuantityInStore = 30 },
                        new Item { ItemName = "Fish", Description = "Frozen Fish Fillet, 1 lb", QuantityInStore = 15 },
                        new Item { ItemName = "Shrimp", Description = "Frozen Shrimp, 1 lb", QuantityInStore = 10 },
                        new Item { ItemName = "Ice Cream", Description = "Vanilla Ice Cream, 1 Gallon", QuantityInStore = 20 },
                    };

                    context.Items.AddRange(items);
                    context.SaveChanges();

                    // ----- Step 3: Insert Orders -----
                    var orders = new List<Order>
                    {
                        new Order
                        {
                            OrderStatus = "Completed",
                            DeliveryDate = DateTime.UtcNow.AddDays(-1),
                            UserId = user1.UserId,
                            OrderItems = new List<OrderItem>
                            {
                                new OrderItem { ItemId = items[1].ItemId, QuantityToPick = 8 }, // Coffee
                                new OrderItem { ItemId = items[3].ItemId, QuantityToPick = 6 }, // Bread
                                new OrderItem { ItemId = items[2].ItemId, QuantityToPick = 4 }, // Chocolate
                                new OrderItem { ItemId = items[4].ItemId, QuantityToPick = 4 }, // Butter
                                new OrderItem { ItemId = items[6].ItemId, QuantityToPick = 5 }, // Eggs
                                new OrderItem { ItemId = items[5].ItemId, QuantityToPick = 3 }, // Cheese
                            }
                        },
                        new Order
                        {
                            OrderStatus = "InProgress",
                            DeliveryDate = DateTime.UtcNow.AddDays(5),
                            UserId = user2.UserId,
                            OrderItems = new List<OrderItem>
                            {
                                new OrderItem { ItemId = items[9].ItemId, QuantityToPick = 4 }, // Orange Juice
                                new OrderItem { ItemId = items[11].ItemId, QuantityToPick = 6 }, // Yogurt
                                new OrderItem { ItemId = items[8].ItemId, QuantityToPick = 5 }, // Bananas
                                new OrderItem { ItemId = items[0].ItemId, QuantityToPick = 7 }, // Milk
                            }
                        },
                        new Order
                        {
                            OrderStatus = "InProgress",
                            DeliveryDate = DateTime.UtcNow.AddDays(-3),
                            UserId = user1.UserId,
                            OrderItems = new List<OrderItem>
                            {
                                new OrderItem { ItemId = items[3].ItemId, QuantityToPick = 6 }, // Bread
                                new OrderItem { ItemId = items[6].ItemId, QuantityToPick = 4 }, // Eggs
                                new OrderItem { ItemId = items[2].ItemId, QuantityToPick = 5 }, // Chocolate
                                new OrderItem { ItemId = items[5].ItemId, QuantityToPick = 5 }, // Cheese
                            }
                        },
                    };

                    context.Orders.AddRange(orders);
                    context.SaveChanges();

                    // ----- Step 4: Insert Additional Orders and OrderItems -----

                    for (int i = 3; i < 20; i++) // Creating orders #4 to #20
                    {
                        var order = new Order
                        {
                            OrderStatus = i % 3 == 0 ? "Completed" : "InProgress",
                            DeliveryDate = DateTime.UtcNow.AddDays(-i),
                            UserId = (i % 2 == 0) ? user1.UserId : user2.UserId,
                            OrderItems = new List<OrderItem>()
                        };

                        // Randomly assign some items to the order
                        var random = new Random();
                        int numberOfItems = random.Next(3, 7); // Each order has between 3 to 6 items

                        for (int j = 0; j < numberOfItems; j++)
                        {
                            var randomItem = items[random.Next(items.Count)];
                            int quantity = random.Next(1, 11); // Quantity between 1 to 10

                            // Avoid duplicate items in the same order
                            if (!order.OrderItems.Any(oi => oi.ItemId == randomItem.ItemId))
{
    order.OrderItems.Add(new OrderItem
    {
        ItemId = randomItem.ItemId,
        QuantityToPick = quantity
    });
}

                        }

                        orders.Add(order);
                    }

                    context.Orders.AddRange(orders.GetRange(3, orders.Count - 3));
                    context.SaveChanges();
                }

                // Commit the transaction
                transaction.Commit();
            }
            catch (Exception ex)
            {
                // Rollback the transaction if any error occurs
                transaction.Rollback();
                throw new Exception("An error occurred while seeding the database.", ex);
            }
        }
    }
}
