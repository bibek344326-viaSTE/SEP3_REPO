namespace SEP3_T3_ASP_Core_WebAPI.ApiContracts.OrderItemDto;

public class OrderItemDto
{
    public int OrderItemId { get; set; }
    public int OrderId { get; set; }
    public int ItemId { get; set; }
    public int QuantityToPick { get; set; }
}