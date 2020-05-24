package cl.propiedades.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;


public  class FileUtils {





	public static void writeFile(String path, String content){
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			

			fw = new FileWriter(path);
			bw = new BufferedWriter(fw);
			bw.write(content);

			System.out.println(path+" Done");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (Exception ex) {

				ex.printStackTrace();

			}
		}
	}
	public static String[] listFile(String folder, String ext) {

		GenericExtFilter filter = new GenericExtFilter(ext);

		File dir = new File(folder);

		if(dir.isDirectory()==false){
			System.out.println("Directory does not exists : " + folder);
			return null;
		}

		// list out all the file name and filter by the extension
		String[] list = dir.list(filter);

		if (list.length == 0) {
			System.out.println("no files end with : " + ext);
			return null;
		}

/*		for (String file : list) {
			String temp = new StringBuffer(folder).append(File.separator)
					.append(file).toString();
			System.out.println("file : " + temp);
		}*/
		return list;
	}
	public static void readFile(String fileName){
		BufferedReader br = null;
		FileReader fr = null;

		try {

			//br = new BufferedReader(new FileReader(FILENAME));
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	
}
