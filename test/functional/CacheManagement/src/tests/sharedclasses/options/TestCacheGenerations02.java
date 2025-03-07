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

package tests.sharedclasses.options;

import java.util.Iterator;
import java.util.List;

import tests.sharedclasses.TestUtils;

/*
 * Create cache generations (we will have generations 1 and 3 around), run destroyAll and check all generations of all caches disappear
 */
public class TestCacheGenerations02 extends TestUtils {
  public static void main(String[] args) {
    runDestroyAllCaches();    
    createGenerations01();
    runDestroyAllCaches();
    listAllCaches();
    List compatibleCaches = processOutputForCompatibleCacheList();
    // Should be no entries for Foo or Bar in that list!
    for (Iterator iterator = compatibleCaches.iterator(); iterator.hasNext();) {
		String name = (String) iterator.next();
		if (name.equals("Foo")) fail("Did not expect to find an entry for 'Foo' in the compatible cache list");
		if (name.equals("Bar")) fail("Did not expect to find an entry for 'Bar' in the compatible cache list");
	}
  }
}
