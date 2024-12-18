﻿using Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;
using SEP3_T3_ASP_Core_WebAPI;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

namespace EfcRepositories.Repositories;

public class EfcOrderRepository : IOrderRepository
{
    private readonly AppDbContext ctx;

    public EfcOrderRepository(AppDbContext ctx)
    {
        this.ctx = ctx;
    }

    public async Task<Order> GetOrderById(int id)
    {
        var order = await ctx.Orders
            .Include(order => order.OrderItems)
            .Include(order => order.AssignedUser)
            .Include(order => order.CreatedBy)
            .SingleOrDefaultAsync(o => o.OrderId == id);

        if (order == null)
        {
            throw new InvalidOperationException($"Order with ID {id} not found.");
        }

        return order;
    }

    public async Task<Order> AddOrderAsync(Order order)
    {
        if (order.CreatedById <= 0)
        {
            throw new Exception("The 'CreatedBy' field is required and must contain a valid UserId.");
        }

        var createdByUser = await ctx.Users.FirstOrDefaultAsync(u => u.UserId == order.CreatedById);
        if (createdByUser == null)
        {
            throw new Exception($"User with UserId '{order.CreatedById}' not found.");
        }

        order.CreatedAt = DateTimeOffset.Now;
        order.CreatedBy = createdByUser;

        EntityEntry<Order> entityEntry = await ctx.Orders.AddAsync(order);
        await ctx.SaveChangesAsync();
        return entityEntry.Entity;
    }

    public async Task<Order> UpdateOrderAsync(Order order)
    {
        var existingOrder = await ctx.Orders.SingleOrDefaultAsync(o => o.OrderId == order.OrderId);
        if (existingOrder == null)
        {
            throw new InvalidOperationException("Order does not exist.");
        }

        ctx.Entry(existingOrder).CurrentValues.SetValues(order);
        await ctx.SaveChangesAsync();
        return existingOrder;
    }

    public async Task<Order> DeleteOrderAsync(int id)
    {
        var existingOrder = await ctx.Orders.SingleOrDefaultAsync(o => o.OrderId == id);
        if (existingOrder == null)
        {
            throw new InvalidOperationException("Order does not exist.");
        }

        ctx.Orders.Remove(existingOrder);
        await ctx.SaveChangesAsync();
        return existingOrder;
    }

    public async Task<List<Order>> GetAllOrders()
    {
        return await ctx.Orders
            .Include(order => order.OrderItems)
            .Include(order => order.AssignedUser)
            .Include(order => order.CreatedBy)
            .ToListAsync();
    }

    public Task<IQueryable<Order>> GetAllOrdersByType(string type)
    {
        throw new NotImplementedException();
    }

    public async Task<Order> GetSingleAsync(int orderId)
    {
        var order = await ctx.Orders
            .Include(order => order.OrderItems)
            .Include(order => order.AssignedUser)
            .Include(order => order.CreatedBy)
            .SingleOrDefaultAsync(o => o.OrderId == orderId);

        if (order == null)
        {
            throw new InvalidOperationException($"Order with ID {orderId} not found.");
        }

        return order;
    }

    public IQueryable<Order> GetMany()
    {
        return ctx.Orders.AsQueryable();
    }

    public async Task<Order> GetOrderByUserIdAsync(int userId)
    {
        var order = await ctx.Orders
            .Include(order => order.OrderItems)
            .Include(order => order.AssignedUser)
            .Include(order => order.CreatedBy)
            .SingleOrDefaultAsync(o => o.UserId == userId);

        if (order == null)
        {
            throw new InvalidOperationException($"Order for user with ID {userId} not found.");
        }

        return order;
    }
}
