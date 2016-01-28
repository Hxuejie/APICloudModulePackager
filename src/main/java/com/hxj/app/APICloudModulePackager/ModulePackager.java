package com.hxj.app.APICloudModulePackager;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.hxj.common.file.FileUtils;

/**
 * APICloud模块打包
 * 
 * @author Hxuejie hxuejie@126.com
 */
public class ModulePackager {

	/**
	 * 项目路径
	 */
	private String		projectPath;
	/**
	 * 模块名称
	 */
	private String		moduleName;
	/**
	 * 打包输出路径
	 */
	private String		outputPath;
	/**
	 * 源码列表
	 */
	private List<File>	sourceList;
	/**
	 * 引用库列表
	 */
	private List<File>	libList;
	/**
	 * 资源列表
	 */
	private List<File>	resList;
	/**
	 * module.json
	 */
	private File		moduleFile;

	/**
	 * 执行模块打包
	 */
	public void doPack() {
		try {
			// 1.创建输出目录结构
			File outFile = new File(outputPath);
			if (outFile.exists()) {
				FileUtils.deleteDirFiles(outFile);
			} else {
				if (!outFile.createNewFile()) {
					// create fail
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
			// 2.4 module.json
			FileUtils.copyFile(this.moduleFile, moduleFile);
			String str = FileUtils.readJSONFile(this.moduleFile);
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
}
