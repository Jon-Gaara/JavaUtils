package com.yumaolin.util.FileCompression;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
/*
 *用ant中的org.apache.tools.ant.taskdefs.Zip来 进行ZIP文件压缩
 */
public class ZipToolsByAnt {
  private File zipFile;
  
  public ZipToolsByAnt(String pathName){
	  zipFile = new File(pathName);
  }
  public void compress(String srcPathName){
	  File srcDir = new File(srcPathName);
	  if(!srcDir.exists()){
		  throw new RuntimeException(srcPathName+"不存在");
	  }
	  Project project = new Project();
	  Zip zip = new Zip();
	  zip.setProject(project);
	  zip.setDestFile(zipFile);
	  FileSet fileSet = new FileSet();
	  fileSet.setProject(project);
	  if(srcDir.isDirectory()){
		  fileSet.setDir(srcDir);//压缩目录
	  }else{
		  fileSet.setFile(srcDir);//压缩文件
	  }
	  //fileSet.setIncludes("**/*.java");包括哪些文件或文件夹 eg:zip.setIncludes("*.java"); 
	  //fileSet.setExcludes("**/*.java");排除哪些文件或文件夹
	  zip.addFileset(fileSet);
	  zip.execute();
  }
}
