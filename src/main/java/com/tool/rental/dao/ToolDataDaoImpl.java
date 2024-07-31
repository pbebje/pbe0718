package com.tool.rental.dao;

import com.tool.rental.beans.RentalAgreement;
import com.tool.rental.beans.Tool;
import com.tool.rental.beans.ToolCharge;
import com.tool.rental.beans.ToolType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
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

    @Override
    public List<Tool> getAllTools() throws Exception {_logger.info("Starting getToolById(int) DAO call.");
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

    @Override
    public List<Tool> getAllToolsByType(String type) throws Exception {
        _logger.info("Starting getAllToolsByType(String) DAO call.");
        List<Tool> retList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "Select a.id, a.tool_code, a.tool_type_id, a.brand From tools a " +
                    "Join tool_types b On a. tool_type_id = b.id Where Lower(b.tool_type) = Lower(?)";
            _logger.debug("getAllToolsByType(String) SQL statement: " + sql + " param value = " + type);
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
            _logger.error("getAllToolsByType(String) SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error connecting to Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End getAllToolsByType(String) DAO call.");
        return retList;
    }

    @Override
    public List<Tool> getAllToolsByBrand(String brand) throws Exception {
        _logger.info("Starting getAllToolsByBrand(String) DAO call.");
        List<Tool> retList = new ArrayList<>();
        try {
            Connection conn = getConnection();
            String sql = "Select id, tool_code, tool_type_id, brand From tools Where Lower(brand) = Lower(?)";
            _logger.debug("getAllToolsByBrand(String) SQL statement: " + sql + " param value = " + brand);
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
            _logger.error("getAllToolsByBrand(String) SQL error: " + e.getMessage());
            Exception ex = new Exception("There was an error connecting to Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        _logger.info("End getAllToolsByBrand(String) DAO call.");
        return retList;
    }

    @Override
    public void insertTool(Tool tool) throws Exception {

    }

    @Override
    public void updateTool(Tool tool) throws Exception {

    }

    @Override
    public void deleteTool(int id) throws Exception {
        Connection conn = getConnection();
        String sql = "Delete from tools Where id = ?";
    }

    @Override
    public void insertToolType(ToolType toolType) throws Exception {

    }

    @Override
    public void updateToolType(ToolType toolType) throws Exception {

    }

    @Override
    public void deleteToolType(int id) throws Exception {

    }

    @Override
    public void insertToolCharge(ToolCharge toolCharge) throws Exception {

    }

    @Override
    public void updateToolCharge(ToolCharge toolCharge) throws Exception {

    }

    @Override
    public void deleteToolCharge(int id) throws Exception {

    }

    private Connection getConnection() throws Exception{
        Connection conn;
        try {
            InputStream input = new FileInputStream("src/main/resources/db.properties");
            Properties prop = new Properties();
            prop.load(input);
            String driver = prop.getProperty("jdbc.driver");
            if (driver != null) {
                Class.forName(driver) ;
            }
            conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("dbuser"), prop.getProperty("dbpassword"));
        } catch (IOException e) {
            Exception ex = new Exception("There was a problem connection to the properties file defining the database connection. Please call customer support number in your software support contract.");
            throw ex;
        } catch (SQLException e) {
            Exception ex = new Exception("There was an error connecting to Tool data. Please call customer support number in your software support contract.");
            throw ex;
        }
        return conn;
    }
}
