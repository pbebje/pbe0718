package com.tool.rental;

import com.tool.rental.beans.RentalAgreement;
import com.tool.rental.bo.ToolDataBoImpl;
import com.tool.rental.dao.ToolDataDaoImpl;
import com.tool.rental.helpers.CalculationHelper;
import com.tool.rental.helpers.Constants;
import com.tool.rental.helpers.DateHelpers;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Precision;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class PointOfSale {

    public static void main(String[] args) {
        PointOfSale pos = new PointOfSale();
        String toolCode = null;
        String numDays = null;
        String discount = null;
        String startDate = null;
        String errStr = null;
        RentalAgreement rentalAgreement = null;
        try {
            rentalAgreement = pos.calcRentalAgreement(toolCode, numDays, discount, startDate);
        } catch (Exception e) {
            errStr = e.getMessage();
        }
        pos.printRentalAgreement(rentalAgreement, errStr);
    }

    public RentalAgreement calcRentalAgreement(String toolCode, String startDate, String numDays, String discount) throws Exception {
        ToolDataBoImpl toolDataBo = new ToolDataBoImpl(new ToolDataDaoImpl());
        List<String> errors = new ArrayList<>();
        int days = 0;
        double discountPct = 0d;
        LocalDate checkOutDate = null;
        try {
            // requirement doc says to validate number of days input into screen and NOT the number of chargeable days
            days = parseInt(numDays);
            if (days < 1) {
                errors.add(Constants.NUM_RENTAL_DAYS_ERR_MSG);
            }
        } catch (NumberFormatException e) {
            errors.add(Constants.NUM_RENTAL_DAYS_ERR_MSG);
        }
        try {
            discountPct = parseDouble(discount) / 100;
            if (discountPct < 0d || discountPct > 1.0d) {
                errors.add(Constants.DISCOUNT_ERR_MSG);
            }
        } catch (NumberFormatException e) {
            errors.add(Constants.DISCOUNT_ERR_MSG);
        }
        try {
            checkOutDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("MM/dd/yy"));
        } catch (DateTimeParseException e) {
            errors.add(Constants.CHECKOUT_DATE_ERR_MSG);
        }
        if (errors.size() > 0) {
            String msg = "";
            for (String err : errors) {
                msg += (err + "\n");
            }
            Exception ex = new Exception(msg);
            throw ex;
        }
        try {
            RentalAgreement rentalAgreement = toolDataBo.getRentalAgreementData(toolCode);
            if (rentalAgreement != null) {
                rentalAgreement.setCheckOutDate(checkOutDate);
                days = calcChargeableDays(days, rentalAgreement);
                rentalAgreement.setRentalDays(days);
                rentalAgreement.setDiscountPercent(discountPct);
                rentalAgreement.setPreDiscountCharge(Precision.round(rentalAgreement.getDailyCharge() * days, 2));
                rentalAgreement.setDiscountAmount(Precision.round(rentalAgreement.getPreDiscountCharge() * discountPct, 2));
                rentalAgreement.setFinalCharge(Precision.round(rentalAgreement.getPreDiscountCharge() - rentalAgreement.getDiscountAmount(), 2));
            }
            return rentalAgreement;
        } catch (Exception e) {
            throw e;
        }
    }

    private int calcChargeableDays(int days, RentalAgreement rentalAgreement) {
        int daysToRemove = 0;
        if (!rentalAgreement.isHolidayCharge()) {
            daysToRemove += DateHelpers.numHolidaysInDateRange(rentalAgreement.getCheckOutDate(), days);
        }
        if (!rentalAgreement.isWeekendCharge()) {
            daysToRemove += DateHelpers.numWeekendDaysInDateRange(rentalAgreement.getCheckOutDate(), days);
        }
        return  days >= daysToRemove ? (days - daysToRemove) : 0;
    }

    private void printRentalAgreement(RentalAgreement rentalAgreement, String errMsg) {
        if (StringUtils.isBlank(errMsg)) {
            System.out.println("Tool code: " + rentalAgreement.getToolCode().toUpperCase());
            System.out.println("Tool type: " + rentalAgreement.getToolType());
            System.out.println("Tool brand: " + rentalAgreement.getToolBrand());
            System.out.println("Rental days: " + rentalAgreement.getRentalDays());
            System.out.println("Check out date: " + rentalAgreement.getCheckOutDate().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
            System.out.println("Due date: " + rentalAgreement.getCheckOutDate().plusDays(rentalAgreement.getRentalDays()).format(DateTimeFormatter.ofPattern("dd/MM/yy")));
            System.out.println("Daily rental charge: " + rentalAgreement.getDailyCharge());
            System.out.println("Charge days: " + rentalAgreement.getRentalDays());
            System.out.println("Pre-discount charge: " + rentalAgreement.getPreDiscountCharge());
            System.out.println("Discount percent: " + CalculationHelper.formatPercentage(rentalAgreement.getDiscountPercent(), 0));
            System.out.println("Discount amount: " + CalculationHelper.formatCurrency(rentalAgreement.getDiscountAmount()));
            System.out.println("Final charge: " + CalculationHelper.formatCurrency(rentalAgreement.getFinalCharge()));
        } else {
            System.out.print(errMsg);
        }
    }
}
