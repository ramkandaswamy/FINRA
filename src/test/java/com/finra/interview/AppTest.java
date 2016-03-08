/**
 * 
 */
package com.finra.interview;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.LongStream;

import static org.junit.Assert.assertTrue;

/**
 * @author rkandaswamy15
 *
 */
public class AppTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		File files = new File("/var/tmp/user/finra");
		if (!files.exists()) {
			if (files.mkdirs()) {
				System.out.println("Multiple directories are created!");
			} else {
				System.out.println("Failed to create multiple directories!");
			}
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		Path p=Paths.get("/","var", "tmp","usr","finra",getFileName());
		Files.deleteIfExists(p);
	}

    private String getFileName(){
     LocalDate now=LocalDate.now();
     String fileName=String.format("scrutinyFirms_%d%d%d.txt",now.getYear(),now.getMonth().getValue(),now.getDayOfMonth());
     return fileName;
 }
	@Test
	public void testShouldCreateFiles() throws InterruptedException {
	
		StringBuilder sb=new StringBuilder();
		LongStream.rangeClosed(4391000, 4392000).forEach(e->sb.append(e+" "));;
		sb.append(" 0");
		InputStream stubInputStream = 
			     IOUtils.toInputStream(sb.toString());
//		"4390116 8675309 7365000  0"
		int hour=LocalTime.now().getHour();
		int min=LocalTime.now().getMinute();
		App.perform(stubInputStream,hour,min,5,30000L);
        File files = new File("/user/finra/"+getFileName());
        final AtomicBoolean exists = new AtomicBoolean(files.exists());
        assertTrue("file created with todays date", exists.get());
	}

}
