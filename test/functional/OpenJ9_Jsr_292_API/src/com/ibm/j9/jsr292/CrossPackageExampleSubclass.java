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
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
 *******************************************************************************/
package com.ibm.j9.jsr292;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;

import examples.PackageExamples;

/**
 * @author mesbah
 *
 */
public class CrossPackageExampleSubclass extends PackageExamples {
	public int addPublic(int a, int b) {return a+b+2;} // overridden addPublic that returns something different
	public int addProtected(int a, int b) {return -1;}
	public int addPublic_Child(int a, int b) {return a+b+2;} 
	
	public int nonStaticPublicField_Child;
	public static int staticPublicField_Child;
	
	private int nonStaticPrivateField_Child;
	private static int staticPrivateField_Child;
	
	protected int nonStaticProtectedField_Child;
	
	public static Lookup getLookup() {
		return MethodHandles.lookup();
	}
	
	public String call_finalProtectedMethod() {
		/* necessary to validate the result of this call from outside the package / hierarchy */
		return finalProtectedMethod();
	}
	
	public CrossPackageExampleSubclass() { }
	public CrossPackageExampleSubclass(int a, int b) {
		super();
		nonStaticPublicField_Child = a + b; 
	}
	
}
