package pimpmykerberos.beans;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

import pimpmykerberos.core.Core;
import pimpmykerberos.utils.Fonctions;

public class User {

	String description;
	String email;
	String name;
	String password;

	Map<String, Camera> cameras = new HashMap<String, Camera>();

	public User() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void saveUser() {
		XStream aStream = new XStream();
		try {
			File aFile = new File(System.getProperty("dataPath", Core.getInstance().getDataPath()));
			Fonctions.trace("DBG",
					"Saving file to " + aFile.getAbsolutePath() + "/pimpMyKerberos_user_" + name + ".xml", "CORE");
			aStream.toXML(this, new FileWriter(aFile.getAbsolutePath() + "/pimpMyKerberos_user_" + name + ".xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadUsers() {
		if ( ! Core.getInstance().getUsers().containsKey("admin"))
				{
				Core.getInstance().getUsers().put("admin", getAdminUser());
				}
	}

	public static User getAdminUser() {
		User admin = new User();
		admin.setDescription("Administrator");
		admin.setName("admin");
		admin.setPassword(Core.getInstance().getAdminPassword());
		return admin;

	}

	public void deleteUser() {
		File aFile = new File(name + ".xml");
		aFile.delete();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Map<String, Camera> getCameras() {
		return cameras;
	}

	public void setCameras(Map<String, Camera> cameras) {
		this.cameras = cameras;
	}

	public static boolean isAuthentified(String user, String passwd) {
		User aUser = Core.getInstance().getUsers().get(user);
		if (aUser != null && passwd != null && passwd.equals(aUser.getPassword())) {
			return true;
		}
		return false;
	}

}
