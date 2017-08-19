import eu.larkc.csparql.core.engine.ConsoleFormatter;
import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;
import org.apache.log4j.PropertyConfigurator;

import java.text.ParseException;

/**
 * Created by ruhandosreis on 02/07/17.
 */
public class Test1 {

    public static void main(String[] args) throws ParseException {
        //Configure log4j logger for the csparql engine
        PropertyConfigurator.configure("log4j_configuration/csparql_readyToGoPack_log4j.properties");

        String queryBody = "REGISTER STREAM IsInFs AS "
                + "PREFIX : <http://www.streamreasoning.org/ontologies/sr4ld2014-onto#> "
                + "CONSTRUCT { ?person :isIn ?room } "
                + "FROM STREAM <http://streamreasoning.org/streams/fs> [RANGE 10s STEP 1s] "
                + "WHERE { "
                + "?person :posts [ :who ?person ; :where ?room ] "
                + "}";

        FoursquareStreamer fs = new FoursquareStreamer(
                "http://streamreasoning.org/streams/fs"
                , "http://www.streamreasoning.org/ontologies/sr4ld2014-onto#"
                , 1000L);

        //Create csparql engine instance
        CsparqlEngineImpl engine = new CsparqlEngineImpl();

        //Initialize the engine instance
        //The initialization creates the static engine (SPARQL) and the stream engine (CEP)
        engine.initialize();

        //Register new stream in the engine
        engine.registerStream(fs);

        Thread fsThread = new Thread(fs);

        //Register new query in the engine
        CsparqlQueryResultProxy c = engine.registerQuery(queryBody, false);

        //Attach a result consumer to the query result proxy to print the results on the console
        c.addObserver(new ConsoleFormatter());

        //Start the thread that put the triples in the engine
        fsThread.start();

    }
}
