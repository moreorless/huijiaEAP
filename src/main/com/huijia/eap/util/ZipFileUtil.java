package com.huijia.eap.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;


/**
 * Zip文件工具
 * <p>
 * 依赖于org.apache.tools.zip包，解决Java内置Zip类不支持中文文件名的问题
 * </p>
 * 
 */
public class ZipFileUtil
{
	/**
	 * 压缩文件，<b>注意使用GBK编码格式解析ZIP文件中的文件名，详见{@link #zip(File[], OutputStream, String)}</b>
	 * @param file 待压缩文件
	 * @param output 压缩后的输出流
	 * @throws IOException
	 */
	public static final void zip(File file, OutputStream output) throws IOException {
		zip(new File[] {file}, output, null, null);
	}
	
	/**
	 * 压缩文件，<b>注意使用GBK编码格式解析ZIP文件中的文件名，详见{@link #zip(File[], OutputStream, String)}</b>
	 * @param files 待压缩文件数组
	 * @param output 压缩后的输出流
	 * @throws IOException
	 */
	public static final void zip(File[] files, OutputStream output) throws IOException {
		zip(files, output, null, null);
	}
	
	/**
	 * 压缩文件
	 * @param files 待压缩文件数组
	 * @param output 压缩后的输出流
	 * @param encoding ZIP文件中文件名的编码方式，如不指定则使用GBK，以尽可能适应中文文件名
	 * @throws IOException
	 */
	public static final void zip(File[] files, OutputStream output, String encoding) throws IOException {
		zip(files, output, encoding, null);
	}
	
	/**
	 * 压缩文件
	 * @param files 待压缩文件数组
	 * @param output 压缩后的输出流
	 * @param entry zip文件的entry，null时为根入口
	 * @throws IOException
	 */
	private static final void zip(File[] files, OutputStream output, String encoding, String entry) throws IOException
	{
		boolean created = false;
		ZipOutputStream zout = null;
		if (output instanceof ZipOutputStream)
		{
			zout = (ZipOutputStream) output;
		}
		else
		{
			zout = new ZipOutputStream(output);
			created = true;
		}
		
		if (encoding != null)
			zout.setEncoding(encoding);
		else
			zout.setEncoding("GBK");

		for (int i = 0; i < files.length; i++)
		{
			File file = files[i];
			if (!file.exists())
				continue;
			if (file.isFile())
			{
				if (entry == null || entry.length() == 0)
				{
					zout.putNextEntry(new ZipEntry(file.getName()));
				}
				else
				{
					zout.putNextEntry(new ZipEntry(entry + "/"
							+ file.getName()));
				}
				FileInputStream fin = new FileInputStream(file);
				byte[] buf = new byte[1024];
				int m;
				while ((m = fin.read(buf)) != -1)
				{
					zout.write(buf, 0, m);
				}
				try
				{
					fin.close();
				}
				catch (IOException e)
				{
					//
				}
			}
			else if (file.isDirectory())
			{
				File[] subFiles = file.listFiles();
				String parent = null;
				if (entry == null || entry.length() == 0)
				{
					parent = file.getName();
					zout.putNextEntry(new ZipEntry(parent + "/"));
				}
				else
				{
					parent = entry + "/" + file.getName();
					zout.putNextEntry(new ZipEntry(parent + "/"));
				}
				zip(subFiles, zout, encoding, parent);
			}
		}
		try
		{
			if (created)
				zout.close();
		}
		catch (IOException e)
		{
			//
		}
	}
	
	/**
	 * 解压zip文件，<b>注意使用GBK编码格式解析ZIP文件中的文件名，详见{@link #unzip(File, File, String)}</b>
	 * @param zipFile 待解压的zip文件
	 * @param outputPath 解压后存放的路径
	 * @return
	 * @throws IOException
	 */
	public static final Set<File> unzip(File zipFile, File outputPath) throws IOException {
		return unzip(zipFile, outputPath, null);
	}
	
	/**
	 * 解压zip文件
	 * @param zipFile 待解压的zip文件
	 * @param outputPath 解压后存放的路径
	 * @param encoding ZIP文件中文件名的编码方式，如不指定则使用GBK，以尽可能适应中文文件名
	 * @throws IOException
	 */
	public static final Set<File> unzip(File zipFile, File outputPath, String encoding) throws IOException
	{
		if (zipFile == null || !zipFile.exists() || !zipFile.isFile())
			throw new IOException("No such file or file is not a directory");
		if (outputPath != null && !outputPath.exists())
			outputPath.mkdir();
		
		ZipFile zf;
		try
		{
			zf = new ZipFile(zipFile, encoding == null ? "GBK" : encoding);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw new IOException("No such zip file:" + zipFile.getAbsolutePath());
		}
		Enumeration<?> e = zf.getEntries();
		ZipEntry ze;
		InputStream is = null;
		File entryFile;
		FileOutputStream fos = null;
		Set<File> result = new LinkedHashSet<File>();
		while (e.hasMoreElements())
		{
			ze = (ZipEntry) e.nextElement();
			String entryName = ze.getName();
			if (entryName.endsWith("/"))
				entryName = entryName.substring(0, entryName.length() - 1);
			StringBuffer sb = new StringBuffer();
			String[] dirEntries = entryName.split("/");
			for (int i = 0; i < dirEntries.length - 1; i++)
			{
				sb.append(dirEntries[i]).append(File.separator);
				entryFile = new File(outputPath.getPath() + File.separator + sb.toString());
				entryFile.mkdir();
				result.add(entryFile);
			}
			if (ze.isDirectory())
			{
				sb.append(dirEntries[dirEntries.length - 1]).append(File.separator);
				entryFile = new File(outputPath.getPath() + File.separator + sb.toString());
				entryFile.mkdir();
				result.add(entryFile);
			}
			else
			{
				entryFile = new File(outputPath.getPath() + File.separator + ze.getName());
				if (!entryFile.getParentFile().exists())
					entryFile.getParentFile().mkdirs();
				try
				{
					entryFile.createNewFile();
					fos = new FileOutputStream(entryFile);
					is = zf.getInputStream(ze);
					int len;
					byte[] buf = new byte[1024];
					while ((len = is.read(buf)) != -1)
						fos.write(buf, 0, len);
				}
				catch (IOException ex)
				{//FileNotFoundException, ZipException, IOException
					File[] files = outputPath.listFiles();
					for (int i = 0; i < files.length; i++)
						files[i].delete();
					throw ex;
				}
				finally
				{
					try
					{
						if (fos != null)
							fos.close();
						if (is != null)
							is.close();
					}
					catch (IOException e1)
					{
						//
					}
				}
				result.add(entryFile);
			}
		}
		try {
			if(zf != null) {
				zf.close();
				zf = null;
			}
		}catch(IOException ioe) {
			//
		}
		return result;
	}
}
