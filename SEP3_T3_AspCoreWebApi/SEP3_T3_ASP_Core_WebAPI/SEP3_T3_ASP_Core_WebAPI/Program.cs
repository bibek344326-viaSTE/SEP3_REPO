using EfcRepositories.Repositories;
using Entities;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using SEP3_T3_ASP_Core_WebAPI;
using SEP3_T3_ASP_Core_WebAPI.Data; // Ensure DataSeeder is accessible
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;
<<<<<<< HEAD
using SEP3_T3_ASP_Core_WebAPI.SignalR_Helpers;
=======
using Microsoft.Extensions.Logging; // For logging
>>>>>>> master

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

<<<<<<< HEAD
builder.Services.AddScoped<StockService>();

// Add services to the container.
builder.Services.AddControllers(); // Adds support for MVC controllers
builder.Services.AddEndpointsApiExplorer(); // Adds support for minimal APIs
builder.Services.AddSwaggerGen(); // Adds Swagger support
=======
// Configure DbContext with PostgreSQL provider and connection string
builder.Services.AddDbContext<AppDbContext>();
>>>>>>> master


// Add repository services
builder.Services.AddScoped<IOrderItemRepository, EfcOrderItemRepository>();
builder.Services.AddScoped<IOrderRepository, EfcOrderRepository>();
builder.Services.AddScoped<IItemRepository, EfcItemRepository>();
builder.Services.AddScoped<IUserRepository, EfcUserRepository>();
builder.Services.AddScoped<IAuthRepository, EfcAuthRepository>();
builder.Services.AddScoped<IPasswordHasher<User>, PasswordHasher<User>>();

// Configure Authorization
builder.Services.AddAuthorization(options =>
{
    options.AddPolicy("ApiScope", policy =>
    {
        policy.RequireAuthenticatedUser();
    });
});

<<<<<<< HEAD
builder.Services.AddScoped<IOrderItemRepository, EfcOrderItemRepository>();
builder.Services.AddScoped<IOrderRepository, EfcOrderRepository>();
builder.Services.AddScoped<IItemRepository, EfcItemRepository>();
builder.Services.AddScoped<IUserRepository, EfcUserRepository>();
builder.Services.AddScoped<IAuthRepository, EfcAuthRepository>();
builder.Services.AddScoped<IPasswordHasher<User>, PasswordHasher<User>>();



builder.Services.AddDbContext<AppDbContext>();

=======
// (Optional) Configure Authentication here if applicable, e.g., JWT Bearer
>>>>>>> master

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

// Correct Order: Authentication before Authorization
app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

<<<<<<< HEAD
app.MapHub<StockHub>("/stockHub");
=======
// Data Seeding
using (var scope = app.Services.CreateScope())
{
    var services = scope.ServiceProvider;
    try
    {
        var dbContext = services.GetRequiredService<AppDbContext>();

        // Apply migrations if necessary
        dbContext.Database.Migrate();

        // Retrieve configuration flags
        var configuration = services.GetRequiredService<IConfiguration>();
        bool clearDatabase = configuration.GetValue<bool>("DataSeeding:ClearDatabase");
        bool seedData = configuration.GetValue<bool>("DataSeeding:SeedData");

        // Get PasswordHasher
        var passwordHasher = services.GetRequiredService<IPasswordHasher<User>>();

        // Seed data based on configuration flags
        DataSeeder.Seed(dbContext, passwordHasher, clearDatabase, seedData);
    }
    catch (Exception ex)
    {
        var logger = services.GetRequiredService<ILogger<Program>>();
        logger.LogError(ex, "An error occurred while seeding the database.");
    }
}
>>>>>>> master

app.Run();
