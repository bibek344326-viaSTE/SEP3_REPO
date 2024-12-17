using Entities;
using Entities.DTOs;
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

    public async Task<Order> AddOrderAsync(CreateOrderDTO orderDto)
    {
        // Validate the CreatedBy User
        if ( orderDto.CreatedBy <= 0)
        {
            throw new Exception("The 'CreatedBy' field is required and must contain a valid UserId.");
        }

        // Validate if the CreatedBy user exists
        var createdByUser = await ctx.Users.FirstOrDefaultAsync(u => u.UserId == orderDto.CreatedBy);
        if (createdByUser == null)
        {
            throw new Exception($"User with UserId '{orderDto.CreatedBy}' not found.");
        }

        // Create a new Order entity from the CreateOrderDTO
        var order = new Order
        {
            OrderStatus = OrderStatus.IN_PROGRESS, // Default order status
            DeliveryDate = orderDto.DeliveryDate,
            CreatedAt = orderDto.CreatedAt == default ? DateTimeOffset.Now : orderDto.CreatedAt, // Use provided CreatedAt or default to now
            CreatedById = createdByUser.UserId,
            UserId = null, // This might be set later if needed
            OrderItems = new List<OrderItem>() // Initialize empty list of order items
        };

        // Add the order to the context and save it to generate the OrderId
        EntityEntry<Order> entityEntry = await ctx.Orders.AddAsync(order);
        await ctx.SaveChangesAsync();

        // Get the generated OrderId from the saved order
        int orderId = entityEntry.Entity.OrderId;

        // Check if the order has any order items
        if (orderDto.OrderItems != null && orderDto.OrderItems.Any())
        {
            // Extract all the item IDs from the request to check if they exist in the system
            var itemIds = orderDto.OrderItems.Select(oi => oi.item.ItemId).ToList();

            // Retrieve the matching items from the database
            var items = await ctx.Items.Where(i => itemIds.Contains(i.ItemId)).ToListAsync();

            // Check for missing items or insufficient stock
            foreach (var orderItemDto in orderDto.OrderItems)
            {
                var item = items.FirstOrDefault(i => i.ItemId == orderItemDto.item.ItemId);

                if (item == null)
                {
                    throw new Exception($"Item with ID {orderItemDto.item.ItemId} not found.");
                }

                if (orderItemDto.QuantityToPick > item.QuantityInStore)
                {
                    throw new Exception($"Insufficient quantity for Item ID {orderItemDto.item.ItemId}. Available: {item.QuantityInStore}, Requested: {orderItemDto.QuantityToPick}");
                }
            }

            // Create the order items and update item quantities in the store
            foreach (var orderItemDto in orderDto.OrderItems)
            {
                var item = items.FirstOrDefault(i => i.ItemId == orderItemDto.item.ItemId);

                if (item != null)
                {
                    var orderItem = new OrderItem
                    {
                        OrderId = orderId,
                        ItemId = orderItemDto.item.ItemId,
                        QuantityToPick = orderItemDto.QuantityToPick
                    };

                    // Add the OrderItem to the context
                    await ctx.OrderItems.AddAsync(orderItem);

                    // Reduce the quantity in the store for the item
                    item.QuantityInStore -= orderItemDto.QuantityToPick;
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

    public async Task<List<GetOrderDTO>> GetAllOrders()
    {
        return await ctx.Orders
            .Include(order => order.OrderItems)        // Load the OrderItems for the Order
            .Include(order => order.AssignedUser)     // Load the User assigned to the Order
            .Include(order => order.CreatedBy)       // Load the User who created the Order
            .Select(order => new GetOrderDTO
            {
                OrderId = order.OrderId,
                OrderStatus = order.OrderStatus.ToString(), // Convert enum to string
                DeliveryDate = order.DeliveryDate,
                CreatedAt = order.CreatedAt,
                OrderItems = order.OrderItems.Select(item => new OrderItemDTO
                {
                    item = new ItemDTO
                    {
                        ItemId = item.ItemId,
                        ItemName = item.Item != null ? item.Item.ItemName : "Unknown Item" // Null check without using ?. 
                    },
                    QuantityToPick = item.QuantityToPick
                }).ToList(),
                AssignedUser = order.AssignedUser != null
                    ? 
                         order.AssignedUser.UserName
                    
                    : null, // Return null if AssignedUser is not present
                CreatedBy =  order.CreatedBy.UserName
                
            }).ToListAsync();
    }




    public Task<IQueryable<Order>> GetAllOrdersByType(string type)
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