package Inventory.ServiceLayer;

public class Response
{
    private String ResponseValue;
    private String ErrorMessage;

    public Response(String value, String errorMessage)
    {
        this.ResponseValue = value;
        this.ErrorMessage = errorMessage;
    }

    public String getResponseValue()
    {
        return ResponseValue;
    }

    public String getErrorMessage()
    {
        return ErrorMessage;
    }
}
