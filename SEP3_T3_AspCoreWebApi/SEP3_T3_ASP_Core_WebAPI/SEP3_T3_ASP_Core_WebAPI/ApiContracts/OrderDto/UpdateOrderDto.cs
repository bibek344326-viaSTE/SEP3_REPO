using SEP3_T3_ASP_Core_WebAPI.Models;

namespace SEP3_T3_ASP_Core_WebAPI.ApiContracts.OrderDto;

public class UpdateOrderDto
{
    public required int OrderId { get; set; }
    public required int AssignedUserId { get; set; }
    public DateTime DeliveryDate { get; set; }
    public required string OrderStatus { get; set; }
    public List<OrderItem>? OrderList { get; set; }
}