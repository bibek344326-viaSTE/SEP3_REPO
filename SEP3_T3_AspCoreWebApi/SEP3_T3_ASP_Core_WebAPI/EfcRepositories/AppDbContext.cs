using Entities;
using Microsoft.EntityFrameworkCore;

namespace SEP3_T3_ASP_Core_WebAPI
{
    public class AppDbContext:DbContext
    {
        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseNpgsql("Host=localhost;Port=5432;Database=SEP3Db;Username=postgres;Password=P@ssw0rd");
        }
        //public AppDbContext(DbContextOptions<AppDbContext> options) : base(options) { }

        // DbSet properties for each entity represent tables in the database
        public DbSet<Item> Items { get; set; } // Table for storing Item entities
        public DbSet<Order> Orders { get; set; } // Table for storing Order entities
        public DbSet<User> Users { get; set; } // Table for storing User entities
        public DbSet<OrderItem> OrderItems { get; set; } // Table for storing OrderItem entities
        
        //configuring data source and connection string in OnConfiguring method - PostgreSQL
      

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
