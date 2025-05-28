package DomainLayer;

public class TruckDL {
    public int Number;
    public String Model;
    public String Type;
    public float Weight;
    public float MaxWeight;
    public boolean IsBusy = false;

    public boolean CheckWeight(float weight) {
        return weight <= MaxWeight;
    }

    public TruckDL(int number, String model, String type, float weight, float maxWeight) {
        this.Number = number;
        this.Model = model;
        this.Type = type;
        this.Weight = weight;
        this.MaxWeight = maxWeight;
    }

    public TruckDL(int number, String model, String type, float weight, float maxWeight, boolean isBusy) {
        this.Number = number;
        this.Model = model;
        this.Type = type;
        this.Weight = weight;
        this.MaxWeight = maxWeight;
        this.IsBusy = isBusy;
    }

    public String toString() {
        return "Truck Number: " + Number + ", Model: " + Model + ", Type: " + Type + ", Weight: " + Weight
                + ", Max Weight: " + MaxWeight;
    }

    public Integer GetNumber() {
        return this.Number;
    }

    public String GetModel() {
        return this.Model;
    }

    public String GetType() {
        return this.Type;
    }

    public float GetMaxWeight() {
        return MaxWeight;
    }

    public void changeState() {
        IsBusy = !IsBusy;
    }

    public int getId() {
        return Number;
    }

    public String getLicensePlate() {
        return Model;
    }

    public float getWeight() {
        return Weight;
    }

    public float getMaxWeight() {
        return MaxWeight;
    }

    public boolean getStatus() {
        return IsBusy;
    }

    public String getType() {
        return Type;
    }
}