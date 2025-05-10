package com.zllUserCenter.findfriendbackend.model.enums;


/**
 * 队伍状态枚举
 */
public enum TeamStatusEnum {

    PUBLIC(1,"公开"),
    PRIVATE(2,"私有"),
    SECRET(3,"加密");

    private int value;

    private String text;

    public static TeamStatusEnum getTeamEnumValue(Integer value){
        if(value == null){
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        // 快捷键 iter
        for (TeamStatusEnum teamStatusEnum : values) {
            if(teamStatusEnum.getValue() == value){
                return teamStatusEnum;
            }
        }
        return null;
    }

    TeamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
