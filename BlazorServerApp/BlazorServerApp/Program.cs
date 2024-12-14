using Blazored.Toast;
using BlazorServerApp.Infrastructure.Repositories;
using BlazorServerApp.Application.UseCases;
using BlazorServerApp.Application.Interfaces;
using Microsoft.AspNetCore.Components.Authorization;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddRazorPages();
builder.Services.AddBlazoredToast();
builder.Services.AddServerSideBlazor();

builder.Services.AddScoped<AuthenticationStateProvider, CustomAuthenticationStateProvider>();
builder.Services.AddAuthorizationCore();

// Register the gRPC client for AuthService
builder.Services.AddGrpcClient<AuthService.AuthServiceClient>(options =>
{
    options.Address = new Uri("http://localhost:8090"); // Replace with your Spring Boot gRPC server URL
});

// Register Repositories, UseCases, and Managers using Dependency Injection
builder.Services.AddScoped<IAuthRepository, AuthRepository>(); // Register IAuthRepository
builder.Services.AddScoped<AuthUseCases>(); // Register Use Case
builder.Services.AddScoped<LoginManager>(); // Register LoginManager

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Error");
    app.UseHsts();
}

app.UseHttpsRedirection();
app.UseStaticFiles();
app.UseRouting();

app.MapBlazorHub();
app.MapFallbackToPage("/_Host");

app.Run();
