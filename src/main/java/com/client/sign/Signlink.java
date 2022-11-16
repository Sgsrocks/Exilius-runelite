package com.client.sign;

import java.applet.Applet;
import java.awt.*;
import java.io.*;
import java.net.*;

import com.client.Configuration;
import com.google.common.base.Preconditions;

public final class Signlink {


		public static final RandomAccessFile[] indices = new RandomAccessFile[5];
		public static RandomAccessFile cache_dat = null;
		public static Applet mainapp = null;
		public static String os;
		public static String arch;
		public static EventQueue eventQueue;

		public static void init(Applet px) {

			System.setProperty("java.net.preferIPv4Stack", "true");

			mainapp = px;

			String directory = findcachedir();
			try {

				cache_dat = new RandomAccessFile(directory + "main_file_cache.dat", "rw");
				for (int index = 0; index < 5; index++) {
					indices[index] = new RandomAccessFile(directory + "main_file_cache.idx"
							+ index, "rw");
				}


			} catch (Exception exception) {
				exception.printStackTrace();
			}

			try {
				eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
			} catch (Throwable t) {
			}
			try {
				ThreadGroup t = Thread.currentThread().getThreadGroup();
				do {
					ThreadGroup t1 = t.getParent();
					if (t1 == null)
						break;

					t = t1;
				} while (true);
				int n = t.activeCount();
				if (n > 0) {
					Thread[] h = new Thread[n];
					n = t.enumerate(h);
					if (n > 0)
						for (int n1 = 0; n1 != n; ++n1) {
							Thread r = h[n1];
							if (r == null)
								continue;

							try {
								String s = r.getName();
								if (s != null && s.startsWith("AWT"))
									r.setPriority(1);
							} catch (Throwable w) {
							}
						}
				}
			} catch (Throwable t) {
			}
			os = null;
			try {
				os = System.getProperty("os.name").toLowerCase();
			} catch (Throwable ex) {
			}
			arch = null;
			try {
				arch = System.getProperty("os.arch").toLowerCase();
			} catch (Throwable ex) {
			}
		}

		public static String trim(String s) {
			if (s == null || s.length() == 0)
				return null;

			s = s.trim();
			return s.length() == 0 ? null : s;
		}

	public static final String separator = System.getProperty("file.separator");
	private static final String devCache = "." + separator + Configuration.DEV_CACHE_NAME + separator;
	private static String cacheDir = null;

	private static String setCacheDir(String cacheDir) {
		Signlink.cacheDir = cacheDir;
		System.out.println("Using cache directory: " + cacheDir);
		return cacheDir;
	}

	public static boolean usingDevCache() {
		return cacheDir.equals(devCache);
	}

	public static final String findcachedir() {
		if (cacheDir != null) {
			return cacheDir;
		}

		// Dev cache only loads in dev mode to allow for easy switching.
		if (new File(devCache).exists()) {
			if (Configuration.developerMode) {
				return setCacheDir(devCache);
			}

			System.out.println("Development cache detected but client was not launched in developer mode (-d run argument).");
		}

		// Home directory cache
		String home = System.getProperty("user.home");
		String cacheName = Configuration.cacheName;
		String cacheDir = home + separator + cacheName + separator;
		File file = new File(cacheDir);
		if (file.exists() || file.mkdir()) {
			return setCacheDir(cacheDir);
		}
		return null;
	}
		public static String indexLocation(int cacheIndex, int index) {
			return Signlink.findcachedir() + "index" + cacheIndex + "/"
					+ (index != -1 ? index + ".gz" : "");
		}
	}
