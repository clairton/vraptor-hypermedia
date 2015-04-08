package br.eti.clairton.vraptor.hypermedia;

import java.util.HashSet;
import java.util.Set;

public class HypermediableRoleStub implements HypermediableRole {

	@Override
	public Set<Link> from(String resource, String operation) {
		final Set<Link> links = new HashSet<>();
		if ("show".equals(operation)) {
			// links.add(new Link("/pessoas/1", "remove", "Remover", "DELETE",
			// "application/json"));
			links.add(new Link("/pessoas/1", "update", "Salvar", "PUT",
					"application/json"));
		}
		return links;
	}

}
