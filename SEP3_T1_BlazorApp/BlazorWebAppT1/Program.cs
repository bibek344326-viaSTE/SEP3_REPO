using Blazored.LocalStorage;
using Microsoft.AspNetCore.Components.Authorization;
using SEP3_Blazor_UI.Client.Application.Interfaces;
using SEP3_Blazor_UI.Client.Application.UseCases;
using SEP3_Blazor_UI.Client.Infrastructure;
using SEP3_Blazor_UI.Client.Infrastructure.Repositories;
using SEP3_Blazor_UI.Components;
var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorComponents()
    .AddInteractiveServerComponents();
// Add Blazored LocalStorage service (for token storage)
builder.Services.AddBlazoredLocalStorage();

// Add the authentication state provider as a scoped service
builder.Services.AddScoped<AuthenticationStateProvider, CustomAuthenticationStateProvider>();

// Add repositories
builder.Services.AddScoped<IItemRepository, ItemRepository>();
builder.Services.AddScoped<IOrderRepository, OrderRepository>();
builder.Services.AddScoped<IUserRepository, UserRepository>();
builder.Services.AddScoped<IAuthRepository, AuthRepository>();

// Add use cases
builder.Services.AddScoped<ItemUseCases>();
builder.Services.AddScoped<OrderUseCases>();
builder.Services.AddScoped<UserUseCases>();
builder.Services.AddScoped<AuthUseCases>();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error", createScopeForErrors: true);
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();


app.UseAntiforgery();

app.MapStaticAssets();
app.MapRazorComponents<App>()
    .AddInteractiveServerRenderMode();

app.Run();
