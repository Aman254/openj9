################################################################################
# Copyright IBM Corp. and others 2017
#
# This program and the accompanying materials are made available under
# the terms of the Eclipse Public License 2.0 which accompanies this
# distribution and is available at https://www.eclipse.org/legal/epl-2.0/
# or the Apache License, Version 2.0 which accompanies this distribution and
# is available at https://www.apache.org/licenses/LICENSE-2.0.
#
# This Source Code may also be made available under the following
# Secondary Licenses when the conditions for such availability set
# forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
# General Public License, version 2 with the GNU Classpath
# Exception [1] and GNU General Public License, version 2 with the
# OpenJDK Assembly Exception [2].
#
# [1] https://www.gnu.org/software/classpath/license.html
# [2] https://openjdk.org/legal/assembly-exception.html
#
# SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
################################################################################

set(gc_api_sources
	GuaranteedNurseryRange.cpp
	HeapIteratorAPI.cpp
	HeapIteratorAPIBufferedIterator.cpp
	HeapIteratorAPIRootIterator.cpp
	HeapRootScanner.cpp
)

j9vm_add_library(j9gcapi STATIC
	${gc_api_sources}
)

target_link_libraries(j9gcapi
	PRIVATE
		j9vm_interface
		j9vm_gc_includes

		omrgc
)

if(OMR_MIXED_REFERENCES_MODE_STATIC)
	j9vm_add_library(j9gcapi_full STATIC
		${gc_api_sources}
	)

	target_link_libraries(j9gcapi_full
		PRIVATE
			j9vm_interface
			j9vm_gc_includes

			omrgc_full
	)
endif()
