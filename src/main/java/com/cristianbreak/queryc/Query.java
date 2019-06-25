/*
 * Copyright (c) 2019. Cristian González Morante
 */

package com.cristianbreak.queryc;

import com.cristianbreak.queryc.exceptions.TypeNotFoundException;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Encapsulation of Criteria Query API
 *
 * @param <X> Root type
 * @param <R> Result type
 */
@SuppressWarnings({"unused", "SameParameterValue"})
public class Query<X, R> {

	private CriteriaQuery<R> query;
	private CriteriaBuilder cBuilder;
	private Root<X> root;
	private List<Join> joinList;
	private List<Predicate> filterList;
	private BiFunction<FromSupplier, CriteriaBuilder, Selection<? extends R>> selectionSupplier;
	private Function<FromSupplier, Expression> groupByExpression;
	private OrderExpression orderByExpression;

	private Query(CriteriaBuilder criteriaBuilder, Class<R> resultType) {
		joinList = new ArrayList<>();
		filterList = new ArrayList<>();
		cBuilder = criteriaBuilder;
		query = cBuilder.createQuery(resultType);
	}

	/**
	 * Get the root or the join that you need dynamically
	 * using type to search it
	 *
	 * @param type Type that you need
	 * @return {@link Root} or {@link Join} corresponding to {@param type}
	 */
	private <T> From<?, T> getFrom(Class<T> type) {
		From from;

		if (root.getJavaType().equals(type)) {
			from = root;
		} else {
			try {
				from = joinList.stream()
							   .filter(join -> join.getJavaType().equals(type))
							   .findFirst()
							   .orElseThrow(TypeNotFoundException::new);
			} catch (TypeNotFoundException e) {
				throw new TypeNotPresentException(type.getName(), e);
			}
		}

		return from;
	}

	public Class<R> getReturnType() {
		return query.getResultType();
	}

	public Query<X, R> from(Class<X> from) {
		this.root = query.from(from);
		return this;
	}

	public Query<X, R> rootJoin(Attribute att) {
		joinList.add(root.join(att.getName(), JoinType.INNER));
		return this;
	}

	public Query<X, R> rootLeftJoin(Attribute att) {
		joinList.add(root.join(att.getName(), JoinType.LEFT));
		return this;
	}

	public Query<X, R> rootRightJoin(Attribute att) {
		joinList.add(root.join(att.getName(), JoinType.RIGHT));
		return this;
	}

	public Query<X, R> rootJoin(Attribute att, JoinType type) {
		joinList.add(root.join(att.getName(), type));
		return this;
	}

	public Query<X, R> join(Attribute att) {
		return join(att, JoinType.INNER);
	}

	public Query<X, R> join(Class from, Attribute to) {
		return join(from, to, JoinType.INNER);
	}

	private Query<X, R> join(Class<?> from, Attribute to, JoinType type) {
		From leftPart = joinList.stream()
								.filter(join -> join.getJavaType().equals(from))
								.findFirst()
								.orElse(null);

		if (Objects.isNull(leftPart))
			leftPart = root;

		joinList.add(leftPart.join(to.getName(), type));

		return this;
	}

	public Query<X, R> leftJoin(Attribute att) {
		return join(att, JoinType.LEFT);
	}

	public Query<X, R> rightJoin(Attribute att) {
		return join(att, JoinType.RIGHT);
	}

	private Query<X, R> join(Attribute att, JoinType type) {
		From leftPart;

		if (joinList.isEmpty()) {
			leftPart = root;
		} else {
			leftPart = joinList.get(joinList.size() - 1);
		}

		joinList.add(leftPart.join(att.getName(), type));

		return this;
	}

	public Query<X, R> select(BiFunction<FromSupplier, CriteriaBuilder, Selection<? extends R>> selectionSupplier) {
		this.selectionSupplier = selectionSupplier;
		return this;
	}

	public CriteriaQuery<R> build() {
		if (selectionSupplier != null) {
			query = query.select(selectionSupplier.apply(this::getFrom, cBuilder));
		}

		if (groupByExpression != null) {
			query = query.groupBy(groupByExpression.apply(this::getFrom));
		}

		if (orderByExpression != null) {
			query = query.orderBy(orderByExpression.apply(this::getFrom, cBuilder));
		}

		return query.where(filterList.toArray(new Predicate[]{}));
	}

	public Query<X, R> where(List<Filter> filters) {
		for (Filter filter : filters) {
			Predicate p = filter.apply(this::getFrom, cBuilder, query);
			filterList.add(p);
		}

		if (filterList.isEmpty()) {
			filterList.add(cBuilder.conjunction());
		}

		return this;
	}

	public Query<X, R> groupBy(Function<FromSupplier, Expression> groupByExpression) {
		this.groupByExpression = groupByExpression;
		return this;
	}

	public Query<X, R> orderBy(OrderExpression orderByExpression) {
		this.orderByExpression = orderByExpression;
		return this;
	}

	public static <T> Query<?, T> create(Class<T> resultType, CriteriaBuilder criteriaBuilder) {
		return new Query<>(criteriaBuilder, resultType);
	}

	public Query<X, R> distinct() {
		query.distinct(true);
		return this;
	}

}
