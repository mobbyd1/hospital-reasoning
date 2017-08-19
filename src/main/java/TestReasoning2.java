import eu.larkc.csparql.common.utils.CsparqlUtils;
import eu.larkc.csparql.core.engine.ConsoleFormatter;
import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

/**
 * Created by ruhandosreis on 02/07/17.
 */
public class TestReasoning2 {

    public static void main( String[] args ) {
        try{

            //Configure log4j logger for the csparql engine
            PropertyConfigurator.configure("log4j_configuration/csparql_readyToGoPack_log4j.properties");

            //Create csparql engine instance
            CsparqlEngineImpl engine = new CsparqlEngineImpl();
            //Initialize the engine instance
            //The initialization creates the static engine (SPARQL) and the stream engine (CEP)
            engine.initialize();

            String queryBody = "REGISTER QUERY staticKnowledge AS "
                    + "PREFIX :<http://www.streamreasoning.org/ontologies/sr4ld2014-onto#> "
                    + "SELECT ?p1 ?r1 "
                    + "FROM STREAM <http://streamreasoning.org/streams/fb> [RANGE 1s STEP 1s] "
                    + "FROM <http://streamreasoning.org/roomConnection> "
                    + "WHERE { "
                    + "?p :posts [ :who ?p1 ; :where ?r ] . "
                    + "?r :isConnectedTo ?r1 . "
                    + "} ";

            FacebookStreamer4RoomConnection fb = new FacebookStreamer4RoomConnection("http://streamreasoning.org/streams/fb", "http://www.streamreasoning.org/ontologies/sr4ld2014-onto#", 1000L);

            //Register new streams in the engine
            engine.registerStream(fb);
            Thread fbThread = new Thread(fb);

            ClassLoader classLoader = TestReasoning.class.getClassLoader();

            File roomConnection = new File(classLoader.getResource("roomConnection.rdf").getFile());
            String roomConnectionPath = roomConnection.getAbsolutePath();

            engine.putStaticNamedModel("http://streamreasoning.org/roomConnection", CsparqlUtils.serializeRDFFile(roomConnectionPath));

            //Register new query in the engine
            CsparqlQueryResultProxy c = engine.registerQuery(queryBody, false);

            //Attach a result consumer to the query result proxy to print the results on the console
            c.addObserver(new ConsoleFormatter());

            fbThread.start();

            String updateQuery = "PREFIX : <http://www.streamreasoning.org/ontologies/sr4ld2014-onto#> "
                    + "INSERT DATA "
                    + "{ GRAPH <http://streamreasoning.org/roomConnection> { :room  :isConnectedTo  :room2 } }";
            //Update the static knowledge
            engine.execUpdateQueryOverDatasource(updateQuery);

            updateQuery = "PREFIX : <http://www.streamreasoning.org/ontologies/sr4ld2014-onto#> "
                    + "DELETE DATA "
                    + "{ GRAPH <http://streamreasoning.org/roomConnection> { :room  :isConnectedTo  :room1 } }";
            //Update the static knowledge
            engine.execUpdateQueryOverDatasource(updateQuery);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
