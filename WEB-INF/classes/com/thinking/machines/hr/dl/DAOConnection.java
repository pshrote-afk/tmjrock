package com.thinking.machines.hr.dl;
import java.sql.Connection;
import java.sql.DriverManager;
public class DAOConnection
{
private DAOConnection() //no one need make object
{
}

static public Connection getConnection() throws DAOException
{
Connection connection = null;
try
{
Class.forName("com.mysql.cj.jdbc.Driver"); //When the class is loaded, its static initializer block runs, which typically registers the driver with DriverManager
connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tmdb","tmdbuser","tmdbuser");
return connection;
}catch(Exception exception)
{
throw new DAOException(exception.getMessage());
}
}
}