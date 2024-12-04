using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SEP3_T3_ASP_Core_WebAPI.Models;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

namespace SEP3_T3_ASP_Core_WebAPI.Controllers;

[ApiController]
[Route("[controller]")]
public class OrderItemsController: ControllerBase
{
    private readonly AppDbContext _context;
    
    public OrderItemsController(AppDbContext context)
    {
        _context = context;
    }
  
    //Endpoint to get all order items
    [HttpGet]
    public async Task<ActionResult<IEnumerable<OrderItem>>> GetOrderItems()
    {
        return await _context.OrderItems.ToListAsync();
    }
    
    //Endpoint to get a specific order item
    [HttpGet("{id}")]
    public async Task<ActionResult<OrderItem>> GetOrderItem(int id)
    {
        var orderItem = await _context.OrderItems.FindAsync(id);

        if (orderItem == null)
        {
            return NotFound();
        }

        return orderItem;
    }
    
    //Endpoint to create a new order item
    [HttpPost]
    public async Task<ActionResult<OrderItem>> PostOrderItem(OrderItem orderItem)
    {
        _context.OrderItems.Add(orderItem);
        await _context.SaveChangesAsync();

        return CreatedAtAction("GetOrderItem", new { id = orderItem.OrderItemId }, orderItem);
    }
    
    //Endpoint to update an order item
    [HttpPut("{id}")]
    public async Task<IActionResult> PutOrderItem(int id, OrderItem orderItem)
    {
        if (id != orderItem.OrderItemId)
        {
            return BadRequest();
        }

        _context.Entry(orderItem).State = EntityState.Modified;
        await _context.SaveChangesAsync();
        
        return NoContent();
    }
    
    //Endpoint to delete an order item
    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteOrderItem(int id)
    {
        var orderItem = await _context.OrderItems.FindAsync(id);
        if (orderItem == null)
        {
            return NotFound();
        }

        _context.OrderItems.Remove(orderItem);
        await _context.SaveChangesAsync();

        return NoContent();
    }
    
    //Endpoint to get all order items of a specific order
    [HttpGet("order/{orderId}")]
    public async Task<ActionResult<IEnumerable<OrderItem>>> GetOrderItemsByOrderId(int orderId)
    {
        return await _context.OrderItems.Where(orderItem => orderItem.OrderId == orderId).ToListAsync();
    }
    
    //Endpoint to get all order items of a specific item
    [HttpGet("item/{itemId}")]
    public async Task<ActionResult<IEnumerable<OrderItem>>> GetOrderItemsByItemId(int itemId)
    {
        return await _context.OrderItems.Where(orderItem => orderItem.ItemId == itemId).ToListAsync();
    }
    
    //Endpoint to get all order items of a specific order and item
    [HttpGet("order/{orderId}/item/{itemId}")]
    public async Task<ActionResult<IEnumerable<OrderItem>>> GetOrderItemsByOrderIdAndItemId(int orderId, int itemId)
    {
        return await _context.OrderItems.Where(orderItem => orderItem.OrderId == orderId && orderItem.ItemId == itemId).ToListAsync();
    }
    
}