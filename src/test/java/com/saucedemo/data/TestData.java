package com.saucedemo.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;

public class TestData {

	public static String generateClientId() {

		return new Faker().bothify("????####??######");
	}

	public static String generateRandomString(int length) {
		return new Faker().lorem().characters(length);
	}

	public static String generateRandomEmail() {
		return new Faker().internet().emailAddress();
	}

	public static String generateRandomPhoneNumber() {
		return new Faker().phoneNumber().cellPhone();
	}

	public static String generateRandomName() {
		return new Faker().name().fullName();
	}

	public static String generateRandomAddress() {
		return new Faker().address().fullAddress();
	}

	public static String generateRandomCompany() {
		return new Faker().company().name();
	}

	public static String generateRandomJobTitle() {
		return new Faker().job().title();
	}

	public static String generateRandomText(int length) {
		return new Faker().lorem().characters(length);
	}

	public static String generateRandomUUID() {
		return new Faker().internet().uuid();
	}

	public static String generateRandomIP() {
		return new Faker().internet().ipV4Address();
	}

	public static String generateRandomURL() {
		return new Faker().internet().url();
	}

	public static String generateRandomDate() {
		return new Faker().date().birthday().toString();
	}

	public static String generateRandomTime() {
		Date randomDate = new Faker().date().past(1, TimeUnit.DAYS);
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		return timeFormat.format(randomDate);
	}

	public static String generateRandomColor() {
		return new Faker().color().name();
	}

	public static String generateRandomCurrency() {
		return new Faker().currency().name();
	}

	public static String generateRandomLanguage() {
		Locale[] locales = Locale.getAvailableLocales();
        Locale randomLocale = locales[new Faker().random().nextInt(locales.length)];
        String language = randomLocale.getDisplayLanguage();
        
        // Filter empty or duplicate values (optional)
        return (language == null || language.isEmpty()) ? "English" : language;
	}
}
