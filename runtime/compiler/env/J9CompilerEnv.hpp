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

#ifndef J9_COMPILER_ENV_INCL
#define J9_COMPILER_ENV_INCL

/*
 * The following #define and typedef must appear before any #includes in this file
 */
#ifndef J9_COMPILER_ENV_CONNECTOR
#define J9_COMPILER_ENV_CONNECTOR
   namespace J9 { class CompilerEnv; }
   namespace J9 { typedef J9::CompilerEnv CompilerEnvConnector; }
#endif

#include "env/OMRCompilerEnv.hpp"

extern "C" {
struct J9PortLibrary;
struct J9JavaVM;
}

namespace J9
{

class OMR_EXTENSIBLE CompilerEnv : public OMR::CompilerEnvConnector
   {
public:

   CompilerEnv(J9JavaVM *vm, TR::RawAllocator raw, const TR::PersistentAllocatorKit &persistentAllocatorKit);

   J9PortLibrary * const portLib;
   J9JavaVM * const javaVM;

   void initializeTargetEnvironment();

   void initializeRelocatableTargetEnvironment();

   bool isCodeTossed();

   TR::PersistentAllocator &persistentAllocator();

   TR_PersistentMemory *persistentMemory();

#if defined(J9VM_OPT_JITSERVER)
   TR::PersistentAllocator &persistentGlobalAllocator();
   TR_PersistentMemory *persistentGlobalMemory();
#endif /* defined(J9VM_OPT_JITSERVER) */
   };

}

#endif
