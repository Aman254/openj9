/*******************************************************************************
 * Copyright IBM Corp. and others 2000
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

#include "j9.h"

/* 2 transitions per return type + 1 for all natives */
#define J9JIT_NUM_J2I_TRANSITIONS 11

J9JITConfig *codert_onload(J9JavaVM *javaVM);
UDATA codert_onbootstrap(J9VMThread * vmThread);
void codert_init_helpers_and_targets(J9JITConfig *jitConfig, char isSMP);
UDATA codert_shutdown(J9VMThread * vmThread, IDATA rc);
void codert_OnUnload(J9JavaVM *javaVM);
void codert_freeJITConfig(J9JavaVM *javaVM);
UDATA lookupSendTargetForThunk(J9JavaVM * javaVM, int thunkNumber);

extern "C" {
void revertMethodToInterpreted(J9Method * method);
void * getRuntimeHelperValue(int h);
}

