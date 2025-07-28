/**
 * 严肃声明：
 * 开源版本请务必保留此注释头信息，若删除我方将保留所有法律责任追究！
 * 本系统已申请软件著作权，受国家版权局知识产权以及国家计算机软件著作权保护！
 * 可正常分享和学习源码，不得用于违法犯罪活动，违者必究！
 * Copyright (c) 2019-2020 十三 all rights reserved.
 * 版权所有，侵权必究！
 */
package murray.sales.mall.common;

/**
 * @author Murray
 * @email Murray50325487@gmail.com
 */
public enum ServiceResultEnum {
    ERROR("error"),

    SUCCESS("success"),

    DATA_NOT_EXIST("No Record!"),

    SAME_CATEGORY_EXIST("There existed the Same Classification"),

    SAME_LOGIN_NAME_EXIST("Username is exited"),

    LOGIN_NAME_NULL("Please Enter Username"),

    LOGIN_PASSWORD_NULL("Please Enter Password"),

    LOGIN_VERIFY_CODE_NULL("Please Enter the Code"),

    LOGIN_VERIFY_CODE_ERROR("The Code is Incorrect"),

    SAME_INDEX_CONFIG_EXIST("There existed the same name configurations for index page"),

    GOODS_CATEGORY_ERROR("Classification data Excepted"),

    SAME_GOODS_EXIST("The Existed the same information for the goods"),

    GOODS_NOT_EXIST("Goods NOT Existed"),

    GOODS_PUT_DOWN("Goods were Take off"),

    SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR("Exceed purchasable quantities"),

    SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR("Exceed the capacity of Cart"),

    LOGIN_ERROR("Login Failed"),

    LOGIN_USER_LOCKED("User has been banned"),

    ORDER_NOT_EXIST_ERROR("Order is NOT existed"),

    ORDER_ITEM_NOT_EXIST_ERROR("Order Item is NOT existed"),

    NULL_ADDRESS_ERROR("Address Cannot be NULL"),

    ORDER_PRICE_ERROR("The price of the order is abnormal"),

    ORDER_GENERATE_ERROR("Generating orders excepted"),

    SHOPPING_ITEM_ERROR("The Data for Cart is Excepted"),

    SHOPPING_ITEM_COUNT_ERROR("Stock is Insufficient"),

    ORDER_STATUS_ERROR("The Status of Order is ABNORMAL"),

    CLOSE_ORDER_ERROR("Failed to Close Order"),

    OPERATE_ERROR("Operation Failed"),

    NO_PERMISSION_ERROR("No Authentication"),

    DB_ERROR("database error");

    private String result;

    ServiceResultEnum(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}