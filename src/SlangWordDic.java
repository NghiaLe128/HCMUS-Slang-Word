import java.util.*;
import java.io.*;
import java.util.Map.Entry;

public class SlangWordDic {
	private TreeMap<String, List<String>> treemap = new TreeMap<>();
	private static SlangWordDic slw = new SlangWordDic();
	private int sizeTree;
	private String SLANG_ORIGIN = "slangword_data\\slang.txt";
	private String SLANG_NEW = "slangword_data\\newslang.txt";
	private String FILE_History = "history.txt";

	// Hàm tạo của lớp SlangWordDic
	private SlangWordDic() {
		try {
			// path tới thư mục hiện tại
			String current_path = new java.io.File(".").getCanonicalPath();
			SLANG_NEW = current_path + '\\' + SLANG_NEW;
			SLANG_ORIGIN = current_path + '\\' + SLANG_ORIGIN;
			readFile(SLANG_NEW);
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

	// // Hàm đọc file
	void readFile(String file) throws Exception {
		treemap.clear();
		sizeTree = 0;
	
		try (Scanner scanner = new Scanner(new File(file))) {
			scanner.useDelimiter("\n"); // Sử dụng "\n" thay vì "`" để tách nghĩa
			scanner.nextLine(); // Bỏ qua dòng tiêu đề
	
			while (scanner.hasNext()) {
				String[] parts = scanner.next().split("`");
				if (parts.length >= 2) {
					String slangWord = parts[0].trim();
					List<String> meanings = treemap.getOrDefault(slangWord, new ArrayList<>());
	
					if (parts[1].contains("|")) {
						String[] definitions = parts[1].split("\\|");
						Collections.addAll(meanings, definitions);
						sizeTree += definitions.length - 1;
					} else {
						meanings.add(parts[1]);
					}
	
					treemap.put(slangWord, meanings);
					sizeTree++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Hàm lưu file
	void saveFile(String file) {
		try {
			PrintWriter printWriter = new PrintWriter(new File(file));
			StringBuilder stringBuilder = new StringBuilder();

			stringBuilder.append("Slag`Meaning\n");
			String s[][] = new String[treemap.size()][3];
			Set<String> keySet = treemap.keySet();
			Object[] keyArray = keySet.toArray();
			for (int i = 0; i < treemap.size(); i++) {
				Integer in = i + 1;
				s[i][0] = in.toString();
				s[i][1] = (String) keyArray[i];
				List<String> meaning = treemap.get(keyArray[i]);
				stringBuilder.append(s[i][1] + "`" + meaning.get(0));
				for (int j = 1; j < meaning.size(); j++) {
					stringBuilder.append("|" + meaning.get(j));
				}
				stringBuilder.append("\n");
			}
			printWriter.write(stringBuilder.toString());
			printWriter.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public String[][] getData() {
		String[][] data = new String[sizeTree][3];
		int rowIndex = 0;
	
		for (Map.Entry<String, List<String>> entry : treemap.entrySet()) {
			String slang = entry.getKey();
			List<String> meanings = entry.getValue();
	
			for (String meaning : meanings) {
				data[rowIndex][0] = String.valueOf(rowIndex);
				data[rowIndex][1] = slang;
				data[rowIndex][2] = meaning;
				rowIndex++;
	
				if (rowIndex >= sizeTree) {
					break;
				}
			}
	
			if (rowIndex >= sizeTree) {
				break;
			}
		}
	
		return data;
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
		this.saveFile(SLANG_NEW);
	}

	// Tìm kiếm theo slang word
	public String[][] findByWord(String key) {
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
	
	// Xoá lịch sử tìm kiếm
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

	public void editSlangWord(String oldSlang, String newSlang) {
		if (treemap.containsKey(oldSlang)) {
			List<String> meanings = treemap.get(oldSlang);
			treemap.remove(oldSlang); // Xóa Slang Word cũ
			treemap.put(newSlang, meanings); // Thêm Slang Word mới với cùng nghĩa
			this.saveFile(SLANG_NEW); // Lưu thay đổi vào tệp
			System.out.println("Edit successful. Slang word updated.");
		} else {
			System.out.println("Slang word not found.");
		}
	}
	
	public void editDefinition(String slang, String oldMeaning, String newMeaning) {
		if (treemap.containsKey(slang)) {
			List<String> meanings = treemap.get(slang);
			for (int i = 0; i < meanings.size(); i++) {
				String meaning = meanings.get(i).trim().toLowerCase(); // Chuyển đổi thành chữ thường và loại bỏ khoảng trắng
				if (meaning.equals(oldMeaning.trim().toLowerCase())) {
					meanings.set(i, newMeaning);
					saveFile(SLANG_NEW);
					System.out.println("Edit successful. Definition updated.");
					return;
				}
			}
			System.out.println("Old meaning not found.");
		} else {
			System.out.println("Slang word not found.");
		}
	}
	
	public boolean checkExists(String slag) {
		return treemap.containsKey(slag);
	}
	
	public void addSlangWord(String slang, String meaning, boolean overwrite) {
		List<String> meaningList = treemap.get(slang);
		if (meaningList == null || overwrite) {
			meaningList = new ArrayList<>();
		}
		meaningList.add(meaning);
		treemap.put(slang, meaningList);
		sizeTree++;
		this.saveFile(SLANG_NEW);
	}
	
	public void delete(String slang, String meaning) {
		List<String> meaningList = treemap.get(slang);
		if (meaningList != null) {
			if (meaningList.remove(meaning)) {
				if (meaningList.isEmpty()) {
					treemap.remove(slang);
				}
				sizeTree--;
				this.saveFile(SLANG_NEW);
			}
		}
	}
	
	public void reset() throws Exception {
		readFile(SLANG_ORIGIN);
		this.saveFile(SLANG_NEW);
	}
	
	public String[] random() {
		// Random a number
		int maximum = treemap.size();
		int rand = randInt(0, maximum);
	
		// Get slang meaning
		String s[] = new String[2];
		int index = 0;
		for (Map.Entry<String, List<String>> entry : treemap.entrySet()) {
			if (index == rand) {
				s[0] = entry.getKey();
				s[1] = entry.getValue().get(0);
				break;
			}
			index++;
		}
		return s;
	}
	
	public static int randInt(int minimum, int maximum) {
		return (minimum + (int) (Math.random() * maximum));
	}
	public String[] quiz(int type) {
		String s[] = new String[6];
		String[] slangRandom = this.random();
		
		if (type == 1) {
			s[0] = slangRandom[0];
		} else {
			s[0] = slangRandom[1];
		}
	
		int rand = randInt(1, 4);
		s[rand] = slangRandom[1];
		s[5] = slangRandom[1];
	
		for (int i = 1; i <= 4; i++) {
			if (rand == i) {
				continue;
			}
			String[] slangRand = this.random();
			while (slangRand[0].equals(s[0])) {
				slangRand = this.random();
			}
			s[i] = slangRand[1];
		}
	
		return s;
	}
	
}

