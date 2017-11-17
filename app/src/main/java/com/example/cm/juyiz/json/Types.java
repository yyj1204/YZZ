package com.example.cm.juyiz.json;
/*
首页---水平菜单
 */
public class Types {
    private int typeId;
    private String typeName;

    public Types() {
    }

    public Types(int typeId, String typeName) {
        this.typeId = typeId;
        this.typeName = typeName;
    }

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
