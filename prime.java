
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class prime {

	LinkedList<String> words;
	String address, whereToSave;

	/**
	 * Read the contents of the file passed and store the contents into a string
	 * array
	 */
	public prime(String split, String address, String whereToSave) throws IOException {
		// String w[] = readFile(path, Charset.defaultCharset()).split(split);
		this.address = address;
		this.whereToSave = whereToSave;
		words = new LinkedList<String>();

		URL url;
		InputStream is = null;
		DataInputStream dis;
		String line, content = "";
		url = new URL(address);
		is = url.openStream();
		dis = new DataInputStream(new BufferedInputStream(is));
		while ((line = dis.readLine()) != null) {
			content += line + "\n";
		}

		String w[] = getContent(content).split(split);

		for (int i = 0; i < w.length; i++) {
			words.add(w[i]);
		}
		for (int i = 0; i < words.size(); i++) {
			if (words.get(i).equals("") || words.get(i).equals("-") || words.get(i).equals("&"))
				words.remove(i);
		}

		wirtToFile(address + "\n" + getContent(content) + "\n" + print(), "content");
		is.close();

		LinkedList<String> nextLinks = getLinks(content);
		Scanner sc;
		String newPath = "";

		for (int i = 0; i < nextLinks.size(); i++) {
			// sc = new Scanner(System.in); // Reading from System.in
			// System.out.println("continue?\n");
			// if (sc.next().equals("yes")) {
			newPath = whereToSave + "/" + i;
			new File(newPath).mkdir();

			System.out.println(newPath);

			new prime(split, nextLinks.get(i), newPath);
			// }

		}
	}

	/**
	 * Analyze the Text in 1 word, 2 word together and so on..
	 */

	/*
	 * Calculate the appearance of each word
	 */
	public static String cal(LinkedList<String> w) {
		MyComparator m = new MyComparator();
		String wirtToFile = "";
		LinkedList<node> n = new LinkedList<node>();
		Collections.sort(w);
		int count = 1;
		for (int i = 0; i < w.size(); i++) {
			String s = w.get(i);
			for (int j = i + 1; j < w.size(); j++) {
				if (s.equals(w.get(j))) {
					count++;
					i = j + 1;
				} else
					break;
			}
			if (count > 1) {
				// System.out.println(count + " " + s);
				n.add(new node(s, count));
			}
			count = 1;
		}
		Collections.sort(n, m);
		for (node a : n) {
			// System.out.println(a.count + " " + a.string);
			wirtToFile += a.count + " " + a.string + "\n";
		}
		return wirtToFile;
	}

	public String print() {
		LinkedList<String> temp = new LinkedList<String>();
		int lenght = words.size();
		String wirtToFile = "";
		System.out.println();

		// -----1 word------
		for (int i = 0; i < lenght; i++) {

			temp.add(words.get(i));
		}
		wirtToFile += "-----1 word------\n" + cal(temp) + "\n";

		// -----2 word------
		temp.clear();
		for (int i = 0; i < lenght - 1; i += 2) {

			temp.add(words.get(i) + " " + words.get(i + 1));
			if (i + 2 < lenght)
				temp.add(words.get(i + 1) + " " + words.get(i + 2));
		}
		wirtToFile += "-----2 word------\n" + cal(temp) + "\n";

		// -----3 word------
		temp.clear();
		for (int i = 0; i < lenght - 2; i += 3) {
			temp.add(words.get(i) + " " + words.get(i + 1) + " " + words.get(i + 2));
			if (i + 3 < lenght)
				temp.add(words.get(i + 1) + " " + words.get(i + 2) + " " + words.get(i + 3));
			if (i + 4 < lenght)
				temp.add(words.get(i + 2) + " " + words.get(i + 3) + " " + words.get(i + 4));
		}
		wirtToFile += "-----3 word------\n" + cal(temp) + "\n";

		// -----4 word------
		temp.clear();
		for (int i = 0; i < lenght - 3; i += 4) {

			temp.add(words.get(i) + " " + words.get(i + 1) + " " + words.get(i + 2) + " " + words.get(i + 3));
			if (i + 4 < lenght)
				temp.add(words.get(i + 1) + " " + words.get(i + 2) + " " + words.get(i + 3) + " " + words.get(i + 4));
			if (i + 5 < lenght)
				temp.add(words.get(i + 2) + " " + words.get(i + 3) + " " + words.get(i + 4) + " " + words.get(i + 5));
			if (i + 6 < lenght)
				temp.add(words.get(i + 3) + " " + words.get(i + 4) + " " + words.get(i + 5) + " " + words.get(i + 6));

		}

		return wirtToFile += "-----4 word------\n" + cal(temp) + "\n";
	}

	public String getContent(String content) throws IOException {
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
		return toFile;
	}

	public LinkedList<String> getLinks(String content) throws IOException {
		LinkedList<String> toFile2 = new LinkedList<String>();
		String toFile = "", temp = "";
		StringTokenizer s = new StringTokenizer(content, "<");
		while (s.hasMoreTokens()) {
			temp = s.nextToken();
			if (temp.startsWith("link>")) {
				toFile += temp.substring(5, temp.length()) + "\n";
				toFile2.add(temp.substring(5, temp.length()));
			}
		}
		return toFile2;
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

	public static void main(String[] fileName) throws Exception {
		String address = "http://www.anandtech.com/rss";
		String whereToSave = "G://haifa_project";
		// String path = "F://ttt.txt";
		String split = " |\n|\r|\t|\"|,";

		prime p = new prime(split, address, whereToSave);
		p.print();
	}
}

/*
 * Node the hold string and integer
 */
class node {
	String string;
	int count;

	public node(String string, int count) {
		super();
		this.string = string;
		this.count = count;
	}
}

/*
 * Comparator to node and needed to sort him
 */
class MyComparator implements Comparator<Object> {

	@Override
	public int compare(Object o1, Object o2) {
		if (!(o1 instanceof node) && !(o2 instanceof node))
			throw new InputMismatchException("Country is expected");

		node other1 = (node) o1;
		node other2 = (node) o2;

		return other2.count - other1.count;
	}
}
