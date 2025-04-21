package ServiceLayer;
import BusinessLayer.ReportBL;

import java.time.LocalDateTime;

public class ReportSL
{
    private String name;
    private LocalDateTime date;
    private String body;

    public ReportSL(String name, String body)
    {
        this.name = name;
        this.date = LocalDateTime.now();
        this.body = body;
    }

    public ReportSL(ReportBL report)
    {
        this.name = report.getName();
        this.date = report.getDate();
        this.body = report.getBody();
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