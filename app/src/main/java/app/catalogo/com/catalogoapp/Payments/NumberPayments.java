package app.catalogo.com.catalogoapp.Payments;

public class NumberPayments {
    private double amount;
    private double payment;
    private String numberPayments;

    public String getNumberPayments(double amount, double payment)
    {
        double result = amount/payment;
        return numberPayments = String.valueOf(result);
    }
}
