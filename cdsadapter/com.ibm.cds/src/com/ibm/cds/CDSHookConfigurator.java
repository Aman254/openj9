/*******************************************************************************
 * Copyright IBM Corp. and others 2006
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

package com.ibm.cds;

import org.eclipse.osgi.internal.hookregistry.HookConfigurator;
import org.eclipse.osgi.internal.hookregistry.HookRegistry;

public class CDSHookConfigurator implements HookConfigurator {
	//////////////// HookConfigurator //////////////////
	static public boolean 	SUPPRESS_ERROR_REPORTING = "true".equals(System.getProperty("ibm.cds.suppresserrors"));
	public void addHooks(HookRegistry hookRegistry) {
		try {
			Class.forName("com.ibm.oti.shared.SharedClassHelperFactory");
		} catch (ClassNotFoundException e) {
			// not running on J9
			if (!SUPPRESS_ERROR_REPORTING) {
				System.err.println("The IBM Class Sharing Adaptor will not work in this configuration.");
				System.err.println("You are not running on a J9 Java VM.");
			}
			return;
		}
		CDSHookImpls hooks = new CDSHookImpls();
		hookRegistry.addClassLoaderHook(hooks);
		hookRegistry.addBundleFileWrapperFactoryHook(hooks);
	}

}
