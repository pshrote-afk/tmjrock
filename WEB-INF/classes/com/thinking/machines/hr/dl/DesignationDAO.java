package com.thinking.machines.hr.dl;
import java.util.*;
import java.sql.*;
public class DesignationDAO
{
public void delete(int code) throws DAOException
{
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM designation WHERE CODE=?");
prepareStatement.setInt(1,code);
ResultSet resultSet = prepareStatement.executeQuery();
if(resultSet.next()==false)
{
//means entry for such a code does not exist.
connection.close();
prepareStatement.close();
resultSet.close();
throw new DAOException("Invalid code: "+code);
}
prepareStatement.close();
resultSet.close();
//check if designationCode allotted to any employee
prepareStatement = connection.prepareStatement("SELECT gender FROM employee WHERE designation_code=?"); //gender-cause it is smallest field. We just want to check if it exists. So don't take all fields into memory.
prepareStatement.setInt(1,code);
resultSet = prepareStatement.executeQuery();
if(resultSet.next()==true) 
{
resultSet.close();
prepareStatement.close();
connection.close();
throw new DAOException("Designation cannot be deleted, as it is allotted to atleast 1 employee.");
}
prepareStatement.close();
resultSet.close();
//control reached here means such an entry exists, and is not allotted to any employee
prepareStatement = connection.prepareStatement("DELETE FROM designation WHERE code=?");
prepareStatement.setInt(1,code);
prepareStatement.executeUpdate();
connection.close();
prepareStatement.close();
resultSet.close();
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}


public void update(DesignationDTO designationDTO) throws DAOException
{
int code = designationDTO.getCode();
String title = designationDTO.getTitle();
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM designation WHERE code=?");
prepareStatement.setInt(1,code);
ResultSet resultSet = prepareStatement.executeQuery();
if(resultSet.next()==false)
{
connection.close();
prepareStatement.close();
resultSet.close();
throw new DAOException("Invalid designation code: "+code);
}
prepareStatement = connection.prepareStatement("SELECT * FROM designation WHERE title=? AND CODE!=?");
prepareStatement.setString(1,title);
prepareStatement.setInt(2,code);
resultSet = prepareStatement.executeQuery();
if(resultSet.next()==true)
{
connection.close();
prepareStatement.close();
resultSet.close();
throw new DAOException("Designation already exists: "+title);
}
resultSet.close();
prepareStatement.close();
prepareStatement = connection.prepareStatement("UPDATE designation SET title=? WHERE code=?");
prepareStatement.setString(1,title);
prepareStatement.setInt(2,code);
prepareStatement.executeUpdate();
prepareStatement.close();
connection.close();
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}

public DesignationDTO getByCode(int code) throws DAOException
{
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement prepareStatement = connection.prepareStatement("SELECT * FROM designation WHERE CODE=?");
prepareStatement.setInt(1,code);
ResultSet resultSet;
resultSet = prepareStatement.executeQuery();
if(resultSet.next()==false)
{
resultSet.close();
prepareStatement.close();
connection.close();
throw new DAOException("Invalid designation code: "+code);
}
DesignationDTO designationDTO = new DesignationDTO();
designationDTO.setCode(resultSet.getInt("code"));
designationDTO.setTitle(resultSet.getString("title").trim());
resultSet.close();
prepareStatement.close();
connection.close();
return designationDTO;
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}

public void add(DesignationDTO designationDTO) throws DAOException
{
try
{
Connection connection = DAOConnection.getConnection();
PreparedStatement prepareStatement;
prepareStatement = connection.prepareStatement("SELECT * FROM designation WHERE title=?");
prepareStatement.setString(1,designationDTO.getTitle());
ResultSet resultSet = prepareStatement.executeQuery();
if(resultSet.next()==true) //means some entry with above title already exists. Raise exception
{
resultSet.close();
prepareStatement.close();
connection.close();
throw new DAOException("Designation: "+designationDTO.getTitle()+" already exists.");
}
prepareStatement = connection.prepareStatement("INSERT INTO designation (title) values (?)", Statement.RETURN_GENERATED_KEYS);
prepareStatement.setString(1,designationDTO.getTitle());
prepareStatement.executeUpdate();

resultSet = prepareStatement.getGeneratedKeys();
resultSet.next();
int code = resultSet.getInt(1);
resultSet.close();
prepareStatement.close();
connection.close();
designationDTO.setCode(code);
}catch(SQLException sqlException)
{
throw new DAOException(sqlException.getMessage());
}
}
public List<DesignationDTO> getAll() throws DAOException
{
List<DesignationDTO> designations;
designations = new LinkedList<>();
try
{
Connection connection = DAOConnection.getConnection();
Statement statement;
statement = connection.createStatement();
ResultSet resultSet;
resultSet = statement.executeQuery("SELECT * FROM designation ORDER BY title");

DesignationDTO designationDTO;
int code;
String title;
while(resultSet.next())
{
designationDTO = new DesignationDTO();
code = resultSet.getInt("code");
title = resultSet.getString("title");
designationDTO.setCode(code);
designationDTO.setTitle(title);
designations.add(designationDTO);
}
resultSet.close();
statement.close();
connection.close();
return designations;
}catch(Exception e)
{
throw new DAOException(e.getMessage());
}
}
}