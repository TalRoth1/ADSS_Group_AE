package DomainLayer;

import java.util.*;

public class ShipmentDocumentDL {
    public Map<LocationDL, Map<String, Integer>> items;
    public float Weight;

    public ShipmentDocumentDL(Map<LocationDL, Map<String, Integer>> items) {
        this.items = items;
        this.Weight = 69;
    }

    /*
    gets map of maps
    removes all items with 0 or less quantity
    adds new ones
    */
    public boolean Edit(Map<LocationDL, Map<String,Integer>> newItems)
    {
        for (LocationDL newLocation : newItems.keySet())
        {
            if(items.keySet().contains(newLocation))
            {
                Map<String, Integer> currItemsList = items.get(newLocation);
                Map<String, Integer> newItemList = newItems.get(newLocation);
                currItemsList = editItems(newItemList, currItemsList);
            }
            else
            {
                items.put(newLocation , newItems.get(newLocation));
            }
        }
        CheckItems();
        return true;

    }
    
    //checks for items with 0 or negative quantity
    private void CheckItems()
    {
        for (LocationDL location : items.keySet())
        {
            if (items.get(location).size() == 0)
            {
                items.remove(location);
            }
        }
    }
    //removes the items listed
    public boolean Remove(Map <LocationDL, Map <String,Integer>> newItems)
    {
        for (LocationDL newLocationR : newItems.keySet())
        {
            //checks if location exists
            if (items.keySet().contains(newLocationR))
            {
                for (String itemR : newItems.get(newLocationR).keySet())
                {
                    items.get(newLocationR).remove(itemR);
                }
                if (items.get(newLocationR).size() == 0)
                    items.remove(newLocationR);
            }
            else return false;
        }
        return true;
    }

    public boolean Remove(List<LocationDL> locations)
    {
        for (LocationDL LocationToRemove : locations)
        {
            items.remove(LocationToRemove);
        }
        return true;
    }

    //helper function
    private Map<String,Integer> editItems (Map<String, Integer> newItemsList, Map<String, Integer> currItemsList)
    {
        for (String newItemName : newItemsList.keySet())
        {
            if (newItemsList.get(newItemName) <= 0)
            {
                currItemsList.remove(newItemName);
            }
            else
            {
                currItemsList.put(newItemName , newItemsList.get(newItemName));
            }
        }
        return currItemsList;
    }

    public List<LocationDL> getLocations()
    {
        return new ArrayList<>(items.keySet());
    }

    public Map<LocationDL, Map<String, Integer>> getItemsMap()
    {
        return items;
    }
}
