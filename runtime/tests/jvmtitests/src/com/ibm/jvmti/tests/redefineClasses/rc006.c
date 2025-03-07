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
#include <string.h>

#include "jvmti_test.h"

static agentEnv * env;

static jmethodID id1;
static jmethodID id2;
static char * expected;

jint JNICALL
rc006(agentEnv * agent_env, char * args)
{
	jvmtiError err;
	jvmtiCapabilities capabilities;
	JVMTI_ACCESS_FROM_AGENT(agent_env);

	env = agent_env; 

	memset(&capabilities, 0, sizeof(jvmtiCapabilities));
	capabilities.can_redefine_classes = 1;
	capabilities.can_generate_breakpoint_events = 1;
	err = (*jvmti_env)->AddCapabilities(jvmti_env, &capabilities);
	if (err != JVMTI_ERROR_NONE) {
		error(env, err, "Failed to AddCapabilities");
		return JNI_ERR;
	}						

	return JNI_OK;
}

jboolean JNICALL
Java_com_ibm_jvmti_tests_redefineClasses_rc006_accessStoredIDs(JNIEnv * jni_env, jclass klass, jclass originalClass, jobject obj)
{
	JVMTI_ACCESS_FROM_AGENT(env);
	jvmtiError err;
	jobject result;
	jstring str;
	const char* chars;
	jboolean obsolete;

	/* Make sure these methods are not obsolete... */
	err = (*jvmti_env)->IsMethodObsolete(jvmti_env, id1, &obsolete);
	if (err != JVMTI_ERROR_NONE) {
		error(env, err, "Error while calling isMethodObsolete");
		return JNI_FALSE;
	}
	if (obsolete) {
		error(env, 0, "Redefined method meth1 is obsolete, but shouldn't be");
		return JNI_FALSE;
	}
	err = (*jvmti_env)->IsMethodObsolete(jvmti_env, id2, &obsolete);
	if (err != JVMTI_ERROR_NONE) {
		error(env, err, "Error while calling isMethodObsolete");
		return JNI_FALSE;
	}
	if (obsolete) {
		error(env, 0, "Redefined method meth1 is obsolete, but shouldn't be");
		return JNI_FALSE;
	}

	/* Test the method IDs by invoking them, and checking return values. */
	result = (*jni_env)->CallObjectMethod(jni_env, obj, id1);
	str = (jstring) result;
	chars = (const char *) (*jni_env)->GetStringUTFChars(jni_env, str, NULL);
	if (chars == NULL || strcmp(chars, expected)) {
		error(env, 0, "Return value of method meth1 was not '%s' as expected", expected);
		return JNI_FALSE;
	}

	result = (*jni_env)->CallObjectMethod(jni_env, obj, id2);
	if (!(*jni_env)->IsInstanceOf(jni_env, obj, originalClass)) {
		error(env, 0, "Return value of method meth2 is not of the right class");
		return JNI_FALSE;
	}

	if (!(*jni_env)->IsSameObject(jni_env, result, obj)) {
		error(env, 0, "Return value of method meth2 is not equal to the instance it should return");
		return JNI_FALSE;
	}
	
	expected = "!321";
	return JNI_TRUE;
}

jboolean JNICALL
Java_com_ibm_jvmti_tests_redefineClasses_rc006_redefineClassAndTestMethodIDs(JNIEnv * jni_env, jclass klass, jclass originalClass, jobject obj, jint classBytesSize, jbyteArray classBytes)
{
	JVMTI_ACCESS_FROM_AGENT(env);
	jbyte * class_bytes;
	char * sig = "()Lcom/ibm/jvmti/tests/redefineClasses/rc006_testMethodIDsAfterRedefine_O1;";
	char * classFileName = env->testArgs;
	jvmtiClassDefinition classdef;
	jvmtiError err;
	jobject result;
	jstring str;
	const char* chars;

	/* Retrieve the method ID for meth1. */
	id1 = (*jni_env)->GetMethodID(jni_env, originalClass, "meth1", "()Ljava/lang/String;");
	if (id1 == NULL) {
		error(env, 0, "Unable to retrieve original jmethodID for method meth1");
		return JNI_FALSE;
	}

	/* Retrieve the method ID for meth2. */
	id2 = (*jni_env)->GetMethodID(jni_env, originalClass, "meth2", sig);
	if (id1 == NULL) {
		error(env, 0, "Unable to retrieve original jmethodID for method meth2");
		return JNI_FALSE;
	}

	/* Test the method IDs initially by invoking them, and checking return values. */
	result = (*jni_env)->CallObjectMethod(jni_env, obj, id1);
	str = (jstring) result;
	chars = (const char *) (*jni_env)->GetStringUTFChars(jni_env, str, NULL);
	if (chars == NULL || strcmp(chars, "123!")) {
		error(env, 0, "Return value of method meth1 was not '123!' as expected");
		return JNI_FALSE;
	}

	result = (*jni_env)->CallObjectMethod(jni_env, obj, id2);
	if (!(*jni_env)->IsInstanceOf(jni_env, obj, originalClass)) {
		error(env, 0, "Return value of method meth2 is not of the right class");
		return JNI_FALSE;
	}

	if (!(*jni_env)->IsSameObject(jni_env, result, obj)) {
		error(env, 0, "Return value of method meth2 is not equal to the instance it should return");
		return JNI_FALSE;
	}
	
	err = (*jvmti_env)->Allocate(jvmti_env, classBytesSize, (unsigned char **) &class_bytes);
	if (err != JVMTI_ERROR_NONE) {
		error(env, err, "Unable to allocate temp buffer for the class file");
		return JNI_FALSE;
	}

	(*jni_env)->GetByteArrayRegion(jni_env, classBytes, 0, classBytesSize, class_bytes); 

	classdef.class_bytes = (unsigned char *) class_bytes;
	classdef.class_byte_count = classBytesSize;
	classdef.klass = originalClass;

	err = (*jvmti_env)->RedefineClasses(jvmti_env, 1, &classdef);
	(*jvmti_env)->Deallocate(jvmti_env, (unsigned char *) class_bytes);
	if (err != JVMTI_ERROR_NONE) {
		error(env, err, "RedefineClasses failed");
		return JNI_FALSE;
	}

	/* Access stored IDs here to test them, while still in this method. It will be called again
	 * from Java, to do the same thing, but outside of this method, with a different reference
	 * to originalClass. Both should work.
	 * */
	expected = "!123";
	return Java_com_ibm_jvmti_tests_redefineClasses_rc006_accessStoredIDs(jni_env, klass, originalClass, obj);
}

