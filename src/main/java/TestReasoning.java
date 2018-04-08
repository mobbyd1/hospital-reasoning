import eu.larkc.csparql.common.utils.CsparqlUtils;
import eu.larkc.csparql.core.engine.ConsoleFormatter;
import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

/**
 * Created by ruhandosreis on 02/07/17.
 */
public class TestReasoning {

    public static void main( String[] args ) {
        try{

            ClassLoader classLoader = TestReasoning.class.getClassLoader();

            //Configure log4j logger for the csparql engine
            PropertyConfigurator.configure("log4j_configuration/csparql_readyToGoPack_log4j.properties");

            //Create csparql engine instance
            CsparqlEngineImpl engine = new CsparqlEngineImpl();
            //Initialize the engine instance
            //The initialization creates the static engine (SPARQL) and the stream engine (CEP)
            engine.initialize();

            String queryBody = "REGISTER QUERY staticKnowledge AS "
                    + "PREFIX :<http://www.streamreasoning.org/ontologies/sr4ld2014-onto#> "
                    + "SELECT ?r ?r1 "
                    + "FROM <http://streamreasoning.org/roomConnection> "
                    + "WHERE { "
                    + "?r :isConnectedTo ?r1 . "
                    + "} ";

            File roomConnection = new File(classLoader.getResource("others/roomConnection.rdf").getFile());
            String roomConnectionPath = roomConnection.getAbsolutePath();

            engine.putStaticNamedModel("http://streamreasoning.org/roomConnection", CsparqlUtils.serializeRDFFile(roomConnectionPath));

            FacebookStreamer fb = new FacebookStreamer("http://streamreasoning.org/streams/fb", "http://www.streamreasoning.org/ontologies/sr4ld2014-onto#", 1000L);

            //Register new streams in the engine
            engine.registerStream(fb);
            Thread fbThread = new Thread(fb);

            //Register new query in the engine
            CsparqlQueryResultProxy c = engine.registerQuery(queryBody, false);

            //Attach a result consumer to the query result proxy to print the results on the console
            c.addObserver(new ConsoleFormatter());

            //Start streaming data
            fbThread.start();


//            File rdfsRulesFile = new File(classLoader.getResource("owl.rules").getFile());
//            String rulesPath = rdfsRulesFile.getAbsolutePath();
//
//            File tboxFile = new File(classLoader.getResource("tbox.rdf").getFile());
//            String tboxPath = tboxFile.getAbsolutePath();
//
//            engine.updateReasoner(
//                    c.getSparqlQueryId()
//                    , CsparqlUtils.fileToString(rulesPath)
//                    , ReasonerChainingType.HYBRID
//                    , CsparqlUtils.serializeRDFFile(tboxPath) );

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
