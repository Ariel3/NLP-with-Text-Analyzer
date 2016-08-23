
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.StringTokenizer;

/*
 * 
 */
public class crawling {

	String address, whereToSave;

	public crawling(String address, String whereToSave) throws Exception {
		this.address = address;
		this.whereToSave = whereToSave;
	}

	public void play() throws Exception {
		URL url;
		InputStream is = null;
		DataInputStream dis;
		String line, content = "";
		url = new URL(address);
		is = url.openStream(); // throws an IOException
		dis = new DataInputStream(new BufferedInputStream(is));

		while ((line = dis.readLine()) != null) {
			content += line + "\n";
		}

		getContent(content);
		getLinks(content);
		wirtToFile(address + "\n" + content, "all content");
		is.close();
	}

	public void getContent(String content) throws IOException {
		String toFile = "", temp = "";
		StringTokenizer s = new StringTokenizer(content, "<");
		while (s.hasMoreTokens()) {
			temp = s.nextToken();
			if (temp.startsWith("p>"))
				toFile += temp.substring(2, temp.length()) + "\n";
			if (temp.startsWith("/a> "))
				toFile += temp.substring(3, temp.length()) + "\n";
			if (temp.startsWith("a href=\"") && !temp.endsWith("\">"))
				toFile += temp.split("\">")[1];

		}
		wirtToFile(address + "\n" + toFile, "real content");
	}

	public void getLinks(String content) throws IOException {

		String toFile = "", temp = "";
		StringTokenizer s = new StringTokenizer(content, "<");
		while (s.hasMoreTokens()) {
			temp = s.nextToken();
			if (temp.startsWith("link>"))
				toFile += temp.substring(5, temp.length()) + "\n";

		}
		wirtToFile(address + "\n" + toFile, "more links");
	}

	public void wirtToFile(String content, String filename) throws IOException {
		File file = new File(whereToSave + "/" + filename + ".txt");

		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(content);
		bw.close();
	}

	public static void main(String[] args) throws Exception {
		String address = "http://www.anandtech.com/rss";
		String whereToSave = "G://haifa_project";
		crawling c = new crawling(address, whereToSave);
		c.play();
	}
}
