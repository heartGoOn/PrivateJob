package com.lf.ninghaisystem.bean.entity;

import com.bigkoo.pickerview.model.IPickerViewData;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

/**
 * Created by admin on 2017/12/4.
 */

public class DutyType implements IPickerViewData{

    private int typeId;
    private String typeName;

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String getPickerViewText() {
        return this.typeName;
    }
}
