/*[INCLUDE-IF Sidecar17]*/
/*******************************************************************************
 * Copyright IBM Corp. and others 2016
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
package com.ibm.lang.management.internal;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;

import com.ibm.java.lang.management.internal.ManagementUtils;
import com.ibm.lang.management.ProcessorUsage;

/**
 * Support for the {@link ProcessorUsage} class.
 */
public final class ProcessorUsageUtil {

	private static CompositeType compositeType;

	/**
	 * @return an instance of (@link CompositeType} for the {@link ProcessorUsage} class
	 */
	public static CompositeType getCompositeType() {
		if (null == compositeType) {
			try {
				String[] names = { "user", "system", "idle", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"wait", "busy", "id", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"online", "timestamp" }; //$NON-NLS-1$ //$NON-NLS-2$
				String[] dscs = { "user", "system", "idle", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"wait", "busy", "id", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						"online", "timestamp" }; //$NON-NLS-1$ //$NON-NLS-2$
				OpenType<?>[] types = {
						SimpleType.LONG, SimpleType.LONG, SimpleType.LONG,
						SimpleType.LONG, SimpleType.LONG, SimpleType.INTEGER,
						SimpleType.INTEGER, SimpleType.LONG };

				compositeType = new CompositeType(
						ProcessorUsage.class.getName(),
						ProcessorUsage.class.getName(),
						names, dscs, types);
			} catch (OpenDataException e) {
				if (ManagementUtils.VERBOSE_MODE) {
					e.printStackTrace(System.err);
				}
			}
		}

		return compositeType;
	}

	/**
	 * @param usage a {@link ProcessorUsage} object
	 * @return a {@link CompositeData} object that represents the supplied <code>usage</code> object
	 */
	public static CompositeData toCompositeData(ProcessorUsage usage) {
		CompositeData result = null;

		if (null != usage) {
			CompositeType type = getCompositeType();
			String[] names = { "user", "system", "idle", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					"wait", "busy", "id", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					"online", "timestamp" }; //$NON-NLS-1$ //$NON-NLS-2$
			Object[] values = {
					Long.valueOf(usage.getUser()),
					Long.valueOf(usage.getSystem()),
					Long.valueOf(usage.getIdle()),
					Long.valueOf(usage.getWait()),
					Long.valueOf(usage.getBusy()),
					Integer.valueOf(usage.getId()),
					Integer.valueOf(usage.getOnline()),
					Long.valueOf(usage.getTimestamp()) };

			try {
				result = new CompositeDataSupport(type, names, values);
			} catch (OpenDataException e) {
				if (ManagementUtils.VERBOSE_MODE) {
					e.printStackTrace(System.err);
				}
			}
		}

		return result;
	}

	private ProcessorUsageUtil() {
		super();
	}

}
