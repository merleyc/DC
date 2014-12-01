package parserHTML;

import input.TestDetector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import output.Output;

public class Parser {
	
	private static int nWords = 1;

	public static void main (String args []) {
		Parser parser = new Parser();
		String inputFileDir = "./data1/";
		String outputFile = "./results/saida.txt";;
		
		parser.createOutput(inputFileDir, outputFile);
	}

	private void createOutput(String inputFileDir, String outputFile) {
		Output out = new Output();
		String result = "ID,PALAVRA,No.,GLOSA,SYNSET";
		out.saveFile(outputFile, result, false);
		 
		String[] fileList = (new File(inputFileDir)).list();

		for (int index = 0; index < fileList.length; index++) {
			String file = inputFileDir + "/" + fileList[index];
			result = getAllTags(file);
			
			if (!result.trim().equals("")) {
				out.saveFile(outputFile, result, true);
			}
			else {
				System.out.println(file);	
			}
			
			//System.out.println(index);
		}
		
	}

	public static String getAllTags(String dirFile){
		String result = "";
		
		try {						
			File input=new File(dirFile);	
			int counter = 1;
			if (input.exists() && input.isFile()) {

				String encoding = TestDetector.detectaEncodingImprimeArquivo(dirFile);
				Document document=Jsoup.parse(input, encoding);//"UTF-8");
				Document parse=Jsoup.parse(document.html());

				//Getting title: E.g.: 'Boca' from '<meta property="og:title" content="Sinônimos de Boca" />'. 
				String title = null;
				Elements metaOgTitle = parse.select("meta[property=og:title]");
				if (metaOgTitle != null) {
					title = metaOgTitle.attr("content");
				}
				else {
					title = parse.title();
				}

				title = clearTitle(title);
				Elements body = parse.select("body");
				Elements senses = body.select("[class=\"sentido\"]");
				Elements synonyms = body.select("[class=\"sinonimos\"]");
				
				for(Element sense: senses){
					String syn = clearSyn(synonyms.get(counter-1).text());//.select("[class=\"sinonimo\"]"));
					result += "\n#" + nWords + ";" + title + ";" + counter + ";" + sense.text().replace(':', ' ').trim() + ";" + syn;
					counter++;
					nWords++;
				}

			}
			else {
				System.out.println("Error with file: " + input);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private static String clearTitle(String title) {
		if (title == null) {
			title = "-";
		} else {
			if (title.contains(" ")) {
				StringTokenizer st = new StringTokenizer(title, " ");
				int i = 0;

				while (i < 3 && st.hasMoreTokens()) {
					title = st.nextToken();
					i++;
				}
			}
			if (title.trim().equals("")) {
				title = "-";
			}

		}

		return title;
	}

	private static String clearSyn(String st) {
		StringBuffer newSt = new StringBuffer();

		if (st != null) {
			st = st.trim();

			if (!st.equals("")) {
				boolean isAllowedChar = false;
				int i = 0;
				while (i < st.length() && !isAllowedChar) {
					isAllowedChar = isAllowedChar(st.charAt(i));
					i++;
				}

				while (i < st.length()) {
					newSt.append(st.charAt(i));
					i++;
				}

			}
		}

		st = newSt.toString().replaceAll(",", " |").trim();

		return st;
	}

	private static boolean isAllowedChar(char c) {
		if (c != '0' && c != '1' && c != '2'  && c != '3'  && c != '4'  && c != '5'
				&& c != '6'  && c != '7'  && c != '8'  && c != '9' ) {
			return true;
		}
		return false;
	}
}