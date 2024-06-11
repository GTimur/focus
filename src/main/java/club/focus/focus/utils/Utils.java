package club.focus.focus.utils;

public class Utils {
	public int getHLevel(String header) {
		int count=0;
		if (header != null) {
			for (int i = 0; i < header.length(); i++) {
				if (header.charAt(i) == '*') {
					count++;
				} else if (header.charAt(i) == ' ') {
					break;
				}
			}
		}
		return count;
	}

}
