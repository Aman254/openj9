/*[INCLUDE-IF Sidecar18-SE]*/
/*******************************************************************************
 * Copyright IBM Corp. and others 2007
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] https://openjdk.org/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
 *******************************************************************************/
package com.ibm.dtfj.javacore.parser.framework.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

public class JavaCoreFileReader extends InputStreamReader {

	private File fSource;

	
	
	/**
	 * Gets constructed only if a valid file is passed.
	 * @param input java.io.File to read.
	 * @throws FileNotFoundException if invalid file is passed or file not found.
	 */
	public JavaCoreFileReader(File input) throws FileNotFoundException{
		super(new FileInputStream(input));
		fSource = input;
	}


	/**
	 * 
	 * @return extension of file as a String or null if none exists.
	 */
	public String getExtension() {
		String extension = null;
		if (fSource != null) {
			String[] parts = fSource.getName().split("\\.");
			int partsLength;
			if (parts != null && (partsLength = parts.length) > 0) {
				extension = parts[partsLength - 1];
			}
		}
		return extension;
	}



	/**
	 * 
	 * @return Source for the reader
	 */
	public Object getSource() {
		return fSource;
	}


}
