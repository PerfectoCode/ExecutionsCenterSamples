package com.perfecto.executionCenter;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final String executionCenter   = Props.getExecutionCenter();
    private static Logger logger                  = LogManager.getLogger("Samples Logger");

    /**
     * Releasing the all the executions with the name that sent as  a system property
     * @return JSON contains list of executions that stopped and unstopped
     * @throws UnirestException
     */
    public static String releaseAllName() throws UnirestException {
        String executionsName = Props.getExecutionsName();
        String executionNamesJson = "{\"fields\": {\"name\": [\"" + executionsName + "\"] } }";
        logger.info("Trying to release executions with the name: " + executionsName);
        return releaseRequest(executionNamesJson);
    }

    /**
     * Releasing the id's list (List should be sent as a system property,each id is separated with ';')
     * @return JSON contains list of executions that stopped and unstopped
     * @throws UnirestException
     */
    public static String releaseAllID() throws UnirestException {
        String[] executionsList = Props.getExecutionsID().split(";");
        String executionIDsJson = "{ {\"fields\": \"id\": [";
        for(String id: executionsList) {
            executionIDsJson = executionIDsJson + "\"" + id + "\",";
        }
        executionIDsJson = executionIDsJson.substring(0, executionIDsJson.length()-1) + "] } }";
        logger.info("Trying to release executions with the following ID's: " + executionIDsJson);
        return releaseRequest(executionIDsJson);
    }

    /**
     * Sending request to the execution center API
     * @param body - ID's list or the name of the executions we want to stop
     * @return the response
     * @throws UnirestException
     */
    public static String releaseRequest(String body) throws UnirestException {
        HttpResponse<String> jsonResponse = Unirest.post(executionCenter)
                .header("Content-Type", "application/json")
                .header("Perfecto-Authorization", Props.getToken())
                .body(body).asString();
        return jsonResponse.getBody();
    }

    public static void main (String [] args) throws UnirestException {
        if (!Props.getToken().equalsIgnoreCase("") && !Props.getCloudName().equalsIgnoreCase("")){
            String response = "";
            if (!Props.getExecutionsID().equalsIgnoreCase("")){
                response = releaseAllID();
            }
            else if (!Props.getExecutionsName().equalsIgnoreCase("")){
                response = releaseAllName();
            }
            if (!response.equalsIgnoreCase("")) {
                String[] stoppedExecutions = response.substring(response.indexOf(":[") + 2, response.indexOf("],\"unStoppedExecutions\":")).split(",");
                logger.info("Stopped executions:");
                int i=1;
                for (String execution : stoppedExecutions) {
                    logger.info("#" + (i++) + " " + execution);

                }
            }else{
                logger.error("Response came back empty");
            }

        }else{
            logger.error("You should supply security token and tenant");
        }
    }
}
