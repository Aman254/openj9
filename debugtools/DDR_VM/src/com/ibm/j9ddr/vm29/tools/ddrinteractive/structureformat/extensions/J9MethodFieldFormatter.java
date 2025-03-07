/*******************************************************************************
 * Copyright IBM Corp. and others 2010
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
package com.ibm.j9ddr.vm29.tools.ddrinteractive.structureformat.extensions;

import static com.ibm.j9ddr.StructureTypeManager.TYPE_STRUCTURE_POINTER;

import java.io.PrintStream;

import com.ibm.j9ddr.CorruptDataException;
import com.ibm.j9ddr.tools.ddrinteractive.BaseFieldFormatter;
import com.ibm.j9ddr.tools.ddrinteractive.Context;
import com.ibm.j9ddr.tools.ddrinteractive.FormatWalkResult;
import com.ibm.j9ddr.tools.ddrinteractive.IStructureFormatter;
import com.ibm.j9ddr.tools.ddrinteractive.StructureCommandUtil;
import com.ibm.j9ddr.vm29.j9.ConstantPoolHelpers;
import com.ibm.j9ddr.vm29.pointer.PointerPointer;
import com.ibm.j9ddr.vm29.pointer.generated.J9ClassPointer;
import com.ibm.j9ddr.vm29.pointer.generated.J9MethodPointer;
import com.ibm.j9ddr.vm29.pointer.generated.J9ROMMethodPointer;
import com.ibm.j9ddr.vm29.pointer.helper.J9ClassHelper;
import com.ibm.j9ddr.vm29.pointer.helper.J9MethodHelper;
import com.ibm.j9ddr.vm29.pointer.helper.J9ROMMethodHelper;

/**
 * Adds // class.method (signature) to all !j9method fields
 *
 */
public class J9MethodFieldFormatter extends BaseFieldFormatter
{


	/* (non-Javadoc)
	 * @see com.ibm.j9ddr.tools.ddrinteractive.IFieldFormatter#postFormat(java.lang.String, java.lang.String, int, long, java.io.PrintStream, com.ibm.j9ddr.tools.ddrinteractive.Context, com.ibm.j9ddr.tools.ddrinteractive.IStructureFormatter)
	 */
	@Override
	public FormatWalkResult postFormat(String name, String type, String declaredType,
			int typeCode, long address, PrintStream out,
			Context context, IStructureFormatter structureFormatter) throws CorruptDataException {
		
		if (typeCode == TYPE_STRUCTURE_POINTER && StructureCommandUtil.typeToCommand(type).equals("!j9method")) {

			PointerPointer slotPtr = PointerPointer.cast(address);
			J9MethodPointer method = J9MethodPointer.cast(slotPtr.at(0));
			
			if (method.isNull()) {
				return FormatWalkResult.KEEP_WALKING;
			}
			
			out.print(" // ");
			J9ROMMethodPointer j9romMethod = J9MethodHelper.romMethod(method);
			
			J9ClassPointer clazz = ConstantPoolHelpers.J9_CLASS_FROM_METHOD(method);
			
			out.print(J9ClassHelper.getName(clazz));
			out.print(".");
			out.print(J9ROMMethodHelper.getName(j9romMethod));
			out.print(J9ROMMethodHelper.getSignature(j9romMethod));
		}
		
		return FormatWalkResult.KEEP_WALKING;
	}

}
