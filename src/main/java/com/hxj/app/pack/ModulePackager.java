package com.hxj.app.pack;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hxj.app.pack.ex.PackException;
import com.hxj.common.file.FileUtils;

/**
 * APICloud模块打包
 * 
 * @author Hxuejie hxuejie@126.com
 */
public class ModulePackager {

	private final Logger	LOG	= LoggerFactory.getLogger(ModulePackager.class);

	/**
	 * 项目路径
	 */
	private String			projectPath;
	/**
	 * 模块名称
	 */
	private String			moduleName;
	/**
	 * 打包输出路径
	 */
	private String			outputPath;
	/**
	 * 源码列表
	 */
	private List<File>		sourceList;
	/**
	 * 引用库列表
	 */
	private List<File>		libList;
	/**
	 * 资源列表
	 */
	private List<File>		resList;
	/**
	 * module.json
	 */
	private File			moduleFile;
	/**
	 * AndroidManifest.xml
	 */
	private File			manifestFile;

	/**
	 * 执行模块打包
	 * @throws PackException 
	 */
	public void doPack() throws PackException {
		LOG.info("do pack");
		checkPackInfo();
		try {
			// 1.创建输出目录结构
			File outFile = new File(outputPath);
			if (outFile.exists() && outFile.isDirectory()) {
				LOG.debug("out dir exists, delete inner files.");
				FileUtils.deleteDirFiles(outFile);
			} else {
				LOG.debug("create out dir");
				if (!outFile.mkdirs()) {
					// create fail
					LOG.error("out dir create fail");
					return;
				}
			}
			// 1.1 source
			File srcFile = new File(outFile, "source");
			srcFile.mkdir();
			// 1.2 target
			File targetFile = new File(outFile, "target");
			targetFile.mkdir();
			// 1.3 res_(moduleName)
			File resFile = new File(outFile, "res_" + moduleName);
			resFile.mkdir();
			// 1.4 module.json
			File moduleFile = new File(outFile, "module.json");
			moduleFile.createNewFile();

			// 2.复制文件
			// 2.3 res
			FileUtils.copyFile(this.manifestFile, new File(resFile,this.manifestFile.getName()));
			// 2.4 module.json
			FileUtils.copyFile(this.moduleFile, moduleFile);
			String str = FileUtils.readFileToString(this.moduleFile);
			JsonElement json = new JsonParser().parse(str);
			JsonElement modules = json.getAsJsonObject().get("modules");
			if (modules != null) {
				JsonArray moduleArray = modules.getAsJsonArray();
				for (int i = 0; i < moduleArray.size(); ++i) {
					JsonObject module = moduleArray.get(i).getAsJsonObject();
					JsonElement moduleName = module.get("name");
					if (moduleName == null) {
						continue;
					}
					if (!this.moduleName.equals(moduleName.getAsString())) {
						continue;
					}
					FileUtils.writeString(moduleFile, module.toString());
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 检查打包信息
	 * @return
	 * @throws PackException 
	 */
	private void checkPackInfo() throws PackException {
		if (!FileUtils.isDirExists(projectPath)) {
			throw new PackException("error project path:" + projectPath);
		}
		if (!FileUtils.isFileExists(moduleFile)) {
			throw new PackException("can not file module.json:" + moduleFile);
		}
		if (!FileUtils.isFileExists(manifestFile)) {
			throw new PackException("can not file AndroidManifest.xml:" + manifestFile);
		}
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public List<File> getSourceList() {
		return sourceList;
	}

	public void setSourceList(List<File> sourceList) {
		this.sourceList = sourceList;
	}

	public List<File> getLibList() {
		return libList;
	}

	public void setLibList(List<File> libList) {
		this.libList = libList;
	}

	public List<File> getResList() {
		return resList;
	}

	public void setResList(List<File> resList) {
		this.resList = resList;
	}

	public File getModuleFile() {
		return moduleFile;
	}

	public void setModuleFile(File moduleFile) {
		this.moduleFile = moduleFile;
	}

	public File getManifestFile() {
		return manifestFile;
	}

	public void setManifestFile(File manifestFile) {
		this.manifestFile = manifestFile;
	}
	
	
	
	
}
