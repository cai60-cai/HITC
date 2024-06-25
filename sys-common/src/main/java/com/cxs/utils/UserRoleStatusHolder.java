package com.cxs.utils;

  
public class UserRoleStatusHolder {

    private static final ThreadLocal<Boolean> CURRRENT_USER_ROLE_STATUS = new ThreadLocal<>();

    public static void setCurrrentUserRoleStatus(Boolean flag){
        CURRRENT_USER_ROLE_STATUS.set(flag);
    }

    public static Boolean getCurrrentUserRoleStatus(){
        return CURRRENT_USER_ROLE_STATUS.get();
    }

    public static void cleanCurrrentUserRoleStatus(){
        CURRRENT_USER_ROLE_STATUS.remove();
    }
}
