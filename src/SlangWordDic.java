import java.util.*;
import java.io.*;
import java.util.Map.Entry;
import javax.swing.*;

public class SlangWordDic {
	private TreeMap<String, List<String>> treemap = new TreeMap<>();
	private static SlangWordDic slw = new SlangWordDic();
	private int sizeTree;
	private String FILE_Slang_Word = "slang_data\\slang.txt";
	private String FILE_History = "history.txt";

	// Hàm tạo của lớp SlangWordDic
	private SlangWordDic() {
		try {
			// path tới thư mục hiện tại
			String current_path = new java.io.File(".").getCanonicalPath();
			System.out.println("Current dir:" + current_path);
			FILE_Slang_Word = current_path + '\\' + FILE_Slang_Word;
			readFile(FILE_Slang_Word);
		} catch (Exception e) {
			e.printStackTrace();
			;
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
			// System.out.println(stringBuilder.toString());
			printWriter.write(stringBuilder.toString());
			printWriter.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	void readFile(String file) throws Exception {
		treemap.clear();
		Scanner scanner = new Scanner(new File(file));
		scanner.useDelimiter("`");
		scanner.next(); // Bỏ qua dòng tiêu đề
		String line = scanner.next();
		String[] part = line.split("\n");
		String slword = null;
		int i = 0;
		sizeTree = 0;
		while (scanner.hasNext()) {
			List<String> meaning = new ArrayList<String>();
			slword = part[1].trim(); // Loai bỏ khoảng trắng
			line = scanner.next();
			part = line.split("\n");
			if (treemap.containsKey(slword)) { // check tồn tại slang word chưa
				meaning = treemap.get(slword);
			}
			if (part[0].contains("|")) {
				String[] d = (part[0]).split("\\|");
				Collections.addAll(meaning, d);
				sizeTree += d.length - 1;
			} else {
				meaning.add(part[0]);
			}
			treemap.put(slword, meaning);
			i++;
			sizeTree++;
		}
		scanner.close();
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

	public void saveHistory(String slag, String meaning) throws Exception {
		FileWriter fr = new FileWriter(new File(FILE_History), true);
		fr.write(slag + "`" + meaning + "\n");
		fr.close();
	}

	public void set(String slag, String oldValue, String newValue) {
		List<String> meaning = treemap.get(slag);
		int index = meaning.indexOf(oldValue);
		meaning.set(index, newValue);
		this.saveFile(FILE_Slang_Word);
		// System.out.println("Size of map: " + sizeTree);
	}

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

}
