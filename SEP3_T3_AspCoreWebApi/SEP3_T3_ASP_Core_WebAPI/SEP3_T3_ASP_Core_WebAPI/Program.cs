using Microsoft.EntityFrameworkCore;
using SEP3_T3_ASP_Core_WebAPI;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

// Add services to the container.
builder.Services.AddControllers(); // Adds support for MVC controllers
builder.Services.AddEndpointsApiExplorer(); // Adds support for minimal APIs
builder.Services.AddSwaggerGen(); // Adds Swagger support

// Configure PostgreSQL Database
builder.Services.AddDbContext<AppDbContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection"))); // Connects EF Core to PostgreSQL


var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
