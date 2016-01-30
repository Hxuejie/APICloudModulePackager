package com.hxj.common.file;

import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hxj.common.text.StringUtils;

/**
 * 文件工具类
 * 
 * @author Hxuejie hxuejie@126.com
 */
public class FileUtils {

	private static final Logger	log	= LoggerFactory.getLogger(FileUtils.class);

	/**
	 * 删除文件夹
	 * 
	 * @param dir 文件夹
	 * @return 返回删除结果：true成功,false失败
	 */
	public static boolean deleteDir(File dir) {
		if (!isExists(dir) || !dir.isDirectory()) {
			return false;
		}
		// delete files
		boolean result = deleteDirFiles(dir);
		// delete dir
		log.debug("delete dir: {}", dir);
		return dir.delete() & result;
	}

	/**
	 * 删除目录下的所有文件
	 * 
	 * @param file
	 * @return 返回删除结果：true成功,false失败
	 */
	public static boolean deleteDirFiles(File file) {
		if (!isExists(file)) {
			return false;
		}
		if (file.isFile()) {
			log.debug("delete file:{}", file);
			return file.delete();
		}
		boolean result = true;
		File[] files = file.listFiles();
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
		if (!isExists(src)) {
			return;
		}
		if (!isExists(dst)) {
			if (!dst.createNewFile()) {
				// create fail
				log.error("create target file fail: {}", dst);
				return;
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("copy file: {} --> {}", src, dst);
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
			log.error("copy fail", e);
			throw new IOException(e);
		} finally {
			if (!(closeIO(fr) & closeIO(fw))) {
				throw new IOException("io close error");
			}
		}
	}

	/**
	 * 写字符串到文件
	 * 
	 * @param file
	 * @param str
	 * @throws IOException
	 */
	public static void writeString(File file, String str) throws IOException {
		if (!isExists(file) || str == null) {
			return;
		}
		if (log.isDebugEnabled()) {
			log.debug("write string to file: {} --> {}", str, file);
		}
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(str);
			fw.flush();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("write fail", e);
			throw new IOException(e);
		} finally {
			if (fw != null) {
				fw.close();
			}
		}
	}

	/**
	 * 读取文件为字符串
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(File file) throws IOException {
		if (!isExists(file)) {
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
			log.error("read fail", e);
			throw new IOException(e);
		} finally {
			if (fr != null) {
				fr.close();
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("read file to string: {} --> {}", file, sb.toString());
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
	 * 检测文件/文件夹是否存在
	 * 
	 * @param file
	 * @return 存在返回true,否则false
	 */
	public static boolean isExists(File file) {
		return file != null && file.exists();
	}

	/**
	 * 检测文件夹是否存在
	 * 
	 * @param dir
	 * @return 存在返回true,否则false
	 */
	public static boolean isDirExists(File dir) {
		return isExists(dir) && dir.isDirectory();
	}

	/**
	 * 检测文件夹是否存在
	 * 
	 * @param path 文件夹路径
	 * @return 存在返回true,否则false
	 */
	public static boolean isDirExists(String path) {
		if (StringUtils.isEmpty(path)) {
			return false;
		}
		File dir = new File(path);
		return isDirExists(dir);
	}

	/**
	 * 检测文件是否存在
	 * 
	 * @param file
	 * @return 存在返回true,否则false
	 */
	public static boolean isFileExists(File file) {
		return isExists(file) && file.isFile();
	}

	/**
	 * 检测文件是否存在
	 * 
	 * @param path 文件路径
	 * @return 存在返回true,否则false
	 */
	public static boolean isFileExists(String path) {
		if (StringUtils.isEmpty(path)) {
			return false;
		}
		File file = new File(path);
		return isFileExists(file);
	}

}
