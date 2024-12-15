using Entities;
using Microsoft.EntityFrameworkCore;

namespace SEP3_T3_ASP_Core_WebAPI
{
    public class AppDbContext : DbContext
    {
        public AppDbContext(DbContextOptions<AppDbContext> options)
            : base(options)
        {
        }

        public DbSet<Item> Items { get; set; } // Table for storing Item entities
        public DbSet<Order> Orders { get; set; } // Table for storing Order entities
        public DbSet<User> Users { get; set; } // Table for storing User entities
        public DbSet<OrderItem> OrderItems { get; set; } // Table for storing OrderItem entities

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured) // Avoid configuring twice
            {
                optionsBuilder.UseNpgsql("Host=localhost;Port=5432;Database=sep3db;Username=postgres;Password=P@ssw0rd;Timeout=10;SSL Mode=Prefer");
            }
        }


        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<OrderItem>()
                .HasKey(oi => new { oi.OrderId, oi.ItemId });

            modelBuilder.Entity<OrderItem>()
                .HasOne(oi => oi.Order)
                .WithMany(o => o.OrderItems)
                .HasForeignKey(oi => oi.OrderId);

            modelBuilder.Entity<OrderItem>()
                .HasOne(oi => oi.Item)
                .WithMany(i => i.OrderItems)
                .HasForeignKey(oi => oi.ItemId);

            modelBuilder.Entity<Order>()
                .HasOne(o => o.AssignedUser)
                .WithMany()
                .HasForeignKey(o => o.UserId)
                .OnDelete(DeleteBehavior.Restrict);

            modelBuilder.Entity<Order>()
                .HasOne(o => o.CreatedBy)
                .WithMany()
                .HasForeignKey(o => o.CreatedById)
                .OnDelete(DeleteBehavior.Restrict);
        }
    }
}
