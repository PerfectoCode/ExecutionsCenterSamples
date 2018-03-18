package com.perfecto.executionCenter;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static final String executionCenterStop     = Props.getExecutionCenter() + "/execution-manager/api/v1/executions/stop";
    private static Logger logger                        = LogManager.getLogger("Sample Logger");

    /**
     * Stopping all the executions with the name that (sent as  a system property)
     * @return JSON contains list of executions that stopped and unstopped
     * @throws UnirestException
     */
    public static String releaseByName() throws UnirestException {
        String executionsName = Props.getExecutionsName();
        String executionNamesJson = "{\"fields\": {\"name\": [\"" + executionsName + "\"] } }";
        logger.info("Trying to release executions with the name: " + executionsName);
        return releaseRequest(executionNamesJson);
    }

    /**
     * Stopping all the executions with the owner name (sent as  a system property)
     * @return List of stopped and unstopped executions
     * @throws UnirestException
     */
    public static String releaseByOwner() throws UnirestException {
        String executionsName = Props.getExecutionsOwner();
        String executionNamesJson = "{\"fields\": {\"owner\": [\"" + executionsName + "\"] } }";
        logger.info("Trying to release executions with the owner: " + executionsName);
        return releaseRequest(executionNamesJson);
    }

    /**
     * Stopping all the executions that contains the textFilter - that sent as  a system property
     * @return List of stopped and unstopped executions
     * @throws UnirestException
     */
    public static String releaseByTextFilter() throws UnirestException {
        String text = Props.getTextFilter();
        String executionNamesJson = "{\"freeTextFilter\": \"" + text + "\" }";
        logger.info("Trying to release executions that contains the text: " + text);
        return releaseRequest(executionNamesJson);
    }

    /**
     * Stopping the id's list (List should be sent as a system property,each id is separated with ';')
     * @return List of stopped and unstopped executions
     * @throws UnirestException
     */
    public static String releaseByID() throws UnirestException {
        String[] executionsList = Props.getExecutionsID().split(";");
        String executionIDsJson = "{\"fields\": {\"id\": [";
        for(String id: executionsList) {
            executionIDsJson = executionIDsJson + "\"" + id + "\",";
        }
        executionIDsJson = executionIDsJson.substring(0, executionIDsJson.length()-1) + "] } }";
        logger.info("Trying to release executions with the following ID's: " + executionIDsJson);
        return releaseRequest(executionIDsJson);
    }

    /**
     * Sending request to the execution center API
     * @param body - The executions we want to stop
     * @return the response
     * @throws UnirestException
     */
    public static String releaseRequest(String body) throws UnirestException {
        HttpResponse<String> jsonResponse = Unirest.post(executionCenterStop)
                .header("Content-Type", "application/json")
                .header("Perfecto-Authorization", Props.getToken())
                .body(body).asString();
        return jsonResponse.getBody();
    }

    public static void main (String [] args) throws UnirestException {
        if (!Props.getToken().equalsIgnoreCase("") && !Props.getCloudName().equalsIgnoreCase("")){
            String response = "";
            if (!Props.getExecutionsID().equalsIgnoreCase("")){
                response = releaseByID();
            }
            else if (!Props.getExecutionsName().equalsIgnoreCase("")){
                response = releaseByName();
            }
            else if (!Props.getExecutionsOwner().equalsIgnoreCase("")){
                response = releaseByOwner();
            }
            else if (!Props.getTextFilter().equalsIgnoreCase("")){
                response = releaseByTextFilter();
            }
            if (!response.equalsIgnoreCase("")) {
                String[] stoppedExecutions = response.substring(response.indexOf(":[") + 2, response.indexOf("],\"unStoppedExecutions\":")).split(",");
                if (!stoppedExecutions[0].equalsIgnoreCase("")) {
                    logger.info("Stopped executions:");
                    int i = 1;
                    for (String execution : stoppedExecutions) {
                        logger.info("#" + (i++) + " " + execution);
                    }
                } else {
                    logger.info("No executions found !");
                }
            }else{
                logger.error("Response came back empty");
            }

        }else{
            logger.error("You should supply security token and tenant");
        }
    }
}
