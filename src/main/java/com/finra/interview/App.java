package com.finra.interview;

import java.util.Scanner;

/**
 * Application to  determine if a Firm is under scrutiny today.
 * Enter 0 as firm id to quit/stop the application
 * Please check path permission for /user/finra
 */
public class App 
{	
    public static void main( String[] args ) throws InterruptedException
    {
    	Scanner sc=new Scanner(System.in);
    	Thread t=new FirmScrutinizer();
    	t.start();
    	while(true){
    		int firmId=-1;
    		System.out.println("enter the firm id involved in todays scrutiny. Press 0 to exit");
    		if(sc.hasNext())
    		{
    		firmId=sc.nextInt();	
    		}
    		if(firmId>0)
    			{
    			((FirmScrutinizer) t).addTodayScrutinized(firmId);
    			}
    		else if(firmId==0)
    		{
    			System.exit(0);
    		}
    			
    	}
    }
}
