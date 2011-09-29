package au.id.teda.volumeusage.sax;

public class AccountStatusParseObject {
	public String refreshDate; // When did the application last pull the xml
    public String ipAddress; // ADSL IP address currently connected on
	public String uptime; // Uptime on currnet ADSL IP address
    public String anniversary; // What date does my usage roll over
    public String daysSoFar; // How long am I into my current period
    public String daysToGo; // How many days to go on my current period
    public String peakUsed; // Peak data consumed to date
    public String offpeakUsed; // Offpeak data consumed to date
    public String uploadsUsed;
    public String freezoneUsed;
    public String peakShaped; // I'm I currently shaped during peak times
    public String offpeakShaped; // I'm I currently shaped during offpeak times
	public String peakShapingSpeed;
	public String offpeakShapingSpeed;
}
