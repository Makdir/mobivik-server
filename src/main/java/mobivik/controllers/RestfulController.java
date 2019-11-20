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
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

@RestController
public class RestfulController {

    @RequestMapping(value = "/test",  method = RequestMethod.GET)
    @ResponseBody
    public String testing(HttpServletRequest request)
    {
        String result = "Connected with exception";
        try {
            String agentCode = request.getHeader("agent-code").trim();
            result = agentCode;
        }catch (Exception e){
            //result = String.valueOf(e);
        }
        return  result;
    }

    @RequestMapping(value = "/route", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String sendRoute(HttpServletRequest request)
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
            result = getFileContent(agentCode+ "_route.mv");
        }catch(Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
        }

        return  result;
    }

    @RequestMapping(value = "/goods", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String sendGoods(HttpServletRequest request)
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
            result = getFileContent(agentCode+ "_goods.mv");
        }catch(Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
        }

        return  result;
    }

    private String getFileContent(String fileName) throws Exception {
        StringBuilder fileContent = new StringBuilder();
        String filePath = new StringBuilder().append("D:\\Development\\mobiviks\\obmin\\out\\").append(fileName).toString();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader( new FileInputStream(filePath), "windows-1251") )) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                fileContent.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.print("-------------------------------------------");
//        System.out.print("fileContent = "+fileContent.toString().trim());
        return fileContent.toString().trim();
    }

    @RequestMapping(value = "/payments", method = RequestMethod.GET)
    public @ResponseBody String receivePayments(HttpServletRequest request, @RequestParam("p") String content)
    {
        System.out.println("-------------------------------------------");
        System.out.println("body in model: " + content);

        String result;
        String agentCode = null;
        try {
            agentCode = request.getHeader("agent-code").trim();
        }catch (Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
            return  result;
        }


        String fileName = agentCode.trim() + "_payments.mv";
        try {
            result = saveToFile(fileName, content);
        }catch(Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
        }
        return  result;

    }

    private String saveToFile(String fileName, String content) {
        String filePath = new StringBuilder().append("D:\\Development\\mobiviks\\obmin\\in\\").append(fileName).toString();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(filePath), "windows-1251") )) {
//            String line;
//            while ((line = bufferedWriter.readLine()) != null) {
//                fileContent.append(line);
//            }
            bufferedWriter.write(content);
        }
        catch (Exception e){
            return  "Error writing file";
        }

        return  "success";
    }


}