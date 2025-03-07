
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
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
 *******************************************************************************/


/**
 * @file
 * @ingroup GC_Modron_Standard
 */

#if !defined(GLOBALMARKNOSCANCARDCLEANER_HPP_)
#define GLOBALMARKNOSCANCARDCLEANER_HPP_


#include "j9.h"
#include "j9cfg.h"
#include "j9modron.h"

#include "CardCleaner.hpp"

/**
 * @todo Provide typedef documentation
 * @ingroup GC_Modron_Standard
 */

class MM_GlobalMarkNoScanCardCleaner : public MM_CardCleaner 
{
	/* Data Members */
private:
protected:
public:

	/* Member Functions */
private:
protected:
	/**
	 * Clean a range of addresses (typically within a span of a card).
	 * This class is used for updating the RSM based on the previous mark map and scanning modified objects in the next mark map
	 * (note that next is always a subset of previous so anything not marked in next but marked in previous only requires RSM
	 * inter-region reference updating while objects marked in both require RSM updates and object scanning to update the next
	 * mark map).
	 *
	 * @param[in] env A thread (typically the thread initializing the GC)
	 * @param[in] lowAddress low address of the range to be cleaned
	 * @param[in] highAddress high address of the range to be cleaned 
	 * @param cardToClean[in/out] The card which we are cleaning
	 */		
	virtual void clean(MM_EnvironmentBase *env, void *lowAddress, void *highAddress, Card *cardToClean);

	/**
	 * @see MM_CardCleaner::getVMStateID()
	 */
	virtual UDATA getVMStateID() { return OMRVMSTATE_GC_GLOBAL_MARK_NO_SCAN_CARD_CLEANER; }

public:	
	
	MM_GlobalMarkNoScanCardCleaner()
		: MM_CardCleaner()
	{
		_typeId = __FUNCTION__;
	}
};

#endif /* GLOBALMARKNOSCANCARDCLEANER_HPP_ */
