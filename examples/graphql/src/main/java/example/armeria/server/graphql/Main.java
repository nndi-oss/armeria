package example.armeria.server.graphql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import com.linecorp.armeria.server.graphql.GraphqlService;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        final Server server = newServer(8080);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            server.stop().join();
            logger.info("Server has been stopped.");
        }));

        server.start().join();
    }

    /**
     * Returns a new {@link Server} instance configured with GraphQL HTTP services.
     *
     * @param port the port that the server is to be bound to
     */
    static Server newServer(int port) {
        final ServerBuilder sb = Server.builder();
        return sb.http(port)
                 .service("/graphql", GraphqlService.builder().runtimeWiring(c -> {
                     c.type("Query",
                            typeWiring -> typeWiring.dataFetcher("user", new UserDataFetcher()));
                 }).build())
                 .build();
    }
}
