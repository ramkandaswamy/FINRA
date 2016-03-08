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
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.LongStream;

import static org.junit.Assert.assertFalse;
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
		} else {
            deleteFile();
        }
    }

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
        deleteFile();
    }

    private void deleteFile() throws java.io.IOException {
        Path p = Paths.get("/var", "tmp", "user", "finra", FirmScrutinizer.getFileName());
        Files.deleteIfExists(p);
    }

	@Test
	public void testShouldCreateFiles() throws InterruptedException {
	
		StringBuilder sb=new StringBuilder();
        LongStream.rangeClosed(4391000, 4391200).forEach(e -> sb.append(e + " "));
        ;
        sb.append(" 0");
		InputStream stubInputStream = 
			     IOUtils.toInputStream(sb.toString());
//		"4390116 8675309 7365000  0"
		int hour=LocalTime.now().getHour();
		int min=LocalTime.now().getMinute();
        App.perform(stubInputStream, hour, min, 100, 90L);
        File files = new File("/var/tmp/user/finra/" + FirmScrutinizer.getFileName());
        final AtomicBoolean exists = new AtomicBoolean(files.exists());
        assertTrue("file created with todays date", exists.get());
	}

    @Test
    public void testShouldNotCreateFiles() throws InterruptedException, java.io.IOException {
        deleteFile();
        StringBuilder sb = new StringBuilder();
        LongStream.rangeClosed(4391000, 4391100).forEach(e -> sb.append(e + " "));
        ;
        sb.append(" 0");
        InputStream stubInputStream =
                IOUtils.toInputStream(sb.toString());
//		"4390116 8675309 7365000  0"
        int hour = LocalTime.now().getHour() + 1;
        int min = LocalTime.now().getMinute();
        App.perform(stubInputStream, hour, min, 100, 90L);
        File files = new File("/user/finra/" + FirmScrutinizer.getFileName());
        final AtomicBoolean exists = new AtomicBoolean(files.exists());
        assertFalse("file is not created with todays date", exists.get());
    }

}
