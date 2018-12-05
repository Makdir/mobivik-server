package mobivik.controllers;


import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import javax.servlet.http.HttpServletRequest;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

@RestController
public class RestfulController {

    @RequestMapping(value = "/test",  method = RequestMethod.GET)
    @ResponseBody
    public String testing(HttpServletRequest request)
    {
        String result;
        try {
            String agentCode = request.getHeader("agent-code").trim();
            result = agentCode;
        }catch (Exception e){
            result = String.valueOf(e);
        }
        return  result;
    }

    @RequestMapping(value = "/fromserver", method = RequestMethod.GET)
    @ResponseBody
    public String fromServer(HttpServletRequest request)
    {
        String result;
        String agentCode = null;
        try {
            agentCode = request.getHeader("agent-code").trim();
            result = agentCode;
        }catch (Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
        }

        try {
            result = getFileContent(agentCode);
        }catch(Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
        }

        return  result;
    }

    private String getFileContent(String agentCode) throws Exception {
        StringBuilder fileContent = new StringBuilder();
        try(FileReader reader = new FileReader(new StringBuilder().append("D:\\Development\\mobiviks\\").append(agentCode).append("ad.mv").toString()))
        {
            Scanner scan = new Scanner(reader);
            String line;
            while (scan.hasNextLine()) {
                fileContent.append(scan.nextLine());
            }
            reader.close();
            scan.close();

        }
        catch(IOException e){

            fileContent.append("{\"error\": \"").append(e.getMessage()).append("\"}");
        }
            return fileContent.toString().trim();
    }

    private String makeErrorRespond(){

        return null;
    }
}