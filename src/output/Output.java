package output;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Output {

	/**
	 * Saves a string in a file.
	 * @param fileName name of the final to be created.
	 * @param content to be saved in the file.
	 */
	public void saveFile(String fileName, String content, boolean append) {
		try {
			File file = createFile(fileName, append);
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),append);
			BufferedWriter bw = new BufferedWriter(fw);
			
			if (append) {
				bw.append(content);
			} else {
				bw.write(content);
			}
			
			bw.flush();
			bw.close();

		} catch (IOException e) {
			System.out.println("Error in writing: " + fileName);
			e.printStackTrace();
		}		
	}

	/**
	 * Creates a new file.
	 * @param fileName path+name of the file to be created.
	 * @return the created/existed file.
	 */
	private File createFile(String fileName, boolean append) {
		File file = new File(fileName);
		try {		
			if (file.exists() && !append) {
				file.delete();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			System.out.println("Error in creating: " + fileName);
			e.printStackTrace();

		}

		return file;
	}

}
