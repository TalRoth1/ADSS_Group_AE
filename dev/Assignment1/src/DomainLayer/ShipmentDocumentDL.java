package DomainLayer;

import java.util.*;

public class ShipmentDocumentDL {
    public LocationDL Origin;
    public Map<LocationDL, Map<String, Integer>> items;
    public float Weight;

    public ShipmentDocumentDL(Map<LocationDL, Map<String, Integer>> items, LocationDL origin) {
        this.items = items;
        this.Weight = 0;
        this.Origin = origin;
    }

    public void setWeight(Map<String, Float> Items) {
        float sum = 0;
        for (LocationDL location : items.keySet()) {
            Map<String, Integer> itemList = items.get(location);
            for (String item : itemList.keySet()) {
                if (Items.containsKey(item)) {
                    sum += Items.get(item) * itemList.get(item);
                }
            }
        }
        this.Weight = sum;
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

    public boolean EditOrigin(LocationDL newOrigin, List<LocationDL> locations)
    {
        if (locations.contains(newOrigin))
        {
            Origin = newOrigin;
            return true;
        }
        return false;
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

    public String toString()
    {
        StringBuilder ans = new StringBuilder("Shipment Document: \n");
        String origin  = Origin.toString();
        List<LocationDL> locations = getLocations();
        for ( LocationDL loc : locations)
        {
            ans.append("origin: ").append(origin).append("\n");
            ans.append("destination: ").append(loc.toString()).append("\n");
            ans.append("items: ").append(getItemsString(items.get(loc))).append("\n");
            origin = loc.toString();
        }
        return ans.toString();
    }

    private String getItemsString(Map<String, Integer> items)
    {
        StringBuilder ans = new StringBuilder();
        for (String item : items.keySet())
        {
            ans.append(item).append(": ").append(items.get(item)).append(", ");
        }
        return ans.toString();
    }

    public LocationDL getOrigin()
    {
        return Origin;
    }
}
