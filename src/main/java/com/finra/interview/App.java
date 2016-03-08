package com.finra.interview;

import java.io.InputStream;

/**
 * Application to  determine if a Firm is under scrutiny today.
 * Enter 0 as firm id to quit/stop the application
 * Please check path permission for /user/finra
 */
public class App 
{	
    private static java.util.Scanner sc;

	public static void main( String[] args ) throws InterruptedException
    {
    	InputStream is=System.in;
        perform(is,0,0,1,10000);
    }

    public static void perform(InputStream is,int hr,int min,int sleep, long interval) throws InterruptedException {
        sc = new java.util.Scanner(is);
        Thread t=new FirmScrutinizer(hr,min);
        ((FirmScrutinizer)t).setFileCheckInterval(interval);
        t.start();
        boolean runStatus=true;
        while(runStatus){
            int firmId=-1;
            System.out.println("enter the firm id involved in todays scrutiny. Press 0 to exit");
            if(sc.hasNext())
            {
            	Thread.sleep(sleep);
            firmId=sc.nextInt();
            }
            if(firmId>0)
                {
                ((FirmScrutinizer) t).addTodayScrutinized(firmId);
                }
            else if(firmId==0)
            {
                runStatus=false;
            }

        }
    }
}
