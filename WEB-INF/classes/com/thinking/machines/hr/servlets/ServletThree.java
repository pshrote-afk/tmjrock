package com.thinking.machines.hr.servlets;
import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.thinking.machines.hr.dl.*;
import com.google.gson.*;
public class ServletThree extends HttpServlet
{
public void doGet(HttpServletRequest request,HttpServletResponse response)
{
try
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
}catch(Exception e)
{
//do nothing
}
}

public void doPost(HttpServletRequest request,HttpServletResponse response)
{
try
{
String contentType = request.getContentType();
if(contentType.contains("application/json"))
{
BufferedReader br = request.getReader();
StringBuffer sb = new StringBuffer();
String d;
while(true)
{
d = br.readLine();
if(d==null) break;
sb.append(d);
}
String rawData = sb.toString();
Gson gson = new Gson();
Customer c = gson.fromJson(rawData,Customer.class);

PrintWriter pw = response.getWriter();
response.setContentType("application/json");
pw.print(gson.toJson(c));
pw.flush();
}
else
{
String firstName = request.getParameter("firstName");
String lastName = request.getParameter("lastName");
int age = Integer.parseInt(request.getParameter("age"));

JsonObject jsonObject = new JsonObject();
jsonObject.addProperty("firstName",firstName);
jsonObject.addProperty("lastName",lastName);
jsonObject.addProperty("age",age);

Gson gson = new Gson();
String jsonString = gson.toJson(jsonObject);

PrintWriter pw = response.getWriter();
response.setContentType("application/json");
pw.print(jsonString);
pw.flush();
}
}catch(Exception e)
{
try
{
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}catch(Exception ee)
{
}
}
}//end doPost
}//end class