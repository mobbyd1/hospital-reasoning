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
public class TestReasoningEsmocyp {

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
                + "PREFIX :<urn:x-hp:eg/> "
                + "SELECT ?p ?s "
                + "FROM STREAM <http://streamreasoning.org/streams/hospital> [RANGE 1s STEP 1s] "
                + "FROM <http://streamreasoning.org/hospital-data> "
                + "WHERE { "
                + "?s a :NaoEncontrado . "
                + "?p :temSmartphone ?s . "
                + "?p a :Pessoa . "
                + "} ";

        String queryBody2 = "REGISTER QUERY staticKnowledge AS "
                + "PREFIX :<urn:x-hp:eg/> "
                + "SELECT ?p ?s "
                + "FROM STREAM <http://streamreasoning.org/streams/hospital> [RANGE 1s STEP 1s] "
                + "FROM <http://streamreasoning.org/hospital-data> "
                + "WHERE { "
                + "?p :temSmartphone ?s ."
                + "?p a :NaoVeioTrabalhar . "
                + "} ";

        File esmocypData = new File(classLoader.getResource("esmocypData.rdf").getFile());
        String roomConnectionPath = esmocypData.getAbsolutePath();

        engine.putStaticNamedModel("http://streamreasoning.org/hospital-data", CsparqlUtils.serializeRDFFile(roomConnectionPath));

        HospitalStreamer fb = new HospitalStreamer("http://streamreasoning.org/streams/hospital", "urn:x-hp:eg/", 1000L);

        //Register new streams in the engine
        engine.registerStream(fb);
        Thread fbThread = new Thread(fb);

        //Start streaming data
        fbThread.start();

        //Register new query in the engine
        CsparqlQueryResultProxy c = engine.registerQuery(queryBody2, false);

        //Attach a result consumer to the query result proxy to print the results on the console
        c.addObserver(new ConsoleFormatter());

        File rdfsRulesFile = new File(classLoader.getResource("owl.rules").getFile());
        String rulesPath = rdfsRulesFile.getAbsolutePath();

        File tboxFile = new File(classLoader.getResource("tbox-esmocyp.rdf").getFile());
        String tboxPath = tboxFile.getAbsolutePath();

        engine.updateReasoner(
                c.getSparqlQueryId()
                , CsparqlUtils.fileToString(rulesPath)
                , ReasonerChainingType.BACKWARD
                , CsparqlUtils.serializeRDFFile(tboxPath) );

    }
}
