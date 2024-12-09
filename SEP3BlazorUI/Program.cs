using Microsoft.AspNetCore.Components.Web;
using Microsoft.AspNetCore.Components.WebAssembly.Hosting;
using Microsoft.AspNetCore.Components.Authorization; // Added for AuthenticationStateProvider
using Blazored.LocalStorage; // Added for Blazored LocalStorage
using SEP3BlazorUI;
using SEP3BlazorUI.Application.Interfaces;
using SEP3BlazorUI.Application.UseCases;
using SEP3BlazorUI.Infrastructure.Repositories;
using SEP3BlazorUI.Infrastructure;

var builder = WebAssemblyHostBuilder.CreateDefault(args);
builder.RootComponents.Add<App>("#app");
builder.RootComponents.Add<HeadOutlet>("head::after");

builder.Services.AddScoped(sp => new HttpClient { BaseAddress = new Uri(builder.HostEnvironment.BaseAddress) });
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


await builder.Build().RunAsync();
