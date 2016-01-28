package com.hxj.common.file;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件工具类
 * 
 * @author Hxuejie hxuejie@126.com
 */
public class FileUtils {

	/**
	 * 删除文件夹
	 * 
	 * @param dir 文件夹
	 * @return 返回删除结果：true成功,false失败
	 */
	public static boolean deleteDir(File dir) {
		if (!isFileExists(dir) || !dir.isDirectory()) {
			return false;
		}
		// delete files
		boolean result = deleteDirFiles(dir);
		// delete dir
		return dir.delete() & result;
	}

	/**
	 * 删除目录下的所有文件
	 * 
	 * @param dir
	 * @return 返回删除结果：true成功,false失败
	 */
	public static boolean deleteDirFiles(File dir) {
		if (!isFileExists(dir)) {
			return false;
		}
		if (dir.isFile()) {
			return dir.delete();
		}
		boolean result = true;
		File[] files = dir.listFiles();
		for (File f : files) {
			result &= deleteDirFiles(f);
		}
		return result;
	}

	/**
	 * 复制文件
	 * 
	 * @param src 源文件
	 * @param dst 目标文件
	 * @throws IOException
	 */
	public static void copyFile(File src, File dst) throws IOException {
		if (!isFileExists(src)) {
			return;
		}
		if (!isFileExists(dst)) {
			if (!dst.createNewFile()) {
				// create fail
				return;
			}
		}
		FileReader fr = null;
		FileWriter fw = null;
		try {
			fr = new FileReader(src);
			fw = new FileWriter(dst);
			char[] buff = new char[1024];
			int len;
			while ((len = fr.read(buff)) != -1) {
				fw.write(buff, 0, len);
			}
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		} finally {
			if (!(closeIO(fr) & closeIO(fw))) {
				throw new IOException("io close error");
			}
		}
	}

	public static void writeString(File file, String str) throws IOException {
		if (!isFileExists(file) || str == null) {
			return;
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(str);
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		} finally {
			if (fw != null) {
				fw.close();
			}
		}
	}

	/**
	 * 读取JSON文件
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readJSONFile(File file) throws IOException {
		if (!isFileExists(file)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		FileReader fr = new FileReader(file);
		char[] buff = new char[1024];
		int len;
		try {
			while ((len = fr.read(buff)) != -1) {
				sb.append(buff, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException(e);
		} finally {
			if (fr != null) {
				fr.close();
			}
		}
		return sb.toString();
	}

	/**
	 * 关闭IO
	 * 
	 * @param io
	 * @return
	 */
	private static boolean closeIO(Closeable io) {
		if (io == null) {
			return false;
		}
		try {
			io.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 检测文件是否存在(file != null && file.exists())
	 * 
	 * @param file
	 * @return 存在返回true,否则false
	 */
	private static boolean isFileExists(File file) {
		return file != null && file.exists();
	}
}
