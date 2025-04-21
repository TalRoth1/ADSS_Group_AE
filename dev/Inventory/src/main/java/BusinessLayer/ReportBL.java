package BusinessLayer;
import java.time.LocalDateTime;

public class ReportBL
{
    private String name;
    private LocalDateTime date;
    private String body;

    protected ReportBL(String name, String body)
    {
        this.name = name;
        this.date = LocalDateTime.now();
        this.body = body;
    }

    public String getName()
    {
        return name;
    }

    public String getDateString()
    {
        return date.toString();
    }

    public LocalDateTime getDate()
    {
        return date;
    }

    public String getBody()
    {
        return body;
    }
}