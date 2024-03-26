package bank;

import java.text.DecimalFormat;
import java.time.Period;
import java.time.format.DateTimeParseException;

public class InsuranceCatalogEntry {
    private String idString, name, type;
    private double premium, claims;
    private Period duration;
    private DecimalFormat moneyFormat = new DecimalFormat("#,###.00");

    public InsuranceCatalogEntry(String polId, String name, String type, String duration,
     double premium, double claims){
        this.idString = polId;
        this.name = name;
        this.type = type;
        this.duration = Period.parse(duration);
        this.premium = premium;
        this.claims = claims;
    }

    public InsuranceCatalogEntry(String csvRecordLine){
        try {
            String[] parts = csvRecordLine.split(",");
            this.idString = parts[0];
            this.name = parts[1];
            this.type = parts[2];
            this.premium = Double.parseDouble(parts[3]);
            this.claims = Double.parseDouble(parts[4]);
            this.duration = Period.parse(parts[5]);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("Encountered a null value.");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.err.println(idString + " record's premium or claim value is not a number.");
        } catch (DateTimeParseException e) {
            System.err.println("Period was not in ISO format.");
        }
    }

    public String getIdString(){
        return idString;
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public Period getDurationISO(){
        return duration;
    }

    /**
     * Converts the ISO8061 formatted string into a human readable one.
     * <br /> e.g. P4Y6M7D -> "4 Yrs 6 Mths 7 Days"
     * Will skip time denominations of 0: P2Y4D -> "2 Yrs 4 Days"
     * @return human-readable insurance policy duration "X Yrs Y Mths Z Days"
     * @see <a href="https://en.wikipedia.org/wiki/ISO_8601">ISO8601 Format</a>
     */
    public String getDurationString(){
        int years = duration.getYears();
        int months = duration.getMonths();
        int days = duration.getDays();

        String output = "";
        if (years > 0) {
            output += Integer.toString(years) + " Yrs ";
        }

        if (months > 0){
            output += Integer.toString(months) + " Mths ";
        }

        if (days > 0 ){
            output += Integer.toString(days) + " Days";
        }

        return output;
    }

    public String getPremiumString(){
        return moneyFormat.format(premium);
    }

    public String getClaimBalanceString(){
        return moneyFormat.format(claims);
    }

    public Double getMonthlyPremiumDouble(){
        return premium/12;
    }

    public Double getClaimDouble(){
        return claims;
    }

    public String getEntryString(){
        return name + "       " + idString + "   $" + getPremiumString() + "            $" + 
        getClaimBalanceString() + "        " + getDurationString();
    }
}
