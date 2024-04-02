package account;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.PrintStream;

public class g11_FXE {
    private double sgdToUsdRate;
    private double sgdToJpyRate;
    private double usdToSgdRate;
    private double jpyToSgdRate;
    private double usdToJpyRate;
    private double jpyToUsdRate;

    public g11_FXE() {
        this.setMarketRate();
    }

    public g11_FXE(double var1, double var3, double var5, double var7, double var9, double var11) {
        this.setSgdToUsdRate(var1);
        this.setSgdToJpyRate(var3);
        this.setUsdToSgdRate(var5);
        this.setJpyToSgdRate(var7);
        this.setUsdToJpyRate(var9);
        this.setJpyToUsdRate(var11);
    }

    public double convert(String var1, String var2, double var3) {
        System.out.println("Converting " + String.format("%.2f", var3) + " " + var1 + " to " + var2);
        switch (var1) {
            case "SGD":
                if (var2 == "USD") {
                    return var3 * this.sgdToUsdRate;
                }

                if (var2 == "JPY") {
                    return var3 * this.sgdToJpyRate;
                }
                break;
            case "USD":
                if (var2 == "SGD") {
                    return var3 * this.usdToSgdRate;
                }

                if (var2 == "JPY") {
                    return var3 * this.usdToJpyRate;
                }
                break;
            case "JPY":
                if (var2 == "SGD") {
                    return var3 * this.jpyToSgdRate;
                }

                if (var2 == "USD") {
                    return var3 * this.jpyToUsdRate;
                }
                break;
            default:
                System.out.println("Error in converting");
        }

        return var3;
    }

    public void displayRates() {
        System.out.println("Exchange Rates:");
        PrintStream var10000 = System.out;
        double var10001 = this.getSgdToUsdRate();
        var10000.println("1 SGD = " + var10001 + " USD = " + this.getSgdToJpyRate() + " JPY");
        var10000 = System.out;
        var10001 = this.getUsdToSgdRate();
        var10000.println("1 USD = " + var10001 + " SGD = " + this.getUsdToJpyRate() + " JPY");
        var10000 = System.out;
        var10001 = this.getJpyToSgdRate();
        var10000.println("1 JPY = " + var10001 + " SGD = " + this.getJpyToUsdRate() + " USD");
    }

    public void setMarketRate() {
        this.setSgdToUsdRate(0.75);
        this.setSgdToJpyRate(110.0);
        this.setUsdToSgdRate(1.24);
        this.setJpyToSgdRate(0.009);
        this.setUsdToJpyRate(150.0);
        this.setJpyToUsdRate(0.0067);
    }

    public double getRate(String var1) {
        double var2 = 0.0;
        if (var1 == "JPY") {
            var2 = this.getSgdToJpyRate();
        } else if (var1 == "USD") {
            var2 = this.getSgdToUsdRate();
        } else {
            System.out.println("Currency not available");
        }

        return var2;
    }

    public double getSgdToUsdRate() {
        return this.sgdToUsdRate;
    }

    public double getUsdToSgdRate() {
        return this.usdToSgdRate;
    }

    public double getSgdToJpyRate() {
        return this.sgdToJpyRate;
    }

    public double getJpyToSgdRate() {
        return this.jpyToSgdRate;
    }

    public double getUsdToJpyRate() {
        return this.usdToJpyRate;
    }

    public double getJpyToUsdRate() {
        return this.jpyToUsdRate;
    }

    public void setSgdToUsdRate(double var1) {
        this.sgdToUsdRate = var1;
    }

    public void setUsdToSgdRate(double var1) {
        this.usdToSgdRate = var1;
    }

    public void setSgdToJpyRate(double var1) {
        this.sgdToJpyRate = var1;
    }

    public void setJpyToSgdRate(double var1) {
        this.jpyToSgdRate = var1;
    }

    public void setUsdToJpyRate(double var1) {
        this.usdToJpyRate = var1;
    }

    public void setJpyToUsdRate(double var1) {
        this.jpyToUsdRate = var1;
    }
}
