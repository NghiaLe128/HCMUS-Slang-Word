import java.util.*;
import java.io.*;
import java.util.Map.Entry;
import javax.swing.*;

public class SlangWordDic {
	private TreeMap<String, List<String>> treemap = new TreeMap<>();
	private static SlangWordDic slw = new SlangWordDic();
	private int sizeTree;
	private String FILE_Slang_Word = "slangword_data\\slang.txt";
	private String FILE_History = "history.txt";

	// Hàm tạo của lớp SlangWordDic
	private SlangWordDic() {
		try {
			// path tới thư mục hiện tại
			String current_path = new java.io.File(".").getCanonicalPath();
			FILE_Slang_Word = current_path + '\\' + FILE_Slang_Word;
			readFile(FILE_Slang_Word);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Lấy instance của lớp SlangWord (mô hình Singleton)
	public static SlangWordDic getInstance() {
		if (slw == null) {
			synchronized (SlangWordDic.class) {
				if (slw == null) {
					slw = new SlangWordDic();
				}
			}
		}
		return slw;
	}

	// Hàm lưu file
	void saveFile(String file) {
		try (PrintWriter printWriter = new PrintWriter(new File(file))) {
			printWriter.println("Slang`Meaning");
			int index = 1;
			for (Map.Entry<String, List<String>> entry : treemap.entrySet()) {
				String slangWord = entry.getKey();
				List<String> meanings = entry.getValue();
				String meaningLine = String.join("|", meanings);
				printWriter.printf("%d`%s`%s%n", index, slangWord, meaningLine);
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	// Hàm đọc file
	void readFile(String file) throws Exception {
		treemap.clear();
		sizeTree = 0;
	
		try (Scanner scanner = new Scanner(new File(file)).useDelimiter("`")) {
			scanner.next(); // Bỏ qua dòng tiêu đề
			while (scanner.hasNext()) {
				String[] parts = scanner.next().split("\n");
				String slangWord = parts[1].trim(); // Loại bỏ khoảng trắng
				List<String> meanings = treemap.getOrDefault(slangWord, new ArrayList<>());
	
				if (parts[0].contains("|")) {
					String[] definitions = parts[0].split("\\|");
					Collections.addAll(meanings, definitions);
					sizeTree += definitions.length - 1;
				} else {
					meanings.add(parts[0]);
				}
	
				treemap.put(slangWord, meanings);
				sizeTree++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[][] getData() {
		String s[][] = new String[sizeTree][3];
		Set<String> slagListSet = treemap.keySet();
		Object[] slagList = slagListSet.toArray();
		int index = 0;
		for (int i = 0; i < sizeTree; i++) {
			s[i][0] = String.valueOf(i);
			s[i][1] = (String) slagList[index];
			List<String> meaning = treemap.get(slagList[index]);
			s[i][2] = meaning.get(0);
			for (int j = 1; j < meaning.size(); j++) {
				if (i < sizeTree)
					i++;
				s[i][0] = String.valueOf(i);
				s[i][1] = (String) slagList[index];
				s[i][2] = meaning.get(j);
			}
			index++;
		}
		return s;
	}

	// Hàm lưu lịch sử tìm kiếm
	public void saveHistory(String slag, String meaning) throws Exception {
		FileWriter fr = new FileWriter(new File(FILE_History), true);
		fr.write(slag + "`" + meaning + "\n");
		fr.close();
	}

	// Hàm edit giá trị
	public void set(String slag, String oldValue, String newValue) {
		List<String> meaning = treemap.get(slag);
		int index = meaning.indexOf(oldValue);
		meaning.set(index, newValue);
		this.saveFile(FILE_Slang_Word);
	}

	// Tìm kiếm theo slang word
	public String[][] findByMean(String key) {
		List<String> meaning = treemap.get(key);
		if (meaning == null)
			return null;
		int size = meaning.size();
		String s[][] = new String[size][3];
		for (int i = 0; i < size; i++) {
			s[i][0] = String.valueOf(i);
			s[i][1] = key;
			s[i][2] = meaning.get(i);
		}
		return s;
	}

	// Tìm kiếm theo defination
	public String[][] findByDef(String query) {
		List<String> keyL = new ArrayList<>();
		List<String> meaningL = new ArrayList<>();
		for (Entry<String, List<String>> entry : treemap.entrySet()) {
			List<String> meaning = entry.getValue();
			for (int i = 0; i < meaning.size(); i++) {
				if (meaning.get(i).toLowerCase().contains(query.toLowerCase())) {
					keyL.add(entry.getKey());
					meaningL.add(meaning.get(i));
				}
			}
		}
		int size = keyL.size();
		String s[][] = new String[size][3];

		for (int i = 0; i < size; i++) {
			s[i][0] = String.valueOf(i);
			s[i][1] = keyL.get(i);
			s[i][2] = meaningL.get(i);
		}
		return s;
	}

	// Hàm đọc history
	public String[][] readHistory() {
		List<String> historySlag = new ArrayList<>();
		List<String> historyDefinition = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(new File(FILE_History));
			scanner.useDelimiter("`");
			String line = scanner.next();
			String[] temp = scanner.next().split("\n");
			historySlag.add(line);
			historyDefinition.add(temp[0]);
			while (scanner.hasNext()) {
				line = temp[1];
				temp = scanner.next().split("\n");
				historySlag.add(line);
				historyDefinition.add(temp[0]);
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = historySlag.size();
		String s[][] = new String[size][3];
		for (int i = 0; i < size; i++) {
			s[size - i - 1][0] = String.valueOf(size - i);
			s[size - i - 1][1] = historySlag.get(i);
			s[size - i - 1][2] = historyDefinition.get(i);
		}
		return s;
	}
	
	public void clearHistory() {
		File historyFile = new File(FILE_History);
	
		if (historyFile.exists()) {
			try {
				FileWriter fileWriter = new FileWriter(historyFile, false); // Mở file để ghi và ghi đè nội dung
				fileWriter.write(""); // Ghi nội dung rỗng vào file
				fileWriter.close();
				System.out.println("Dữ liệu trong file lịch sử đã bị xóa.");
			} catch (IOException e) {
				System.out.println("Lỗi: " + e.getMessage());
			}
		} else {
			System.out.println("File lịch sử không tồn tại.");
		}
	}
}

