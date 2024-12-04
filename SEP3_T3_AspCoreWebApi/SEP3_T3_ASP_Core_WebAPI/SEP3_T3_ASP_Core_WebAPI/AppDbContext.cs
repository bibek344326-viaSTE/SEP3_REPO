using Microsoft.EntityFrameworkCore;
using SEP3_T3_ASP_Core_WebAPI.Models;

namespace SEP3_T3_ASP_Core_WebAPI
{
    public class AppDbContext:DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        // DbSet properties for each entity represent tables in the database
        public DbSet<Item> Items { get; set; } // Table for storing Item entities
        public DbSet<Order> Orders { get; set; } // Table for storing Order entities
        public DbSet<User> Users { get; set; } // Table for storing User entities
        public DbSet<OrderItem> OrderItems { get; set; } // Table for storing OrderItem entities

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            // Configure composite primary key for OrderItem
            modelBuilder.Entity<OrderItem>()
                .HasKey(oi => new { oi.OrderId, oi.ItemId });

            // Configure relationships
            modelBuilder.Entity<OrderItem>()
                .HasOne(oi => oi.Order)
                .WithMany(o => o.OrderItems)
                .HasForeignKey(oi => oi.OrderId);

            modelBuilder.Entity<OrderItem>()
                .HasOne(oi => oi.Item)
                .WithMany(i => i.OrderItems)
                .HasForeignKey(oi => oi.ItemId);
        }
    }
}
