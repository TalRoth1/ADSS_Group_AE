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

    protected String getName()
    {
        return name;
    }

    protected String getDateString()
    {
        return date.toString();
    }

    protected LocalDateTime getDate()
    {
        return date;
    }

    protected String getBody()
    {
        return body;
    }
}