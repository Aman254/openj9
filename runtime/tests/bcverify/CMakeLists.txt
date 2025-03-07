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

set(OMR_ENHANCED_WARNINGS OFF)
set_source_files_properties(${j9vm_BINARY_DIR}/bcverify/ut_j9bcverify.c PROPERTIES GENERATED TRUE)
j9vm_add_library(bcvwhite SHARED
	natives/bcvnatives.c

	# TODO do we really need to recompile these sources,
	# or can we just reuse the object files?
	${j9vm_SOURCE_DIR}/bcverify/bcverify.c
	${j9vm_SOURCE_DIR}/bcverify/classrelationships.c
	${j9vm_SOURCE_DIR}/bcverify/clconstraints.c
	${j9vm_SOURCE_DIR}/bcverify/rtverify.c
	${j9vm_SOURCE_DIR}/bcverify/staticverify.c
	${j9vm_SOURCE_DIR}/bcverify/vrfyconvert.c
	${j9vm_SOURCE_DIR}/bcverify/vrfyhelp.c

	# Note that tracegen has put ut_j9bcverify in the binary dir for bcverify
	${j9vm_BINARY_DIR}/bcverify/ut_j9bcverify.c
)

# We need to add an explicit dependency on the actual tracegen step since CMake
# doesn't properly track generated file dependencies across directories.
add_dependencies(bcvwhite trc_j9bcverify)

target_link_libraries(bcvwhite
	PRIVATE
		j9vm_interface
		j9vm_gc_includes

		j9verutil
		j9util
		j9utilcore
		j9avl
		j9hashtable
		j9pool
		j9thr
		j9stackmap
)

target_include_directories(bcvwhite
	PRIVATE
		${j9vm_SOURCE_DIR}/shared_common/include
		# We need to add the binary dir for bcverify to get the tracegen headers.
		${j9vm_BINARY_DIR}/bcverify
)

omr_add_exports(bcvwhite
	Java_com_ibm_j9_test_bcverify_TestNatives_bcvCheckName
	Java_com_ibm_j9_test_bcverify_TestNatives_bcvCheckClassName
	Java_com_ibm_j9_test_bcverify_TestNatives_bcvCheckMethodName
)

j9vm_add_library(bcvrelationships SHARED
	relationships/bcvrelationships.c
)

target_link_libraries(bcvrelationships
	PRIVATE
		j9vm_interface

		j9avl
		j9hashtable
		j9pool
		j9utilcore
)

omr_add_exports(bcvrelationships
	Java_org_openj9_test_classRelationshipVerifier_TestClassRelationshipVerifier_isRelationshipRecorded
)

install(
	TARGETS bcvwhite bcvrelationships
	LIBRARY DESTINATION ${j9vm_SOURCE_DIR}
	RUNTIME DESTINATION ${j9vm_SOURCE_DIR}
)
