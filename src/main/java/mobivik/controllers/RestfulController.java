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
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/settings", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String sendSettings(HttpServletRequest request)
    {
        String result = sendData("settings", request);
        return  result;
    }

    @RequestMapping(value = "/route", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String sendRoute(HttpServletRequest request)
    {
        String result = sendData("route", request);
        return  result;
    }

    @RequestMapping(value = "/goods", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String sendGoods(HttpServletRequest request)
    {
        String result = sendData("goods", request);
        return  result;
    }

    @RequestMapping(value = "/sales", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String sendSales(HttpServletRequest request)
    {
        String result = sendData("sales", request);
        return  result;
    }

    @RequestMapping(value = "/payments", method = RequestMethod.POST)
    public @ResponseBody String receivePayments(HttpServletRequest request)
    {
        String body = "";
        try {
            body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
       System.out.println("route agent_code: " + body);

        String result;
        String agentCode = null;
        try {
            agentCode = request.getHeader("agent-code").trim();
        }catch (Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
            return  result;
        }

        String suffix = getSuffix(request);

        String fileName = agentCode.trim() + "_payments.mv";
        try {
            result = saveToFile(fileName, body, suffix);
        }catch(Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
        }
        return  result;

    }
    @RequestMapping(value = "/buyorders", method = RequestMethod.POST)
    public @ResponseBody String receiveBuyorders(HttpServletRequest request)
    {
        String body = "";
        try {
            body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("route agent_code: " + body);

        String result;
        String agentCode = null;
        try {
            agentCode = request.getHeader("agent-code").trim();
        }catch (Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
            return  result;
        }

        String suffix = getSuffix(request);

        String fileName = agentCode.trim() + "_buyorders.mv";
        try {
            result = saveToFile(fileName, body, suffix);
        }catch(Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
        }
        return  result;

    }

    /* Suffix for subdirectory */
    private String getSuffix(HttpServletRequest request){

        String organization = "";
        try {
            organization = request.getHeader("organization");
        }catch (Exception e){
            return "";
        }

        if(null == organization) return "";

        if(organization.trim()=="") return "";

        return "_" + organization.trim().toLowerCase(Locale.ROOT);
    }

    private String sendData(String dataName, HttpServletRequest request){
        String result;
        String agentCode = null;
        try {
            agentCode = request.getHeader("agent-code").trim();
            result = agentCode;
        }catch (Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
        }
        String fileName = new StringBuilder().append(agentCode).append("_").append(dataName).append(".mv").toString();

        String suffix = getSuffix(request);

        try {
            result = getFileContent(fileName, suffix);
        }catch(Exception e){
            result = new StringBuilder().append("{\"error\": \"").append(e.getMessage()).append("\"}").toString();
        }

        return  result;
    }
    
    private String getFileContent(String fileName, String suffix) throws Exception {
        StringBuilder fileContent = new StringBuilder();
        String filePath = new StringBuilder().append("D:\\mobivik\\obmin").append(suffix).append("\\out\\").append(fileName).toString();

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

    private String saveToFile(String fileName, String content, String suffix) {
        String filePath = new StringBuilder().append("D:\\mobivik\\obmin").append(suffix).append("\\in\\").append(fileName).toString();

        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(filePath), "CP866" ) )) //"IBM866"
        {
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