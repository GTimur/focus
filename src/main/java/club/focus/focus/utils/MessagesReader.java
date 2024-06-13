package club.focus.focus.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import club.focus.focus.dto.MessageDto;

public class MessagesReader {
	private Path inFilePath;

	public MessagesReader(Path inFilePath) {
		this.inFilePath = inFilePath;		
	}

	public MessagesReader() {
	}
	
	public List<MessageDto> readAllFiles(){
		SearchFileByWildcard searchFiles = new SearchFileByWildcard();
		List<String> filesList;
		List<MessageDto> messagesList = new ArrayList<MessageDto>();
		try {
			filesList = searchFiles.searchWithWc(this.inFilePath, "glob:*.{org}");
			for (String file : filesList) {
				System.out.println("Reading file " + file);
				messagesList = readMessages(file);
				messagesList.addAll(messagesList);
			}
			// stream().forEach((c) -> System.out.println(c));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return messagesList;
	}

	private List<MessageDto> readMessages(String fileName) {		
		List<MessageDto> messagesList = new ArrayList<MessageDto>();
		Utils utl = new Utils();

		// public static List<String> messagesList = new ArrayList<String>();
		ArrayList<String> sections = new ArrayList<String>();
		String timeStamp = "";
		String textBody = "";
		MessageDto messageDto;

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"))) {
			String line;
			String prev = "";
			int curLevel = 0;
			int level = 0;

			while ((line = br.readLine()) != null) {
				// System.out.println("line:"+line);
				if (line.startsWith("*")) {
					
					if (!prev.startsWith("---") && textBody != null && !textBody.equals("")) {
						messageDto = new MessageDto(new ArrayList<String>(sections), timeStamp, textBody,
								textBody.hashCode());
						messagesList.add(messageDto);
						textBody = "";
					}
					
					level = utl.getHLevel(line);
					
					if (curLevel < level) {
						while (curLevel < level - 1) {
							sections.add(""); // empty header (when missed)
							curLevel++;
						}
						sections.add(line.replace("* ", "").replace("*", ""));
						curLevel++;
					}	else if (curLevel >= level) {
						while (curLevel >= level) {
							sections.remove(sections.size() - 1);
							curLevel--;
						}
						sections.add(line.replace("* ", "").replace("*", ""));
						curLevel++;
					}
					
				} else if (line.startsWith("---")) {
					if (textBody != null && !textBody.equals("")) {
						messageDto = new MessageDto(new ArrayList<String>(sections), timeStamp, textBody,
								textBody.hashCode());
						messagesList.add(messageDto);
						textBody = "";
					}
				} else {
					if (textBody.equals("")) {
						textBody += line;
					} else {
						textBody += "\n" + line;
					}

					prev = line;
				}

				// messagesList.forEach(System.out::println);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return messagesList;
	}

	private String createMD5Hash(final String input) throws NoSuchAlgorithmException {

		String hashtext = null;
		MessageDigest md = MessageDigest.getInstance("MD5");

		// Compute message digest of the input
		byte[] messageDigest = md.digest(input.getBytes());

		hashtext = convertToHex(messageDigest);
		return hashtext;
	}

	private String convertToHex(final byte[] messageDigest) {
		BigInteger bigint = new BigInteger(1, messageDigest);
		String hexText = bigint.toString(16);
		while (hexText.length() < 32) {
			hexText = "0".concat(hexText);
		}
		return hexText;
	}
}
