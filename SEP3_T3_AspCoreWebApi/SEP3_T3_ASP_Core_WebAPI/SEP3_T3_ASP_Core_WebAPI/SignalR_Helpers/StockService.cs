using Microsoft.AspNetCore.SignalR;

namespace SEP3_T3_ASP_Core_WebAPI.SignalR_Helpers;

public class StockService
{
    private readonly IHubContext<StockHub> _hubContext;
    
    public StockService(IHubContext<StockHub> hubContext)
    {
        _hubContext = hubContext;
    }
    
    public async Task UpdateStock(int itemId, int stockLevel)
    {
        Console.WriteLine($"Stock updated for {itemId}. Current stock: {stockLevel}");

        // Check if stock is low
        if (stockLevel < 10)
        {
            // Notify clients of low stock
            await _hubContext.Clients.All.SendAsync("LowStockAlert", itemId, stockLevel);
        }
    }
}