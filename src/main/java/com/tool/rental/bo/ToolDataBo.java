package com.tool.rental.bo;

import com.tool.rental.beans.RentalAgreement;
import com.tool.rental.beans.Tool;

import java.util.List;

public interface ToolDataBo {
    List<Tool> getAllTools() throws Exception;
    RentalAgreement getRentalAgreementData(String toolCode) throws Exception;

}
