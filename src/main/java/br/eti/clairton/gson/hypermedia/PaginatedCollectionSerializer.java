package br.eti.clairton.gson.hypermedia;

import java.lang.reflect.Type;
import java.util.Collection;

import javax.enterprise.inject.Vetoed;

import br.eti.clairton.repository.Meta;
import br.eti.clairton.repository.PaginatedCollection;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

@Vetoed
public class PaginatedCollectionSerializer<T, X> implements JsonSerializer<PaginatedCollection<T, X>> {
	private JsonSerializer<Collection<T>> delegate;
	
	public PaginatedCollectionSerializer(final JsonSerializer<Collection<T>> delegate) {
		this.delegate = delegate;
	}	
	
	@Override
	public JsonElement serialize(final PaginatedCollection<T, X> src, final Type type, final JsonSerializationContext context) {
		final Collection<T> collection = src;
		final JsonElement json = delegate.serialize(collection, type, context);
		if(JsonObject.class.isInstance(json)){
			final JsonObject object = (JsonObject) json;
			final Meta meta = src.unwrap(Meta.class);
			final JsonElement element = context.serialize(meta);
			object.add("meta", element);
		}
		return json;
	}
}