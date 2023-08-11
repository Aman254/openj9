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
package com.ibm.uma.freemarker;

import com.ibm.uma.UMA;
import com.ibm.uma.UMAException;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BooleanModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

public class Flag implements TemplateHashModel, TemplateScalarModel {
	
	String flag;
	
	public Flag(String flag) throws UMAException {
		if ( !UMA.getUma().getConfiguration().isFlagValid(flag) ) {
			throw new UMAException("bad flag name: " + flag);
		}
		this.flag = flag;
	}

	public TemplateModel get(String arg0) throws TemplateModelException {
		if ( arg0.equals("name")) {
			return new SimpleScalar(flag);
		}
		if ( arg0.equals("enabled")) {
			return new BooleanModel(UMA.getUma().getConfiguration().isFlagSet(flag), new BeansWrapper());
		}
		try {
			TemplateModel platformExtension = com.ibm.uma.UMA.getUma().getPlatform().getDataModelExtension("uma.spec.flags."+flag, arg0);
			if (platformExtension!=null) return platformExtension;
			TemplateModel configurationExtension = com.ibm.uma.UMA.getUma().getConfiguration().getDataModelExtension("uma.spec.flags."+flag, arg0);
			if (configurationExtension!=null) return configurationExtension;
		} catch (UMAException e) {
			throw new TemplateModelException(e);
		}
		return null;
	}
	
	public String getAsString() throws TemplateModelException {
		return flag;
	}

	public boolean isEmpty() throws TemplateModelException {
		return false;
	}

}
