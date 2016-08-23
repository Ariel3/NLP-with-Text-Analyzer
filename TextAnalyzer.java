
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.ByteBuffer;
import java.io.IOException;

/**
 * A text analyzer that processes text and provides information about its word
 * contents. One thing it should be able to support is the ability to create a
 * report that shows a count of how many times each word occurs in the text. The
 * report should be sorted, with a primary sort of word length, and a secondary
 * ASCII sort.
 *
 */
public class TextAnalyzer {

	LinkedList<String> words;

	/**
	 * Read the contents of the file passed and store the contents into a string
	 * array
	 */
	public TextAnalyzer(String path, String split) throws IOException {
		String w[] = readFile(path, Charset.defaultCharset()).split(split);
		words = new LinkedList<String>();

		for (int i = 0; i < w.length; i++) {
			words.add(w[i]);
		}
		for (int i = 0; i < words.size(); i++) {
			if (words.get(i).equals(""))
				words.remove(i);
		}
	}

	/**
	 * Read data from file
	 */
	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();

	}

	/**
	 * Analyze the Text in 1 word, 2 word together and so on..
	 */

	public void print() {
		LinkedList<String> temp = new LinkedList<String>();
		int lenght = words.size();
		System.out.println();

		/*
		 * i+1=i
		 */
		System.out.println("-----1 word------");
		for (int i = 0; i < lenght; i++) {

			temp.add(words.get(i));
		}
		cal(temp);

		/*
		 * i+2=i
		 */
		System.out.println("-----2 word------");
		temp.clear();
		for (int i = 0; i < lenght - 1; i += 2) {

			temp.add(words.get(i) + " " + words.get(i + 1));
			if (i + 2 < lenght)
				temp.add(words.get(i + 1) + " " + words.get(i + 2));
		}
		cal(temp);

		/*
		 * i+3=i
		 */
		System.out.println("-----3 word------");
		temp.clear();
		for (int i = 0; i < lenght - 2; i += 3) {
			temp.add(words.get(i) + " " + words.get(i + 1) + " " + words.get(i + 2));
			if (i + 3 < lenght)
				temp.add(words.get(i + 1) + " " + words.get(i + 2) + " " + words.get(i + 3));
			if (i + 4 < lenght)
				temp.add(words.get(i + 2) + " " + words.get(i + 3) + " " + words.get(i + 4));
		}
		cal(temp);

		/*
		 * i+4=i
		 */
		System.out.println("-----4 word------");
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

		cal(temp);
	}

	/*
	 * Calculate the appearance of each word
	 */
	public static void cal(LinkedList<String> w) {
		MyComparator m = new MyComparator();
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
			System.out.println(a.count + " " + a.string);
		}
	}

	public static void main(String[] fileName) throws IOException {

		String path = "F://ttt.txt";
		String split = " |\n|\r|\t|\"|,";
		TextAnalyzer t = new TextAnalyzer(path, split);

		t.print();
	}
}