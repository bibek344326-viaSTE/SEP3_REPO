using Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.ChangeTracking;
using SEP3_T3_ASP_Core_WebAPI;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

namespace EfcRepositories.Repositories;

public class EfcOrderRepository: IOrderRepository
{
    private readonly AppDbContext ctx;
    public EfcOrderRepository(AppDbContext ctx)
    {
        this.ctx = ctx;
    }

    public Task<Order> GetOrderById(int id)
    {
        throw new NotImplementedException();
    }

    public async Task<Order> AddOrderAsync(Order order)
    {
        order.CreatedAt = DateTime.Now;
        EntityEntry<Order> entityEntry = await ctx.Orders.AddAsync(order);
        await ctx.SaveChangesAsync();
        return entityEntry.Entity;
    }

    public async Task<Order> UpdateOrderAsync(Order order)
    {
        if (!ctx.Orders.Any(o => o.OrderId == order.OrderId))
        {
            throw new InvalidOperationException("Order does not exist");
        }
        ctx.Orders.Update(order);
        await ctx.SaveChangesAsync();
        return order;
    }

    public async Task<Order> DeleteOrderAsync(int id)
    {
        Order? existingOrder = await ctx.Orders.SingleOrDefaultAsync(o=>o.OrderId == id);
        if (existingOrder == null)
        {
            throw new InvalidOperationException("Order does not exist");
        }
        ctx.Orders.Remove(existingOrder);
        await ctx.SaveChangesAsync();
        return existingOrder;
    }

    public async Task<IQueryable<Order>> GetAllOrders()
    {
        return ctx.Orders.AsQueryable();
    }

    public  Task<IQueryable<Order>> GetAllOrdersByType(string type)
    {
        return null;
    }

    public async Task<Order> GetSingleAsync(int orderId)
    {
        return await ctx.Orders.SingleOrDefaultAsync(o => o.OrderId == orderId) ?? throw new InvalidOperationException();
    }

    public IQueryable<Order> GetMany()
    {
        return ctx.Orders.AsQueryable();
    }
    
    public async Task<Order> GetOrderByUserIdAsync(int userId)
    {
        return await ctx.Orders.SingleOrDefaultAsync(o => o.UserId == userId) ?? throw new InvalidOperationException();
    }
    
}