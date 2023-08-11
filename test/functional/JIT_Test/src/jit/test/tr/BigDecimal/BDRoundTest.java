/*******************************************************************************
 * Copyright IBM Corp. and others 2017
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
package jit.test.tr.BigDecimal;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Test(groups = { "level.sanity","component.jit" })
public class BDRoundTest {
   @Test
   public void testRound1() {
      BigDecimal a = new BigDecimal("1.234E+53");
      BigDecimal b = new BigDecimal("1.999E+103");
      MathContext mc = new MathContext(16, RoundingMode.HALF_UP);
      BigDecimal c1 = b.add(a, mc);
      BigDecimal c2 = b.add(a).round(mc);
      AssertJUnit.assertEquals("testRound1", c1, c2);
   }
}
