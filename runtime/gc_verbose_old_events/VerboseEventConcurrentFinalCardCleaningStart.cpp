
/*******************************************************************************
 * Copyright IBM Corp. and others 1991
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

#include "VerboseEventConcurrentFinalCardCleaningStart.hpp"

/**
 * Create an new instance of a MM_VerboseEventConcurrentFinalCardCleaningStart event.
 * @param event Pointer to a structure containing the data passed over the hookInterface
 */
MM_VerboseEvent *
MM_VerboseEventConcurrentFinalCardCleaningStart::newInstance(MM_ConcurrentCollectionCardCleaningStartEvent *event, J9HookInterface** hookInterface)
{
	MM_VerboseEventConcurrentFinalCardCleaningStart *eventObject;
	
	eventObject = (MM_VerboseEventConcurrentFinalCardCleaningStart *)MM_VerboseEvent::create(event->currentThread, sizeof(MM_VerboseEventConcurrentFinalCardCleaningStart));
	if(NULL != eventObject) {
		new(eventObject) MM_VerboseEventConcurrentFinalCardCleaningStart(event, hookInterface);
	}
	return eventObject;
}

/**
 * Populate events data fields.
 * The event calls the event stream requesting the address of events it is interested in.
 * When an address is returned it populates itself with the data.
 */
void
MM_VerboseEventConcurrentFinalCardCleaningStart::consumeEvents(void)
{
}

/**
 * Passes a format string and data to the output routine defined in the passed output agent.
 * @param agent Pointer to an output agent.
 */
void
MM_VerboseEventConcurrentFinalCardCleaningStart::formattedOutput(MM_VerboseOutputAgent *agent)
{
}
