package DomainLayer;

public class TruckDL {
    public int Number;
    public String Model;
    public String Type;
    public float Weight;
    public float MaxWeight;
    
    public boolean CheckWeight(float weight) {
        return weight <= MaxWeight;
    }
    
}
