package io.mne;

import java.io.*;
import java.net.URL;
import java.util.Random;

public class Main {
	
	private static final String PROFILE_NAME_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
	private static final int MINIMAL_PROFILE_NAME_LENGTH = 3;
	private static final int MAXIMAL_PROFILE_NAME_LENGTH = 16;
	private static final int SAMPLE_AMOUNT_PER_LENGTH = 100;
	
	private static final String PROFILE_API_URL_FORMAT = "https://api.mojang.com/users/profiles/minecraft/%s";
	private static final String LINE_SEPARATOR = System.lineSeparator();
	
	private static final Random RANDOM = new Random();
	
	public static void main(String[] args) {
		double totalEstimate = 0;
		
		for(int length = MINIMAL_PROFILE_NAME_LENGTH; length <= MAXIMAL_PROFILE_NAME_LENGTH; length++) {
			
			double amount = Math.pow(PROFILE_NAME_CHARACTERS.length(), length);
			int existingProfileAmount = 0;
			
			for(int i = 0; i < SAMPLE_AMOUNT_PER_LENGTH; i++) {
				
				String profileName = randomProfileName(length);
				boolean profileExists = profileExists(profileName);
				
				if(profileExists) {
					String message = String.format("profile_name: " + profileName);
					System.out.println(message);
					
					existingProfileAmount++;
				}
			}
			
			double estimate = (double) existingProfileAmount / SAMPLE_AMOUNT_PER_LENGTH * amount;
			totalEstimate += estimate;
		}
		
		System.out.println(totalEstimate);
	}
	
	private static String randomProfileName(int length) {
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < length; i++) {
			
			char randomNameCharacter = PROFILE_NAME_CHARACTERS.charAt(RANDOM.nextInt(PROFILE_NAME_CHARACTERS.length()));
			builder.append(randomNameCharacter);
		}
		
		return builder.toString();
	}
	
	private static boolean profileExists(String profileName) {
		return profile(profileName) != null;
	}
	
	private static String profile(String profileName) {
		String url = PROFILE_API_URL_FORMAT.formatted(profileName);
		
		return readTextFromURL(url);
	}
	
	private static String readTextFromURL(String url) {
		try {
			
			URL u = new URL(url);
			InputStream inputStream = u.openStream();
			InputStreamReader inputReader = new InputStreamReader(inputStream);
			BufferedReader bufferedInputReader = new BufferedReader(inputReader);
			
			StringBuilder builder = new StringBuilder();
			
			while(true) {
				
				String line = bufferedInputReader.readLine();
				if(line == null) break;
				
				builder.append(line);
				builder.append(LINE_SEPARATOR);
			}
			
			bufferedInputReader.close();
			
			return builder.toString();
			
		} catch(FileNotFoundException exception) {
			
			return null;
			
		} catch(IOException exception) {
			
			return readTextFromURL(url);
		}
	}
	
}
