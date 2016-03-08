/**
 * 
 */
package com.finra.interview;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * @author ramana
 *
 */
public class FirmScrutinizer extends Thread
{
    private long fileCheckInterval;
	private final int hour;
	private int minute;
	//Using Map here. Could use Hashset that contains firmIds
	private static final Map<Integer,Firm> dailyScrutinizedFirms=initialize();
	private final Set<Firm> todayScrutinizedFirms=Collections.synchronizedSet(new LinkedHashSet<>());
	
	
	FirmScrutinizer(int hr, int min){
		hour=Math.abs(hr)%24;
		minute=Math.abs(min)%60;
	}
	private static Map<Integer,Firm> initialize()
	{
		Map<Integer,Firm> temp=new HashMap<>();
		temp.put(4390116,new Firm(4390116));
		temp.put(8675309,new Firm(8675309));
		temp.put(7365000,new Firm(7365000));
		return Collections.unmodifiableMap(temp);
	}
	public void addTodayScrutinized(int firmId)
	{
		//Check if not exists in the daily map
		if(!dailyScrutinizedFirms.containsKey(firmId))
            synchronized (todayScrutinizedFirms) {
                todayScrutinizedFirms.add(new Firm(firmId));
            }
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(fileCheckInterval);
			} catch (InterruptedException e) {
				// Should replace with logger.error
				e.printStackTrace();
			}
            boolean value1 = checkForTime();
            boolean value2 = checkForCreate(getFileName());
            System.out.printf("Values are, checkForTime %b, checkForCreate %b,todayScrutinizedFirms %s", value1, value2, todayScrutinizedFirms);
            if (value1 && value2 && isNotEmpty(todayScrutinizedFirms)) {
                writeToFile();
            }
        }
	}
	private void writeToFile()
	{
		StringBuilder sb=new StringBuilder();
        Set<Firm> firmSet = null;
        synchronized (todayScrutinizedFirms) {
            firmSet = new LinkedHashSet<>(todayScrutinizedFirms);
            todayScrutinizedFirms.removeAll(firmSet);
        }
        firmSet.stream().forEach(e -> sb.append(e.getFirmId() + "\n"));
		byte[] data=sb.toString().getBytes();
		//replace with log.info
        System.out.printf("Valuefor todayScrutinizedFirms %s", sb.toString());
        if (data != null && data.length > 0) {
            Path p = Paths.get("/var", "tmp", "user", "finra", getFileName());
            try (OutputStream out = new BufferedOutputStream(
				      Files.newOutputStream(p, CREATE_NEW))) {
				      out.write(data, 0, data.length);

				    } catch (IOException x) {
				      //replace below with logger
				    	System.err.println(x);
				    }
		}
	}
	private boolean checkForCreate(String fileNameStr) {
        Path start = Paths.get("/var", "tmp", "user", "finra");
        int maxDepth = 1;
		Optional<Path> joined = null;
		try (Stream<Path> stream = Files.find(start, maxDepth, (path, attr) ->
		        String.valueOf(path).contains(fileNameStr))) {
			joined= stream.findFirst();
			//replace with log.debug
		    System.out.println("Found file for today: " + joined);
		} catch (IOException e) {
			//Should replace this with logger
			e.printStackTrace();
		}
		return joined!=null && !joined.isPresent(); 
	}
	private boolean checkForTime() {
		boolean result=LocalTime.now().getHour()>=hour && LocalTime.now().getMinute()>=minute;
		//replace with log.info
		System.out.println("\nCurrent time is "+LocalTime.now().getHour()+":"+LocalTime.now().getMinute());
		 return result;
	}

    public static String getFileName() {
        LocalDate now = LocalDate.now();
        String fileName = String.format("scrutinyFirms_%d%d%d.txt", now.getYear(), now.getMonth().getValue(), now.getDayOfMonth());
        return fileName;
    }

    public long getFileCheckInterval() {
        return fileCheckInterval;
    }

    public void setFileCheckInterval(final long interval) {
        this.fileCheckInterval = interval;
    }
}
