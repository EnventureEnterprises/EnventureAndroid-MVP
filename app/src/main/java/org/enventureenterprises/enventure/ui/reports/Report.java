package org.enventureenterprises.enventure.ui.reports;

/**
 * Created by mossplix on 7/11/17.
 */

public class Report {
    private Double profit;
    private Double total_earned;
    private Double total_spent;

    public Double getProfit() {
        return this.profit;
    }

    public Double getTotalEarned()
    {
        return this.total_earned;
    }

    public Double getTotalSpent()
    {
        return this.total_spent;
    }

    public void setProfit(Double profit)
    {
        this.profit = profit;
    }

    public void setTotalEarned(Double total_earned)
    {
        this.total_earned = total_earned;
    }

    public void setTotalSpent(Double total_spent)
    {
        this.total_spent = total_spent;
    }
}
