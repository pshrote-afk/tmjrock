package com.thinking.machines.hr.servlets;
import java.util.*;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.thinking.machines.hr.dl.*;
import com.google.gson.*;
public class ServletOne extends HttpServlet
{
public void doPost(HttpServletRequest request,HttpServletResponse response)
{
try
{
response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
}catch(Exception e)
{
//do nothing
}
}
public void doGet(HttpServletRequest request,HttpServletResponse response)
{
try
{
PrintWriter pw = response.getWriter();
response.setContentType("application/json");
response.setCharacterEncoding("utf-8");
DesignationDAO designationDAO = new DesignationDAO();
List<DesignationDTO> designations = designationDAO.getAll();

Gson gson = new Gson();
JsonObject jsonObject = new JsonObject();
jsonObject.addProperty("success",true);
jsonObject.add("data",gson.toJsonTree(designations));
String jsonString = gson.toJson(jsonObject);
pw.print(jsonString);
pw.flush();

}catch(Exception e)
{
try
{
response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
}catch(Exception ee)
{
}
}
}//end doGet
}//end class