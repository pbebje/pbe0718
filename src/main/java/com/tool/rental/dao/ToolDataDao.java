package com.tool.rental.dao;

import com.tool.rental.beans.RentalAgreement;
import com.tool.rental.beans.Tool;
import com.tool.rental.beans.ToolCharge;
import com.tool.rental.beans.ToolType;

import java.util.List;

public interface ToolDataDao {
    RentalAgreement getRentalAgreementData(String toolCode) throws Exception;
    Tool getToolById(int id) throws Exception;
    Tool getToolByCode(String code) throws Exception;
    List<Tool> getAllTools() throws Exception;
    List<Tool> getAllToolsByType(String type) throws Exception;
    List<Tool> getAllToolsByBrand(String brand) throws Exception;
    void insertTool(Tool tool) throws Exception;
    void updateTool(Tool tool) throws Exception;
    void deleteTool(int id) throws Exception;
    void insertToolType(ToolType toolType) throws Exception;
    void updateToolType(ToolType toolType) throws Exception;
    void deleteToolType(int id) throws Exception;
    void insertToolCharge(ToolCharge toolCharge) throws Exception;
    void updateToolCharge(ToolCharge toolCharge) throws Exception;
    void deleteToolCharge(int id) throws Exception;

}
