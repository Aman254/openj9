/*******************************************************************************
 * Copyright IBM Corp. and others 2009
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
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0-only WITH Classpath-exception-2.0 OR GPL-2.0-only WITH OpenJDK-assembly-exception-1.0
 *******************************************************************************/
package com.ibm.j9ddr.corereaders.elf;

import java.io.File;
import java.io.IOException;

import javax.imageio.stream.ImageInputStream;

import com.ibm.j9ddr.corereaders.CoreReader;
import com.ibm.j9ddr.corereaders.ICore;
import com.ibm.j9ddr.corereaders.ICoreFileReader;
import com.ibm.j9ddr.corereaders.InvalidDumpFormatException;

/**
 * @author andhall
 *
 */
public class ELFDumpReaderFactory implements ICoreFileReader
{

	public ICore processDump(String path) throws InvalidDumpFormatException,
			IOException
	{
		return ELFDumpReader.getELFDumpReader(new File(path));
	}
	
	public ICore processDump(ImageInputStream in) throws InvalidDumpFormatException, IOException {
		return ELFDumpReader.getELFDumpReader(in);
	}

	public DumpTestResult testDump(String path) throws IOException
	{
		if (! new File(path).exists()) {
			return DumpTestResult.FILE_NOT_FOUND;
		}
		
		return ELFFileReader.isELF(CoreReader.getFileHeader(path)) ? DumpTestResult.RECOGNISED_FORMAT : DumpTestResult.UNRECOGNISED_FORMAT;
	}
	
	public DumpTestResult testDump(ImageInputStream in) throws IOException
	{
		
		return ELFFileReader.isELF(CoreReader.getFileHeader(in)) ? DumpTestResult.RECOGNISED_FORMAT : DumpTestResult.UNRECOGNISED_FORMAT;
	}

}
