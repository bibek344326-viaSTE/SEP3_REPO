using Entities;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.SignalR;
using SEP3_T3_ASP_Core_WebAPI.ApiContracts.ItemDto;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;
using SEP3_T3_ASP_Core_WebAPI.SignalR_Helpers;


namespace SEP3_T3_ASP_Core_WebAPI.Controllers;

[ApiController]
[Route("[controller]")]
public class ItemsController: ControllerBase
{
    private readonly IItemRepository _itemRepository;
    private readonly IHubContext<StockHub> _hubContext;
    
    public ItemsController(IItemRepository itemRepository, IHubContext<StockHub> hubContext)
    {
        _itemRepository = itemRepository;
        _hubContext = hubContext;
    }
    
    // Create Endpoints
    // POST: /Items
    [HttpPost]
    public async Task<ActionResult<ItemDto>> AddItem([FromBody] CreateItemDto request)
    {
        Item item = Entities.Item.Create(request.ItemName, request.Description, request.QuantityInStore);
        Item created = await _itemRepository.AddItemAsync(item);
        ItemDto dto = new()
        {
            ItemId = created.ItemId,
            ItemName = created.ItemName,
            Description = created.Description,
            QuantityInStore = created.QuantityInStore
        };
        return Created($"/Items/{dto.ItemId}", created);
    }
    
    // PUT: /Items/{id}
    [HttpPut("{id}")]
    public async Task<ActionResult> UpdateItem([FromRoute] int id, [FromBody] UpdateItemDto request)
    {
        try
        {
            Item itemToUpdate = await _itemRepository.GetItemById(id);
            itemToUpdate.ItemName = request.ItemName;
            itemToUpdate.Description = request.Description;
            itemToUpdate.QuantityInStore = request.QuantityInStore;
            
            await _itemRepository.UpdateItemAsync(itemToUpdate);
            return NoContent();
        }
        catch (InvalidOperationException)
        {
            return NotFound($"Item with ID {id} not found.");
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }
    
    // GET: /Items
    [HttpGet]
    public async Task<ActionResult<IEnumerable<ItemDto>>> GetAllItems()
    {
        IQueryable<Item> items = _itemRepository.GetAllItems();
        List<ItemDto> dtos = items.Select(item => new ItemDto
        {
            ItemId = item.ItemId,
            ItemName = item.ItemName,
            Description = item.Description,
            QuantityInStore = item.QuantityInStore
        }).ToList();
        return Ok(dtos);
    }
    
    // GET: /Items/{id}
    [HttpGet("{id}")]
    public async Task<ActionResult<ItemDto>> GetSingleItem([FromRoute] int id)
    {
        try
        {
            Item item = await _itemRepository.GetItemById(id);
            ItemDto dto = new()
            {
                ItemId = item.ItemId,
                ItemName = item.ItemName,
                Description = item.Description,
                QuantityInStore = item.QuantityInStore
            };
            return Ok(dto);
        }
        catch (InvalidOperationException)
        {
            return NotFound($"Item with ID {id} not found.");
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }
    
    // DELETE: /Items/{id}
    [HttpDelete("{id}")]
    public async Task<ActionResult> DeleteItem([FromRoute] int id)
    {
        try
        {
            await _itemRepository.DeleteItemAsync(id);
            return NoContent();
        }
        catch (InvalidOperationException)
        {
            return NotFound($"Item with ID {id} not found.");
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }
    
    //update the quantity of an item
    [HttpPut("{id}/quantity")]
    public async Task<ActionResult> UpdateItemQuantity([FromRoute] int id, [FromBody] UpdateItemDto request)
    {
        try
        {
            Item itemToUpdate = await _itemRepository.GetItemById(id);
            itemToUpdate.QuantityInStore = request.QuantityInStore;

            await _itemRepository.UpdateItemAsync(itemToUpdate);

            // Check for low stock and send a SignalR notification
            if (itemToUpdate.QuantityInStore < 10) // Threshold for low stock
            {
                string message = $"Low stock alert: Item '{itemToUpdate.ItemName}' has only {itemToUpdate.QuantityInStore} units left.";
                await _hubContext.Clients.All.SendAsync("ReceiveLowStockAlert", itemToUpdate.ItemId, itemToUpdate.ItemName, itemToUpdate.QuantityInStore);
            }

            return NoContent();
        }
        catch (InvalidOperationException)
        {
            return NotFound($"Item with ID {id} not found.");
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }
    
    //update the description of an item
    [HttpPut("{id}/description")]
    public async Task<ActionResult> UpdateItemDescription([FromRoute] int id, [FromBody] UpdateItemDto request)
    {
        try
        {
            Item itemToUpdate = await _itemRepository.GetItemById(id);
            itemToUpdate.Description = request.Description;
            
            await _itemRepository.UpdateItemAsync(itemToUpdate);
            return NoContent();
        }
        catch (InvalidOperationException)
        {
            return NotFound($"Item with ID {id} not found.");
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }
    
    //update the name of an item
    [HttpPut("{id}/name")]
    public async Task<ActionResult> UpdateItemName([FromRoute] int id, [FromBody] UpdateItemDto request)
    {
        try
        {
            Item itemToUpdate = await _itemRepository.GetItemById(id);
            itemToUpdate.ItemName = request.ItemName;
            
            await _itemRepository.UpdateItemAsync(itemToUpdate);
            return NoContent();
        }
        catch (InvalidOperationException)
        {
            return NotFound($"Item with ID {id} not found.");
        }
        catch (Exception e)
        {
            Console.WriteLine(e);
            return StatusCode(500, $"An error occurred: {e.Message}");
        }
    }
}