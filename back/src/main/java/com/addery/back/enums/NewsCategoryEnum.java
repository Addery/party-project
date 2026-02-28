package com.addery.back.enums;

import lombok.Getter;

@Getter
public enum NewsCategoryEnum {
    policy("时政要闻"),
    party_building("党建动态"),
    organization("组织建设"),
    technology("科技创新"),
    party_discipline("党风廉政"),
    grassroots_party("基层党建"),
    law("法律法规"),
    economy("经济发展"),
    health("卫生健康"),
    society("社会民生"),
    environment("环境保护"),
    education("教育工作"),
    culture("文化动态"),
    sports("体育新闻"),
    safety("安全警示");


    private final String displayName;

    NewsCategoryEnum(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 根据字符串值（英文名称）获取枚举对象
     * @param value 字符串值
     * @return 枚举对象，如果不存在则返回null
     */
    public static NewsCategoryEnum fromValue(String value) {
        for (NewsCategoryEnum category : NewsCategoryEnum.values()) {
            if (category.name().equals(value)) {
                return category;
            }
        }
        return null;
    }
    
    /**
     * 根据中文显示名称获取枚举对象
     * @param displayName 中文显示名称
     * @return 枚举对象，如果不存在则返回null
     */
    public static NewsCategoryEnum fromDisplayName(String displayName) {
        for (NewsCategoryEnum category : NewsCategoryEnum.values()) {
            if (category.getDisplayName().equals(displayName)) {
                return category;
            }
        }
        return null;
    }

    /**
     * 获取所有分类的字符串值
     * @return 分类字符串数组
     */
    public static String[] getAllValues() {
        String[] values = new String[NewsCategoryEnum.values().length];
        for (int i = 0; i < NewsCategoryEnum.values().length; i++) {
            values[i] = NewsCategoryEnum.values()[i].getDisplayName();
        }
        return values;
    }
}