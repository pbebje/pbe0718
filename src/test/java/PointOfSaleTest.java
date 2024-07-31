import com.tool.rental.PointOfSale;
import com.tool.rental.beans.RentalAgreement;
import com.tool.rental.helpers.CalculationHelper;
import com.tool.rental.helpers.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PointOfSaleTest {
    PointOfSale pos = new PointOfSale();
    @Test
    // Test 1 on document
    public void testPercentExcepton() {
        // Sept 2015 has labor day on 09/07/2015
        // Exceptions should be thrown for invalid Discount
        Exception ex = assertThrows(Exception.class, () -> pos.calcRentalAgreement("JAKR", "09/03/15", "5", "101"));
        assertTrue(ex.getMessage().contains(Constants.DISCOUNT_ERR_MSG));
    }

    @Test
    // Test 2 on document
    public void testLadderFourthOnSaturdayWithDiscount() {
        // 07/04/2020 the fourth is on Saturday
        // Ladder has Weekend Charge True and Holiday charge False
        // 07/02 - Thursday, 07/03 Friday, 07/04 - Saturday
        // days of charge should be 2 as the Fourth of July is actually observed July 3
        // Chargeable days in list: 07/02 - Thursday, 09/04 - Saturday
        // Pre-discount charge is 1.99 * 2 = 3.98
        // Discount amount = 3.98 * .10 = 39.8 round half up to .40
        // Final charge is 3.98  - .40 = 3.58
        // NOTE using RoundingMode.HALF_UP so .5 always rounds up
        // (2.5 rounds to 3, 1.5 rounds to 2, -1.5 rounds to -2, -2.5 rounds to -3)
        try {
            RentalAgreement rentalAgreement = pos.calcRentalAgreement("LADW", "07/02/20", "3", "10");
            assertEquals(rentalAgreement.getRentalDays(), 2);
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getPreDiscountCharge(), 2), "3.98");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getDiscountAmount(), 2), ".40");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getFinalCharge(), 2), "3.58");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    // Test 3 on document
    public void testChainsawFourthOnSaturdayWithDiscount() {
        // 07/04/2015 the fourth is on Saturday
        // Chainsaw has Weekend Charge False and Holiday charge True
        // 07/02 - Thursday, 07/03 Friday, 07/04 - Saturday, 07/05 - Sunday, 07/06 - Monday
        // days of charge should be 3 as the Fourth of July is actually observed July 3 and is chargeable
        // weekend days are NOT chargeable
        // Chargeable days in list: 07/02 - Thursday, 07/06 - Monday, 07/07 - Tuesday, 07/08 - Wednesday, 07/09 - Thursday, 07/10 Friday
        // Pre-discount charge is 1.49 * 3 = 4.47
        // Discount amount = 4.47 * .25 = 1.1175 round half up to 1.12
        // Final charge is 4.47 - 1.12 = 3.35
        // NOTE using RoundingMode.HALF_UP so .5 always rounds up
        // (2.5 rounds to 3, 1.5 rounds to 2, -1.5 rounds to -2, -2.5 rounds to -3)
        try {
            RentalAgreement rentalAgreement = pos.calcRentalAgreement("CHNS", "07/02/15", "5", "25");
            assertEquals(rentalAgreement.getRentalDays(), 3);
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getPreDiscountCharge(), 2), "4.47");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getDiscountAmount(), 2), "1.12");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getFinalCharge(), 2), "3.35");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    // Test 4 on document
    public void testJackhammerLaborDay() {
        // Sept 2015 has labor day on 09/07/2015
        // Jackhammer has Weekend Charge False and Holiday charge False
        // 09/03 - Thursday, 09/04 Friday, 09/05 - Saturday, 09/06 - Sunday, 09/07 - Monday, 09/08 - Tuesday
        // days of charge should be 3 as labor Day is Monday 09/07/2015 and is NOT chargeable
        // weekend days are NOT chargeable
        // Chargeable days in list: 09/03 - Thursday, 09/04 Friday, 09/08 - Tuesday
        // Pre-discount charge is 2.99 * 3 = 8.97
        // Discount amount = 9.97 * .0 = 0
        // Final charge is 9.97 - 0 = 8.97
        // NOTE using RoundingMode.HALF_UP so .5 always rounds up
        // (2.5 rounds to 3, 1.5 rounds to 2, -1.5 rounds to -2, -2.5 rounds to -3)
        try {
            RentalAgreement rentalAgreement = pos.calcRentalAgreement("JAKD", "09/03/15", "6", "0");
            assertEquals(rentalAgreement.getRentalDays(), 3);
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getPreDiscountCharge(), 2), "8.97");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getDiscountAmount(), 2), ".00");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getFinalCharge(), 2), "8.97");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    // Test 5 on document
    public void testJackhammerFourthOnSaturdayLongerThanWeek() {
        // 07/04/2015 the fourth is on Saturday
        // Jackhammer has Weekend Charge False and Holiday charge False
        // 07/02 - Thursday, 07/03 Friday, 07/04 - Saturday, 07/05 - Sunday, 07/06 - Monday,
        // 07/07 - Tuesday, 07/08 - Wednesday, 07/09 - Thursday, 07/10 Friday
        // days of charge should be 6 as the Fourth of July is actually observed July 3 and is NOT chargeable
        // weekend days are NOT chargeable.
        // Chargeable days in list: 07/02 - Thursday, 07/06 - Monday, 07/07 - Tuesday, 07/08 - Wednesday, 07/09 - Thursday, 07/10 Friday
        // Pre-discount charge is 2.99 * 6 = 17.94
        // Discount amount = 17.94 * .0 = 0
        // Final charge is 19.94 - 0 = 17.94
        // NOTE using RoundingMode.HALF_UP so .5 always rounds up
        // (2.5 rounds to 3, 1.5 rounds to 2, -1.5 rounds to -2, -2.5 rounds to -3)
        try {
            RentalAgreement rentalAgreement = pos.calcRentalAgreement("JAKR", "07/02/15", "9", "0");
            assertEquals(rentalAgreement.getRentalDays(), 6);
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getPreDiscountCharge(), 2), "17.94");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getDiscountAmount(), 2), ".00");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getFinalCharge(), 2), "17.94");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    // Test 6 on document
    public void testJackhammerFourthOnSaturdayWithDiscount() {
        // 07/04/2020 the fourth is on Saturday
        // Jackhammer has Weekend Charge False and Holiday charge False
        // 07/02 - Thursday, 07/03 Friday, 07/04 - Saturday, 07/05 - Sunday
        // days of charge should be 1 as the Fourth of July is actually observed July 3 and is chargeable
        // weekend days are NOT chargeable
        // Chargeable days in list: 07/02 - Thursday
        // Pre-discount charge is 2.99 * 1 = 2.99
        // Discount amount = 2.99 * .5 = 1.495 round half up to 1.50
        // Final charge is 2.99 - 1.5 = 1.49
        // NOTE using RoundingMode.HALF_UP so .5 always rounds up
        // (2.5 rounds to 3, 1.5 rounds to 2, -1.5 rounds to -2, -2.5 rounds to -3)
        try {
            RentalAgreement rentalAgreement = pos.calcRentalAgreement("JAKR", "07/02/20", "4", "50");
            assertEquals(rentalAgreement.getRentalDays(), 1);
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getPreDiscountCharge(), 2), "2.99");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getDiscountAmount(), 2), "1.50");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getFinalCharge(), 2), "1.49");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    // Extra test
    public void testChainsawFourthOnSunday() {
        // 07/04/2021 the fourth is on Sunday
        // Chainsaw has Weekend Charge False and Holiday charge True
        // 07/02 - Thursday, 07/03 Friday, 07/04 - Saturday, 07/05 - Sunday, 07/06 - Monday
        // days of charge should be 3 as the Fourth of July is actually observed July 6 and is chargeable
        // weekend days are NOT chargeable
        // Chargeable days in list - 07/02 - Thursday, 07/03 Friday, 07/06 - Monday
        // Pre-discount charge is 1.49 * 3 = 4.47
        // Discount amount = 4.47 * 0 = 0
        // Final charge is 4.47 - 0 = 4.47
        // NOTE using RoundingMode.HALF_UP so .5 always rounds up
        // (2.5 rounds to 3, 1.5 rounds to 2, -1.5 rounds to -2, -2.5 rounds to -3)
        try {
            RentalAgreement rentalAgreement = pos.calcRentalAgreement("CHNS", "07/02/21", "5", "0");
            assertEquals(rentalAgreement.getRentalDays(), 3);
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getPreDiscountCharge(), 2), "4.47");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getDiscountAmount(), 2), ".00");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getFinalCharge(), 2), "4.47");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    // Extra test
    public void testNoChargeableDays() {
        // 07/04/2020 the fourth is on Saturday
        // Jackhammer has Weekend Charge False and Holiday charge False
        // 07/03 Friday, 07/04 - Saturday, 07/04 - Thursday
        // days of charge should be 0 as the Fourth of July is actually observed July 3 and
        // both Holidays and Weekends are NOT chargeable
        // Pre-discount charge is 2.99 * 0 = .00
        // Discount amount = .00 * .00 = .00
        // Final charge is .00
        // NOTE using RoundingMode.HALF_UP so .5 always rounds up
        // (2.5 rounds to 3, 1.5 rounds to 2, -1.5 rounds to -2, -2.5 rounds to -3)
        try {
            RentalAgreement rentalAgreement = pos.calcRentalAgreement("JAKR", "07/03/20", "3", "0");
            assertEquals(rentalAgreement.getRentalDays(), 0);
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getPreDiscountCharge(), 2), ".00");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getDiscountAmount(), 2), ".00");
            assertEquals(CalculationHelper.formatDecimal(rentalAgreement.getFinalCharge(), 2), ".00");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    // Extra test
    public void testChainsawAllInputBadData() {
        // Valid tool code but all input data is invalid
        // Exceptions should be thrown for all validations
        Exception ex = assertThrows(Exception.class, () -> pos.calcRentalAgreement("CHNS", "bhvbcx", "bnvbx", "hfhgfhfg"));
        assertTrue(ex.getMessage().contains(Constants.DISCOUNT_ERR_MSG));
        assertTrue(ex.getMessage().contains(Constants.CHECKOUT_DATE_ERR_MSG));
        assertTrue(ex.getMessage().contains(Constants.NUM_RENTAL_DAYS_ERR_MSG));
    }

    @Test
    // Extra test
    public void testInvalidToolCode() {
        // Invalid tool code but all input data is valid
        // Null RentalAgreement object should be returned
        try {
            RentalAgreement rentalAgreement = pos.calcRentalAgreement("CHNS213", "07/02/20", "4", "0");
            assertNull(rentalAgreement);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
