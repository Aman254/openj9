/*******************************************************************************
 * Copyright IBM Corp. and others 2005
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
package APITests;

import com.ibm.oti.shared.HelperAlreadyDefinedException;

import CustomCLs.CustomURLLoader;
import Utilities.URLClassPathCreator;

public class URLGetDifferentHelperTest {

	public static void main(String[] args) {
		URLGetDifferentHelperTest test = new URLGetDifferentHelperTest();
		test.run();
	}
	
	public void run(){
		
		boolean resultURL = false;
		boolean resultToken = false;
		URLClassPathCreator pathCreator = new URLClassPathCreator("./Pets;");
		
		CustomURLLoader loader = new CustomURLLoader(pathCreator.createURLClassPath());
		
		try{
			loader.getURLClasspathHelper();
		} catch (HelperAlreadyDefinedException e) {
			resultURL = true;
		}
		
		try{
			loader.getTokenHelper();
		} catch (Exception e) {
			resultToken = true;
		}
		
		if((resultURL == true) && (resultToken == true)){
			System.out.println("\nTEST PASSED");
		} else {
			System.out.println("\nTEST FAILED");
		}
	}
}
