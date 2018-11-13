package com.patchmng.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileHelper {
	
	public static String getCurrentPath(){
		File file = new File("");		
		String path = file.getAbsolutePath();
		return path;
	}
	
	public static String getCurrentProjectPath(){
		String path = getClassPath();
		//String path = "E:/aliyunProjects/qfmis/out/artifacts/classmate_war_exploded/WEB-INF";
		String filename = StringUtilsEx.leftStr(path, "/WEB-INF");
		return filename;
	}
	
	public static String getClassPath()
    {
		//D:/360yunpan2/workspace1/webAd/build/classes/com/qfmis/libary/file/FileHelper.class
		String strClassName = FileHelper.class.getName();
		String strClassFileName = strClassName.substring(
				strClassName.lastIndexOf(".") + 1, strClassName.length());
		URL url = null;
		url = FileHelper.class.getResource(strClassFileName + ".class");
		String strURL = url.toString();
		strURL = strURL.substring(strURL.indexOf('/') + 1);
		boolean isWin = false;
		int index = strURL.indexOf(":");
		if (index > 0)
			isWin = true;
		if (!isWin)
			return "/" + strURL;
		return strURL;
    }
	
	public static String pathSlash(String path){
		if (path.trim().indexOf("/") != 0)
			return "/" + path;
		else
			return path;
	} 
	
	public static String getClassPathRoot() {
		String currentClassPath = getClassPath();
		return currentClassPath.substring(0, currentClassPath.indexOf("classes") + 7);
	}
	
	public static String loadTxtFile(String fileName) throws IOException {
		return loadTxtFile(new File(fileName));
	}
	
	public static String loadTxtFile(File file) throws IOException {
		String result = "";
		BufferedReader br = null;
		String data = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(file), "UTF-8"));
			while ((data = br.readLine()) != null) {
				result += data;
			}
		} finally {
			if(null != br)
				br.close();
		}
		return result;
	}

	public static void saveTxtFile(String fileName, String context, String charSet)
			throws IOException {
		File f = new File(fileName);		
		String path = f.getParent();
		File dir = new File(path);
		if (!dir.exists()){
			dir.mkdirs();
		}
		if (!f.exists()) {
			f.createNewFile();
		}
		OutputStreamWriter write = new OutputStreamWriter(
				new FileOutputStream(f), charSet);
		BufferedWriter writer = new BufferedWriter(write);
		writer.write(context);
		writer.close();
	}

	public static boolean isExistFile(String fileName) {
		File file = new File(fileName);
		return file.exists() && file.isFile();
	}
	
	
	/**
	 * @param dir
	 * @return 判断目录是否存在
	 */
	public static boolean isExistDir(String dir){
		File file = new File(dir);
		return file.exists() && file.isDirectory();
	}
	
	/**
	 * 如果目录不存在则创建
	 * @param dir
	 */
	public static void makeDirs(String dir){
		File file = new File(dir);
		if (!file.exists()){
			file.mkdirs();
		}		
	}	
	
	public static File saveFile(File file, String fileName) throws Exception{
		File toFile = null; 
		InputStream is = null;  
		OutputStream os = null; 
		try {
			// 基于myFile创建一个文件输入流
			is = new FileInputStream(file);
			// 设置目标文件
			toFile = new File(fileName);
			// 创建一个输出流
			os = new FileOutputStream(toFile);
			// 设置缓存
			byte[] buffer = new byte[1024];  
			int length = 0;
			// 读取myFile文件输出到toFile文件中
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			// 关闭输入流
			is.close();
			// 关闭输出流
			os.close();
		}
		return toFile;
	}
	
	public static void searchFileList(List<String> list, File file){
		list.add(file.getPath());
		File[] fileList = file.listFiles();
		if (fileList != null){
			for (File f : fileList) {
				searchFileList(list, f);
			}	
		}
		
	}

	public static List<String> listFileAbsPaths(File rootFile, String prefix, String suffix, 
			boolean containSubFolder) {
		List<String> absPaths = new ArrayList<String>();
		File[] subFiles = rootFile.listFiles();
		for(File file : subFiles) {
			listFiles(file, prefix, suffix, containSubFolder, absPaths);
		}
		return absPaths;
	}
	
	private static void listFiles(File file, String prefix, String suffix, 
			boolean containSubFolder, List<String> absPaths) {
		if (file.isDirectory() && containSubFolder == true) {
			File[] t = file.listFiles();
			for (int i = 0; i < t.length; i++) {
				listFiles(t[i], prefix, suffix, containSubFolder, absPaths);
			}
		} else if(file.isFile()) {
			String fileName = file.getName();
			if(isPrefixLegal(fileName, prefix) && isSuffixLegal(fileName, suffix)) {
				absPaths.add(file.getAbsolutePath());
			}
		}
	}
	
	private static boolean isPrefixLegal(String fileName, String prefix) {
		if(!StringUtilsEx.isEmpty(prefix)) {
			return fileName.indexOf(prefix) == 0;
		}
		return true;
	}
	
	private static boolean isSuffixLegal(String fileName, String suffix) {
		if (!StringUtilsEx.isEmpty(suffix)) {
			int dotIdx = fileName.lastIndexOf(".");
			String _suffix = "";
			if (dotIdx != -1) {
				_suffix = fileName.substring(dotIdx + 1);
			}
			return _suffix.equals(suffix);
		}
		return true;
	}
	
	public static int unpack(byte[] b) {
		int num = 0;
		for (int i = 0; i < b.length; i++) {
			num = 256 * num + (b[b.length - 1 - i] & 0xff);
		}
		return num;
	}

	/**
	 * 获取文件版本信息
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getVersion(String filePath) {
		String result = "";
		File file = new File(filePath);
		RandomAccessFile raf = null;
		byte[] buffer;
		String str;
		try {
			raf = new RandomAccessFile(file, "r");
			buffer = new byte[64];
			raf.read(buffer);
			str = "" + (char) buffer[0] + (char) buffer[1];
			if (!"MZ".equals(str)) {
				return result;
			}

			int peOffset = unpack(new byte[] { buffer[60], buffer[61],
					buffer[62], buffer[63] });
			if (peOffset < 64) {
				return result;
			}

			raf.seek(peOffset);
			buffer = new byte[24];
			raf.read(buffer);
			str = "" + (char) buffer[0] + (char) buffer[1];
			if (!"PE".equals(str)) {
				return result;
			}
			int machine = unpack(new byte[] { buffer[4], buffer[5] });
			if (machine != 332) {
				return result;
			}

			int noSections = unpack(new byte[] { buffer[6], buffer[7] });
			int optHdrSize = unpack(new byte[] { buffer[20], buffer[21] });
			raf.seek(raf.getFilePointer() + optHdrSize);
			boolean resFound = false;
			for (int i = 0; i < noSections; i++) {
				buffer = new byte[40];
				raf.read(buffer);
				str = "" + (char) buffer[0] + (char) buffer[1]
						+ (char) buffer[2] + (char) buffer[3]
						+ (char) buffer[4];
				if (".rsrc".equals(str)) {
					resFound = true;
					break;
				}
			}
			if (!resFound) {
				return result;
			}

			int infoVirt = unpack(new byte[] { buffer[12], buffer[13],
					buffer[14], buffer[15] });
			int infoSize = unpack(new byte[] { buffer[16], buffer[17],
					buffer[18], buffer[19] });
			int infoOff = unpack(new byte[] { buffer[20], buffer[21],
					buffer[22], buffer[23] });
			raf.seek(infoOff);
			buffer = new byte[infoSize];
			raf.read(buffer);
			int numDirs = unpack(new byte[] { buffer[14], buffer[15] });
			boolean infoFound = false;
			int subOff = 0;
			for (int i = 0; i < numDirs; i++) {
				int type = unpack(new byte[] { buffer[i * 8 + 16],
						buffer[i * 8 + 17], buffer[i * 8 + 18],
						buffer[i * 8 + 19] });
				if (type == 16) { // FILEINFO resource
					infoFound = true;
					subOff = unpack(new byte[] { buffer[i * 8 + 20],
							buffer[i * 8 + 21], buffer[i * 8 + 22],
							buffer[i * 8 + 23] });
					break;
				}
			}
			if (!infoFound) {
				return result;
			}

			subOff = subOff & 0x7fffffff;
			infoOff = unpack(new byte[] { buffer[subOff + 20],
					buffer[subOff + 21], buffer[subOff + 22],
					buffer[subOff + 23] }); // offset of first FILEINFO
			infoOff = infoOff & 0x7fffffff;
			infoOff = unpack(new byte[] { buffer[infoOff + 20],
					buffer[infoOff + 21], buffer[infoOff + 22],
					buffer[infoOff + 23] }); // offset to data
			int dataOff = unpack(new byte[] { buffer[infoOff],
					buffer[infoOff + 1], buffer[infoOff + 2],
					buffer[infoOff + 3] });
			dataOff = dataOff - infoVirt;

			int version1 = unpack(new byte[] { buffer[dataOff + 48],
					buffer[dataOff + 48 + 1] });
			int version2 = unpack(new byte[] { buffer[dataOff + 48 + 2],
					buffer[dataOff + 48 + 3] });
			int version3 = unpack(new byte[] { buffer[dataOff + 48 + 4],
					buffer[dataOff + 48 + 5] });
			int version4 = unpack(new byte[] { buffer[dataOff + 48 + 6],
					buffer[dataOff + 48 + 7] });
			result = version2 + "." + version1 + "." + version4 + "."
					+ version3;
			return result;

		} catch (FileNotFoundException e) {
			return result;
		} catch (IOException e) {
			return result;
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
				}
			}
		}		 
	}
	
	/**
	 * @param file
	 * @return 取得文件的最后修改时间
	 */
	public static long getFileLastModifiedTime(String file) {
		File f = new File(file);
		long modifiedTime = f.lastModified();
		Date d = new Date(modifiedTime);
		return DateTimeUtils.datetime14long(d);
	}

    public static File saveMultiPartFile(MultipartFile myFile, String path, String fielname) throws IOException {
        File targetFile = new File(path, fielname);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        myFile.transferTo(targetFile);
		return targetFile;
    }

	public static boolean deleteDir(String path, List<String> logs){
		File file = new File(path);
		if(!file.exists()){//判断是否待删除目录是否存在
			System.err.println("The dir are not exists!");
			logs.add("目录不存在：" + path);
			return false;
		}
		String[] content = file.list();//取得当前目录下所有文件和文件夹
		for(String name : content){
			File temp = new File(path, name);
			if(temp.isDirectory()){//判断是否是目录
				deleteDir(temp.getAbsolutePath(), logs);//递归调用，删除目录里的内容
				if (!temp.delete()) {//删除空目录
					logs.add("目录删除失败：" + path);
				}
			}else{
				if(!temp.delete()){//直接删除文件
					System.err.println("Failed to delete " + name);
					logs.add("文件删除失败：" + name);
				}
			}
		}
		return true;
	}

    public static void main(String[] args) {
		try {
			String path = "D:/source/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/webAd/WEB-INF/classes/com/qfmis/model/config/datasource";
			List<String> list = new ArrayList<String>();
			searchFileList(list, new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String filePath = "D:\\source\\delphi\\yishi\\output\\qfdr.exe";
		System.out.println(getVersion(filePath));
		filePath = "D:\\source\\delphi\\yishi\\output\\dpkComm.bpl";
		System.out.println(getVersion(filePath));
	}


}
