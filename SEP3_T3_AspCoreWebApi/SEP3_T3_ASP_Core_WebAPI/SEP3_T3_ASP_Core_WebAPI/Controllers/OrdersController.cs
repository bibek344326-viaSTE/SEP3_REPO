using Microsoft.AspNetCore.Components;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using SEP3_T3_ASP_Core_WebAPI.Models;
using RouteAttribute = Microsoft.AspNetCore.Mvc.RouteAttribute;

namespace SEP3_T3_ASP_Core_WebAPI.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class OrdersController : ControllerBase
    {
        private readonly AppDbContext _context;

        public OrdersController(AppDbContext context)
        {
            _context = context;
        }

        //Endpoint to get all orders
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Order>>> GetOrders()
        {
            return await _context.Orders.ToListAsync();
        }
        
        //Endpoint to get a specific order
        [HttpGet("{id}")]
        public async Task<ActionResult<Order>> GetOrder(int id)
        {
            var order = await _context.Orders.FindAsync(id);

            if (order == null)
            {
                return NotFound();
            }

            return order;
        }
        
        //Endpoint to create a new order
        [HttpPost]
        public async Task<ActionResult<Order>> PostOrder(Order order)
        {
            _context.Orders.Add(order);
            await _context.SaveChangesAsync();

            return CreatedAtAction("GetOrder", new { id = order.OrderId }, order);
        }
        
        //Endpoint to update an order
        [HttpPut("{id}")]
        public async Task<IActionResult> PutOrder(int id, Order order)
        {
            if (id != order.OrderId)
            {
                return BadRequest();
            }

            _context.Entry(order).State = EntityState.Modified;

            try
            {
                await _context.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                throw new Exception($"Order with id {id} not found");
            }

            return NoContent();
        }
        //Endpoint to delete an order
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteOrder(int id)
        {
            var order = await _context.Orders.FindAsync(id);
            if (order == null)
            {
                return NotFound();
            }

            _context.Orders.Remove(order);
            await _context.SaveChangesAsync();

            return NoContent();
        }
        
        //Endpoint to get all orderItems in an order
        
        [HttpGet("{id}/orderitems")]
        public async Task<ActionResult<IEnumerable<Item>>> GetOrderItems(int id)
        {
            var order = await _context.Orders.FindAsync(id);
            if (order == null)
            {
                return NotFound();
            }
            return await _context.OrderItems.Where(oi => oi.OrderId == id).Select(oi => oi.Item).ToListAsync();
        }
        
        //Endpoint to add an Orderitem to an order
        [HttpPost("{id}/orderitems")]
        public async Task<ActionResult<OrderItem>> PostOrderItem(int id, OrderItem orderItem)
        {
            var order = await _context.Orders.FindAsync(id);
            if (order == null)
            {
                return NotFound();
            }
            orderItem.OrderId = id;
            _context.OrderItems.Add(orderItem);
            await _context.SaveChangesAsync();
            return CreatedAtAction("GetOrderItem", new { id = orderItem.Order }, orderItem);
        }
        
    }
}
