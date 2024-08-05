package com.tool.rental;

import com.tool.rental.beans.RentalAgreement;
import com.tool.rental.bo.ToolDataBoImpl;
import com.tool.rental.dao.ToolDataDaoImpl;
import com.tool.rental.helpers.CalculationHelper;
import com.tool.rental.helpers.Constants;
import com.tool.rental.helpers.DateHelpers;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.tool.rental.helpers.Constants.NEW_LINE;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class PointOfSale {

    private static final Logger _logger = LoggerFactory.getLogger(PointOfSale.class);

    public static void main(String[] args) {
        PointOfSale pos = new PointOfSale();
        String errStr = null;
        RentalAgreement rentalAgreement = null;
        try {
            rentalAgreement = pos.calcRentalAgreement("LADW", "07/02/20", "3", "10");
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
        } catch (Exception e) {
            errStr = e.getMessage();
        } finally {
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
            pos.printRentalAgreementConsole(rentalAgreement, errStr);
        }
        errStr = null;
        try {
            rentalAgreement = pos.calcRentalAgreement("CHNS", "07/02/15", "5", "25");
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
        } catch (Exception e) {
            errStr = e.getMessage();
        } finally {
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
            pos.printRentalAgreementConsole(rentalAgreement, errStr);
        }
        errStr = null;
        try {
            rentalAgreement = pos.calcRentalAgreement("JAKD", "09/03/15", "6", "0");
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
        } catch (Exception e) {
            errStr = e.getMessage();
        } finally {
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
            pos.printRentalAgreementConsole(rentalAgreement, errStr);
        }
        errStr = null;
        try {
            rentalAgreement = pos.calcRentalAgreement("JAKR", "07/02/15", "9", "0");
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
        } catch (Exception e) {
            errStr = e.getMessage();
        } finally {
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
            pos.printRentalAgreementConsole(rentalAgreement, errStr);
        }
        errStr = null;
        try {
            rentalAgreement = pos.calcRentalAgreement("JAKR", "07/02/20", "4", "50");
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
        } catch (Exception e) {
            errStr = e.getMessage();
        } finally {
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
            pos.printRentalAgreementConsole(rentalAgreement, errStr);
        }
        errStr = null;
        try {
            rentalAgreement = pos.calcRentalAgreement("CHNS", "07/02/21", "5", "0");
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
        } catch (Exception e) {
            errStr = e.getMessage();
        } finally {
            _logger.info("main code printout " + pos.printRentalAgreement(rentalAgreement, errStr));
            pos.printRentalAgreementConsole(rentalAgreement, errStr);
        }
    }

    public RentalAgreement calcRentalAgreement(String toolCode, String startDate, String numDays, String discount) throws Exception {
        _logger.info("calcRentalAgreement call. toolCode-" + toolCode + " numDays-" + numDays + " discount-" + discount + " startDate-" + startDate);
        ToolDataBoImpl toolDataBo = new ToolDataBoImpl(new ToolDataDaoImpl());
        List<String> errors = new ArrayList<>();
        int days = 0;
        double discountPct = 0d;
        LocalDate checkOutDate = null;
        try {
            // requirement doc says to validate number of days input into screen and NOT the number of chargeable days
            // Chargeable days is calculated after setup for requested tool is found
            days = parseInt(numDays);
            if (days < 1) {
                errors.add(Constants.NUM_RENTAL_DAYS_ERR_MSG);
            }
        } catch (NumberFormatException e) {
            errors.add(Constants.NUM_RENTAL_DAYS_ERR_MSG);
        }
        try {
            discountPct = parseDouble(discount) / 100;
            _logger.info("calcRentalAgreement call.  discount-" + discountPct);
            if (discountPct < 0d || discountPct > 1.0d) {
                _logger.error("calcRentalAgreement call.  discount-" + discountPct);
                errors.add(Constants.DISCOUNT_ERR_MSG);
            }
        } catch (NumberFormatException e) {
            errors.add(Constants.DISCOUNT_ERR_MSG);
        }
        try {
            _logger.info("calcRentalAgreement call.  checkOutDate -" + checkOutDate);
            checkOutDate = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("MM/dd/yy"));
            _logger.info("calcRentalAgreement call.  checkOutDate object-" + checkOutDate);
        } catch (DateTimeParseException e) {
            _logger.error("calcRentalAgreement call. DateTimeParseException - " + e.getMessage());
            errors.add(Constants.CHECKOUT_DATE_ERR_MSG);
        }
        if (errors.size() > 0) {
            String msg = "";
            for (String err : errors) {
                msg += (err + NEW_LINE);
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

    private void printRentalAgreementConsole(RentalAgreement rentalAgreement, String errMsg) {
        if (StringUtils.isBlank(errMsg)) {
            try {
                System.out.println("Tool code: " + rentalAgreement.getToolCode().toUpperCase());
                System.out.println("Tool type: " + rentalAgreement.getToolType());
                System.out.println("Tool brand: " + rentalAgreement.getToolBrand());
                System.out.println("Rental days: " + rentalAgreement.getRentalDays());
                System.out.println("Check out date: " + rentalAgreement.getCheckOutDate().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
                System.out.println("Due date: " + rentalAgreement.getCheckOutDate().plusDays(rentalAgreement.getRentalDays()).format(DateTimeFormatter.ofPattern("dd/MM/yy")));
                System.out.println("Daily rental charge: " + rentalAgreement.getDailyCharge());
                System.out.println("Charge days: " + rentalAgreement.getRentalDays());
                System.out.println("Pre-discount charge: " +  CalculationHelper.formatCurrency(rentalAgreement.getPreDiscountCharge()));
                System.out.println("Discount percent: " + CalculationHelper.formatPercentage(rentalAgreement.getDiscountPercent(), 0));
                System.out.println("Discount amount: " + CalculationHelper.formatCurrency(rentalAgreement.getDiscountAmount()));
                System.out.println("Final charge: " + CalculationHelper.formatCurrency(rentalAgreement.getFinalCharge()));
            } catch (Exception e) {
                _logger.error("printRentalAgreement(RentalAgreement,String) error: " + e.getMessage());
                System.out.print( "There was an error printing the rental Agreement. Please call customer support number in your software support contract." + NEW_LINE);
            }
        } else {
            System.out.print(errMsg);
        }
    }

    private String printRentalAgreement(RentalAgreement rentalAgreement, String errMsg) {
        if (StringUtils.isBlank(errMsg)) {
            try {
                return "Tool code: " + rentalAgreement.getToolCode().toUpperCase() + NEW_LINE + "Tool type: " +
                        rentalAgreement.getToolType() + NEW_LINE + "Tool brand: " + rentalAgreement.getToolBrand() + NEW_LINE +
                        "Rental days: " + rentalAgreement.getRentalDays() + NEW_LINE + "Check out date: " +
                        rentalAgreement.getCheckOutDate().format(DateTimeFormatter.ofPattern("dd/MM/yy")) + NEW_LINE +
                        "Due date: " + rentalAgreement.getCheckOutDate().plusDays(rentalAgreement.getRentalDays()).
                        format(DateTimeFormatter.ofPattern("dd/MM/yy")) + "Daily rental charge: " +
                        rentalAgreement.getDailyCharge() + NEW_LINE + "Charge days: " + rentalAgreement.getRentalDays()  + NEW_LINE +
                        "Pre-discount charge: " +  CalculationHelper.formatCurrency(rentalAgreement.getPreDiscountCharge()) + NEW_LINE + "Discount percent: " +
                        CalculationHelper.formatPercentage(rentalAgreement.getDiscountPercent(), 0) + NEW_LINE +
                        "Discount amount: " + CalculationHelper.formatCurrency(rentalAgreement.getDiscountAmount()) + NEW_LINE +
                        "Final charge: " + CalculationHelper.formatCurrency(rentalAgreement.getFinalCharge());
            } catch (Exception e) {
                _logger.error("printRentalAgreement(RentalAgreement,String) error: " + e.getMessage());
                return "There was an error printing the rental Agreement. Please call customer support number in your software support contract." + NEW_LINE;
            }
        } else {
            return errMsg;
        }
    }
}
