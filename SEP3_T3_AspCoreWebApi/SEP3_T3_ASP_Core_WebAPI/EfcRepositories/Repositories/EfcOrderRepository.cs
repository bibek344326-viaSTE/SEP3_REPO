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
        // Set the creation date for the order
        order.CreatedAt = DateTimeOffset.Now;

        // Add the order to the context and save it to generate the OrderId
        EntityEntry<Order> entityEntry = await ctx.Orders.AddAsync(order);
        await ctx.SaveChangesAsync();

        // Get the generated OrderId from the saved order
        int orderId = entityEntry.Entity.OrderId;

        // Check if the order has any order items
        if (order.OrderItems != null && order.OrderItems.Any())
        {
            // Verify quantities before making any updates to the database
            foreach (var orderItem in order.OrderItems)
            {
                var item = await ctx.Items.FindAsync(orderItem.ItemId);
                if (item == null)
                {
                    throw new Exception($"Item with ID {orderItem.ItemId} not found.");
                }

                if (orderItem.QuantityToPick > item.QuantityInStore)
                {
                    throw new Exception($"Insufficient quantity for Item ID {orderItem.ItemId}. Available: {item.QuantityInStore}, Requested: {orderItem.QuantityToPick}");
                }
            }

            // Update the OrderId for each order item and add them to the context
            foreach (var orderItem in order.OrderItems)
            {
                orderItem.OrderId = orderId; // Set the OrderId for each OrderItem
                await ctx.OrderItems.AddAsync(orderItem); // Add the OrderItem to the OrderItems table

                // Reduce the quantity in store for the item
                var item = await ctx.Items.FindAsync(orderItem.ItemId);
                if (item != null)
                {
                    item.QuantityInStore -= orderItem.QuantityToPick;
                    ctx.Items.Update(item);
                }
            }
            // Save changes after all items are added and quantities are updated
            await ctx.SaveChangesAsync();
        }

        // Return the created order with its generated ID and updated OrderItems
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