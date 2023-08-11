/*******************************************************************************
 * Copyright IBM Corp. and others 2001
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
package com.ibm.j9ddr.util;

public final class RuntimeTypeResolutionUtils
{
	
	// Design 42819
	// Discover runtime type based on identifier field.

	public static String cleanTypeStr(String type)
	{
		/*
		 * Unfortunately not all compilers behave the same with regards to the __FUNCTION__ macro, examples:
		 * GCC:		mm_heapregiondescriptorstandard
		 * MSVC:	mm_heapregiondescriptorstandard::mm_heapregiondescriptorstandard
		 * XL C:	mm_heapregiondescriptorstandard::mm_heapregiondescriptorstandard(mm_environmentstandard *, void *, void *)
		 */
		
		int idxEnd = type.indexOf(':');
		if (-1 != idxEnd) { // MSVC or XL C
			type = type.substring(0, idxEnd);
		}
		return type;
	}
	
}
