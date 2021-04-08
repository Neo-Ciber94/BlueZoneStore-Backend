package com.iso610.api;

import dao.DAO;
import dao.ProductDB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import models.Product;

@Path("products")
public class ProductResource {
	private static final DAO<Product> products = ProductDB.INSTANCE;

	@Context
	private UriInfo uriInfo;
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getAllProducts() {
		return Response.ok().entity(products.getAll()).build();
	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getProduct(@PathParam("id") int id) {
		return Response.ok().entity(products.getById(id)).build();
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response createProduct(Product newProduct) {
		var product = products.add(newProduct);
		var newProductUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(product.getId())).build();
		return Response.created(newProductUri).entity(product).build();
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updateProduct(Product newProduct) {
		var result = products.update(newProduct);
		if (result.isPresent()) {
			var product = result.get();
			var newProductUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(product.getId())).build();
			return Response.created(newProductUri).entity(product).build();	
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
	
	@DELETE
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response deleteProduct(@PathParam("id") int id) {
		var result = products.delete(id);
		if (result.isPresent()) {
			var product = result.get();
			var newProductUri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(product.getId())).build();
			return Response.ok(newProductUri).entity(product).build();	
		} else {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
	}
}
