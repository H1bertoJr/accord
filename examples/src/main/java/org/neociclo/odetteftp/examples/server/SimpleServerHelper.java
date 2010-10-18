/**
 * Neociclo Accord, Open Source B2B Integration Suite
 * Copyright (C) 2005-2010 Neociclo, http://www.neociclo.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * $Id$
 */
package org.neociclo.odetteftp.examples.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.neociclo.odetteftp.protocol.OdetteFtpObject;
import org.neociclo.odetteftp.protocol.VirtualFile;
import org.neociclo.odetteftp.util.ProtocolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
class SimpleServerHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServerHelper.class);
	public static final FilenameFilter EXCHANGES_FILENAME_FILTER = new FilenameFilter() {
		public boolean accept(File dir, String name) {
			return (name.endsWith(".vfile") || name.endsWith(".notif"));
		}
	};

	private SimpleServerHelper() {
	}

	public static String createFileName(OdetteFtpObject obj) {
		StringBuffer sb = new StringBuffer();
		sb.append(obj.getOriginator()).append('$');
		sb.append(obj.getDestination()).append('$');
		sb.append(ProtocolUtil.formatDate("yyyyMMddHHmmSS.sss", obj.getDateTime())).append('$');
		sb.append(obj.getDatasetName());
		if (obj instanceof VirtualFile) {
			sb.append(".vfile");
		} else {
			sb.append(".notif");
		}
		return sb.toString();
	}

	public static File getServerDataDir(File baseDir) {
		return new File(baseDir, "data");
	}

	public static File getUserDir(File baseDir, String userCode) {
		return new File(baseDir, userCode.toLowerCase());
	}

	public static File getUserMailboxDir(File baseDir, String userCode) {
		return new File(getUserDir(baseDir, userCode), "mailbox");
	}

	public static File getUserWorkDir(File baseDir, String userCode) {
		return new File(getUserDir(baseDir, userCode), "work");
	}

	public static File getUserConfigFile(File baseDir, String userCode) {
		return new File(getUserDir(baseDir, userCode), "accord-oftp.conf");
	}

	public static OdetteFtpObject loadObject(File input) throws IOException {
	
		OdetteFtpObject obj = null;
	
		FileInputStream stream = new FileInputStream(input);
		ObjectInputStream os = new ObjectInputStream(stream);
	
		try {
			obj = (OdetteFtpObject) os.readObject();
		} catch (ClassNotFoundException cnfe) {
			LOGGER.error("Cannot load Odette FTP Object file: " + input, cnfe);
		} finally {
			try {
				os.close();
			} catch (Throwable t) {
				// do nothing
			}
			try {
				stream.close();
			} catch (Throwable t) {
				// do nothing
			}
		}
	
		return obj;
	}

	public static void storeObject(File output, OdetteFtpObject obj) throws IOException {
	
		FileOutputStream stream = new FileOutputStream(output);
	
		ObjectOutputStream os = new ObjectOutputStream(stream);
	
		try {
			os.writeObject(obj);
			os.flush();
			stream.flush();
		} finally {
			try {
				os.close();
				stream.close();
			} catch (Throwable t) {
				// do nothing
			}
		}
	
	}

}
