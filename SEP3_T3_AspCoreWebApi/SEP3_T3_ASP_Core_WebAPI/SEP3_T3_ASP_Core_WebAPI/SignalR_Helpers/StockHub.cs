using Microsoft.AspNetCore.SignalR;

namespace SEP3_T3_ASP_Core_WebAPI.SignalR_Helpers;

public class StockHub:Hub
{
    public async Task NotifyLowStock(int itemId, int stockLevel)
    {
        //Broadcast the stock level to all Inventory admin users
        await Clients.All.SendAsync("LowStockAlert", itemId, stockLevel);
    }
}