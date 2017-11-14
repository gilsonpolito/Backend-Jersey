package br.com.gilsonpolito.rest;

import br.com.gilsonpolito.bean.Contato;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/contatos")
public class ContatoRest {

    private static final Map<Integer, Contato> LISTA = new HashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        GenericEntity<List<Contato>> entity = new GenericEntity<List<Contato>>(new ArrayList<>(ContatoRest.LISTA.values())) {
        };
        return Response.status(Response.Status.OK).entity(entity).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Response findById(@PathParam("id") int id) {
        Contato contato = null;
        for (Contato c : ContatoRest.LISTA.values()) {
            if (c.getId() == id) {
                contato = c;
                break;
            }
        }
        if (contato != null) {
            return Response.status(Response.Status.OK).entity(contato).build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Contato " + id + " não localizado").build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Contato contato) {
        try {
            ContatoRest.LISTA.put(contato.getId(), contato);
            return Response.status(Response.Status.CREATED).entity(contato).build();
        } catch (UnsupportedOperationException | ClassCastException | NullPointerException | IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Falaha ao atualizar contato " + contato.getId() + ": " + e.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Contato contato) {
        try {
            contato.setId(contato.getGerador());
            ContatoRest.LISTA.put(contato.getId(), contato);
            return Response.status(Response.Status.CREATED).entity(contato).build();
        } catch (UnsupportedOperationException | ClassCastException | NullPointerException | IllegalArgumentException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Falha ao criar contato: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") int id) {
        try {
            ContatoRest.LISTA.remove(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (UnsupportedOperationException | ClassCastException | NullPointerException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Falha ao excluir contato: " + e.getMessage()).build();
        }
    }
}
