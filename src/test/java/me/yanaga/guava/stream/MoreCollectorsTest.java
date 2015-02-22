package me.yanaga.guava.stream;

/*
 * #%L
 * guava-stream
 * %%
 * Copyright (C) 2015 Edson Yanaga
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableTable;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class MoreCollectorsTest {

	@Test
	public void testToImmutableMap() throws Exception {
		ImmutableMap<String, Integer> map = Stream.of("1", "2", "3")
				.collect(MoreCollectors.toImmutableMap(i -> i, Integer::valueOf));
		assertThat(map).isInstanceOf(ImmutableMap.class);
		assertThat(map.keySet()).containsOnly("1", "2", "3");
		assertThat(map.values()).containsOnly(1, 2, 3);
		assertThat(map.get("1")).isEqualTo(1);
		assertThat(map.get("2")).isEqualTo(2);
		assertThat(map.get("3")).isEqualTo(3);
	}

	@Test
	public void testToImmutableSortedMap() throws Exception {
		ImmutableSortedMap<String, Integer> map = Stream.of("2", "3", "1")
				.collect(MoreCollectors.toImmutableSortedMap(i -> i, Integer::valueOf));
		assertThat(map).isInstanceOf(ImmutableSortedMap.class);
		assertThat(map.keySet()).containsExactly("1", "2", "3");
		assertThat(map.values()).containsOnly(1, 2, 3);
		assertThat(map.get("1")).isEqualTo(1);
		assertThat(map.get("2")).isEqualTo(2);
		assertThat(map.get("3")).isEqualTo(3);
	}

	@Test
	public void testToImmutableSortedMapWithComparator() throws Exception {
		ImmutableSortedMap<String, Integer> map = Stream.of("2", "3", "1")
				.collect(MoreCollectors.toImmutableSortedMap(
						i -> i,
						Integer::valueOf,
						(o1, o2) -> o2.compareTo(o1)));
		assertThat(map).isInstanceOf(ImmutableSortedMap.class);
		assertThat(map.keySet()).containsExactly("3", "2", "1");
		assertThat(map.values()).containsOnly(1, 2, 3);
		assertThat(map.get("1")).isEqualTo(1);
		assertThat(map.get("2")).isEqualTo(2);
		assertThat(map.get("3")).isEqualTo(3);
	}

	@Test
	public void testToImmutableBiMap() throws Exception {
		ImmutableBiMap<String, Integer> biMap = Stream.of("2", "3", "1")
				.collect(MoreCollectors.toImmutableBiMap(i -> i, Integer::valueOf));
		assertThat(biMap).isInstanceOf(ImmutableBiMap.class);
		assertThat(biMap.keySet()).containsOnly("1", "2", "3");
		assertThat(biMap.values()).containsOnly(1, 2, 3);
		assertThat(biMap.get("1")).isEqualTo(1);
		assertThat(biMap.get("2")).isEqualTo(2);
		assertThat(biMap.get("3")).isEqualTo(3);
		BiMap<Integer, String> inverse = biMap.inverse();
		assertThat(inverse.keySet()).containsOnly(1, 2, 3);
		assertThat(inverse.values()).containsOnly("1", "2", "3");
	}

	@Test
	public void testToImmutableList() throws Exception {
		ImmutableList<String> list = Stream.of("a", "b", "c")
				.collect(MoreCollectors.toImmutableList());
		assertThat(list).isInstanceOf(ImmutableList.class);
		assertThat(list).containsExactly("a", "b", "c");
	}

	@Test
	public void testToImmutableSet() throws Exception {
		ImmutableSet<String> set = Stream.of("a", "b", "c", "z", "c", "g", "a", "f")
				.collect(MoreCollectors.toImmutableSet());
		assertThat(set).isInstanceOf(ImmutableSet.class);
		assertThat(set).containsOnly("a", "b", "c", "z", "g", "f");
	}

	@Test
	public void testToImmutableSortedSet() throws Exception {
		ImmutableSet<String> set = Stream.of("a", "b", "c", "z", "c", "g", "a", "f")
				.collect(MoreCollectors.toImmutableSortedSet());
		assertThat(set).isInstanceOf(ImmutableSortedSet.class);
		assertThat(set).containsExactly("a", "b", "c", "f", "g", "z");
	}

	@Test
	public void testToImmutableSortedSetWithComparator() throws Exception {
		ImmutableSet<String> set = Stream.of("a", "b", "c", "z", "c", "g", "a", "f")

				.collect(MoreCollectors.toImmutableSortedSet((o1, o2) -> o2.compareTo(o1)));
		assertThat(set).isInstanceOf(ImmutableSortedSet.class);
		assertThat(set).containsExactly("z", "g", "f", "c", "b", "a");
	}

	@Test
	public void testToImmutableMultiset() throws Exception {
		ImmutableMultiset<String> multiset = Stream.of("a", "b", "c", "a", "a", "b", "a", "f")
				.collect(MoreCollectors.toImmutableMultiset());
		assertThat(multiset.size()).isEqualTo(8);
		assertThat(multiset.count("a")).isEqualTo(4);
		assertThat(multiset.count("b")).isEqualTo(2);
		assertThat(multiset.elementSet()).containsOnly("a", "b", "c", "f");
	}

	@Test
	public void testToImmutableSortedMultiset() throws Exception {
		ImmutableSortedMultiset<String> multiset = Stream.of("b", "c", "a", "a", "b", "a", "a", "f")
				.collect(MoreCollectors.toImmutableSortedMultiset());
		assertThat(multiset.size()).isEqualTo(8);
		assertThat(multiset.count("a")).isEqualTo(4);
		assertThat(multiset.count("b")).isEqualTo(2);
		assertThat(multiset.elementSet()).containsExactly("a", "b", "c", "f");
	}

	@Test
	public void testToImmutableSortedMultisetWithComparator() throws Exception {
		ImmutableSortedMultiset<String> multiset = Stream.of("b", "c", "a", "a", "b", "a", "a", "f")
				.collect(MoreCollectors.toImmutableSortedMultiset((a, b) -> b.compareTo(a)));
		assertThat(multiset.size()).isEqualTo(8);
		assertThat(multiset.count("a")).isEqualTo(4);
		assertThat(multiset.count("b")).isEqualTo(2);
		assertThat(multiset.elementSet()).containsExactly("f", "c", "b", "a");
	}

	@Test
	public void testToImmutableMultimap() throws Exception {
		ImmutableMultimap<Integer, String> multimap = Stream.of(1, 2, 3, 4, 5, 1, 2, 3, 4, 1, 2, 3)
				.collect(MoreCollectors.toImmutableMultimap(i -> i, i -> Integer.toString(i)));
		assertThat(multimap.size()).isEqualTo(12);
		assertThat(multimap.keySet().size()).isEqualTo(5);
		assertThat(multimap.get(1)).containsOnly("1", "1", "1");
		assertThat(multimap.get(4)).containsOnly("4", "4");
		assertThat(multimap.get(5)).containsOnly("5");
	}

	@Test
	public void testToImmutableListMultimap() throws Exception {
		ImmutableListMultimap<Integer, String> multimap = Stream.of(1, 2, 3, 4, 5, 1, 2, 3, 4, 1, 2, 3)
				.collect(MoreCollectors.toImmutableListMultimap(i -> i, i -> Integer.toString(i)));
		assertThat(multimap.size()).isEqualTo(12);
		assertThat(multimap.keySet().size()).isEqualTo(5);
		assertThat(multimap.get(1)).isInstanceOf(List.class);
		assertThat(multimap.get(1)).containsOnly("1", "1", "1");
		assertThat(multimap.get(4)).containsOnly("4", "4");
		assertThat(multimap.get(5)).containsOnly("5");
	}

	@Test
	public void testToImmutableSetMultimap() throws Exception {
		ImmutableSetMultimap<Integer, String> multimap = Stream.of(1, 2, 3, 4, 5, 1, 2, 3, 4, 1, 2, 3)
				.collect(MoreCollectors.toImmutableSetMultimap(i -> i, i -> Integer.toString(i)));
		assertThat(multimap.size()).isEqualTo(5);
		assertThat(multimap.keySet().size()).isEqualTo(5);
		assertThat(multimap.get(1)).isInstanceOf(Set.class);
		assertThat(multimap.get(1)).containsOnly("1");
		assertThat(multimap.get(4)).containsOnly("4");
		assertThat(multimap.get(5)).containsOnly("5");
	}

	@Test
	public void testToImmutableSetMultimapNaturalOrder() throws Exception {
		ImmutableSetMultimap<String, String> setMultimap = Stream.of(MapEntry.of("b", "3"), MapEntry.of("a", "1"), MapEntry.of("b", "1"), MapEntry.of("a", "1"))
				.collect(MoreCollectors.toImmutableSetMultimapNaturalOrder(MapEntry::getKey, MapEntry::getValue));
		assertThat(setMultimap.size()).isEqualTo(3);
		assertThat(setMultimap.keySet().size()).isEqualTo(2);
		assertThat(setMultimap.get("a")).isInstanceOf(Set.class);
		assertThat(setMultimap.get("a")).containsOnly("1");
		assertThat(setMultimap.get("b")).containsOnly("1", "3");
	}

	@Test
	public void testToImmutableSetMultimapWithComparator() throws Exception {
		ImmutableSetMultimap<String, String> setMultimap = Stream.of(MapEntry.of("b", "3"), MapEntry.of("a", "1"), MapEntry.of("b", "1"), MapEntry.of("a", "1"))
				.collect(MoreCollectors.toImmutableSetMultimap(
						MapEntry::getKey,
						MapEntry::getValue,
						(a, b) -> b.compareTo(a),
						(a, b) -> b.compareTo(a)));
		assertThat(setMultimap.size()).isEqualTo(3);
		assertThat(setMultimap.keySet().size()).isEqualTo(2);
		assertThat(setMultimap.get("a")).isInstanceOf(Set.class);
		assertThat(setMultimap.get("a")).containsExactly("1");
		assertThat(setMultimap.get("b")).containsExactly("3", "1");

	}

	@Test
	public void testToImmutableTable() throws Exception {
		ImmutableTable<String, Integer, String> table = Stream.of(TableEntry.of("a", 3, "abc"), TableEntry.of("z", 2, "def"), TableEntry.of("f", 1, "ghi"))

				.collect(MoreCollectors.toImmutableTable(TableEntry::getRow, TableEntry::getColumn, TableEntry::getValue));
		assertThat(table).isInstanceOf(ImmutableTable.class);
		assertThat(table.rowKeySet()).containsOnly("a", "f", "z");
		assertThat(table.columnKeySet()).containsOnly(1, 2, 3);
		assertThat(table.values()).containsOnly("abc", "def", "ghi");
	}

	@Test
	public void testToImmutableSortedTable() throws Exception {
		ImmutableTable<String, Integer, String> table = Stream.of(TableEntry.of("a", 3, "abc"), TableEntry.of("z", 2, "def"), TableEntry.of("f", 1, "ghi"))

				.collect(MoreCollectors.toImmutableSortedTable(TableEntry::getRow, TableEntry::getColumn, TableEntry::getValue));
		assertThat(table).isInstanceOf(ImmutableTable.class);
		assertThat(table.rowKeySet()).containsExactly("a", "f", "z");
		assertThat(table.columnKeySet()).containsExactly(1, 2, 3);
		assertThat(table.values()).containsOnly("abc", "def", "ghi");
	}

	@Test
	public void testToImmutableSortedTableWithComparator() throws Exception {
		ImmutableTable<String, Integer, String> table = Stream.of(TableEntry.of("a", 3, "abc"), TableEntry.of("z", 2, "def"), TableEntry.of("f", 1, "ghi"))

				.collect(MoreCollectors.toImmutableSortedTable(
						TableEntry::getRow,
						TableEntry::getColumn,
						TableEntry::getValue,
						(o1, o2) -> o2.compareTo(o1),
						(o1, o2) -> o2.compareTo(o1)));
		assertThat(table).isInstanceOf(ImmutableTable.class);
		assertThat(table.rowKeySet()).containsExactly("z", "f", "a");
		assertThat(table.columnKeySet()).containsExactly(3, 2, 1);
		assertThat(table.values()).containsOnly("abc", "def", "ghi");
	}

	private static class MapEntry {

		private final String key;

		private final String value;

		private MapEntry(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public static MapEntry of(String key, String value) {
			return new MapEntry(key, value);
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}

	}

	private static class TableEntry {

		private final String row;

		private final Integer column;

		private final String value;

		private TableEntry(String row, Integer column, String value) {
			this.row = row;
			this.column = column;
			this.value = value;
		}

		public static TableEntry of(String row, Integer column, String value) {
			return new TableEntry(row, column, value);
		}

		public String getRow() {
			return row;
		}

		public Integer getColumn() {
			return column;
		}

		public String getValue() {
			return value;
		}

	}

}
