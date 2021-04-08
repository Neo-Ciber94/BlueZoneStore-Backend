package dao;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import models.Product;

public class ProductDB implements DAO<Product> {
	public static final ProductDB INSTANCE = new ProductDB();

	private static HikariDataSource dataSource;

	public static void start() {
		if (dataSource != null) {
			throw new IllegalStateException("Database instance is already initialized");
		}

		// Load database credentials and connect
		var config = new HikariConfig("/config.properties");
		config.setMaximumPoolSize(10);

		dataSource = new HikariDataSource(config);
	}

	public static void close() {
		if (dataSource != null) {
			dataSource.close();
		}
	}

	@Override
	public Product add(Product entity) {
		try(var connection = dataSource.getConnection()){
			var statement = connection.prepareStatement(
					"INSERT INTO Product ([name], [description], [price], [available]) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
			);
			
			statement.setString(1, entity.getName());
			statement.setString(2, entity.getDescription());
			statement.setBigDecimal(3, entity.getPrice());
			statement.setInt(4, entity.getAvailable());
			
			var count = statement.executeUpdate();	
			var resultSet = statement.getGeneratedKeys();
			var newEntityId = 0;
			assert(count > 0);
		
			while (resultSet.next()) {
				newEntityId = resultSet.getInt(1);
			}

			return getById(newEntityId).get();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Optional<Product> update(Product entity) {		
		try(var connection = dataSource.getConnection()){
			var statement = connection.prepareStatement(
					"UPDATE Product SET [name]=?, [description]=?, [price]=?, [available]=? WHERE id = ?", Statement.RETURN_GENERATED_KEYS
			);
			
			statement.setString(1, entity.getName());
			statement.setString(2, entity.getDescription());
			statement.setBigDecimal(3, entity.getPrice());
			statement.setInt(4, entity.getAvailable());
			statement.setInt(5, entity.getId());
			
			var count = statement.executeUpdate();	
			if (count == 0) {
				return Optional.empty();
			}
			
			return getById(entity.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Optional.empty();
	}

	@Override
	public Optional<Product> delete(int id) {
		try(var connection = dataSource.getConnection()){
			var removed = getById(id);
			var statement = connection.prepareStatement("DELETE FROM Product WHERE id = ?");
			statement.setInt(1, id);
			
			var count = statement.executeUpdate();	
			assert(count > 0);
			return removed;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Optional.empty();
	}

	@SuppressWarnings("null")
	@Override
	public Optional<Product> getById(int id) {		
		try(var connection = dataSource.getConnection()){
			var statement = connection.prepareStatement("SELECT * FROM Product WHERE id = ?");
			statement.setInt(1, id);
			
			var resultSet = statement.executeQuery();
			var product = new Product();
			var hasResult = false;
			
			while(resultSet.next()) {
				hasResult = true;
				product.setId(resultSet.getInt(1));
				product.setName(resultSet.getString(2));
				product.setDescription(resultSet.getString(3));
				product.setPrice(resultSet.getBigDecimal(4));
				product.setAvailable(resultSet.getInt(5));
			}
			
			if (hasResult == false) {
				return Optional.empty();
			}
			
			return Optional.of(product);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Optional.empty();
	}

	@Override
	public List<Product> getAll() {
		var result = new ArrayList<Product>();
		
		try(var connection = dataSource.getConnection()){
			var statement = connection.prepareStatement("SELECT * FROM Product");
			var resultSet = statement.executeQuery();
			
			while(resultSet.next()) {
				var product = new Product();
				product.setId(resultSet.getInt(1));
				product.setName(resultSet.getString(2));
				product.setDescription(resultSet.getString(3));
				product.setPrice(resultSet.getBigDecimal(4));
				product.setAvailable(resultSet.getInt(5));
				result.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
}
