package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import jakarta.validation.constraints.NotNull;
import models.Entity;

@NotNull
public class InMemoryDAO<T extends Entity> implements DAO<T> {
	private static int nextId = 1;
	private final List<T> items;
	
	@SafeVarargs
	public InMemoryDAO(T... items) {
		this.items = new ArrayList<>();
		
		for (var entity : items) {
			add(entity);
		}
	}

	@SuppressWarnings("null")
	@Override
	public T add(T entity) {
		entity.setId(nextId++);
		items.add(entity);
		return entity;
	}

	@Override
	public Optional<T> update(T entity) {
	    @SuppressWarnings("null")
		var index = IntStream.range(0, items.size())
	    		.filter(i -> items.get(i).getId() == entity.getId())
	    		.findFirst();
	    
	    if (index.isPresent()) {
		    items.set(index.getAsInt(), entity);
			return Optional.of(entity);	
	    } else {
	    	return Optional.empty();
	    }
	}

	@Override
	public Optional<T> delete(int id) {
	    var index = IntStream.range(0, items.size())
	    		.filter(i -> items.get(i).getId() == id)
	    		.findFirst();
	    
	    if (index.isPresent()) {
		    var temp = items.get(index.getAsInt());
		    items.remove(index.getAsInt());
			return Optional.of(temp);	
	    } else {
	    	return Optional.empty();
	    }
	}

	@Override
	public Optional<T> getById(int id) {
		return items.stream()
				.filter(e -> e.getId() == id)
				.findFirst();
	}

	@Override
	public List<T> getAll() {
		return new ArrayList<>(items);
	}
	
	public boolean contains(T entity) {
		return IntStream.range(0, items.size())
				.filter(i -> items.get(i).getId() == entity.getId())
				.findFirst()
				.isPresent();
	}

}
