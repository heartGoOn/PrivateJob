package com.lf.ninghaisystem.bean.entity;

import java.io.Serializable;

/**
 * Created by admin on 2017/11/23.
 */

public class PjWordType implements Serializable{

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
}
