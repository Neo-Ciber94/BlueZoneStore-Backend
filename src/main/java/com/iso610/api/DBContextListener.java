package com.iso610.api;

//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;

import dao.ProductDB;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

//@WebListener
public class DBContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ProductDB.start();
		System.out.println("Database initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ProductDB.close();
		System.out.println("Database closed");
	}

}
