using EfcRepositories.Repositories;
using SEP3_T3_ASP_Core_WebAPI;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

// Add services to the container.
builder.Services.AddControllers(); // Adds support for MVC controllers
builder.Services.AddEndpointsApiExplorer(); // Adds support for minimal APIs
builder.Services.AddSwaggerGen(); // Adds Swagger support

builder.Services.AddAuthorization(options =>
{
    options.AddPolicy("ApiScope", policy =>
    {
        policy.RequireAuthenticatedUser();
    });
});

builder.Services.AddScoped<IOrderItemRepository, EfcOrderItemRepository>();
builder.Services.AddScoped<IOrderRepository, EfcOrderRepository>();
builder.Services.AddScoped<IItemRepository, EfcItemRepository>();
builder.Services.AddScoped<IUserRepository, EfcUserRepository>();
builder.Services.AddDbContext<AppDbContext>();


var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.UseAuthentication();

app.MapControllers();

app.Run();
