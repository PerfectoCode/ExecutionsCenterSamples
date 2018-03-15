package com.perfecto.executionCenter;

public class Props {

    private static String cloudName         = System.getProperty("cloudName", "");
    private static String securityToken     = System.getProperty("token", "");
    private static String executionsID      = System.getProperty("executionsID", "");
    private static String executionsName    = System.getProperty("executionsID", "");
    private static String executionsOwner   = System.getProperty("executionsOwner", "");
    private static String textFilter        = System.getProperty("textFilter", "");

    static {
        if (executionsID == null || executionsID.trim().isEmpty()) {
            executionsID = "";
        }

        if (executionsName == null || executionsName.trim().isEmpty()) {
            executionsName = "";
        }

        if (executionsOwner == null || executionsOwner.trim().isEmpty()){
            executionsOwner = "";
        }

        if (textFilter == null || textFilter.trim().isEmpty()){
            textFilter = "";
        }
    }


    public static String getCloudName(){
        return cloudName;
    }

    public static String getToken(){
        return securityToken;
    }

    public static String getExecutionsID(){
        return executionsID;
    }

    public static String getExecutionsName(){
        return executionsName;
    }

    public static String getExecutionsOwner(){
        return executionsOwner;
    }

    public static String getTextFilter(){
        return textFilter;
    }

    public static String getExecutionCenter(){
        return "https://" + getCloudName() + ".executions.perfectomobile.com";
    }


}
