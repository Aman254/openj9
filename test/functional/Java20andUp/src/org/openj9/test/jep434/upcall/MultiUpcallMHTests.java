/*******************************************************************************
 * Copyright IBM Corp. and others 2023
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
package org.openj9.test.jep434.upcall;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.AssertJUnit;

import java.lang.invoke.MethodHandle;

import java.lang.foreign.Arena;
import java.lang.foreign.Linker;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import static java.lang.foreign.ValueLayout.*;

/**
 * Test cases for JEP 434: Foreign Linker API (Second Preview) intended for
 * the situations when the multiple primitive specific upcalls happen within
 * the same memory arena.scope() or from different memory arena.scope()s.
 */
@Test(groups = { "level.sanity" })
public class MultiUpcallMHTests {
	private static Linker linker = Linker.nativeLinker();

	static {
		System.loadLibrary("clinkerffitests");
	}
	private static final SymbolLookup nativeLibLookup = SymbolLookup.loaderLookup();

	@Test
	public void test_addTwoBoolsWithOrByUpcallMH_SameScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_BOOLEAN, JAVA_BOOLEAN, JAVA_BOOLEAN, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("add2BoolsWithOrByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_add2BoolsWithOr,
					FunctionDescriptor.of(JAVA_BOOLEAN, JAVA_BOOLEAN, JAVA_BOOLEAN), arena.scope());
			boolean result = (boolean)mh.invoke(true, false, upcallFuncAddr1);
			Assert.assertEquals(result, true);

			MemorySegment upcallFuncAddr2 = linker.upcallStub(UpcallMethodHandles.MH_add2BoolsWithOr,
					FunctionDescriptor.of(JAVA_BOOLEAN, JAVA_BOOLEAN, JAVA_BOOLEAN), arena.scope());
			result = (boolean)mh.invoke(true, false, upcallFuncAddr2);
			Assert.assertEquals(result, true);

			MemorySegment upcallFuncAddr3 = linker.upcallStub(UpcallMethodHandles.MH_add2BoolsWithOr,
					FunctionDescriptor.of(JAVA_BOOLEAN, JAVA_BOOLEAN, JAVA_BOOLEAN), arena.scope());
			result = (boolean)mh.invoke(true, false, upcallFuncAddr3);
			Assert.assertEquals(result, true);
		}
	}

	@Test
	public void test_addTwoBoolsWithOrByUpcallMH_DiffScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_BOOLEAN, JAVA_BOOLEAN, JAVA_BOOLEAN, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("add2BoolsWithOrByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_add2BoolsWithOr,
					FunctionDescriptor.of(JAVA_BOOLEAN, JAVA_BOOLEAN, JAVA_BOOLEAN), arena.scope());
			boolean result = (boolean)mh.invoke(true, false, upcallFuncAddr);
			Assert.assertEquals(result, true);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_add2BoolsWithOr,
					FunctionDescriptor.of(JAVA_BOOLEAN, JAVA_BOOLEAN, JAVA_BOOLEAN), arena.scope());
			boolean result = (boolean)mh.invoke(true, false, upcallFuncAddr);
			Assert.assertEquals(result, true);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_add2BoolsWithOr,
					FunctionDescriptor.of(JAVA_BOOLEAN, JAVA_BOOLEAN, JAVA_BOOLEAN), arena.scope());
			boolean result = (boolean)mh.invoke(true, false, upcallFuncAddr);
			Assert.assertEquals(result, true);
		}
	}

	@Test
	public void test_createNewCharFromCharAndCharFromPointerByUpcallMH_SameScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_CHAR, ADDRESS, JAVA_CHAR, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("createNewCharFromCharAndCharFromPointerByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());

			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_createNewCharFromCharAndCharFromPointer,
					FunctionDescriptor.of(JAVA_CHAR, ADDRESS, JAVA_CHAR), arena.scope());
			MemorySegment charSegmt1 = allocator.allocate(JAVA_CHAR, 'B');
			char result = (char)mh.invoke(charSegmt1, 'D', upcallFuncAddr1);
			Assert.assertEquals(result, 'C');

			MemorySegment upcallFuncAddr2 = linker.upcallStub(UpcallMethodHandles.MH_createNewCharFromCharAndCharFromPointer,
					FunctionDescriptor.of(JAVA_CHAR, ADDRESS, JAVA_CHAR), arena.scope());
			MemorySegment charSegmt2 = allocator.allocate(JAVA_CHAR, 'B');
			result = (char)mh.invoke(charSegmt2, 'D', upcallFuncAddr2);
			Assert.assertEquals(result, 'C');

			MemorySegment upcallFuncAddr3 = linker.upcallStub(UpcallMethodHandles.MH_createNewCharFromCharAndCharFromPointer,
					FunctionDescriptor.of(JAVA_CHAR, ADDRESS, JAVA_CHAR), arena.scope());
			MemorySegment charSegmt3 = allocator.allocate(JAVA_CHAR, 'B');
			result = (char)mh.invoke(charSegmt3, 'D', upcallFuncAddr3);
			Assert.assertEquals(result, 'C');
		}
	}

	@Test
	public void test_createNewCharFromCharAndCharFromPointerByUpcallMH_DiffScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_CHAR, ADDRESS, JAVA_CHAR, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("createNewCharFromCharAndCharFromPointerByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_createNewCharFromCharAndCharFromPointer,
					FunctionDescriptor.of(JAVA_CHAR, ADDRESS, JAVA_CHAR), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment charSegmt = allocator.allocate(JAVA_CHAR, 'B');
			char result = (char)mh.invoke(charSegmt, 'D', upcallFuncAddr1);
			Assert.assertEquals(result, 'C');
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_createNewCharFromCharAndCharFromPointer,
					FunctionDescriptor.of(JAVA_CHAR, ADDRESS, JAVA_CHAR), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment charSegmt = allocator.allocate(JAVA_CHAR, 'B');
			char result = (char)mh.invoke(charSegmt, 'D', upcallFuncAddr1);
			Assert.assertEquals(result, 'C');
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_createNewCharFromCharAndCharFromPointer,
					FunctionDescriptor.of(JAVA_CHAR, ADDRESS, JAVA_CHAR), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment charSegmt = allocator.allocate(JAVA_CHAR, 'B');
			char result = (char)mh.invoke(charSegmt, 'D', upcallFuncAddr1);
			Assert.assertEquals(result, 'C');
		}
	}

	@Test
	public void test_addByteAndByteFromNativePtrByUpcallMH_SameScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_BYTE, JAVA_BYTE, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("addByteAndByteFromNativePtrByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_addByteAndByteFromPointer,
					FunctionDescriptor.of(JAVA_BYTE, JAVA_BYTE, ADDRESS), arena.scope());
			byte result = (byte)mh.invoke((byte)33, upcallFuncAddr1);
			Assert.assertEquals(result, (byte)88);

			MemorySegment upcallFuncAddr2 = linker.upcallStub(UpcallMethodHandles.MH_addByteAndByteFromPointer,
					FunctionDescriptor.of(JAVA_BYTE, JAVA_BYTE, ADDRESS), arena.scope());
			result = (byte)mh.invoke((byte)33, upcallFuncAddr2);
			Assert.assertEquals(result, (byte)88);

			MemorySegment upcallFuncAddr3 = linker.upcallStub(UpcallMethodHandles.MH_addByteAndByteFromPointer,
					FunctionDescriptor.of(JAVA_BYTE, JAVA_BYTE, ADDRESS), arena.scope());
			result = (byte)mh.invoke((byte)33, upcallFuncAddr3);
			Assert.assertEquals(result, (byte)88);
		}
	}

	@Test
	public void test_addByteAndByteFromNativePtrByUpcallMH_DiffScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_BYTE, JAVA_BYTE, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("addByteAndByteFromNativePtrByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addByteAndByteFromPointer,
					FunctionDescriptor.of(JAVA_BYTE, JAVA_BYTE, ADDRESS), arena.scope());
			byte result = (byte)mh.invoke((byte)33, upcallFuncAddr);
			Assert.assertEquals(result, (byte)88);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addByteAndByteFromPointer,
					FunctionDescriptor.of(JAVA_BYTE, JAVA_BYTE, ADDRESS), arena.scope());
			byte result = (byte)mh.invoke((byte)33, upcallFuncAddr);
			Assert.assertEquals(result, (byte)88);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addByteAndByteFromPointer,
					FunctionDescriptor.of(JAVA_BYTE, JAVA_BYTE, ADDRESS), arena.scope());
			byte result = (byte)mh.invoke((byte)33, upcallFuncAddr);
			Assert.assertEquals(result, (byte)88);
		}
	}

	@Test
	public void test_addShortAndShortFromPtr_RetPtr_ByUpcallMH_SameScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_SHORT, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("addShortAndShortFromPtr_RetPtr_ByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());

			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_addShortAndShortFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_SHORT), arena.scope());
			MemorySegment shortSegmt1 = allocator.allocate(JAVA_SHORT, (short)444);
			MemorySegment resultAddr1 = (MemorySegment)mh.invoke(shortSegmt1, (short)555, upcallFuncAddr1);
			MemorySegment resultSegmt1 = MemorySegment.ofAddress(resultAddr1.address(), JAVA_SHORT.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt1.get(JAVA_SHORT, 0), (short)999);

			MemorySegment upcallFuncAddr2 = linker.upcallStub(UpcallMethodHandles.MH_addShortAndShortFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_SHORT), arena.scope());
			MemorySegment shortSegmt2 = allocator.allocate(JAVA_SHORT, (short)444);
			MemorySegment resultAddr2 = (MemorySegment)mh.invoke(shortSegmt2, (short)555, upcallFuncAddr2);
			MemorySegment resultSegmt2 = MemorySegment.ofAddress(resultAddr2.address(), JAVA_SHORT.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt2.get(JAVA_SHORT, 0), (short)999);

			MemorySegment upcallFuncAddr3 = linker.upcallStub(UpcallMethodHandles.MH_addShortAndShortFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_SHORT), arena.scope());
			MemorySegment shortSegmt3 = allocator.allocate(JAVA_SHORT, (short)444);
			MemorySegment resultAddr3 = (MemorySegment)mh.invoke(shortSegmt3, (short)555, upcallFuncAddr3);
			MemorySegment resultSegmt3 = MemorySegment.ofAddress(resultAddr3.address(), JAVA_SHORT.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt3.get(JAVA_SHORT, 0), (short)999);
		}
	}

	@Test
	public void test_addShortAndShortFromPtr_RetPtr_ByUpcallMH_DiffScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_SHORT, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("addShortAndShortFromPtr_RetPtr_ByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addShortAndShortFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_SHORT), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment shortSegmt = allocator.allocate(JAVA_SHORT, (short)444);
			MemorySegment resultAddr = (MemorySegment)mh.invoke(shortSegmt, (short)555, upcallFuncAddr);
			MemorySegment resultSegmt = MemorySegment.ofAddress(resultAddr.address(), JAVA_SHORT.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt.get(JAVA_SHORT, 0), (short)999);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addShortAndShortFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_SHORT), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment shortSegmt = allocator.allocate(JAVA_SHORT, (short)444);
			MemorySegment resultAddr = (MemorySegment)mh.invoke(shortSegmt, (short)555, upcallFuncAddr);
			MemorySegment resultSegmt = MemorySegment.ofAddress(resultAddr.address(), JAVA_SHORT.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt.get(JAVA_SHORT, 0), (short)999);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addShortAndShortFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_SHORT), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment shortSegmt = allocator.allocate(JAVA_SHORT, (short)444);
			MemorySegment resultAddr = (MemorySegment)mh.invoke(shortSegmt, (short)555, upcallFuncAddr);
			MemorySegment resultSegmt = MemorySegment.ofAddress(resultAddr.address(), JAVA_SHORT.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt.get(JAVA_SHORT, 0), (short)999);
		}
	}

	@Test
	public void test_addTwoIntsByUpcallMH_SameScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_INT, JAVA_INT, JAVA_INT, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("add2IntsByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_add2Ints,
					FunctionDescriptor.of(JAVA_INT, JAVA_INT, JAVA_INT), arena.scope());
			int result = (int)mh.invoke(111112, 111123, upcallFuncAddr1);
			Assert.assertEquals(result, 222235);

			MemorySegment upcallFuncAddr2 = linker.upcallStub(UpcallMethodHandles.MH_add2Ints,
					FunctionDescriptor.of(JAVA_INT, JAVA_INT, JAVA_INT), arena.scope());
			result = (int)mh.invoke(111112, 111123, upcallFuncAddr2);
			Assert.assertEquals(result, 222235);

			MemorySegment upcallFuncAddr3 = linker.upcallStub(UpcallMethodHandles.MH_add2Ints,
					FunctionDescriptor.of(JAVA_INT, JAVA_INT, JAVA_INT), arena.scope());
			result = (int)mh.invoke(111112, 111123, upcallFuncAddr3);
			Assert.assertEquals(result, 222235);
		}
	}

	@Test
	public void test_addTwoIntsByUpcallMH_DiffScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_INT, JAVA_INT, JAVA_INT, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("add2IntsByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_add2Ints,
					FunctionDescriptor.of(JAVA_INT, JAVA_INT, JAVA_INT), arena.scope());
			int result = (int)mh.invoke(111112, 111123, upcallFuncAddr);
			Assert.assertEquals(result, 222235);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_add2Ints,
					FunctionDescriptor.of(JAVA_INT, JAVA_INT, JAVA_INT), arena.scope());
			int result = (int)mh.invoke(111112, 111123, upcallFuncAddr);
			Assert.assertEquals(result, 222235);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_add2Ints,
					FunctionDescriptor.of(JAVA_INT, JAVA_INT, JAVA_INT), arena.scope());
			int result = (int)mh.invoke(111112, 111123, upcallFuncAddr);
			Assert.assertEquals(result, 222235);
		}
	}

	@Test
	public void test_addTwoIntsReturnVoidByUpcallMH_SameScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.ofVoid(JAVA_INT, JAVA_INT, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("add2IntsReturnVoidByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_add2IntsReturnVoid,
					FunctionDescriptor.ofVoid(JAVA_INT, JAVA_INT), arena.scope());
			mh.invoke(111454, 111398, upcallFuncAddr1);

			MemorySegment upcallFuncAddr2 = linker.upcallStub(UpcallMethodHandles.MH_add2IntsReturnVoid,
					FunctionDescriptor.ofVoid(JAVA_INT, JAVA_INT), arena.scope());
			mh.invoke(111454, 111398, upcallFuncAddr2);

			MemorySegment upcallFuncAddr3 = linker.upcallStub(UpcallMethodHandles.MH_add2IntsReturnVoid,
					FunctionDescriptor.ofVoid(JAVA_INT, JAVA_INT), arena.scope());
			mh.invoke(111454, 111398, upcallFuncAddr3);
		}
	}

	@Test
	public void test_addTwoIntsReturnVoidByUpcallMH_DiffScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.ofVoid(JAVA_INT, JAVA_INT, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("add2IntsReturnVoidByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_add2IntsReturnVoid,
					FunctionDescriptor.ofVoid(JAVA_INT, JAVA_INT), arena.scope());
			mh.invoke(111454, 111398, upcallFuncAddr);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_add2IntsReturnVoid,
					FunctionDescriptor.ofVoid(JAVA_INT, JAVA_INT), arena.scope());
			mh.invoke(111454, 111398, upcallFuncAddr);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_add2IntsReturnVoid,
					FunctionDescriptor.ofVoid(JAVA_INT, JAVA_INT), arena.scope());
			mh.invoke(111454, 111398, upcallFuncAddr);
		}
	}

	@Test
	public void test_addLongAndLongFromPointerByUpcallMH_SameScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_LONG, ADDRESS, JAVA_LONG, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("addLongAndLongFromPointerByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());

			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_addLongAndLongFromPointer,
					FunctionDescriptor.of(JAVA_LONG, ADDRESS, JAVA_LONG), arena.scope());
			MemorySegment longSegmt1 = allocator.allocate(JAVA_LONG, 5742457424L);
			long result = (long)mh.invoke(longSegmt1, 6666698235L, upcallFuncAddr1);
			Assert.assertEquals(result, 12409155659L);

			MemorySegment upcallFuncAddr2 = linker.upcallStub(UpcallMethodHandles.MH_addLongAndLongFromPointer,
					FunctionDescriptor.of(JAVA_LONG, ADDRESS, JAVA_LONG), arena.scope());
			MemorySegment longSegmt2 = allocator.allocate(JAVA_LONG, 5742457424L);
			result = (long)mh.invoke(longSegmt2, 6666698235L, upcallFuncAddr2);
			Assert.assertEquals(result, 12409155659L);

			MemorySegment upcallFuncAddr3 = linker.upcallStub(UpcallMethodHandles.MH_addLongAndLongFromPointer,
					FunctionDescriptor.of(JAVA_LONG, ADDRESS, JAVA_LONG), arena.scope());
			MemorySegment longSegmt3 = allocator.allocate(JAVA_LONG, 5742457424L);
			result = (long)mh.invoke(longSegmt3, 6666698235L, upcallFuncAddr3);
			Assert.assertEquals(result, 12409155659L);
		}
	}

	@Test
	public void test_addLongAndLongFromPointerByUpcallMH_DiffScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_LONG, ADDRESS, JAVA_LONG, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("addLongAndLongFromPointerByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addLongAndLongFromPointer,
					FunctionDescriptor.of(JAVA_LONG, ADDRESS, JAVA_LONG), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment longSegmt = allocator.allocate(JAVA_LONG, 5742457424L);
			long result = (long)mh.invoke(longSegmt, 6666698235L, upcallFuncAddr);
			Assert.assertEquals(result, 12409155659L);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addLongAndLongFromPointer,
					FunctionDescriptor.of(JAVA_LONG, ADDRESS, JAVA_LONG), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment longSegmt = allocator.allocate(JAVA_LONG, 5742457424L);
			long result = (long)mh.invoke(longSegmt, 6666698235L, upcallFuncAddr);
			Assert.assertEquals(result, 12409155659L);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addLongAndLongFromPointer,
					FunctionDescriptor.of(JAVA_LONG, ADDRESS, JAVA_LONG), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment longSegmt = allocator.allocate(JAVA_LONG, 5742457424L);
			long result = (long)mh.invoke(longSegmt, 6666698235L, upcallFuncAddr);
			Assert.assertEquals(result, 12409155659L);
		}
	}

	@Test
	public void test_addFloatAndFloatFromNativePtrByUpcallMH_SameScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_FLOAT, JAVA_FLOAT, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("addFloatAndFloatFromNativePtrByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_addFloatAndFloatFromPointer,
					FunctionDescriptor.of(JAVA_FLOAT, JAVA_FLOAT, ADDRESS), arena.scope());
			float result = (float)mh.invoke(5.74F, upcallFuncAddr1);
			Assert.assertEquals(result, 12.53F, 0.01F);

			MemorySegment upcallFuncAddr2 = linker.upcallStub(UpcallMethodHandles.MH_addFloatAndFloatFromPointer,
					FunctionDescriptor.of(JAVA_FLOAT, JAVA_FLOAT, ADDRESS), arena.scope());
			result = (float)mh.invoke(5.74F, upcallFuncAddr2);
			Assert.assertEquals(result, 12.53F, 0.01F);

			MemorySegment upcallFuncAddr3 = linker.upcallStub(UpcallMethodHandles.MH_addFloatAndFloatFromPointer,
					FunctionDescriptor.of(JAVA_FLOAT, JAVA_FLOAT, ADDRESS), arena.scope());
			result = (float)mh.invoke(5.74F, upcallFuncAddr3);
			Assert.assertEquals(result, 12.53F, 0.01F);
		}
	}

	@Test
	public void test_addFloatAndFloatFromNativePtrByUpcallMH_DiffScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(JAVA_FLOAT, JAVA_FLOAT, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("addFloatAndFloatFromNativePtrByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addFloatAndFloatFromPointer,
					FunctionDescriptor.of(JAVA_FLOAT, JAVA_FLOAT, ADDRESS), arena.scope());
			float result = (float)mh.invoke(5.74F, upcallFuncAddr);
			Assert.assertEquals(result, 12.53F, 0.01F);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addFloatAndFloatFromPointer,
					FunctionDescriptor.of(JAVA_FLOAT, JAVA_FLOAT, ADDRESS), arena.scope());
			float result = (float)mh.invoke(5.74F, upcallFuncAddr);
			Assert.assertEquals(result, 12.53F, 0.01F);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addFloatAndFloatFromPointer,
					FunctionDescriptor.of(JAVA_FLOAT, JAVA_FLOAT, ADDRESS), arena.scope());
			float result = (float)mh.invoke(5.74F, upcallFuncAddr);
			Assert.assertEquals(result, 12.53F, 0.01F);
		}
	}

	@Test
	public void test_addDoubleAndDoubleFromPtr_RetPtr_ByUpcallMH_SameScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_DOUBLE, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("addDoubleAndDoubleFromPtr_RetPtr_ByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());

			MemorySegment upcallFuncAddr1 = linker.upcallStub(UpcallMethodHandles.MH_addDoubleAndDoubleFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_DOUBLE), arena.scope());
			MemorySegment doubleSegmt1 = allocator.allocate(JAVA_DOUBLE, 1159.748D);
			MemorySegment resultAddr1 = (MemorySegment)mh.invoke(doubleSegmt1, 1262.795D, upcallFuncAddr1);
			MemorySegment resultSegmt1 = MemorySegment.ofAddress(resultAddr1.address(), JAVA_DOUBLE.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt1.get(JAVA_DOUBLE, 0), 2422.543D, 0.001D);

			MemorySegment upcallFuncAddr2 = linker.upcallStub(UpcallMethodHandles.MH_addDoubleAndDoubleFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_DOUBLE), arena.scope());
			MemorySegment doubleSegmt2 = allocator.allocate(JAVA_DOUBLE, 1159.748D);
			MemorySegment resultAddr2 = (MemorySegment)mh.invoke(doubleSegmt2, 1262.795D, upcallFuncAddr2);
			MemorySegment resultSegmt2 = MemorySegment.ofAddress(resultAddr2.address(), JAVA_DOUBLE.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt2.get(JAVA_DOUBLE, 0), 2422.543D, 0.001D);

			MemorySegment upcallFuncAddr3 = linker.upcallStub(UpcallMethodHandles.MH_addDoubleAndDoubleFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_DOUBLE), arena.scope());
			MemorySegment doubleSegmt3 = allocator.allocate(JAVA_DOUBLE, 1159.748D);
			MemorySegment resultAddr3 = (MemorySegment)mh.invoke(doubleSegmt3, 1262.795D, upcallFuncAddr3);
			MemorySegment resultSegmt3 = MemorySegment.ofAddress(resultAddr3.address(), JAVA_DOUBLE.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt3.get(JAVA_DOUBLE, 0), 2422.543D, 0.001D);
		}
	}

	@Test
	public void test_addDoubleAndDoubleFromPtr_RetPtr_ByUpcallMH_DiffScope() throws Throwable {
		FunctionDescriptor fd = FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_DOUBLE, ADDRESS);
		MemorySegment functionSymbol = nativeLibLookup.find("addDoubleAndDoubleFromPtr_RetPtr_ByUpcallMH").get();
		MethodHandle mh = linker.downcallHandle(functionSymbol, fd);

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addDoubleAndDoubleFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_DOUBLE), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment doubleSegmt = allocator.allocate(JAVA_DOUBLE, 1159.748D);
			MemorySegment resultAddr = (MemorySegment)mh.invoke(doubleSegmt, 1262.795D, upcallFuncAddr);
			MemorySegment resultSegmt = MemorySegment.ofAddress(resultAddr.address(), JAVA_DOUBLE.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt.get(JAVA_DOUBLE, 0), 2422.543D, 0.001D);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addDoubleAndDoubleFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_DOUBLE), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment doubleSegmt = allocator.allocate(JAVA_DOUBLE, 1159.748D);
			MemorySegment resultAddr = (MemorySegment)mh.invoke(doubleSegmt, 1262.795D, upcallFuncAddr);
			MemorySegment resultSegmt = MemorySegment.ofAddress(resultAddr.address(), JAVA_DOUBLE.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt.get(JAVA_DOUBLE, 0), 2422.543D, 0.001D);
		}

		try (Arena arena = Arena.openConfined()) {
			MemorySegment upcallFuncAddr = linker.upcallStub(UpcallMethodHandles.MH_addDoubleAndDoubleFromPtr_RetPtr,
					FunctionDescriptor.of(ADDRESS, ADDRESS, JAVA_DOUBLE), arena.scope());
			SegmentAllocator allocator = SegmentAllocator.nativeAllocator(arena.scope());
			MemorySegment doubleSegmt = allocator.allocate(JAVA_DOUBLE, 1159.748D);
			MemorySegment resultAddr = (MemorySegment)mh.invoke(doubleSegmt, 1262.795D, upcallFuncAddr);
			MemorySegment resultSegmt = MemorySegment.ofAddress(resultAddr.address(), JAVA_DOUBLE.byteSize(), arena.scope());
			Assert.assertEquals(resultSegmt.get(JAVA_DOUBLE, 0), 2422.543D, 0.001D);
		}
	}
}
