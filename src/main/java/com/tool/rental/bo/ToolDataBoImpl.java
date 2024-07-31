package com.tool.rental.bo;

import com.tool.rental.beans.RentalAgreement;
import com.tool.rental.beans.Tool;
import com.tool.rental.dao.ToolDataDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ToolDataBoImpl implements ToolDataBo {
    private ToolDataDao toolDataDao;

    @Autowired
    public ToolDataBoImpl(ToolDataDao toolDataDao) {
        this.toolDataDao = toolDataDao;
    }

    public List<Tool> getAllTools() throws Exception {
        return toolDataDao.getAllTools();
    }

    @Override
    public RentalAgreement getRentalAgreementData(String toolCode) throws Exception {
        return toolDataDao.getRentalAgreementData(toolCode);
    }
}
