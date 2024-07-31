package com.tool.rental.beans;

public class ToolCharge {
    private int id;
    private int toolTypeId;
    private double dailyChargeAmount;
    private boolean isWeekdayCharge;
    private boolean isWeekendCharge;
    private boolean isHolidayCharge;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getToolTypeId() {
        return toolTypeId;
    }

    public void setToolTypeId(int toolTypeId) {
        this.toolTypeId = toolTypeId;
    }

    public double getDailyChargeAmount() {
        return dailyChargeAmount;
    }

    public void setDailyChargeAmount(double dailyChargeAmount) {
        this.dailyChargeAmount = dailyChargeAmount;
    }

    public boolean isWeekdayCharge() {
        return isWeekdayCharge;
    }

    public void setWeekdayCharge(boolean weekdayCharge) {
        isWeekdayCharge = weekdayCharge;
    }

    public boolean isWeekendCharge() {
        return isWeekendCharge;
    }

    public void setWeekendCharge(boolean weekendCharge) {
        isWeekendCharge = weekendCharge;
    }

    public boolean isHolidayCharge() {
        return isHolidayCharge;
    }

    public void setHolidayCharge(boolean holidayCharge) {
        isHolidayCharge = holidayCharge;
    }
}
