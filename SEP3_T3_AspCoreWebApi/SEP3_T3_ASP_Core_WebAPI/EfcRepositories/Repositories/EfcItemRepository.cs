using Entities;
using Microsoft.EntityFrameworkCore.ChangeTracking;
using SEP3_T3_ASP_Core_WebAPI;
using SEP3_T3_ASP_Core_WebAPI.RepositoryContracts;

namespace EfcRepositories.Repositories;

public class EfcItemRepository: IItemRepository
{
    private readonly AppDbContext _ctx;
    public EfcItemRepository(AppDbContext ctx)
    {
        this._ctx = ctx;
    }
    
    // Get an item by its id
    public async Task<Item> GetItemById(int id)
    {
        return await _ctx.Items.FindAsync(id) ?? throw new InvalidOperationException();
    }

    // Add an item to the database
    public async Task<Item> AddItemAsync(Item item)
    {
        EntityEntry<Item> entityEntry = await _ctx.Items.AddAsync(item);
        await _ctx.SaveChangesAsync();
        return entityEntry.Entity;
    }

    // Update an item in the database
    public async Task<Item> UpdateItemAsync(Item item)
    {
        if (!_ctx.Items.Any(i => i.ItemId == item.ItemId))
        {
            throw new InvalidOperationException("Item does not exist");
        }
        _ctx.Items.Update(item);
        await _ctx.SaveChangesAsync();
        return item;
    }

    // Delete an item from the database
    public async Task<Item> DeleteItemAsync(int id)
    {
        Item? existingItem = await _ctx.Items.FindAsync(id);
        if (existingItem == null)
        {
            throw new InvalidOperationException("Item does not exist");
        }
        _ctx.Items.Remove(existingItem);
        await _ctx.SaveChangesAsync();
        return existingItem;
    }
    
    // Get all items from the database
    public IQueryable<Item> GetAllItems()
    {
        return _ctx.Items.AsQueryable();
    }

    // Get all items of a specific type from the database
    public IQueryable<Item> GetAllItemsByType(string type)
    {
        return null;
    }
}