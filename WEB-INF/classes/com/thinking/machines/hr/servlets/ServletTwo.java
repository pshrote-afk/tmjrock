package com.thinking.machines.hr.servlets;
import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import com.thinking.machines.hr.dl.*;
public class ServletTwo extends HttpServlet
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
int code = Integer.parseInt(request.getParameter("code"));
PrintWriter pw = response.getWriter();
response.setContentType("text/plain");
DesignationDAO designationDAO = new DesignationDAO();
	try
	{
	DesignationDTO designationDTO = designationDAO.getByCode(code);
	pw.println(designationDTO.getCode()+", "+designationDTO.getTitle());
	}catch(DAOException daoException)
	{
	System.out.println(daoException.getMessage());
	pw.print("INVALID");
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
}//end doGet
}//end class