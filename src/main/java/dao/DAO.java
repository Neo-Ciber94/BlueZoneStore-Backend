package dao;

import java.util.List;
import java.util.Optional;

import models.Entity;

public interface DAO<T extends Entity> {
	public T add(T entity);
	public Optional<T> update(T entity);
	public Optional<T> delete(int id);
	public Optional<T> getById(int id);
	public List<T> getAll();
}
