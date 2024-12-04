namespace SEP3_T3_ASP_Core_WebAPI.ApiContracts.ItemDto;

public class UpdateItemDto
{
    public required int ItemId { get; set; }
    public required string ItemName { get; set; }
    public string? Description { get; set; }
    public int QuantityInStore { get; set; }
}