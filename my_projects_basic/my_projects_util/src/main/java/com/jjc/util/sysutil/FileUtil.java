package com.jjc.util.sysutil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具
 * @Title:
 * @Description:
 * @Company:
 * @Author:yctong
 * @Created Date:2020年4月23日
 */
public class FileUtil {

	public static String readFileByLines(InputStream is) {
		BufferedReader reader = null;
		StringBuffer sb = new StringBuffer();
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				sb.append(tempString + "\n");
			}
			reader.close();
		} catch (IOException localIOException2) {
			localIOException2.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException localIOException3) {
				}
		}
		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> readFileToList(File file) {
		BufferedReader reader = null;
		List list = new ArrayList();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				list.add(tempString);
			}
			reader.close();
		} catch (IOException localIOException2) {
			localIOException2.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException localIOException3) {
				}
		}
		return list;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> readFileToList(File file, String encodType) {
		BufferedReader reader = null;
		List list = new ArrayList();
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encodType));

			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				if ((tempString.charAt(0) < 'a') || (tempString.charAt(0) > 'z'))
					tempString = tempString.substring(1);
				list.add(tempString);
			}
			reader.close();
		} catch (IOException localIOException2) {
			localIOException2.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException localIOException3) {
				}
		}
		return list;
	}

	public static void writeFile(File file, String content, Boolean flag) {
		try {
			if (!file.exists())
				file.createNewFile();
			FileWriter writer = new FileWriter(file, flag.booleanValue());
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeFile(File file, String content, Boolean flag, String encodType) {
		try {
			FileOutputStream writerStream = new FileOutputStream(file, flag.booleanValue());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, encodType));

			writer.write(content);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copyFolder(String oldPath, String newPath) {
		try {
			new File(newPath).mkdirs();
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; ++i) {
				if (oldPath.endsWith(File.separator))
					temp = new File(oldPath + file[i]);
				else {
					temp = new File(oldPath + File.separator + file[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);

					FileOutputStream output = new FileOutputStream(newPath + "/" + temp.getName().toString());
					byte[] b = new byte[5120];

					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory())
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void reName(String oldName, String newName) {
		File oldF = new File(oldName);
		File newF = new File(newName);
		oldF.renameTo(newF);
	}

	public static void copyFilesFromList(String listFile, String targetFloder) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(listFile));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				copyFile(tempString, targetFloder);
			}
			reader.close();
		} catch (IOException localIOException2) {
			localIOException2.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException localIOException3) {
				}
		}
	}

	public static void copyFile(String oldPath, String newPath) {
		try {
			File temp = new File(oldPath);
			FileInputStream input = new FileInputStream(temp);

			FileOutputStream output = new FileOutputStream(newPath + "/" + temp.getName().toString());
			byte[] b = new byte[5120];

			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean deleteFiles(List<String> files) {
		boolean flag = true;
		for (String file : files) {
			flag = delete(file);
			if (!flag)
				break;
		}
		return flag;
	}

	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			return false;
		}
		if (file.isFile()) {
			return deleteFile(fileName);
		}
		return deleteDirectory(fileName);
	}

	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if ((file.exists()) && (file.isFile()))
			return file.delete();
		return false;
	}

	public static boolean deleteDirectory(String dir) {
		if (!dir.endsWith(File.separator))
			dir = dir + File.separator;
		File dirFile = new File(dir);
		if ((!dirFile.exists()) || (!dirFile.isDirectory()))
			return false;
		boolean flag = true;
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; ++i) {
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (flag)
					continue;
				break;
			}
			if (files[i].isDirectory()) {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag) {
			return false;
		}
		return dirFile.delete();
	}
}
