using Entities;
using Microsoft.AspNetCore.Mvc;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;
using RouteAttribute = Microsoft.AspNetCore.Mvc.RouteAttribute;

namespace SEP3_T3_ASP_Core_WebAPI.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class OrdersController : ControllerBase
    {
        private readonly IOrderRepository orderRepository;

        public OrdersController(IOrderRepository orderRepository)
        {
            this.orderRepository = orderRepository;
        }

        // ********** CREATE Endpoints **********
        // POST: /Orders
        [HttpPost]
        public async Task<ActionResult<Order>> AddOrder([FromBody] Order order)
        {
            Order created = await orderRepository.AddOrderAsync(order);
            return Created($"/Orders/{created.OrderId}", created);
        }

        // ********** UPDATE Endpoints **********
        // PUT: /Orders/{id}
        [HttpPut("{id}")]
        public async Task<ActionResult> UpdateOrder([FromRoute] int id, [FromBody] Order order)
        {
            try
            {
                Order orderToUpdate = await orderRepository.GetOrderById(id);
                orderToUpdate.OrderItems = order.OrderItems;
                orderToUpdate.OrderStatus = order.OrderStatus;
                orderToUpdate.DeliveryDate = order.DeliveryDate;

                await orderRepository.UpdateOrderAsync(orderToUpdate);
                return NoContent();
            }
            catch (InvalidOperationException)
            {
                return NotFound($"Order with ID {id} not found.");
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return StatusCode(500, $"An error occurred: {e.Message}");
            }
        }

        [HttpPut("{id}/status")]
        public async Task<ActionResult> UpdateOrderStatus([FromRoute] int id, [FromBody] OrderStatus orderStatus)
        {
            try
            {
                var orderToUpdate = await orderRepository.GetOrderById(id);
                orderToUpdate.OrderStatus = orderStatus;
                await orderRepository.UpdateOrderAsync(orderToUpdate);
                return NoContent();
            }
            catch (InvalidOperationException)
            {
                return NotFound($"Order with ID {id} not found.");
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return StatusCode(500, $"An error occurred: {e.Message}");
            }
        }

        // DELETE: /Orders/{id}
        [HttpDelete("{id}")]
        public async Task<ActionResult> DeleteOrder([FromRoute] int id)
        {
            try
            {
                await orderRepository.DeleteOrderAsync(id);
                return NoContent();
            }
            catch (InvalidOperationException)
            {
                return NotFound($"Order with ID {id} not found.");
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return StatusCode(500, $"An error occurred: {e.Message}");
            }
        }

        // ********** GET Endpoints **********
        // GET: /Orders/{id}
        [HttpGet("{id}")]
        public async Task<ActionResult<Order>> GetSingleOrder([FromRoute] int id)
        {
            try
            {
                Order order = await orderRepository.GetOrderById(id);
                return Ok(order);
            }
            catch (InvalidOperationException)
            {
                return NotFound($"Order with ID {id} not found.");
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
                return StatusCode(500, $"An error occurred: {e.Message}");
            }
        }

        // GET: /Orders
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Order>>> GetAllOrders()
        {
            IQueryable<Order> orders = await orderRepository.GetAllOrders();
            return Ok(orders);
        }
    }
}
