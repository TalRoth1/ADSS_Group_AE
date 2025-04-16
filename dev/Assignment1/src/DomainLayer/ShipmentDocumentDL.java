package DomainLayer;

import java.util.*;

public class ShipmentDocumentDL {
    public Map<LocationDL, Map<String, Integer>> items;
    public float Weight;

    public ShipmentDocumentDL(Map<LocationDL, Map<String, Integer>> items) {
        this.items = items;
        this.Weight = 69;
    }

    public boolean Edit(Map <LocationDL, Map <String,Integer>> newItems)
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
        return true;

    }

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


    private Map<String,Integer> editItems (Map<String, Integer> newItemsList, Map<String, Integer> currItemsList)
    {
        for (String newItemName : newItemsList.keySet())
        {
            currItemsList.put(newItemName , newItemsList.get(newItemName));
        }

        return currItemsList;
    }

    public List<LocationDL> getLocations()
    {
        return new ArrayList<>(items.keySet());
    }
}
