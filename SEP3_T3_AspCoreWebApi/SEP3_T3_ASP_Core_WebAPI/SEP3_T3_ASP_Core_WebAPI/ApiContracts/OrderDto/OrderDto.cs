using SEP3_T3_ASP_Core_WebAPI.ApiContracts.OrderItemDto;

namespace SEP3_T3_ASP_Core_WebAPI.ApiContracts.OrderDto;

public class OrderDto
{
    public required int OrderId { get; set; }
    public required string OrderStatus { get; set; }
    public required DateTime DeliveryDate { get; set; }
    public required int AssignedUserId { get; set; }
    public List<OrderItem>? OrderList { get; set; }
}