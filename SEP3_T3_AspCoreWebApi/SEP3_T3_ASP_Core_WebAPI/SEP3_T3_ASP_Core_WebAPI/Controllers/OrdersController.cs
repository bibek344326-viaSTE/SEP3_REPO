using Entities;
using Entities.DTOs;
using Microsoft.AspNetCore.Mvc;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

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

        [HttpPost]
        public async Task<ActionResult<bool>> AddOrder([FromBody] CreateOrderDTO createOrderDTO)
        {
            var order = new Order
            {
                DeliveryDate = createOrderDTO.DeliveryDate,
                CreatedById = createOrderDTO.CreatedBy,
                OrderItems = createOrderDTO.OrderItems.Select(itemDto => new OrderItem
                {
                    ItemId = itemDto.item.ItemId,
                    QuantityToPick = itemDto.QuantityToPick
                }).ToList()
            };

            var createdOrder = await orderRepository.AddOrderAsync(order);
            return Ok(true);
        }

        [HttpPut("{id}")]
        public async Task<ActionResult> UpdateOrder([FromRoute] int id, [FromBody] Order order)
        {
            try
            {
                var orderToUpdate = await orderRepository.GetOrderById(id);
                orderToUpdate.OrderStatus = order.OrderStatus;
                orderToUpdate.DeliveryDate = order.DeliveryDate;
                orderToUpdate.OrderItems = order.OrderItems;

                await orderRepository.UpdateOrderAsync(orderToUpdate);
                return NoContent();
            }
            catch (InvalidOperationException)
            {
                return NotFound($"Order with ID {id} not found.");
            }
        }

        [HttpGet("{id}")]
        public async Task<ActionResult<GetOrderDTO>> GetSingleOrder([FromRoute] int id)
        {
            try
            {
                var order = await orderRepository.GetOrderById(id);
                var orderDto = new GetOrderDTO
                {
                    OrderId = order.OrderId,
                    OrderStatus = order.OrderStatus.ToString(),
                    DeliveryDate = order.DeliveryDate,
                    CreatedAt = order.CreatedAt,
                    OrderItems = order.OrderItems.Select(oi => new OrderItemDTO
                    {
                        item = new ItemDTO
                        {
                            ItemId = oi.ItemId,
                            ItemName = oi.Item?.ItemName ?? "Unknown Item"
                        },
                        QuantityToPick = oi.QuantityToPick
                    }).ToList(),
                    AssignedUser = order.AssignedUser?.UserName,
                    CreatedBy = order.CreatedBy.UserName
                };

                return Ok(orderDto);
            }
            catch (InvalidOperationException)
            {
                return NotFound($"Order with ID {id} not found.");
            }
        }

        [HttpGet]
        public async Task<ActionResult<List<GetOrderDTO>>> GetAllOrders()
        {
            var orders = await orderRepository.GetAllOrders();
            var orderDtos = orders.Select(order => new GetOrderDTO
            {
                OrderId = order.OrderId,
                OrderStatus = order.OrderStatus.ToString(),
                DeliveryDate = order.DeliveryDate,
                CreatedAt = order.CreatedAt,
                OrderItems = order.OrderItems.Select(oi => new OrderItemDTO
                {
                    item = new ItemDTO
                    {
                        ItemId = oi.ItemId,
                        ItemName = oi.Item?.ItemName ?? "Unknown Item"
                    },
                    QuantityToPick = oi.QuantityToPick
                }).ToList(),
                AssignedUser = order.AssignedUser?.UserName,
                CreatedBy = order.CreatedBy.UserName
            }).ToList();

            return Ok(orderDtos);
        }
    }
}
