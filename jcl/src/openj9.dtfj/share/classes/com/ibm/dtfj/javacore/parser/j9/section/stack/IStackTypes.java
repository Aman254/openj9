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
package com.ibm.dtfj.javacore.parser.j9.section.stack;

public interface IStackTypes {
	
	public static final String STACK_SECTION = "Stack";
	
	public static final String T_BTTHREADID = "BTTHREADID";
	public static final String T_1BTSTACKENT = "1BTSTACKENT"; 
	/*
	 * Attributes
	 */
	public static final String STACK_THREAD = "stack_thread";
	public static final String STACK_MODULE = "stack_module";
	public static final String STACK_ROUTINE = "stack_routine";
	public static final String STACK_OFFSET = "stack_offset";
	public static final String STACK_PROC_ADDRESS = "stack_proc_address";
	public static final String STACK_ROUTINE_ADDRESS = "stack_routine_address";
	public static final String STACK_FILE = "stack_file";
	public static final String STACK_LINE = "stack_line";
}
