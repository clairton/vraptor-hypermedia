package br.eti.clairton.gson.hypermedia;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import br.eti.clairton.inflector.Inflector;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Serialize uma {@link Collection} de {@link Model}.
 *
 * @author Clairton Rodrigo Heinzen<clairton.rodrigo@gmail.com>
 */
public abstract class HypermediableCollectionSerializer<T> extends TagMixin implements JsonSerializer<Collection<T>> {
	private final HypermediableRule navigator;
	private final Inflector inflector;

	@Deprecated
	protected HypermediableCollectionSerializer() {
		this(null, null);
	}

	public HypermediableCollectionSerializer(final HypermediableRule navigator, final Inflector inflector) {
		super(inflector);
		this.navigator = navigator;
		this.inflector = inflector;
	}

	/**
	 * {@inheritDoc}.
	 */
	@Override
	public JsonElement serialize(final Collection<T> src, final Type type, final JsonSerializationContext context) {
		final JsonElement element = serialize(src, context);
		final Iterator<T> iterator = src.iterator();
		final Class<T> cType = getCollectionType();
		if (iterator.hasNext() && cType.isInstance(iterator.next())) {
			return serializeLinks(src, element, context);
		} else {
			return element;
		}
	}

	protected JsonElement serializeLinks(final Collection<T> src, JsonElement element, final JsonSerializationContext context) {
		final String tag = tag(src);
		if(tag.equals(inflector.pluralize(getResource()))){
			final JsonObject json = new JsonObject();
			json.add(tag, element);
			final Set<Link> links = navigator.from(src, getResource(), getOperation());
			json.add("links", context.serialize(links));
			return json;
		}else{
			return element;
		}
	}

	protected JsonElement serialize(final Collection<T> src, final JsonSerializationContext context) {
		final JsonArray collection = new JsonArray();
		for (final Object h : src) {
			collection.add(context.serialize(h));
		}
		return collection;
	}

	protected abstract Class<T> getCollectionType();

	protected abstract String getResource();

	protected abstract String getOperation();
}