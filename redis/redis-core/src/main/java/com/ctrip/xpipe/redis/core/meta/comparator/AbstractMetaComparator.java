package com.ctrip.xpipe.redis.core.meta.comparator;


import com.ctrip.xpipe.redis.core.BaseEntity;
import com.ctrip.xpipe.redis.core.meta.MetaClone;
import com.ctrip.xpipe.redis.core.meta.MetaComparator;
import com.ctrip.xpipe.redis.core.meta.MetaComparatorVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unidal.tuple.Triple;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author wenchao.meng
 *
 * Sep 2, 2016
 */
public abstract class AbstractMetaComparator<V extends Serializable, T, C extends Enum<C>> implements MetaComparator<T, C>{
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected Set<T> added = new HashSet<>(); 
	protected Set<T> removed = new HashSet<>(); 
	@SuppressWarnings("rawtypes")
	protected Set<MetaComparator>  modified= new HashSet<>();
	
	protected List<ConfigChanged<C>> configChanged = new LinkedList<>();

	protected V current;
	protected V future;

	private boolean isShallowChange = false;

	public AbstractMetaComparator() {
	}

	public AbstractMetaComparator(V current, V future) {
		this.current = current;
		this.future = future;
	}

	@Override
	public boolean isShallowChange() {
		return isShallowChange;
	}

	@Override
	public void compare() {
		checkShallowChange();
		doDetailedCompare();
	}

	protected void checkShallowChange() {

	    if (current == null && future == null) {
			isShallowChange = false;
			return;
		}

	    if (current == null || future == null) {
			isShallowChange = true;
			return;
		}

	    try {
			V currentClone = MetaClone.clone(current);
			V futureClone = MetaClone.clone(future);
			for (Field field : currentClone.getClass().getDeclaredFields()) {
				if (!needCheck(field.getType())) {
					resetField(field, currentClone);
				}
			}
			for (Field field : futureClone.getClass().getDeclaredFields()) {
				if (!needCheck(field.getType())) {
					resetField(field, futureClone);
				}
			}
			isShallowChange = !(currentClone.toString().equals(futureClone.toString()));
		} catch (Throwable t) {
	        logger.error("[checkShallowChange] UNLIKELY", t);
		}
	}

	private void resetField(Field field, V instance) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		Class<?> clazz = field.getType();
		field.setAccessible(true);
		if (Map.class.isAssignableFrom(clazz)) {
		    clazz.getDeclaredMethod("clear").invoke(field.get(instance));
		    return;
		}
		if (Collection.class.isAssignableFrom(clazz)) {
			clazz.getDeclaredMethod("clear").invoke(field.get(instance));
			return;
		}
		field.set(instance, null);
	}

	private boolean needCheck(Class<?> clazz) {
	    if (String.class.isAssignableFrom(clazz)) {
	        return true;
		}
	    if (Number.class.isAssignableFrom(clazz)) {
	        return true;
		}
		return clazz.isPrimitive();
	}

	protected void doDetailedCompare() {}

	/**
	 * @param current
	 * @param future
	 * @return added, modified, delted
	 */
	protected <Type> Triple<Set<Type>, Set<Type>, Set<Type>> getDiff(Set<Type> current, Set<Type> future) {
		
		Set<Type> added = new HashSet<>(future);
		Set<Type> modified = new HashSet<>(future);
		Set<Type> deleted = new HashSet<>(current);
		
		added.removeAll(deleted);
		modified.retainAll(deleted);
		deleted.removeAll(future);
		
		return new Triple<Set<Type>, Set<Type>, Set<Type>>(added, modified, deleted);
	}

	@Override
	public Set<T> getAdded() {
		return added;
	}

	@Override
	public Set<T> getRemoved() {
		return removed;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Set<MetaComparator> getMofified() {
		return modified;
	}

	public List<ConfigChanged<C>> getConfigChanged() {
		return new LinkedList<>(configChanged);
	}

	protected boolean reflectionEquals(BaseEntity<?> currentMeta, BaseEntity<?> futureMeta) {

		if(currentMeta == null){
			return futureMeta == null;
		}
		if(futureMeta == null){
			return false;
		}

		return currentMeta.toString().equals(futureMeta.toString());
	}

	@Override
	public int totalChangedCount() {
		return added.size() + removed.size() + modified.size();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void accept(MetaComparatorVisitor<T> visitor) {
		
		for(T ad : added){
			visitor.visitAdded(ad);
		}
		
		for(MetaComparator comparator : modified){
			visitor.visitModified(comparator);
		}
		
		for(T rm : removed){
			visitor.visitRemoved(rm);
		}
	}
	
	
	@Override
	public String toString() {
		return String.format("%s{added:%s, removed:%s, changed:%s}", idDesc(), added, removed, modified);
	}

}
