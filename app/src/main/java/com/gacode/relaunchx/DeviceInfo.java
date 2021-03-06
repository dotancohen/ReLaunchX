package com.gacode.relaunchx;

/**
 * originate from CoolReader
 * http://http://sourceforge.net/projects/crengine/
 */

import android.app.Application;
import android.os.Build;
import android.util.Log;

import java.io.File;

public class DeviceInfo {

	public final static String MANUFACTURER;
	public final static String MODEL;
	public final static String DEVICE;
	public final static boolean SAMSUNG_BUTTONS_HIGHLIGHT_PATCH;
	public final static boolean EINK_SCREEN;
	public final static boolean EINK_SCREEN_UPDATE_MODES_SUPPORTED;
	public final static boolean NOOK_NAVIGATION_KEYS;
	public final static boolean EINK_NOOK;
	public final static boolean FORCE_LIGHT_THEME;
	public final static boolean EINK_SONY;
	public final static boolean SONY_NAVIGATION_KEYS;
	public final static boolean USE_CUSTOM_TOAST;
	public final static boolean AMOLED_SCREEN;
	public final static boolean EINK_ONYX;

	static {
		MANUFACTURER = getBuildField("MANUFACTURER");
		MODEL = getBuildField("MODEL");
		DEVICE = getBuildField("DEVICE");
		SAMSUNG_BUTTONS_HIGHLIGHT_PATCH = MANUFACTURER.toLowerCase()
				.contentEquals("samsung")
				&& (MODEL.contentEquals("GT-S5830") || MODEL
						.contentEquals("GT-S5660")); // More models?
		AMOLED_SCREEN = MANUFACTURER.toLowerCase().contentEquals("samsung")
				&& (MODEL.toLowerCase().startsWith("gt-i")); // AMOLED screens:
																// GT-IXXXX
		EINK_NOOK = MANUFACTURER.toLowerCase().contentEquals("barnesandnoble")
				&& (MODEL.contentEquals("NOOK")|| MODEL.contentEquals("BNRV300") || MODEL.contentEquals("BNRV350"))
				&& DEVICE.toLowerCase().contentEquals("zoom2");
		EINK_SONY = MANUFACTURER.toLowerCase().contentEquals("sony")
				&& MODEL.contentEquals("PRS-T1");
		EINK_ONYX = MANUFACTURER.toLowerCase().contentEquals("onyx")
				&& MODEL.contentEquals("max");
		EINK_SCREEN = EINK_SONY || EINK_NOOK || EINK_ONYX; // TODO: set to true for eink
												// devices like Nook Touch

		NOOK_NAVIGATION_KEYS = EINK_NOOK; // TODO: add autodetect
		SONY_NAVIGATION_KEYS = EINK_SONY;
		EINK_SCREEN_UPDATE_MODES_SUPPORTED = EINK_SCREEN && EINK_NOOK; // TODO:
																		// add
																		// autodetect
		FORCE_LIGHT_THEME = EINK_SCREEN;
		USE_CUSTOM_TOAST = EINK_SCREEN;
	}

	public static boolean isRooted() {
		File isrooted = new File("/system/bin", "su");
		if (isrooted.exists()) {
			return true;
		}

		isrooted = new File("/system/xbin", "su");
		if (isrooted.exists()) {
			return true;
		}

		isrooted = new File ("/su/bin", "su");
		if (isrooted.exists()){
			return true;
		}

		return false;
	}

	public static boolean isCompatibleDevice(Application app) {
		String[] allowedModels = app.getApplicationContext().getResources()
				.getStringArray(R.array.allowed_models);
		String[] allowedDevices = app.getApplicationContext().getResources()
				.getStringArray(R.array.allowed_devices);
		String[] allowedManufacts = app.getApplicationContext().getResources()
				.getStringArray(R.array.allowed_manufacturers);
		String[] allowedProducts = app.getApplicationContext().getResources()
				.getStringArray(R.array.allowed_products);

		if (!checkField(allowedModels, Build.MODEL))
			return false;
		if (!checkField(allowedDevices, Build.DEVICE))
			return false;
		if (!checkField(allowedManufacts, Build.MANUFACTURER))
			return false;
		if (!checkField(allowedProducts, Build.PRODUCT))
			return false;
		return true;
	}

	private static boolean checkField(String[] a, String f) {
		for (int i = 0; i < a.length; i++)
			if (a[i].equals("*") || a[i].equals(f))
				return true;
		return false;
	}

	private static String getBuildField(String fieldName) {

		try {
			return (String) Build.class.getField(fieldName).get(null);
		} catch (Exception e) {
			Log.d("cr3", "Exception while trying to check Build." + fieldName);
			return "";
		}
	}
}
