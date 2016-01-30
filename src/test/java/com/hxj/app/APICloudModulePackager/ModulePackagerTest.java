package com.hxj.app.APICloudModulePackager;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.hxj.app.pack.ModulePackager;
import com.hxj.app.pack.ex.PackException;
import com.hxj.common.file.FileUtils;

public class ModulePackagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDoPack() throws PackException {
		final String outputPath = "C:/Users/Hxuejie/Desktop/TestModule";
		final String moudleName = "cameraDemoModule";
		final String projectPath = "D:/Develop/Projects/CameraDemo";
		final File moduldFile = new File(projectPath + "/assets/uzmap/module.json");
		final File manifestFile = new File(projectPath + "/AndroidManifest.xml");
		final List<File> srcList = new ArrayList<>();
		srcList.add(new File("D:/Develop/Projects/CameraDemo/src/com/hxj/app/camerademo/CameraActivity.java"));
		srcList.add(new File("D:/Develop/Projects/CameraDemo/src/com/hxj/app/camerademo/CameraDemoModule.java"));
		srcList.add(new File("D:/Develop/Projects/CameraDemo/src/com/hxj/app/camerademo/ColorPickerDialog.java"));

		ModulePackager mp = new ModulePackager();
		mp.setOutputPath(outputPath);
		mp.setProjectPath(projectPath);
		mp.setModuleName(moudleName);
		mp.setModuleFile(moduldFile);
		mp.setManifestFile(manifestFile);
		mp.doPack();

		// check
		assertTrue(FileUtils.isDirExists(outputPath));
		assertTrue(FileUtils.isDirExists(outputPath + "/res_" + moudleName));
		assertTrue(FileUtils.isFileExists(outputPath + "/res_" + moudleName + "/AndroidManifest.xml"));
		assertTrue(FileUtils.isDirExists(outputPath + "/source"));
		assertTrue(FileUtils.isDirExists(outputPath + "/target"));
		assertTrue(FileUtils.isFileExists(outputPath + "/module.json"));
	}

}
