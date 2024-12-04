using SEP3_T3_ASP_Core_WebAPI.Models;

namespace SEP3_T3_ASP_Core_WebAPI.ApiContracts.OrderDto;

public class CreateOrderDto
{
    public int UserId { get; set; }
    public required string OrderStatus { get; set; }
    public List<OrderItem>? OrderList { get; set; }
    public DateTime DeliveryDate { get; set; }
}