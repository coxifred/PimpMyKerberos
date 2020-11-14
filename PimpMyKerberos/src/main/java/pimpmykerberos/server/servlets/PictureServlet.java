package pimpmykerberos.server.servlets;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jcodec.api.FrameGrab;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import pimpmykerberos.beans.Camera;
import pimpmykerberos.core.Core;
import pimpmykerberos.utils.Fonctions;
import pimpmykerberos.utils.MJpegReaderRunner;

public class PictureServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 474719652648270932L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) {

		String camName = request.getParameter("camName");
		// jpg or mp4 (optional)
		String type = request.getParameter("type");
		File aFile = new File(Core.getInstance().getKerberosioPath() + "/" + camName + "/capture");
		if (aFile.exists()) {
			Camera aCamera = Core.getInstance().getUsers().get("admin").getCameras().get(camName);
			if (aCamera != null) {
				if (type != null && !"".equals(type) && "live".equals(type)) {
					performLive(aCamera,request,response);
				} else {

					if (aCamera.getTimeToFile().size() > 0) {
						File lastFile = new File(aCamera.getTimeToFile().lastEntry().getValue());
						if (type != null && !"".equals(type)) {
							lastFile = foundLastType(type, aCamera);
						} else {
							lastFile = new File(aCamera.getTimeToFile().lastEntry().getValue());
						}

						Fonctions.trace("DBG", "Last File is " + lastFile.getAbsolutePath(), "PictureServlet");
						if (lastFile != null && lastFile.exists()) {
							if (lastFile.getAbsolutePath().endsWith(".jpg")) {
								performJpg(lastFile, request, response);
							} else {
								performMp4(lastFile, request, response);
							}

						} else {
							Fonctions.trace("ERR", "Last File " + lastFile.getAbsolutePath() + " is missing",
									"PictureServlet");
						}

					} else {
						Fonctions.trace("ERR", "Camera " + camName + " not yet populated", "PictureServlet");
					}
				}
			} else {
				Fonctions.trace("ERR", "Camera " + camName + " not exist", "PictureServlet");
			}

		} else {
			Fonctions.trace("ERR", "File " + aFile.getAbsolutePath() + " exists but contain no file", "PictureServlet");
		}

//		// Try to look if it's a local file
//		File localFile = new File(url);
//		if (localFile.exists()) {
//			Fonctions.trace("DBG", "It's a local file " + url + " trying to diplay", "PictureServlet");
//			try {
//				out = response.getOutputStream();
//				InputStream fin = new FileInputStream(url);
//				out = response.getOutputStream();
//				int ch = 0;
//				;
//				while ((ch = fin.read()) != -1) {
//					out.write(ch);
//				}
//
//				fin.close();
//
//				out.close();
//			} catch (Exception e) {
//				Fonctions.trace("ERR", "Can't display local file " + url, "PictureServlet");
//			}
//
//		} else {
//
//			try {
//				out = response.getOutputStream();
//
//				Fonctions.trace("DBG", "Start  to " + url, "PictureServlet");
//				InputStream fin = new URL(url).openStream();
//				Random aRandom = new Random();
//				File outputFile = new File(
//						System.getProperty("java.io.tmpdir") + "/pimpMyKerberos" + aRandom.nextInt());
//				OutputStream outStream = new FileOutputStream(outputFile, false);
//
//				int read;
//				int totalRead = 0;
//				int maxRead = 1000000;
//				byte[] bytes = new byte[maxRead];
//				while ((read = fin.read(bytes)) < maxRead) {
//					if (totalRead >= maxRead) {
//						break;
//					}
//					totalRead += read;
//					Fonctions.trace("DBG", "Read " + totalRead + " bytes / " + maxRead, "PictureServlet");
//
//				}
//				Fonctions.trace("DBG", "Writing to temporary file " + outputFile.getAbsolutePath(), "PictureServlet");
//				outStream.write(bytes, 0, read);
//				outStream.close();
//				Picture picture = FrameGrab.getFrameFromFile(outputFile, 1);
//				outputFile.delete();
//				fin.close();
//
//				out.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//
//			}
	}

	private File foundLastType(String type, Camera aCamera) {
		File aFile = null;
		TreeMap<Long, String> treeMap = aCamera.getTimeToFile();
			List<String> aReverseList = new ArrayList<String>(treeMap.values());
			Collections.reverse(aReverseList);
			for (String fileName : aReverseList) {
				if (fileName.endsWith(type) && new File(fileName).exists()) {
					aFile = new File(fileName);
					break;
				}
			}
		
		return aFile;
	}

	
	private void performLive(Camera aCamera, HttpServletRequest request, HttpServletResponse response) {
		MJpegReaderRunner reader=new MJpegReaderRunner();
		String url = "http://" + aCamera.getIp() + ":" + aCamera.getBroadcastPort();
		InputStream fin=null;
		try {
			fin = new URL(url).openStream();
			reader.init(fin);
			reader.start();
			response.setContentType("image/jpeg");
			ServletOutputStream out = response.getOutputStream();
			Fonctions.attendre(300);
			reader.getJpgOut().writeTo(out);
			out.flush();
		} catch (Exception e) {
			Fonctions.trace("ERR", "Can't extract jpeg from live stream " + url + " error:" + e.getMessage(),
					"PictureServlet");
			e.printStackTrace();
		} finally
		{
			try {
				if ( fin != null) 
				{
				fin.close();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private void performJpg(File lastFile, HttpServletRequest request, HttpServletResponse response) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(lastFile);
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("image/jpeg");
			int ch = 0;
			while ((ch = fis.read()) != -1) {
				out.write(ch);
			}
			fis.close();
		} catch (Exception e) {
			Fonctions.trace("ERR", "Can't read " + lastFile.getAbsolutePath() + " error:" + e.getMessage(),
					"PictureServlet");
		}

	}

	private void performMp4(File lastFile, HttpServletRequest request, HttpServletResponse response) {
		try {
			Picture picture = FrameGrab.getFrameFromFile(lastFile, 1);
			BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
			ServletOutputStream out = response.getOutputStream();
			response.setContentType("image/jpeg");
			ImageIO.write(bufferedImage, "jpeg", out);
			out.flush();
		} catch (Exception e) {
			Fonctions.trace("ERR", "Can't read " + lastFile.getAbsolutePath() + " error:" + e.getMessage(),
					"PictureServlet");
		}

	}

}
