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
package j9vm.test.classunloading.testcases;

import j9vm.test.classunloading.*;

/**
 *		Create a classloader (do not load any classes) and let it be unloaded.
 *
 */
public class EmptyClassLoaderFinalizationTest extends ClassUnloadingTestParent {
public static void main(String[] args) throws Exception  {
	new EmptyClassLoaderFinalizationTest().runTest();
}

protected String[] unloadableItems() { 
	return new String[] {"ClassLoader"};
}
protected String[] itemsToBeUnloaded() { 
	return new String[] {"ClassLoader"};
}

protected void createScenario() throws Exception {
	new SimpleJarClassLoader("ClassLoader", jarFileName);
}
}
