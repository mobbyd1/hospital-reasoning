import eu.larkc.csparql.common.utils.CsparqlUtils;
import eu.larkc.csparql.common.utils.ReasonerChainingType;
import eu.larkc.csparql.core.engine.ConsoleFormatter;
import eu.larkc.csparql.core.engine.CsparqlEngineImpl;
import eu.larkc.csparql.core.engine.CsparqlQueryResultProxy;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;

/**
 * Created by ruhandosreis on 19/08/17.
 */
public class TestReasoningEsmocypTemperature2 {

    public static void main(String[] args) throws Exception {

        ClassLoader classLoader = TestReasoning.class.getClassLoader();

        //Configure log4j logger for the csparql engine
        PropertyConfigurator.configure("log4j_configuration/csparql_readyToGoPack_log4j.properties");

        //Create csparql engine instance
        CsparqlEngineImpl engine = new CsparqlEngineImpl();
        //Initialize the engine instance
        //The initialization creates the static engine (SPARQL) and the stream engine (CEP)
        engine.initialize();

        String queryBody = "REGISTER QUERY staticKnowledge AS "
                + "PREFIX :<http://www.semanticweb.org/esmocyp#> "
                + "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                + "SELECT ?s ?t "
                + "FROM STREAM <http://streamreasoning.org/streams/hospital> [RANGE 1s STEP 1s] "
                + "FROM <http://streamreasoning.org/hospital-data> "
                + "WHERE { "
                //+ "?s :temSensorMuitoQuente ?t "
                + "?s a :SalaEmPerigo "
                + "} ";

        File esmocypData = new File(classLoader.getResource("teste-temperatura-humidade/100-salas/esmocyp-temperature-data.rdf").getFile());
        String roomConnectionPath = esmocypData.getAbsolutePath();

        engine.putStaticNamedModel("http://streamreasoning.org/hospital-data", CsparqlUtils.serializeRDFFile(roomConnectionPath));

        HospitalStreamer fb = new HospitalStreamer("http://streamreasoning.org/streams/hospital", "http://www.semanticweb.org/esmocyp#", 1000L);

        //Register new streams in the engine
        engine.registerStream(fb);
        Thread fbThread = new Thread(fb);

        //Start streaming data
        fbThread.start();

        //Register new query in the engine
        CsparqlQueryResultProxy c = engine.registerQuery(queryBody, false);

        //Attach a result consumer to the query result proxy to print the results on the console
        c.addObserver(new ConsoleFormatter());

        File rdfsRulesFile = new File(classLoader.getResource("owl.rules").getFile());
        String rulesPath = rdfsRulesFile.getAbsolutePath();

        File tboxFile = new File(classLoader.getResource("teste-temperatura-humidade/esmocyp-temperature.owl").getFile());
        String tboxPath = tboxFile.getAbsolutePath();

        engine.updateReasoner(
                c.getSparqlQueryId()
                , null
                , ReasonerChainingType.BACKWARD
                , CsparqlUtils.serializeRDFFile(tboxPath) );

    }
}
