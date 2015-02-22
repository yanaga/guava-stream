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

import java.util.Comparator;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.CONCURRENT;
import static java.util.stream.Collector.Characteristics.UNORDERED;

public class MoreCollectors {

	private MoreCollectors() {
	}

	public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper) {
		return toImmutableMap(ImmutableMap::builder, keyMapper, valueMapper, UNORDERED, CONCURRENT);
	}

	public static <T, K extends Comparable<K>, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper) {
		return toImmutableSortedMap(keyMapper, valueMapper, Comparator.naturalOrder());
	}

	public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper,
			Comparator<K> comparator) {
		return toImmutableMap(() -> ImmutableSortedMap.orderedBy(comparator), keyMapper, valueMapper, CONCURRENT);
	}

	public static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper) {
		return toImmutableMap(ImmutableBiMap::builder, keyMapper, valueMapper, UNORDERED, CONCURRENT);
	}

	private static <T, K, V, M extends ImmutableMap<K, V>> Collector<T, ?, M> toImmutableMap(
			Supplier<ImmutableMap.Builder<K, V>> supplier,
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper,
			Collector.Characteristics... characteristics) {
		return Collector.of(
				supplier,
				new BiConsumer<ImmutableMap.Builder<K, V>, T>() {
					@Override
					public void accept(ImmutableMap.Builder<K, V> objectObjectBuilder, T t) {
						objectObjectBuilder.put(keyMapper.apply(t), valueMapper.apply(t));
					}
				},
				new BinaryOperator<ImmutableMap.Builder<K, V>>() {
					@Override
					public ImmutableMap.Builder<K, V> apply(ImmutableMap.Builder<K, V> kuBuilder, ImmutableMap.Builder<K, V> kuBuilder2) {
						return kuBuilder.putAll(kuBuilder2.build());
					}
				},
				new Function<ImmutableMap.Builder<K, V>, M>() {
					@SuppressWarnings("unchecked")
					@Override
					public M apply(ImmutableMap.Builder<K, V> kuBuilder) {
						return (M) kuBuilder.build();
					}
				},
				characteristics);
	}

	public static <T> Collector<T, ?, ImmutableList<T>> toImmutableList() {
		return Collector.of(
				ImmutableList::builder,
				new BiConsumer<ImmutableList.Builder<T>, T>() {
					@Override
					public void accept(ImmutableList.Builder<T> objectBuilder, T t) {
						objectBuilder.add(t);
					}
				},
				new BinaryOperator<ImmutableList.Builder<T>>() {
					@Override
					public ImmutableList.Builder<T> apply(ImmutableList.Builder<T> objectBuilder, ImmutableList.Builder<T> objectBuilder2) {
						return objectBuilder.addAll(objectBuilder2.build());
					}
				},
				new Function<ImmutableList.Builder<T>, ImmutableList<T>>() {
					@Override
					public ImmutableList<T> apply(ImmutableList.Builder<T> tBuilder) {
						return tBuilder.build();
					}
				},
				UNORDERED, CONCURRENT);
	}

	public static <T> Collector<T, ?, ImmutableSet<T>> toImmutableSet() {
		return toImmutableSet(ImmutableSet::builder, UNORDERED, CONCURRENT);
	}

	public static <T extends Comparable<T>> Collector<T, ?, ImmutableSortedSet<T>> toImmutableSortedSet() {
		return toImmutableSortedSet(Comparator.<T>naturalOrder());
	}

	public static <T> Collector<T, ?, ImmutableSortedSet<T>> toImmutableSortedSet(Comparator<T> comparator) {
		return toImmutableSet(() -> {
			return ImmutableSortedSet.orderedBy(comparator);
		}, CONCURRENT);
	}

	private static <T, S extends ImmutableSet<T>, B extends ImmutableSet.Builder<T>> Collector<T, B, S> toImmutableSet(
			Supplier<B> supplier,
			Collector.Characteristics... characteristics) {
		return Collector.of(
				supplier,
				new BiConsumer<B, T>() {
					@Override
					public void accept(B objectBuilder, T t) {
						objectBuilder.add(t);
					}
				},
				new BinaryOperator<B>() {
					@SuppressWarnings("unchecked")
					@Override
					public B apply(B objectBuilder, B objectBuilder2) {
						return (B) objectBuilder.addAll(objectBuilder2.build());
					}
				},
				new Function<B, S>() {
					@SuppressWarnings("unchecked")
					@Override
					public S apply(B tBuilder) {
						return (S) tBuilder.build();
					}
				},
				characteristics);
	}

	public static <T> Collector<T, ?, ImmutableMultiset<T>> toImmutableMultiset() {
		return toImmutableMultiset(ImmutableMultiset::builder, UNORDERED, CONCURRENT);
	}

	public static <T extends Comparable<T>> Collector<T, ?, ImmutableSortedMultiset<T>> toImmutableSortedMultiset() {
		return toImmutableSortedMultiset(Comparator.<T>naturalOrder());
	}

	public static <T> Collector<T, ?, ImmutableSortedMultiset<T>> toImmutableSortedMultiset(Comparator<T> comparator) {
		return toImmutableMultiset(() -> ImmutableSortedMultiset.orderedBy(comparator), CONCURRENT);
	}

	private static <T, B extends ImmutableMultiset.Builder<T>, M extends ImmutableMultiset<T>> Collector<T, ?, M> toImmutableMultiset(
			Supplier<B> supplier,
			Collector.Characteristics... characteristics) {
		return Collector.of(
				supplier,
				new BiConsumer<B, T>() {
					@Override
					public void accept(B objectBuilder, T t) {
						objectBuilder.add(t);
					}
				},
				new BinaryOperator<B>() {
					@SuppressWarnings("unchecked")
					@Override
					public B apply(B objectBuilder, B objectBuilder2) {
						return (B) objectBuilder.addAll(objectBuilder2.build());
					}
				},
				new Function<B, M>() {
					@SuppressWarnings("unchecked")
					@Override
					public M apply(B tBuilder) {
						return (M) tBuilder.build();
					}
				},
				characteristics);
	}

	public static <T, K, V> Collector<T, ?, ImmutableMultimap<K, V>> toImmutableMultimap(
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper) {
		return toImmutableMultimap(ImmutableMultimap::builder, keyMapper, valueMapper, UNORDERED, CONCURRENT);
	}

	public static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper) {
		return toImmutableMultimap(ImmutableListMultimap::builder, keyMapper, valueMapper, UNORDERED, CONCURRENT);
	}

	public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper) {
		return toImmutableMultimap(ImmutableSetMultimap::builder, keyMapper, valueMapper, UNORDERED, CONCURRENT);
	}

	public static <T, K extends Comparable<K>, V extends Comparable<V>> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimapNaturalOrder(
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper) {
		return toImmutableMultimap(() -> ImmutableSetMultimap.<K, V>builder()
						.orderKeysBy(Comparator.naturalOrder())
						.orderValuesBy(Comparator.naturalOrder()),
				keyMapper, valueMapper, UNORDERED, CONCURRENT);
	}

	public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper,
			Comparator<? super K> keyComparator,
			Comparator<? super V> valueComparator) {
		return toImmutableMultimap(() -> ImmutableSetMultimap.<K, V>builder()
						.orderKeysBy(keyComparator)
						.orderValuesBy(valueComparator),
				keyMapper, valueMapper, UNORDERED, CONCURRENT);
	}

	private static <T, K, V, B extends ImmutableMultimap.Builder<K, V>, M extends ImmutableMultimap<K, V>> Collector<T, ?, M> toImmutableMultimap(
			Supplier<B> supplier,
			Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper,
			Collector.Characteristics... characteristics) {
		return Collector.of(
				supplier,
				new BiConsumer<B, T>() {
					@Override
					public void accept(B objectBuilder, T t) {
						objectBuilder.put(keyMapper.apply(t), valueMapper.apply(t));
					}
				},
				new BinaryOperator<B>() {
					@SuppressWarnings("unchecked")
					@Override
					public B apply(B objectBuilder, B objectBuilder2) {
						return (B) objectBuilder.putAll(objectBuilder2.build());
					}
				},
				new Function<B, M>() {
					@SuppressWarnings("unchecked")
					@Override
					public M apply(B tBuilder) {
						return (M) tBuilder.build();
					}
				},
				characteristics);
	}

	public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(
			Function<? super T, ? extends R> rowMapper,
			Function<? super T, ? extends C> columnMapper,
			Function<? super T, ? extends V> valueMapper) {
		return Collector.of(
				ImmutableTable::builder,
				new BiConsumer<ImmutableTable.Builder<R, C, V>, T>() {
					@Override
					public void accept(ImmutableTable.Builder<R, C, V> rcvBuilder, T t) {
						rcvBuilder.put(rowMapper.apply(t), columnMapper.apply(t), valueMapper.apply(t));
					}
				},
				new BinaryOperator<ImmutableTable.Builder<R, C, V>>() {
					@Override
					public ImmutableTable.Builder<R, C, V> apply(ImmutableTable.Builder<R, C, V> rcvBuilder, ImmutableTable.Builder<R, C, V> rcvBuilder2) {
						return rcvBuilder.putAll(rcvBuilder2.build());
					}
				},
				new Function<ImmutableTable.Builder<R, C, V>, ImmutableTable<R, C, V>>() {
					@Override
					public ImmutableTable<R, C, V> apply(ImmutableTable.Builder<R, C, V> rcvBuilder) {
						return rcvBuilder.build();
					}
				},
				UNORDERED, CONCURRENT);
	}

	public static <T, R extends Comparable<R>, C extends Comparable<C>, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableSortedTable(
			Function<? super T, ? extends R> rowMapper,
			Function<? super T, ? extends C> columnMapper,
			Function<? super T, ? extends V> valueMapper) {
		return toImmutableSortedTable(rowMapper, columnMapper, valueMapper, Comparator.<R>naturalOrder(), Comparator.<C>naturalOrder());
	}

	public static <T, R, C, V> Collector<T, ?, ImmutableTable<R, C, V>> toImmutableSortedTable(
			Function<? super T, ? extends R> rowMapper,
			Function<? super T, ? extends C> columnMapper,
			Function<? super T, ? extends V> valueMapper,
			Comparator<R> rowComparator,
			Comparator<C> columnComparator) {
		return Collector.of(
				new Supplier<ImmutableTable.Builder<R, C, V>>() {
					@Override
					public ImmutableTable.Builder<R, C, V> get() {
						return ImmutableTable.<R, C, V>builder()
								.orderRowsBy(rowComparator)
								.orderColumnsBy(columnComparator);
					}
				},
				new BiConsumer<ImmutableTable.Builder<R, C, V>, T>() {
					@Override
					public void accept(ImmutableTable.Builder<R, C, V> rcvBuilder, T t) {
						rcvBuilder.put(rowMapper.apply(t), columnMapper.apply(t), valueMapper.apply(t));
					}
				},
				new BinaryOperator<ImmutableTable.Builder<R, C, V>>() {
					@Override
					public ImmutableTable.Builder<R, C, V> apply(ImmutableTable.Builder<R, C, V> rcvBuilder, ImmutableTable.Builder<R, C, V> rcvBuilder2) {
						return rcvBuilder.putAll(rcvBuilder2.build());
					}
				},
				new Function<ImmutableTable.Builder<R, C, V>, ImmutableTable<R, C, V>>() {
					@Override
					public ImmutableTable<R, C, V> apply(ImmutableTable.Builder<R, C, V> rcvBuilder) {
						return rcvBuilder.build();
					}
				},
				CONCURRENT);
	}

}
