package com.perfecto.executionCenter;

public class Props {

    private static String cloudName            = System.getProperty("cloudName", "");
    private static String securityToken     = System.getProperty("token", "");
    private static String executionsID      = System.getProperty("executionsID", "");
    private static String executionsName    = System.getProperty("executionsID", "");
    private static String executionCenter   = System.getProperty("executionCenter", "");

    static {
        if (executionsID == null || executionsID.trim().isEmpty()) {
            executionsID = "";
        }

        if (executionsName == null || executionsName.trim().isEmpty()) {
            executionsName = "";
        }

        if (executionCenter == null || executionCenter.trim().isEmpty()){
            executionCenter = "https://" + getCloudName() + ".executions.perfectomobile.com";
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

    public static String getExecutionCenter(){
        return executionCenter;
    }


}
