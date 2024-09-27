package com.ecommerce.cozashop.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;


public class ResourceUtils {

	public static String getFileContent(String file) {
		InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream(file);
		try {
			String contents = IOUtils.toString(is,StandardCharsets.UTF_8);
			return contents;
		} catch (IOException e) {
			return null;
		}
	}
	
	public static String getFileByte(String file) {
		InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream(file);
		try {
			String contents = IOUtils.toString(is,StandardCharsets.UTF_8);
			return contents;
		} catch (IOException e) {
			return null;
		}
	}
}
