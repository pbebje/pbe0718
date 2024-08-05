package com.tool.rental.dao;

import com.tool.rental.beans.RentalAgreement;
import com.tool.rental.beans.Tool;
import com.tool.rental.beans.ToolCharge;
import com.tool.rental.beans.ToolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ToolDataDaoImpl implements ToolDataDao {

    private static final Logger _logger = LoggerFactory.getLogger(ToolDataDaoImpl.class);

    /*
     * This Method is used to return RentalAgreement object by a single tool code value
     * @param toolCode - String value of a Tool Code, query is case-insensitive
     * @return - RentalAgreement object with Tool setup data, could return null
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public RentalAgreement getRentalAgreementData(String toolCode) throws Exception {
        _logger.info("Starting getRentalAgreementData(String) DAO call.");
        RentalAgreement rentalAgreement = null;
        try {
            Connection conn = getConnection();
            String sql = "Select a.tool_code, c.tool_type, a.brand, b.daily_charge_amount, " +
                    "b.is_weekday_charge, b.is_weekend_charge, b.is_holiday_charge " +
                    "From tools a Join tool_charges b On a.tool_type_id = b.tool_type_id " +
                    "Join tool_types c On a.tool_type_id = c.id " +
                    "Where Lower(a.tool_code) = Lower(?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, toolCode);
            _logger.debug("getRentalAgreementData(String) SQL statement: " + sql + " param value = " + toolCode);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rentalAgreement = new RentalAgreement();
                rentalAgreement.setDailyCharge(rs.getDouble("daily_charge_amount"));
                rentalAgreement.setToolCode(rs.getString("tool_code"));
                rentalAgreement.setToolType(rs.getString("tool_type"));
                rentalAgreement.setToolBrand(rs.getString("brand"));
                rentalAgreement.setWeekdayCharge(rs.getBoolean("is_weekday_charge"));
                rentalAgreement.setWeekendCharge(rs.getBoolean("is_weekend_charge"));
                rentalAgreement.setHolidayCharge(rs.getBoolean("is_holiday_charge"));
                break; // tool_code should be unique, added here for sanity check
            }
        } catch (Exception e) {
            _logger.error("getRentalAgreementData(String) SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error connecting to Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("Ending getRentalAgreementData(String) DAO call.");
        return rentalAgreement;
    }

    /*
     * This Method is used to return a Tool object by a single PK value
     * @param id - int value of PK
     * @return - Tool object by PK value, could return null
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public Tool getToolById(int id) throws Exception {
        _logger.info("Starting getToolById(int) DAO call.");
        Tool tool = null;
        try {
            Connection conn = getConnection();
            String sql = "Select id, tool_code, tool_type_id, brand from tools Where id = ?";
            _logger.debug("getToolById(int) SQL statement: " + sql + " param value = " + id);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                tool = new Tool();
                tool.setId(rs.getInt("id"));
                tool.setToolCode(rs.getString("tool_code"));
                tool.setToolTypeId(rs.getInt("tool_type_id"));
                tool.setBrand(rs.getString("brand"));
                break; // tool_code should be unique, added here for sanity check
            }
        } catch (Exception e) {
            _logger.error("getToolById(int) SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error connecting to Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End getToolById() DAO call.");
        return tool;
    }

    /*
     * This Method is used to return a Tool object by a single tool code value.
     * @param toolCode - String value of a Tool Code, query is case-insensitive
     * @return - Tool object by that Tool Code, could return null
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public Tool getToolByCode(String toolCode) throws Exception {
        _logger.info("Starting getToolByCode(String) DAO call.");
        Tool tool = null;
        try {
            Connection conn = getConnection();
            String sql = "Select id, tool_code, tool_type_id, brand from tools Where Lower(tool_code) = Lower(?)";
            _logger.debug("getToolByCode(String) SQL statement: " + sql + " param value = " + toolCode);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, toolCode);
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                tool = new Tool();
                tool.setId(rs.getInt("id"));
                tool.setToolCode(rs.getString("tool_code"));
                tool.setToolTypeId(rs.getInt("tool_type_id"));
                tool.setBrand(rs.getString("brand"));
                break; // tool_code should be unique, added here for sanity check
            }
        } catch (Exception e) {
            _logger.error("getToolByCode(String) SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error connecting to Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End getToolByCode(String) DAO call.");
        return tool;
    }

    /*
     * This Method is used to return all Tools.
     * @return - List<Tool> of all Tools, could be an empty List but will NOT return null
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public List<Tool> getAllTools() throws Exception {
        _logger.info("Starting getAllTools() DAO call.");
        List<Tool> retList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "Select id, tool_code, tool_type_id, brand from tools";
            _logger.debug("getAllTools() SQL statement: " + sql);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Tool tool = new Tool();
                tool.setId(rs.getInt("id"));
                tool.setToolCode(rs.getString("tool_code"));
                tool.setToolTypeId(rs.getInt("tool_type_id"));
                tool.setBrand(rs.getString("brand"));
                retList.add(tool);
            }
        } catch (Exception e) {
            _logger.error("getAllTools() SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error connecting to Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End getAllTools() DAO call.");
        return retList;
    }

    /*
     * This Method is used to return all Tools by a single type value. Method is null safe.
     * @param type - String value of a Tool Type, query is case-insensitive
     * @return - List<Tool> of all Tools by that Tool Type, could be an empty List but will NOT return null
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public List<Tool> getAllToolsByType(String type) throws Exception {
        _logger.info("Starting getAllToolsByType(String) DAO call.");
        List<Tool> retList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "Select a.id, a.tool_code, a.tool_type_id, a.brand From tools a " +
                    "Join tool_types b On a. tool_type_id = b.id Where Lower(b.tool_type) = Lower(?)";
            _logger.debug("getAllToolsByType(String) SQL statement: " + sql + " param value = " + type);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                Tool tool = new Tool();
                tool.setId(rs.getInt("id"));
                tool.setToolCode(rs.getString("tool_code"));
                tool.setToolTypeId(rs.getInt("tool_type_id"));
                tool.setBrand(rs.getString("brand"));
                retList.add(tool);
            }
        } catch (Exception e) {
            _logger.error("getAllToolsByType(String) SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error connecting to Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End getAllToolsByType(String) DAO call.");
        return retList;
    }

    /*
     * This Method is used to return all Tools by a single brand value. Method is null safe.
     * @param brand - String value of a Tool Brand, query is case-insensitive
     * @return - List<Tool> of all Tools by that Tool Brand, could be an empty List but will NOT return null
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public List<Tool> getAllToolsByBrand(String brand) throws Exception {
        _logger.info("Starting getAllToolsByBrand(String) DAO call.");
        List<Tool> retList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "Select id, tool_code, tool_type_id, brand From tools Where Lower(brand) = Lower(?)";
            _logger.debug("getAllToolsByBrand(String) SQL statement: " + sql + " param value = " + brand);
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, brand);
            ResultSet rs = ps.executeQuery(sql);
            while (rs.next()) {
                Tool tool = new Tool();
                tool.setId(rs.getInt("id"));
                tool.setToolCode(rs.getString("tool_code"));
                tool.setToolTypeId(rs.getInt("tool_type_id"));
                tool.setBrand(rs.getString("brand"));
                retList.add(tool);
            }
        } catch (Exception e) {
            _logger.error("getAllToolsByBrand(String) SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error connecting to Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End getAllToolsByBrand(String) DAO call.");
        return retList;
    }

    /*
     * This Method inserts a new Tool
     * NOTE!!! PK field must be null so PK value will be auto generated by DBMS. It is the responsibility of the
     *         database architect to implement correct PK auto generation per the DBMS used.
     * @param tool - Tool object with data
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public void insertTool(Tool tool) throws Exception {
        _logger.info("Starting insertTool(Tool) DAO call.");
        try {
            Connection conn = getConnection();
            String sql = "Insert into tools(tool_code,tool_type_id,brand) Values(?,?,?)";
            _logger.debug("insertTool(Tool) SQL statement: " + sql + " param value = " + tool.getToolCode() + " param value = " + tool.getToolTypeId()+ " param value = " + tool.getBrand());
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tool.getToolCode());
            ps.setInt(2, tool.getToolTypeId());
            ps.setString(3, tool.getBrand());
            ps.executeUpdate(sql);
        } catch (Exception e) {
            _logger.error("insertTool(Tool)  SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error deleting Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End insertTool(Tool) DAO call.");
    }

    /*
     * This Method updates a Tool by a single PK value
     * NOTE!!! All fields are updated (except PK) on each call so unchanged data must also be populated
     * @param tool - Tool object with data
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public void updateTool(Tool tool) throws Exception {
        _logger.info("Starting updateTool(Tool) DAO call.");
        try {
            Connection conn = getConnection();
            String sql = "Update tools Set tool_code = ?, tool_type_id = ?, brand = ? Where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, tool.getToolCode());
            ps.setInt(2, tool.getToolTypeId());
            ps.setString(3, tool.getBrand());
            ps.setInt(4, tool.getId());
            _logger.debug("updateTool(Tool) SQL statement: " + sql + " param value = " + tool.getToolCode() + " param value = " + tool.getToolTypeId() + " param value = " + tool.getBrand() + " param value = " + tool.getId());
            ps.executeUpdate(sql);
        } catch (Exception e) {
            _logger.error("updateTool(Tool)  SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error updating Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End updateTool(Tool)  DAO call.");
    }

    /*
     * This Method deletes a Tool by a single PK value
     * @param id - int value of PK
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public void deleteTool(int id) throws Exception {
        _logger.info("Starting deleteTool(int) DAO call.");
        try {
            Connection conn = getConnection();
            String sql = "Delete from tools Where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            _logger.debug("deleteTool(Tool) SQL statement: " + sql + " param value = " + id);
            ps.executeUpdate(sql);
        } catch (Exception e) {
            _logger.error("deleteTool(int)  SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error deleting Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End deleteTool(int)  DAO call.");
    }

    /*
     * This Method inserts a new ToolType
     * NOTE!!! PK field must be null so PK value will be auto generated by DBMS. It is the responsibility of the
     *         database architect to implement correct PK auto generation per the DBMS used.
     * @param toolType - ToolType object with data
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public void insertToolType(ToolType toolType) throws Exception {
        _logger.info("Starting insertToolType(ToolType) DAO call.");
        try {
            Connection conn = getConnection();
            String sql = "Insert into tool_types(tool_type) Values(?)";
            _logger.debug("insertToolType(ToolType) SQL statement: " + sql + " param value = " + toolType.getToolType());
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, toolType.getToolType());
            ps.executeUpdate(sql);
        } catch (Exception e) {
            _logger.error("insertToolType(ToolType)  SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error inserting new Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End insertToolType(ToolType) DAO call.");
    }

    /*
     * This Method updates a ToolType by a single PK value
     * NOTE!!! All fields are updated (except PK) on each call so unchanged data must also be populated
     * @param toolType - ToolType object with data
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public void updateToolType(ToolType toolType) throws Exception {
        _logger.info("Starting updateToolType(ToolType) DAO call.");
        try {
            Connection conn = getConnection();
            String sql = "Update tool_types Set tool_type = ? Where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, toolType.getToolType());
            ps.setInt(2, toolType.getId());
            _logger.debug("updateToolType(ToolType) SQL statement: " + sql + " param value = " + toolType.getToolType() + " param value = " + toolType.getId());
            ps.executeUpdate(sql);
        } catch (Exception e) {
            _logger.error("updateToolType(ToolType)  SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error updating Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End updateToolType(ToolType)  DAO call.");
    }

    /*
     * This Method deletes a ToolType by a single PK value
     * @param id - int value of PK
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public void deleteToolType(int id) throws Exception {
        _logger.info("Starting deleteToolType(int) DAO call.");
        try {
            Connection conn = getConnection();
            String sql = "Delete from tool_types Where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate(sql);
        } catch (Exception e) {
            _logger.error("deleteToolType(int)  SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error deleting Tool Type data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End deleteToolType(int)  DAO call.");
    }

    /*
     * This Method inserts a new ToolCharge
     * NOTE!!! PK field must be null so PK value will be auto generated by DBMS. It is the responsibility of the
     *         database architect to implement correct PK auto generation per the DBMS used.
     * @param toolCharge - ToolCharge object with data
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public void insertToolCharge(ToolCharge toolCharge) throws Exception {
        _logger.info("Starting insertToolCharge(ToolCharge) DAO call.");
        try {
            Connection conn = getConnection();
            String sql = "Insert into tool_charges(tool_type_id,daily_charge_amount,is_weekday_charge,is_weekend_charge,is_holiday_charge) Values(?,?,?,?,?)";
            _logger.debug("insertToolCharge(ToolCharge) SQL statement: " + sql + " param value = " + toolCharge.getToolTypeId() +
                    " param value = " + toolCharge.getDailyChargeAmount() + " param value = " + toolCharge.isWeekdayCharge() + " param value = "
                    + toolCharge.isWeekendCharge() + " param value = " + toolCharge.isHolidayCharge());
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, toolCharge.getToolTypeId());
            ps.setDouble(2, toolCharge.getDailyChargeAmount());
            ps.setInt(3, toolCharge.isWeekdayCharge() ? 1 : 0);
            ps.setInt(4, toolCharge.isWeekendCharge() ? 1 : 0);
            ps.setInt(5, toolCharge.isHolidayCharge() ? 1 : 0);
            ps.executeUpdate(sql);
        } catch (Exception e) {
            _logger.error("insertToolCharge(ToolCharge)  SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error inserting new Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End insertToolCharge(ToolCharge) DAO call.");
    }

    /*
     * This Method updates a ToolCharge by a single PK value
     * NOTE!!! All fields are updated (except PK) on each call so unchanged data must also be populated
     * @param toolCharge - ToolCharge object with data
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public void updateToolCharge(ToolCharge toolCharge) throws Exception {
        _logger.info("Starting updateToolCharge(ToolCharge) DAO call.");
        try {
            Connection conn = getConnection();
            String sql = "Update tool_charges Set tool_type_id = ?, daily_charge_amount = ?, is_weekday_charge = ?," +
                    " is_weekend_charge = ?, is_holiday_charge = ? Where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, toolCharge.getToolTypeId());
            ps.setDouble(2, toolCharge.getDailyChargeAmount());
            ps.setInt(3, toolCharge.isWeekdayCharge() ? 1 : 0);
            ps.setInt(4, toolCharge.isWeekendCharge() ? 1 : 0);
            ps.setInt(5, toolCharge.isHolidayCharge() ? 1 : 0);
            ps.setInt(6, toolCharge.getId());
            _logger.debug("updateToolCharge(ToolCharge) SQL statement: " + sql + " param value = " + toolCharge.getToolTypeId() +
                    " param value = " + toolCharge.getDailyChargeAmount() + " param value = " + toolCharge.isWeekdayCharge() +
                    " param value = " + toolCharge.isWeekendCharge() +  " param value = " + toolCharge.isHolidayCharge() + " param value = " + toolCharge.getId());
            ps.executeUpdate(sql);
        } catch (Exception e) {
            _logger.error("updateToolCharge(ToolCharge)  SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error updating Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End updateToolCharge(ToolCharge)  DAO call.");
    }

    /*
     * This Method deletes a ToolCharge by a single PK value
     * @param id - int value of PK
     * @throws - Exception, returns user-friendly message to be displayed from calling class
     */
    @Override
    public void deleteToolCharge(int id) throws Exception {
        _logger.info("Starting deleteToolCharge(int) DAO call.");
        try {
            Connection conn = getConnection();
            String sql = "Delete from tool_charges Where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate(sql);
        } catch (Exception e) {
            _logger.error("deleteToolCharge(int)  SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error deleting Tool Charge data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End deleteToolCharge(int)  DAO call.");
    }

    /*
     * This Method is used to make a connection to a database. Database connection info comes from a properties file
     * so database backend can be switched without recompiling Java source
     * @return -Connection object
     * @throws - IOExceptions if properties file is not found or cannot be read, SQLException if Database cannot be
     *           connected to. Returns user-friendly message to be displayed from calling class.
     */
    private Connection getConnection() throws Exception{
        _logger.info("Starting getConnection DAO call.");
        Connection conn;
        try {
//            InputStream input = new FileInputStream("src/main/resources/db.properties");
            InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties");
            Properties prop = new Properties();
            prop.load(input);
            String driver = prop.getProperty("jdbc.driver");
            //  Not all databases require a driver, for example sqlite
            if (driver != null) {
                Class.forName(driver) ;
            }
            conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("dbuser"), prop.getProperty("dbpassword"));
        } catch (IOException e) {
            _logger.error("getConnection DAO call IOException - " + e.getMessage());
            Exception ex = new Exception("There was a problem connection to the properties file defining the database connection. Please call customer support number in your software support contract.");
            throw ex;
        } catch (SQLException e) {
            _logger.error("getConnection DAO call SQLException - " + e.getMessage());
            Exception ex = new Exception("There was an error connecting to Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End getConnection DAO call.");
        return conn;
    }
}
